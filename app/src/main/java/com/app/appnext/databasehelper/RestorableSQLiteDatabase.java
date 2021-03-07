package com.app.appnext.databasehelper;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

/**
 * A wrapper to replicate android's SQLiteDatabase class with restoring capability.
 * This wrapper makes it possible to undo changes made after execution of SQL commands.
 */
@SuppressWarnings("UnusedDeclaration")
public class RestorableSQLiteDatabase {

    private static RestorableSQLiteDatabase mInstance = null;
    private SQLiteDatabase mSQLiteDatabase;
    private static final String TAG = "SQLiteDatabase";

    /**
     * The hash table to map a tag to its restoring queries.
     */
    private Hashtable<String, ArrayList<String>> mTagQueryTable;

    /**
     * The hash table to map a tag to the parameters should be used in the queries.
     */
    private Hashtable<String, ArrayList<String[]>> mTagQueryParameters;

    /**
     * Maps the table name to its ROWID column name.
     */
    private HashMap<String, String> mTableRowid;

    /**
     * Constructs a new instance of the RestorableSQLiteDatabase only if no instance is constructed.
     * @param mSQLiteDatabase the instance of the SQLiteDatabase to be wrapped.
     * @param tableRowid maps the table name to its ROWID column name.
     * @return an instance of this class.
     */
    public static RestorableSQLiteDatabase getInstance(SQLiteDatabase mSQLiteDatabase, HashMap<String, String> tableRowid) {
        if(mInstance == null) {
            mInstance = new RestorableSQLiteDatabase(mSQLiteDatabase, tableRowid);
        }
        return mInstance;
    }

    /**
     * Constructs a new instance of the RestorableSQLiteDatabase only if no instance is constructed.
     * @param helper the instance of the SQLiteOpenHelper to open a database using {@link android.database.sqlite.SQLiteOpenHelper#getWritableDatabase() getWritableDatabase} method.
     * @param tableRowid maps the table name to its ROWID column name.
     * @return an instance of this class.
     */
    public static <T extends SQLiteOpenHelper> RestorableSQLiteDatabase getInstance(T helper, HashMap<String, String> tableRowid) {
        if(mInstance == null) {
            mInstance = new RestorableSQLiteDatabase(helper, tableRowid);
        }
        return mInstance;
    }

    /**
     * Constructs a new instance of the RestorableSQLiteDatabase.
     * @param mSQLiteDatabase the instance of the SQLiteDatabase to be wrapped.
     * @param tableRowid maps the table name to its ROWID column name.
     * @return an instance of this class.
     */
    public static RestorableSQLiteDatabase getNewInstance(SQLiteDatabase mSQLiteDatabase, HashMap<String, String> tableRowid) {
        mInstance = new RestorableSQLiteDatabase(mSQLiteDatabase, tableRowid);
        return mInstance;
    }

    /**
     * Constructs a new instance of the RestorableSQLiteDatabase.
     * @param helper the instance of the SQLiteOpenHelper to open a database using {@link android.database.sqlite.SQLiteOpenHelper#getWritableDatabase() getWritableDatabase} method.
     * @param tableRowid maps the table name to its ROWID column name.
     * @return an instance of this class.
     */
    public static <T extends SQLiteOpenHelper> RestorableSQLiteDatabase getNewInstance(T helper, HashMap<String, String> tableRowid) {
        mInstance = new RestorableSQLiteDatabase(helper, tableRowid);
        return mInstance;
    }

    /**
     * Private constructor of singleton pattern.
     * @param mSQLiteDatabase the instance of the SQLiteDatabase to be wrapped.
     * @param tableRowid maps the table name to its ROWID column name.
     */
    public RestorableSQLiteDatabase(SQLiteDatabase mSQLiteDatabase, HashMap<String, String> tableRowid) {
        mTagQueryTable = new Hashtable<>();
        mTagQueryParameters = new Hashtable<>();
        mTableRowid = tableRowid;
        this.mSQLiteDatabase = mSQLiteDatabase;
    }

    /**
     * Private constructor of singleton pattern.
     * @param helper the instance of the SQLiteOpenHelper to open a database using {@link android.database.sqlite.SQLiteOpenHelper#getWritableDatabase() getWritableDatabase} method.
     * @param tableRowid maps the table name to its ROWID column name.
     */
    public <T extends SQLiteOpenHelper> RestorableSQLiteDatabase(T helper, HashMap<String, String> tableRowid) {
        mTagQueryTable = new Hashtable<>();
        mTagQueryParameters = new Hashtable<>();
        mTableRowid = tableRowid;
        this.mSQLiteDatabase = helper.getWritableDatabase();
    }

