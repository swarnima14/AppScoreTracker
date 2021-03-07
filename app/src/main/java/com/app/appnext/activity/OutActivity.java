package com.app.appnext.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.appnext.R;
import com.app.appnext.databasehelper.DBHelper;
import com.app.appnext.modelclasses.PlayerScore;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class OutActivity extends AppCompatActivity{

    int idS, idN, idBowl, idNewS;
    AutoCompleteTextView tvOut;
    MaterialButton doneBtn;
    TextInputLayout layEtS, layEtF;
    TextInputEditText etSName, etFielderName;
    LinearLayout lay1;
    RadioButton rbNon, rbStr;
    String oldS, newS, status, bowlerName;
    String[] outTypes = { "Bowled", "LBW", "Caught", "Stumped", "Run Out", "Hit Wicket", "Retired Out", "Obstructing the field" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_layout);

        tvOut = findViewById(R.id.tvOut);
        lay1 = findViewById(R.id.lay1);
        doneBtn = findViewById(R.id.doneBtn);
        etSName = findViewById(R.id.etSName);
        rbNon = findViewById(R.id.rbNon); rbStr = findViewById(R.id.rbStr);
        etFielderName = findViewById(R.id.etFielderName);
        layEtS = findViewById(R.id.layEtS);
        layEtF = findViewById(R.id.layEtF);


        idS = getIntent().getIntExtra("idS", 0);
        idN = getIntent().getIntExtra("idN", 0);
        idBowl =getIntent().getIntExtra("idBowl", 0);

        bowlerName = new DBHelper(this).getPlayersDataById(idBowl).getName();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, outTypes);
        tvOut.setAdapter(adapter);

        tvOut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String select = outTypes[position];
                if(select.equals("Bowled") || select.equals("LBW") || select.equals("Hit Wicket") || select.equals("Retired Out"))
                {
                    layEtS.setVisibility(View.VISIBLE);
                    etSName.setVisibility(View.VISIBLE);
                    doneBtn.setVisibility(View.VISIBLE);
                    layEtF.setVisibility(View.INVISIBLE);
                    lay1.setVisibility(View.INVISIBLE);

                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(select.equals("Bowled"))
                                status = "b " + bowlerName;
                            else if(select.equals("LBW"))
                                status = "lbw b " + bowlerName;
                            else if(select.equals("Hit Wicket"))
                                status = "hit wicket";
                            else
                                status = "retired out";

                            final String sName = etSName.getText().toString();

                            if(!TextUtils.isEmpty(sName)) {

                                PlayerScore p = new DBHelper(getApplicationContext()).insertPlayerData(sName, "striker", 0, 0, 0, 0, 0, 0.0);
                                idNewS = p.getId();
                                oldS = new DBHelper(getApplicationContext()).getPlayersDataById(idS).getName();
                                newS = new DBHelper(getApplicationContext()).getPlayersDataById(idNewS).getName();
                                Boolean bool = new DBHelper(getApplicationContext()).updatePlayerStatusById(idS, status);
                                Intent intent = new Intent();
                                intent.putExtra("oldS", oldS);
                                intent.putExtra("newS", sName);
                                intent.putExtra("idSout", idS);
                                intent.putExtra("who", "str");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });

                }

                else if(select.equals("Run Out")){
                    PlayerScore p = new DBHelper(getApplicationContext()).getPlayersDataById(idS);
                    PlayerScore p1 = new DBHelper(getApplicationContext()).getPlayersDataById(idN);

                    layEtS.setHint("New Player");
                    layEtF.setHint("Fielder's Name");
                    layEtS.setVisibility(View.VISIBLE);
                    layEtF.setVisibility(View.VISIBLE);
                    rbStr.setText(p.getName());
                    rbNon.setText(p1.getName());
                    lay1.setVisibility(View.VISIBLE);
                    doneBtn.setVisibility(View.VISIBLE);
                    etFielderName.setVisibility(View.VISIBLE);
                    etSName.setVisibility(View.VISIBLE);

                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newS = etSName.getText().toString();
                            String fielder = etFielderName.getText().toString();
                            status = "run out: " +fielder;
                            if(rbStr.isChecked())
                            {
                                PlayerScore p2 = new DBHelper(getApplicationContext()).insertPlayerData(newS, "striker",0,0,0,0,0,0.0);
                                idNewS = p2.getId();
                                newS = new DBHelper(getApplicationContext()).getPlayersDataById(idNewS).getName();
                                oldS = new DBHelper(getApplicationContext()).getPlayersDataById(idS).getName();
                                Boolean bool = new DBHelper(getApplicationContext()).updatePlayerStatusById(idS, status);
                                Intent intent = new Intent();
                                intent.putExtra("newS", newS);
                                intent.putExtra("idSout", idS);
                                intent.putExtra("who", "str");
                                intent.putExtra("oldS", oldS);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            if(rbNon.isChecked())
                            {
                                PlayerScore p2 = new DBHelper(getApplicationContext()).insertPlayerData(newS, "non-striker",0,0,0,0,0,0.0);
                                idNewS = p2.getId();
                                newS = new DBHelper(getApplicationContext()).getPlayersDataById(idNewS).getName();
                                oldS = new DBHelper(getApplicationContext()).getPlayersDataById(idN).getName();
                                Boolean bool = new DBHelper(getApplicationContext()).updatePlayerStatusById(idN, status);
                                Intent intent = new Intent();
                                intent.putExtra("newS", newS);
                                intent.putExtra("who", "non");
                                intent.putExtra("idSout", idN);
                                intent.putExtra("oldS", oldS);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });
                }
                else if(select.equals("Stumped")){

                    layEtF.setHint("Keeper's Name");
                    layEtS.setHint("New Player");
                    layEtS.setVisibility(View.VISIBLE);
                    layEtF.setVisibility(View.VISIBLE);
                    doneBtn.setVisibility(View.VISIBLE);
                    etFielderName.setVisibility(View.VISIBLE);
                    etSName.setVisibility(View.VISIBLE);
                    lay1.setVisibility(View.INVISIBLE);

                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final String sName = etSName.getText().toString();
                            String keeper = etFielderName.getText().toString();
                            if(!TextUtils.isEmpty(sName)) {

                                PlayerScore p = new DBHelper(getApplicationContext()).insertPlayerData(sName, "striker", 0, 0, 0, 0, 0, 0.0);
                                idNewS = p.getId();
                                status = "stumped: " + keeper + " " + "b" + bowlerName;
                                oldS = new DBHelper(getApplicationContext()).getPlayersDataById(idS).getName();

                                newS = new DBHelper(getApplicationContext()).getPlayersDataById(idNewS).getName();
                                Intent intent = new Intent();
                                intent.putExtra("oldS", oldS);
                                intent.putExtra("newS", sName);
                                intent.putExtra("idSout", idS);
                                intent.putExtra("who", "str");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });
                }
                else if(select.equals("Obstructing the field")){
                    PlayerScore p = new DBHelper(getApplicationContext()).getPlayersDataById(idS);
                    PlayerScore p1 = new DBHelper(getApplicationContext()).getPlayersDataById(idN);

                    rbStr.setText(p.getName());
                    rbNon.setText(p1.getName());
                    layEtF.setHint("Keeper's Name");
                    layEtS.setHint("New Player");
                    layEtS.setVisibility(View.VISIBLE);
                    layEtF.setVisibility(View.VISIBLE);
                    doneBtn.setVisibility(View.VISIBLE);
                    etFielderName.setVisibility(View.VISIBLE);
                    etSName.setVisibility(View.VISIBLE);
                    lay1.setVisibility(View.VISIBLE);

                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newS = etSName.getText().toString();
                            String fielder = etFielderName.getText().toString();
                            status = "obt";
                            if(rbStr.isChecked())
                            {
                                PlayerScore p2 = new DBHelper(getApplicationContext()).insertPlayerData(newS, "striker",0,0,0,0,0,0.0);
                                idNewS = p2.getId();
                                newS = new DBHelper(getApplicationContext()).getPlayersDataById(idNewS).getName();
                                oldS = new DBHelper(getApplicationContext()).getPlayersDataById(idS).getName();
                                Boolean bool = new DBHelper(getApplicationContext()).updatePlayerStatusById(idS, status);
                                Intent intent = new Intent();
                                intent.putExtra("newS", newS);
                                intent.putExtra("idSout", idS);
                                intent.putExtra("oldS", oldS);
                                intent.putExtra("who", "str");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            if(rbNon.isChecked())
                            {
                                PlayerScore p2 = new DBHelper(getApplicationContext()).insertPlayerData(newS, "non-striker",0,0,0,0,0,0.0);
                                idNewS = p2.getId();
                                newS = new DBHelper(getApplicationContext()).getPlayersDataById(idNewS).getName();
                                oldS = new DBHelper(getApplicationContext()).getPlayersDataById(idN).getName();
                                Boolean bool = new DBHelper(getApplicationContext()).updatePlayerStatusById(idN, status);
                                Intent intent = new Intent();
                                intent.putExtra("newS", newS);
                                intent.putExtra("idSout", idN);
                                intent.putExtra("oldS", oldS);
                                intent.putExtra("who", "non");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });
                }
                else if(select.equals("Caught")){
                    PlayerScore p = new DBHelper(getApplicationContext()).getPlayersDataById(idS);
                    PlayerScore p1 = new DBHelper(getApplicationContext()).getPlayersDataById(idN);

                    rbStr.setText(p.getName());
                    rbNon.setText(p1.getName());
                    layEtF.setHint("Keeper's Name");
                    layEtS.setHint("New Player");
                    layEtS.setVisibility(View.VISIBLE);
                    layEtF.setVisibility(View.VISIBLE);
                    doneBtn.setVisibility(View.VISIBLE);
                    etFielderName.setVisibility(View.VISIBLE);
                    etSName.setVisibility(View.VISIBLE);
                    lay1.setVisibility(View.VISIBLE);

                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newS = etSName.getText().toString();
                            String fielder = etFielderName.getText().toString();
                            status = "c " + fielder + "b " + bowlerName;
                            if(rbStr.isChecked())
                            {
                                PlayerScore p2 = new DBHelper(getApplicationContext()).insertPlayerData(newS, "striker",0,0,0,0,0,0.0);
                                idNewS = p2.getId();
                                newS = new DBHelper(getApplicationContext()).getPlayersDataById(idNewS).getName();
                                oldS = new DBHelper(getApplicationContext()).getPlayersDataById(idS).getName();
                                Boolean bool = new DBHelper(getApplicationContext()).updatePlayerStatusById(idS, status);
                                Intent intent = new Intent();
                                intent.putExtra("newS", newS);
                                intent.putExtra("idSout", idS);
                                intent.putExtra("oldS", oldS);
                                intent.putExtra("who", "str");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            if(rbNon.isChecked())
                            {
                                PlayerScore p2 = new DBHelper(getApplicationContext()).insertPlayerData(newS, "non-striker",0,0,0,0,0,0.0);
                                idNewS = p2.getId();
                                newS = new DBHelper(getApplicationContext()).getPlayersDataById(idNewS).getName();
                                oldS = new DBHelper(getApplicationContext()).getPlayersDataById(idN).getName();
                                Boolean bool = new DBHelper(getApplicationContext()).updatePlayerStatusById(idN, status);
                                Intent intent = new Intent();
                                intent.putExtra("newS", newS);
                                intent.putExtra("idSout", idN);
                                intent.putExtra("oldS", oldS);
                                intent.putExtra("who", "non");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });
                }
                else{
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}
