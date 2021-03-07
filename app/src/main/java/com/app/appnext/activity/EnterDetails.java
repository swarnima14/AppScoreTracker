package com.app.appnext.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.app.appnext.databasehelper.DBHelper;
import com.app.appnext.R;
import com.app.appnext.modelclasses.TeamDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class EnterDetails extends AppCompatActivity {

    private TextInputEditText etOne,etTwo,etOvers;
    private MaterialButton btnSubmit, btnSub;
    private DBHelper db;
    String type, nameA, nameB;
    RadioButton rbTeamA, rbTeamB, rbBat, rbBall;
    LinearLayout tossLay;
    ConstraintLayout constraintLayout;
    TeamDetails teamDetails, teamDetails1, teamDetails2, teamDetails3;
    int overs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);

        type = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle("Enter Details for");

        etOne = findViewById(R.id.etOne);
        etTwo = findViewById(R.id.etTwo);
        etOvers = findViewById(R.id.etOvers);
        btnSubmit = findViewById(R.id.btnSubmit);
        rbBall = findViewById(R.id.rbBall);
        rbBat = findViewById(R.id.rbBat);
        rbTeamA = findViewById(R.id.rbTeamA);
        rbTeamB = findViewById(R.id.rbTeamB);
        btnSub = findViewById(R.id.btnSub);
        constraintLayout = findViewById(R.id.conLay);
        tossLay = findViewById(R.id.tossLay);

        constraintLayout.setVisibility(View.VISIBLE);
        tossLay.setVisibility(View.GONE);

        db = new DBHelper(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             nameA = etOne.getText().toString().trim();
             nameB = etTwo.getText().toString().trim();
             overs = Integer.parseInt(etOvers.getText().toString().trim());

            if(TextUtils.isEmpty(nameA) || TextUtils.isEmpty(nameB) || TextUtils.isEmpty(etOvers.getText().toString()))
                Toast.makeText(EnterDetails.this, "All fields required", Toast.LENGTH_SHORT).show();

            else {
                Boolean check = db.insertDataIntoTable(nameA, nameB, type, Integer.parseInt(etOvers.getText().toString()));

                rbTeamA.setText(nameA);
                rbTeamB.setText(nameB);

                constraintLayout.setVisibility(View.GONE);
                tossLay.setVisibility(View.VISIBLE);
                setTossDetails();
            }

            }
        });
    }

    private void setTossDetails() {

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rbTeamA.isChecked())
                {
                    if(rbBat.isChecked())
                    {
                        teamDetails1 = new TeamDetails( 0,nameA, "won","bat",0, 0,overs,0);
                        teamDetails2 = new TeamDetails( 0,nameB, "lost","ball",0, 0, overs, 0);


                    }
                    else
                    {
                        teamDetails1 = new TeamDetails( 0,nameA, "won","ball",0, 0, overs, 0);
                        teamDetails2 = new TeamDetails( 0,nameB, "lost","bat",0, 0, overs, 0);

                    }
                    TeamDetails checkScore = db.insertScore(teamDetails1);
                    TeamDetails checkScoreB = db.insertScore(teamDetails2);
                    moveToPlayerActivity(checkScore, checkScoreB);
                }
                else if(rbTeamB.isChecked())
                {

                        if(rbBat.isChecked())
                        {
                            teamDetails1 = new TeamDetails(0, nameA, "lost","ball",0, 0, overs, 0);
                            teamDetails2 = new TeamDetails( 0, nameB,"won","bat",0, 0, overs, 0);

                        }
                        else
                        {
                            teamDetails1 = new TeamDetails( 0, nameA, "lost","bat",0, 0, overs, 0);
                            teamDetails2 = new TeamDetails( 0, nameB,"won","ball",0, 0, overs, 0);

                        }
                    TeamDetails checkScore = db.insertScore(teamDetails1);
                    TeamDetails checkScoreB = db.insertScore(teamDetails2);
                    moveToPlayerActivity(checkScore, checkScoreB);
                }
                else
                {
                    Toast.makeText(EnterDetails.this, "select fields to continue", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void moveToPlayerActivity(TeamDetails t1, TeamDetails t2){
         teamDetails = new DBHelper(this).getTossDetailsData(nameA, t1.getId());
         teamDetails3 = new DBHelper(this).getTossDetailsData(nameB, t2.getId());
         int idA = t1.getId();
         int idB = t2.getId();
        Intent intent = new Intent(EnterDetails.this, PlayerActivity.class);
        intent.putExtra("nameA", nameA);
        intent.putExtra("nameB", nameB);
        intent.putExtra("idA", idA);
        intent.putExtra("idB", idB);
        finish();
        startActivity(intent);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}