    /**
     * Provides the instance of wrapped SQLiteDatabase.
     * @return the instance of wrapped SQLiteDatabase.
     */
    public SQLiteDatabase getSQLiteDatabase() {
        return mSQLiteDatabase;
    }

    /**
     * Checks if the hash table contains the tag.
     * @param tag possible tag of restoring query.
     * @return true if the hash table contains the tag; false otherwise.
     * @throws IllegalArgumentException if the tag is null.
     */
    public boolean containsTag(String tag) {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        return mTagQueryTable.containsKey(tag);
    }

    /**
     * Provides a Set view of the tags contained in the hash table.
     * @return a Set view of the tags contained in the hash table.
     */
    public Set<String> tagSet() {
        return mTagQueryTable.keySet();
    }

    /**
     * Provides the query to which the tag is mapped.
     * @param tag possible tag of restoring queries.
     * @throws IllegalArgumentException if the tag is null.
     * @return the queries to which the tag is mapped, or null if the hash table contains no mapping for the tag.
     */
    public ArrayList<String> getQueries(String tag) {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        return mTagQueryTable.get(tag);
    }

    /**
     * Provides the hash table.
     * @return the hash table.
     */
    public Hashtable<String, ArrayList<String>> getTagQueryTable() {
        return mTagQueryTable;
    }

    /**
     * Provides the parameters hash table.
     * @return the parameters hash table.
     */
    public Hashtable<String, ArrayList<String[]>> getTagQueryParameters() {
        return mTagQueryParameters;
    }

    /**
     * Changes the hash table.
     * @param tagQueryTable the substitute hash table.
     */
    public void setTagQueryTable(Hashtable<String, ArrayList<String>> tagQueryTable) {
        this.mTagQueryTable = tagQueryTable;
    }

    /**
     * Changes the parameters hash table.
     * @param tagQueryParameters the substitute hash table.
     */
    public void setTagQueryParameters(Hashtable<String, ArrayList<String[]>> tagQueryParameters) {
        this.mTagQueryParameters = tagQueryParameters;
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#insert(String, String, android.content.ContentValues) insert} method.
     * @param tag the tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public long insert(String table, String nullColumnHack, ContentValues values, String tag) {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        try {
            return insertWithOnConflict(table, nullColumnHack, values, SQLiteDatabase.CONFLICT_NONE, tag);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
            return -1;
        }
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#insertOrThrow(String, String, android.content.ContentValues) insertOrThrow} method.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public long insertOrThrow(String table, String nullColumnHack, ContentValues values, String tag)
            throws SQLException {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        return insertWithOnConflict(table, nullColumnHack, values, SQLiteDatabase.CONFLICT_NONE, tag);
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#replace(String, String, android.content.ContentValues) replace} method.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public long replace(String table, String nullColumnHack, ContentValues initialValues, String tag) {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        try {
            return insertWithOnConflict(table, nullColumnHack, initialValues,
                    SQLiteDatabase.CONFLICT_REPLACE, tag);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + initialValues, e);
            return -1;
        }
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#replaceOrThrow(String, String, android.content.ContentValues) replaceOrThrow} method.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public long replaceOrThrow(String table, String nullColumnHack,
                               ContentValues initialValues, String tag) throws SQLException {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        return insertWithOnConflict(table, nullColumnHack, initialValues,
                SQLiteDatabase.CONFLICT_REPLACE, tag);
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#replaceOrThrow(String, String, android.content.ContentValues) insertWithOnConflict} method.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public long insertWithOnConflict(String table, String nullColumnHack,
                                     ContentValues initialValues, int conflictAlgorithm, String tag) {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        ArrayList<String> queries = new ArrayList<>();
        ArrayList<String[]> queriesParameters = new ArrayList<>();

        // Determines if restoring query of replacement is generated
        boolean restore_status = false;

        // Generates replacement restoring query
        if (conflictAlgorithm == SQLiteDatabase.CONFLICT_REPLACE) {
            Cursor restoring_cursor = mSQLiteDatabase.query(
                    table,
                    null,
                    mTableRowid.get(table) + " = ?",
                    new String[] {(String) initialValues.get(mTableRowid.get(table))},
                    null,
                    null,
                    null,
                    null
            );

            if (restoring_cursor.moveToFirst()) {
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE ");
                sql.append(table);
                sql.append(" SET ");

                int i = 0;
                String[] parameters = new String[restoring_cursor.getColumnCount()];

                for (String columnName : restoring_cursor.getColumnNames()) {
                    if (columnName.equals(mTableRowid.get(table)))
                        continue;

                    if (i > 0) sql.append(", ");

                    sql.append(columnName);
                    sql.append(" = ?");
                    parameters[i] = restoring_cursor.getString(restoring_cursor.getColumnIndex(columnName));

                    i++;
                }

                sql.append(" WHERE ");
                sql.append(mTableRowid.get(table));
                sql.append(" = ?");
                parameters[i] = (String) initialValues.get(mTableRowid.get(table));

                queries.add(sql.toString());
                queriesParameters.add(parameters);

                restore_status = true;
            }

            restoring_cursor.close();
        }

        // Executes query
        long id = mSQLiteDatabase.insertWithOnConflict(
                table,
                nullColumnHack,
                initialValues,
                conflictAlgorithm
        );

        // Generates query to restore insertion
        if (!restore_status) {
            queries.add("DELETE FROM " + table + " WHERE " + mTableRowid.get(table) + " = ?");
            queriesParameters.add(new String[] {id + ""});
        }

        // Add queries and their parameters if no error has occurred
        if (id != -1) {
            mTagQueryTable.put(tag, queries);
            mTagQueryParameters.put(tag, queriesParameters);
        }

        return id;
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#update(String, android.content.ContentValues, String, String[]) update} method.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs, String tag) {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        return updateWithOnConflict(table, values, whereClause, whereArgs, SQLiteDatabase.CONFLICT_NONE, tag);
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#updateWithOnConflict(String, android.content.ContentValues, String, String[], int) updateWithOnConflict} method.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public int updateWithOnConflict(String table, ContentValues values,
                                    String whereClause, String[] whereArgs, int conflictAlgorithm, String tag) {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        generateRestoringUpdate(
                table,
                whereClause,
                whereArgs,
                tag
        );

        return mSQLiteDatabase.updateWithOnConflict(
                table,
                values,
                whereClause,
                whereArgs,
                conflictAlgorithm
        );
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#delete(String, String, String[]) delete} method.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public int delete(String table, String whereClause, String[] whereArgs, String tag) {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        generateRestoringDelete(
                table,
                whereClause,
                whereArgs,
                tag
        );

