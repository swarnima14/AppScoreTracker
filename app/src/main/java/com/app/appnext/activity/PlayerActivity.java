package com.app.appnext.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.appnext.R;
import com.app.appnext.databasehelper.DBHelper;
import com.app.appnext.modelclasses.PlayerScore;
import com.google.android.material.button.MaterialButton;

public class PlayerActivity extends AppCompatActivity{

    private EditText etStriker, etNonStriker, etBowler;
    private Button btnStart;

    private String striker, nonStriker, bowler, nameA, nameB, type;

    PlayerScore playerScore;
    int idA, idB;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);

        nameA = getIntent().getStringExtra("nameA");
        nameB = getIntent().getStringExtra("nameB");
        idA = getIntent().getIntExtra("idA", 0);
        idB = getIntent().getIntExtra("idB", 0);

       // type = getIntent().getStringExtra("type");

        etStriker = findViewById(R.id.etStriker);
        etNonStriker = findViewById(R.id.etNonStriker);
        etBowler = findViewById(R.id.etBowler);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                striker = etStriker.getText().toString();
                nonStriker = etNonStriker.getText().toString();
                bowler = etBowler.getText().toString();

                if(TextUtils.isEmpty(striker) || TextUtils.isEmpty(nonStriker) || TextUtils.isEmpty(bowler)){
                    Toast.makeText(PlayerActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PlayerScore bool1 = new DBHelper(getApplicationContext()).insertPlayerData(striker,"striker", 0, 0,0,0,0,0.0);
                    PlayerScore bool2 = new DBHelper(getApplicationContext()).insertPlayerData(nonStriker, "non-striker", 0, 0,0,0,0,0.0);
                    PlayerScore bool3 = new DBHelper(getApplicationContext()).insertPlayerData(bowler, "bowler", 0, 0,0,0,0,0.0);


                    Intent intent = new Intent(PlayerActivity.this, ScoreCardActivity.class);
                    intent.putExtra("striker", striker);
                    intent.putExtra("non", nonStriker);
                    intent.putExtra("bowler", bowler);
                    intent.putExtra("idS", bool1.getId());
                    intent.putExtra("idN", bool2.getId());
                    intent.putExtra("idBowl", bool3.getId());
                    intent.putExtra("nameA", nameA);
                    intent.putExtra("nameB", nameB);
                    intent.putExtra("idA", idA);
                    intent.putExtra("idB", idB);

                    finish();
                    startActivity(intent);
                }

            }
        });

    }


}
