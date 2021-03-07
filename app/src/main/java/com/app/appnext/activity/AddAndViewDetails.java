package com.app.appnext.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.appnext.databasehelper.DBHelper;
import com.app.appnext.R;
import com.app.appnext.adapter.AdapterView;
import com.app.appnext.modelclasses.ModelView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddAndViewDetails extends AppCompatActivity implements AdapterView.hereItemClicked {

    private String name;
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private AdapterView adapterView;
    private ArrayList<ModelView> modelViewArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

       /* name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);

        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.rcView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        modelViewArrayList = new ArrayList<ModelView>();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddAndViewDetails.this, EnterDetails.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });*/

    }


    @Override
    protected void onStart() {
        super.onStart();

      /*  name = getIntent().getStringExtra("name");
        Cursor cursor = new DBHelper(this).getData(name);
        modelViewArrayList.clear();

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ModelView modelView = new ModelView(cursor.getString(0), cursor.getString(1),cursor.getString(2));
                modelViewArrayList.add(modelView);
            }

            adapterView = new AdapterView(this, modelViewArrayList);
            recyclerView.setAdapter(adapterView);
        }*/

    }

    @Override
    public void onHereItemClicked(int index) {

      /*  String a = modelViewArrayList.get(index).getNameA();
        String b = modelViewArrayList.get(index).getNameB();
        Intent intent = new Intent(AddAndViewDetails.this, PlayerActivity.class);
        startActivity(intent);*/

    }
}