        return mSQLiteDatabase.delete(
                table,
                whereClause,
                whereArgs
        );
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#rawQuery(String, String[]) rawQuery} method.
     * This method uses {@link net.sf.jsqlparser.parser.CCJSqlParserUtil#parse(String) parser} method to parse the SQL query.
     * Unlike the rawQuery of the SQLiteDatabase, there is no need to call the moveToFirst method of the returned Cursor to apply SQL query.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    public Cursor rawQuery(String sql, String[] selectionArgs, String tag)
            throws JSQLParserException, ClassCastException {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        Statement statement = CCJSqlParserUtil.parse(sql);

        generateRawUpdateDeleteQuery(statement, sql, selectionArgs, tag);

        Cursor cursor =  mSQLiteDatabase.rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        if (sql.toLowerCase(Locale.getDefault()).contains("insert into")) {
            Insert insertStatement = (Insert) statement;
            String table = insertStatement.getTable().getName();
            generateInsertRawQuery(table, tag);
        }

        return cursor;
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#rawQuery(String, String[]) rawQuery} method.
     * This method uses {@link net.sf.jsqlparser.parser.CCJSqlParserUtil#parse(String) parser} method to parse the SQL query.
     * Unlike the rawQuery of the SQLiteDatabase, there is no need to call the moveToFirst method of the returned Cursor to apply SQL query.
     * @param tag The tag to be mapped to the restoring query.
     * @throws IllegalArgumentException if the tag is null.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Cursor rawQuery(String sql, String[] selectionArgs,
                           CancellationSignal cancellationSignal, String tag)
            throws JSQLParserException, ClassCastException {
        if (tag == null)
            throw new IllegalArgumentException("The tag must not be null.");

        Statement statement = CCJSqlParserUtil.parse(sql);

        generateRawUpdateDeleteQuery(statement, sql, selectionArgs, tag);

        Cursor cursor = mSQLiteDatabase.rawQuery(sql, selectionArgs, cancellationSignal);
        cursor.moveToFirst();

        if (sql.toLowerCase(Locale.getDefault()).contains("insert into")) {
            Insert insertStatement = (Insert) statement;
            String table = insertStatement.getTable().getName();
            generateInsertRawQuery(table, tag);
        }

        return cursor;
    }

    /**
     * Generates the restoring query of rawQuery methods.
     * @param sql the SQL query.
     * @param selectionArgs arguments to be replaced with ? in the SQL query.
     * @throws JSQLParserException
     */
    private void generateRawUpdateDeleteQuery(Statement statement, String sql, String[] selectionArgs, String tag)
            throws JSQLParserException, ClassCastException {

        String table;
        String where;

        if (sql.toLowerCase(Locale.getDefault()).contains("update")) {

            Update updateStatement = (Update) statement;
            table = updateStatement.getTables().get(0).getName();
            where = updateStatement.getWhere().toString();

            // Generates selectionArgs for where
            int argsNumberBeforeWhere = countOccurrences(sql.toLowerCase(Locale.getDefault()).substring(0, sql.toLowerCase(Locale.getDefault()).indexOf("where")), "?");
            int argsNumberInWhere = countOccurrences(where, "?");

            String[] whereArgs = new String[argsNumberInWhere];

            System.arraycopy(selectionArgs, argsNumberBeforeWhere, whereArgs, 0, argsNumberInWhere);

            generateRestoringUpdate(
                    table,
                    where,
                    whereArgs,
                    tag
            );

        } else if (sql.toLowerCase(Locale.getDefault()).contains("delete")) {

            Delete deleteStatement = (Delete) statement;
            table = deleteStatement.getTable().getName();
            where = deleteStatement.getWhere().toString();

            // Generates selectionArgs for where
            int argsNumberBeforeWhere = countOccurrences(sql.toLowerCase(Locale.getDefault()).substring(0, sql.toLowerCase(Locale.getDefault()).indexOf("where")), "?");
            int argsNumberInWhere = countOccurrences(where, "?");

            String[] whereArgs = new String[argsNumberInWhere];

            System.arraycopy(selectionArgs, argsNumberBeforeWhere, whereArgs, 0, argsNumberInWhere);

            generateRestoringDelete(
                    table,
                    where,
                    whereArgs,
                    tag
            );
        }
    }

    /**
     * Counts the number of occurrences of substring in the string.
     * Reference: http://stackoverflow.com/a/23517296/1922137
     * @param str the string to check.
     * @param sub the substring to count.
     * @return the number of occurrences of substring in the string.
     */
    private int countOccurrences(String str, String sub) {
        return (str.length() - str.replace(sub, "").length()) / sub.length();
    }

    /**
     * Generates the restoring query of rawQuery insertion.
     * @param table the table name.
     * @param tag the tag mapped to restoring queries.
     * @throws JSQLParserException
     * @throws ClassCastException
     */
    private void generateInsertRawQuery(String table, String tag)
            throws JSQLParserException, ClassCastException {
        ArrayList<String> queries = new ArrayList<>();
        ArrayList<String[]> queriesParameters = new ArrayList<>();

        /*Cursor cursor = mSQLiteDatabase.query(
                false,
                table,
                new String[] {mTableRowid.get(table)},
                null,
                null,
                null,
                null,
                mTableRowid.get(table) + " DESC",
                "1"
        );*/

        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT MAX(" + mTableRowid.get(table) + ") FROM " + table, null);

        if (cursor.moveToFirst()) {
            queries.add("DELETE FROM " + table + " WHERE " + mTableRowid.get(table) + " = ?");
            queriesParameters.add(new String[]{cursor.getString(0)});

            mTagQueryTable.put(tag, queries);
            mTagQueryParameters.put(tag, queriesParameters);
        }

        cursor.close();
    }

    /**
     * Generates the restoring query of rawQuery deletion.
     * @param table the table name.
     * @param whereClause the optional WHERE clause to apply when deleting.
     * @param whereArgs arguments to be replaced with ? in the SQL query.
     * @param tag the tag mapped to restoring queries.
     */
    private void generateRestoringDelete(String table,
                                         String whereClause,
                                         String[] whereArgs,
                                         String tag) {
        // Gets all affected_rows
        Cursor restoring_cursor = mSQLiteDatabase.query(
                table,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        ArrayList<String> queries = new ArrayList<>();
        ArrayList<String[]> queriesParameters = new ArrayList<>();

        // Generates restoring queries
        while (restoring_cursor.moveToNext()) {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT OR REPLACE INTO ");
            sql.append(table);

            int i = 0;
            String[] parameters = new String[restoring_cursor.getColumnCount()];

            StringBuilder sql_columns = new StringBuilder();
            StringBuilder sql_values = new StringBuilder();

            for (String columnName : restoring_cursor.getColumnNames()) {
                if (i > 0) {
                    sql_columns.append(", ");
                    sql_values.append(", ");
                } else {
                    sql_columns.append(" (");
                    sql_values.append(" (");
                }

                sql_columns.append(columnName);
                sql_values.append("?");
                parameters[i] = restoring_cursor.getString(restoring_cursor.getColumnIndex(columnName));

                i++;
            }

            sql_columns.append(")");
            sql_values.append(")");

            sql.append(sql_columns.toString());
            sql.append(" VALUES ");
            sql.append(sql_values.toString());

            queries.add(sql.toString());
            queriesParameters.add(parameters);
        }

        restoring_cursor.close();

        mTagQueryTable.put(tag, queries);
        mTagQueryParameters.put(tag, queriesParameters);
    }

    /**
     * Generates the restoring query of rawQuery updating.
     * @param table the table name.
     * @param whereClause the optional WHERE clause to apply when updating.
     * @param whereArgs arguments to be replaced with ? in the SQL query.
     * @param tag the tag mapped to restoring queries.
     */
    private void generateRestoringUpdate(String table,
                                         String whereClause,
                                         String[] whereArgs,
                                         String tag) {
        // Gets all affected_rows
        Cursor restoring_cursor = mSQLiteDatabase.query(
                table,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        ArrayList<String> queries = new ArrayList<>();
        ArrayList<String[]> queriesParameters = new ArrayList<>();

        // Generates restoring queries
        while (restoring_cursor.moveToNext()) {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ");
            sql.append(table);
            sql.append(" SET ");

            int i = 0;
            String[] parameters = new String[restoring_cursor.getColumnCount() + 1];

            for (String columnName : restoring_cursor.getColumnNames()) {
                if (i > 0) sql.append(", ");

                sql.append(columnName);
                sql.append(" = ?");
                parameters[i] = restoring_cursor.getString(restoring_cursor.getColumnIndex(columnName));

                i++;
            }

            sql.append(" WHERE ");
            sql.append(mTableRowid.get(table));
            sql.append(" = ?");
            parameters[i] = restoring_cursor.getString(restoring_cursor.getColumnIndex(mTableRowid.get(table)));

            queries.add(sql.toString());
            queriesParameters.add(parameters);
        }

        restoring_cursor.close();

        mTagQueryTable.put(tag, queries);
        mTagQueryParameters.put(tag, queriesParameters);
    }

    /**
     * Restores all restoring SQL queries.
     * @return possible number of restored queries to which tag is mapped.
     */
    public int restoreAll() {
        return restore(mTagQueryTable.keySet());
    }

    /**
     * Restores the queries to which each tag is mapped.
     * @param tags an array of tags mapped to restoring SQL queries.
     * @return possible number of restored queries to which tag is mapped.
     */
    public int restore(String[] tags) {
        int restored_queries = 0;

        for (String tag : tags) {
            restored_queries += restore(tag);
        }

        return restored_queries;
    }

    /**
     * Restores the queries to which each tag is mapped.
     * @param tags a set of tags mapped to restoring SQL queries.
     * @return possible number of restored queries to which tag is mapped.
     */
    public int restore(Set<String> tags) {
        int restored_queries = 0;

        for (String tag : tags) {
            restored_queries += restore(tag);
        }

        return restored_queries;
    }

    /**
     * Restores the SQL queries to which the tag is mapped.
     * @param tag the tag mapped to restoring queries.
     * @return possible number of restored queries to which tag is mapped.
     */
    public int restore(String tag) {
        ArrayList<String> queries = mTagQueryTable.get(tag);
        ArrayList<String[]> parameters = mTagQueryParameters.get(tag);

        if (queries != null && parameters != null) {
            int restored_queries = queries.size();

            for (int i = 0; i < restored_queries; i++) {
                mSQLiteDatabase.rawQuery(
                        queries.get(i),
                        parameters.get(i)
                ).moveToFirst();
            }

            mTagQueryTable.remove(tag);
            mTagQueryParameters.remove(tag);

            return restored_queries;
        }

        return 0;
    }

    /**
     * Use the {@link android.database.sqlite.SQLiteDatabase#close() close} method.
     * Use the reopen methods to reopen the SQLite database.
     */
    public void close() {
        mSQLiteDatabase.close();
    }

    /**
     * Reopens the SQLite database.
     * @param helper the instance of the SQLiteOpenHelper to open a database using {@link android.database.sqlite.SQLiteOpenHelper#getWritableDatabase() getWritableDatabase} method.
     */
    public <T extends SQLiteOpenHelper> void reopen(T helper) {
        mSQLiteDatabase = helper.getWritableDatabase();
    }

    /**
     * Reopens the SQLite database.
     * @param mSqLiteDatabase the instance of the SQLiteDatabase to be wrapped.
     */
    public void reopen(SQLiteDatabase mSqLiteDatabase) {
        this.mSQLiteDatabase = mSqLiteDatabase;
    }

}

