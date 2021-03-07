package com.app.appnext.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appnext.R;
import com.app.appnext.adapter.AdapterViewRun;
import com.app.appnext.databasehelper.DBHelper;
import com.app.appnext.databasehelper.RestorableSQLiteDatabase;
import com.app.appnext.modelclasses.PlayerScore;
import com.app.appnext.modelclasses.TeamDetails;
import com.app.appnext.modelclasses.ViewOverRuns;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ScoreCardActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvStriker, tvNon, tvBowler, tvStrikerRun, tvNonRun, tvBowlerRuns, tvRun, tvBowlerWickets, tvWicket,tvName, tvBowlerER, tvBowlerOvers;
    TextView strikerFours, strikerSixes, nonFours, nonSixes, tvOvers, tvStrikerRate, tvNonRate, tvStrikerBalls, tvNonBalls, tvTotalOvers;
    MaterialButton btnSwap, btnRetire, btnB, btnNB, btnWB, btnLB, btnWicket, btnPenalty, btnRetireBowler, btnUndo;
    RadioButton  rbS, rbN;
    TextView rb0,rb1,rb2,rb3,rb4,rb5,rb6;
    EditText etRetire, etRetireBowler;
    MaterialCardView card1, card2, card3, card4, card6;

    PlayerScore playerScore1, playerScore2, playerScore3;
    CardView cardRetire;
    DecimalFormat decimalFormat;

    String striker, nonStriker, bowler, strikerName, nonName, nameA, nameB, type;

    String s1,s2, status, retire, playerToBe;
    Button btnDone, btnDoneBowler;
    double strikerRate, nonRate, bowlerER;
    int totalRuns, totalWickets, strikerBallCount, nonBallCount, totalOvers, ballCount, nRetire = 0, bowlerWickets, overB;
    TeamDetails teamDetails1,teamDetails2;
    int idA, idB, idS, idN, idBowl, strikeID, nonId, bowlId, id, retId, remainder, quotient, currOvers;
    Boolean wb = false, nb = false, lb = false, b = false;
    Double val;

    String overView;
    RecyclerView rvView;
    ArrayList<ViewOverRuns> viewOverRuns;
    AdapterViewRun adapterViewRun;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_card_layout);

        striker = getIntent().getStringExtra("striker");
        nonStriker = getIntent().getStringExtra("non");
        bowler = getIntent().getStringExtra("bowler");

        nameA = getIntent().getStringExtra("nameA");
        nameB = getIntent().getStringExtra("nameB");
        idA = getIntent().getIntExtra("idA", 0);
        idB = getIntent().getIntExtra("idB", 0);
        idS = getIntent().getIntExtra("idS", 0);
        idN = getIntent().getIntExtra("idN", 0);
        idBowl = getIntent().getIntExtra("idBowl", 0);

        tvStriker = findViewById(R.id.tvStriker);
        tvStrikerRun = findViewById(R.id.tvStrikerRun);
        tvNon = findViewById(R.id.tvNon);
        tvNonRun = findViewById(R.id.tvNonRuns);
        tvBowler = findViewById(R.id.tvBowler);
        tvBowlerRuns = findViewById(R.id.tvBowlerRuns);
        tvBowlerWickets = findViewById(R.id.tvBowlerWickets);
        tvRun = findViewById(R.id.tvRun);
        tvWicket = findViewById(R.id.tvWicket);
        tvName = findViewById(R.id.tvName);
        nonFours = findViewById(R.id.nonFours);
        nonSixes = findViewById(R.id.nonSixes);
        strikerFours = findViewById(R.id.strikerFours);
        strikerSixes = findViewById(R.id.strikerSixes);
        tvStrikerRate = findViewById(R.id.strikerRate);
        tvNonRate = findViewById(R.id.nonRate);
        tvOvers = findViewById(R.id.tvOvers);
        tvStrikerBalls = findViewById(R.id.tvStrikerBalls);
        tvNonBalls = findViewById(R.id.tvNonBalls);
        tvTotalOvers = findViewById(R.id.tvTotalOvers);
        tvBowlerER = findViewById(R.id.tvBowlerER);
        tvBowlerOvers = findViewById(R.id.tvBowlerOvers);

        btnSwap = findViewById(R.id.btnSwap);
        btnRetire = findViewById(R.id.btnRetire);
        btnDone = findViewById(R.id.btnDone);
        btnB = findViewById(R.id.btnB);
        btnLB = findViewById(R.id.btnLB);
        btnWB = findViewById(R.id.btnWb);
        btnNB = findViewById(R.id.btnNb);
        btnWicket = findViewById(R.id.btnWicket);
        btnPenalty = findViewById(R.id.btnPenalty);
        btnRetireBowler = findViewById(R.id.btnRetireBowler);
        btnDoneBowler = findViewById(R.id.btnDoneBowler);
        btnUndo = findViewById(R.id.btnUndo);

        etRetire = findViewById(R.id.etRetire);
        etRetireBowler = findViewById(R.id.etRetireBowler);

        cardRetire = findViewById(R.id.card5);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card6 = findViewById(R.id.card6);

        rb0 = findViewById(R.id.rb0); rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2); rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4); rb5 = findViewById(R.id.rb5);
        rb6 = findViewById(R.id.rb6); rbS = findViewById(R.id.rbS);
        rbN = findViewById(R.id.rbN);

        decimalFormat = new DecimalFormat(".#");

        rvView = findViewById(R.id.rvOverRuns);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvView.setLayoutManager(linearLayoutManager);
        viewOverRuns = new ArrayList<>();

        strikerName = striker;
        nonName = nonStriker;
        strikeID = idS;
        nonId = idN;
        bowlId = idBowl;
        setUpFields();

    }

    public void setUpFields() {

        rb0.setOnClickListener(this);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb3.setOnClickListener(this);
        rb4.setOnClickListener(this);
        rb5.setOnClickListener(this);
        rb6.setOnClickListener(this);

        teamDetails1 = new DBHelper(this).getTossDetailsData(nameA, idA);
        teamDetails2 = new DBHelper(this).getTossDetailsData( nameB,idB);

        if(teamDetails1.getChoose().equals("bat")) {
            tvName.setText(teamDetails1.getName());
            tvRun.setText(String.valueOf(teamDetails1.getRuns()));
            tvWicket.setText(String.valueOf(teamDetails1.getWickets()));
            tvTotalOvers.setText(String.valueOf(teamDetails1.getTotalOvers()));
            tvOvers.setText(String.valueOf(teamDetails1.getCurrentOvers()));
            totalOvers = teamDetails1.getTotalOvers();
            val = teamDetails1.getCurrentOvers();
        }
        if(teamDetails2.getChoose().equals("bat")) {
            tvName.setText(teamDetails2.getName());
            tvRun.setText(String.valueOf(teamDetails2.getRuns()));
            tvWicket.setText(String.valueOf(teamDetails2.getWickets()));
            tvTotalOvers.setText(String.valueOf(teamDetails2.getTotalOvers()));
            tvOvers.setText(String.valueOf(teamDetails2.getCurrentOvers()));
            totalOvers = teamDetails2.getTotalOvers();
            val = teamDetails2.getCurrentOvers();
        }

        setData(retire);

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                functionSwap();

            }
        });

    btnRetire.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            card6.setVisibility(View.INVISIBLE);
            rbS.setText(strikerName);
            rbN.setText(nonName);

            cardRetire.setVisibility(View.VISIBLE);

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    retire = etRetire.getText().toString();

                    if (rbS.isChecked())
                        playerToBe = strikerName;
                    if (rbN.isChecked())
                        playerToBe = nonName;

                    if(nRetire < 10)
                    setData(retire);

                    cardRetire.setVisibility(View.INVISIBLE);
                    nRetire++;

                    /*if(nRetire <= 10)
                    updateWicket(nRetire, idBowl);*/
                }
            });
        }
    });
    btnWB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            wb = true;
            btnLB.setEnabled(false);
            btnNB.setEnabled(false);
            btnB.setEnabled(false);
        }
    });
    btnNB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nb = true;
            btnWB.setEnabled(false);
        }
    });
    btnLB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            lb = true;
            rb0.setEnabled(false);
            rb0.setBackgroundResource(R.drawable.runs_button_background_disable);
            btnWB.setEnabled(false);
            btnWB.setEnabled(false);
        }
    });
    btnB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            b = true;
            rb0.setEnabled(false);
            rb0.setBackgroundResource(R.drawable.runs_button_background_disable);
        }
    });

    btnWicket.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ScoreCardActivity.this, OutActivity.class);
            intent.putExtra("idS",strikeID);
            intent.putExtra("idN",nonId);
            intent.putExtra("idBowl",idBowl);
            startActivityForResult(intent, 100);
        }
    });

    btnRetireBowler.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cardRetire.setVisibility(View.INVISIBLE);
            card6.setVisibility(View.VISIBLE);
            btnDoneBowler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ScoreCardActivity.this, "", Toast.LENGTH_SHORT).show();
                    String newBowler = etRetireBowler.getText().toString();
                    PlayerScore playerScoreBowler = new DBHelper(getApplicationContext()).insertPlayerData(newBowler, "bowler", 0, 0 ,0 , 0, 0, 0.0);
                    idBowl = playerScoreBowler.getId();
                    bowlId = playerScoreBowler.getId();
                    bowler = newBowler;
                    Toast.makeText(ScoreCardActivity.this, "b: "+bowler, Toast.LENGTH_SHORT).show();
                    tvBowler.setText(bowler);
                    tvBowlerRuns.setText(String.valueOf(playerScoreBowler.getRuns()));
                    tvBowlerWickets.setText(String.valueOf(playerScoreBowler.getWickets()));
                    tvBowlerOvers.setText(String.valueOf(0.0));
                    tvBowlerER.setText(String.valueOf(0.0));
                    setData(retire);
                    card6.setVisibility(View.INVISIBLE);
                }
            });

        }
    });

    btnPenalty.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ScoreCardActivity.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(ScoreCardActivity.this).inflate(R.layout.penalty_layout, viewGroup, false);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            alertDialog.findViewById(R.id.rbNeg1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns - 1;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });
            alertDialog.findViewById(R.id.rbNeg2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns - 2;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });
            alertDialog.findViewById(R.id.rbNeg3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns - 3;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });
            alertDialog.findViewById(R.id.rbNeg4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns - 4;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });
            alertDialog.findViewById(R.id.rbNeg5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns - 5;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });
            alertDialog.findViewById(R.id.rbPos1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns + 1;updateTeamRuns(totalRuns);
                }
            });
            alertDialog.findViewById(R.id.rbPos2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns + 2;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });
            alertDialog.findViewById(R.id.rbPos3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns + 3;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });
            alertDialog.findViewById(R.id.rbPos4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns + 4;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });
            alertDialog.findViewById(R.id.rbPos5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalRuns = totalRuns + 5;updateTeamRuns(totalRuns);alertDialog.dismiss();
                }
            });

        }
    });

    btnUndo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*DBHelper dbHelper = new DBHelper(ScoreCardActivity.this);

            dbHelper.undoData("INSERT PLAYER DATA");
            setData(retire);*/
        }
    });
}

    private void functionSwap() {

        s1 = tvStriker.getText().toString();
        s2 = tvNon.getText().toString();

        strikerName = s2;
        nonName = s1;

        id = strikeID;
        strikeID = nonId;
        nonId = id;

        Boolean bool1 = new DBHelper(getApplicationContext()).updateStatus(s1,"striker");
        Boolean bool2 = new DBHelper(getApplicationContext()).updateStatus(s2,"non-striker");

        setData(retire);
    }


    private void setData(String retire) {

        PlayerScore player = null;

            if (retire != null && playerToBe.equals(strikerName)) {
                Boolean ret = new DBHelper(getApplicationContext()).onRetire(playerToBe);
                PlayerScore addRet = new DBHelper(getApplicationContext()).insertPlayerData(retire, "striker", 0, 0, 0, 0, 0, 0.0);
                retId = addRet.getId();
                playerScore1 = new DBHelper(this).getPlayersData(retire, retId);
                playerScore2 = new DBHelper(this).getPlayersData(nonName, nonId);
                strikerName = retire;
                striker = retire;
                strikeID = retId;
                strikerBallCount = 0;
                nonBallCount = playerScore2.getBalls();
                strikerRate = 0.0;
                retire = null;
            }
            else if (retire != null && playerToBe.equals(nonName)) {
                Boolean ret = new DBHelper(getApplicationContext()).onRetire(playerToBe);
                PlayerScore addRet = new DBHelper(getApplicationContext()).insertPlayerData(retire, "non-striker", 0, 0, 0, 0, 0, 0.0);
                retId = addRet.getId();
                playerScore1 = new DBHelper(this).getPlayersData(strikerName, strikeID);
                playerScore2 = new DBHelper(this).getPlayersData(retire, retId);
                nonName = retire;
                nonStriker = retire;
                nonId = retId;
                nonBallCount = 0;
                strikerBallCount = playerScore1.getBalls();
                nonRate = 0.0;
                retire = null;
            }
            else{
                playerScore1 = new DBHelper(this).getPlayersData(strikerName, strikeID);
                playerScore2 = new DBHelper(this).getPlayersData(nonName, nonId);
            }

        playerScore3 = new DBHelper(this).getPlayersData(bowler, idBowl);

        ArrayList<PlayerScore> playerScoreArrayList;
        playerScoreArrayList = new ArrayList<PlayerScore>();
        playerScoreArrayList.add(playerScore1);
        playerScoreArrayList.add(playerScore2);
        playerScoreArrayList.add(playerScore3);

        for(int i=0; i<3 ;i++){
            player = playerScoreArrayList.get(i);
            if(player!=null)
            if(player.getStatus().equals("striker")) {
                strikerName = player.getName();
                tvStriker.setText(player.getName());
                tvStrikerRun.setText(String.valueOf(player.getRuns()));
                strikerFours.setText(String.valueOf(player.getFours()));
                strikerSixes.setText(String.valueOf(player.getSixes()));
                tvStrikerBalls.setText(String.valueOf(player.getBalls()));
                tvStrikerRate.setText(String.valueOf(player.getStrikeRate()));

                if(player.getBalls()>0) {
                    player.setStrikeRate(player.getRuns()*100.0/player.getBalls());
                    strikerRate = player.getStrikeRate();
                    tvStrikerRate.setText(String.valueOf(strikerRate));
                }
            }
            if(player.getStatus().equals("non-striker")) {
                nonName = player.getName();
                tvNon.setText(player.getName());
                tvNonRun.setText(String.valueOf(player.getRuns()));
                nonSixes.setText(String.valueOf(player.getSixes()));
                nonFours.setText(String.valueOf(player.getFours()));
                tvNonBalls.setText(String.valueOf(player.getBalls()));
                tvNonRate.setText(String.valueOf(player.getStrikeRate()));

                if(player.getBalls()>0) {
                    player.setStrikeRate(player.getRuns()*100.0/player.getBalls());
                    nonRate = player.getStrikeRate();
                    tvNonRate.setText(String.valueOf(nonRate));
                }
            }
            if(player.getStatus().equals("bowler")) {
                tvBowler.setText(player.getName());
                tvBowlerRuns.setText(String.valueOf(player.getRuns()));
                overB = player.getBalls();
                double fi;
                int x = overB / 6;
                int y = overB % 6;
                fi = x + y * 0.1;
                if(fi>0){
                    player.setStrikeRate(player.getRuns()/fi);
                    tvBowlerOvers.setText(String.valueOf(fi));
                    tvBowlerER.setText(String.valueOf(player.getStrikeRate()));
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

            if (currOvers < totalOvers * 6) {
                switch (view.getId()) {
                    case R.id.rb0: {
                        if(lb || b){
                            Toast.makeText(this, "Not applicable", Toast.LENGTH_SHORT).show();
                        }
                        updateRun(strikeID, 0);
                        break;
                    }
                    case R.id.rb1:
                        updateRun(strikeID, 1);
                        break;

                    case R.id.rb2:
                        updateRun(strikeID, 2);
                        break;

                    case R.id.rb3:
                        updateRun(strikeID, 3);
                        break;

                    case R.id.rb4:
                        updateRun(strikeID, 4);
                        break;
                    case R.id.rb5:
                        updateRun(strikeID, 5);
                        break;
                    case R.id.rb6:
                        updateRun(strikeID, 6);
                        break;
                }
            }
        }


    private void updateRun(int n, int run){
        PlayerScore p1, p2, playerScore1, playerScore2 = null;

        Double finalrate;
        if(!nb){
        currOvers++;

        quotient = currOvers / 6;
        remainder = currOvers % 6;
        val = quotient + (remainder * 0.1);
        Toast.makeText(this, "val: "+ val , Toast.LENGTH_SHORT).show();
    }

    if (strikerName.equals(striker)) {
        if(!wb) {

            strikerBallCount++;

            ballCount = strikerBallCount;
        }
    } else {
        if(!wb) {
            nonBallCount++;
            ballCount = nonBallCount;
        }
    }


    playerScore1 = new DBHelper(this).getPlayersData(strikerName, n);
        if(b && nb){
            playerScore1.setRuns(playerScore1.getRuns());
            overView = "B + NB";

        }
        else if(wb && b){
            playerScore1.setRuns(playerScore1.getRuns());
            overView = "B + WB";
        }
    else if(nb) {
        playerScore1.setRuns(playerScore1.getRuns() + run);
            overView = "NB";
    }
    else if(wb){
        playerScore1.setRuns(playerScore1.getRuns());
            overView = "WB";
    }
    else if(b || lb){
        playerScore1.setRuns(playerScore1.getRuns());
        if(b)
            overView = "B";
        if(lb)
            overView = "LB";
    }

    else{
        playerScore1.setRuns(playerScore1.getRuns() + run);
        overView = String.valueOf(run);
    }
    playerScore1.setBalls(ballCount);
    if (strikerName.equals(striker)) {
        strikerRate = playerScore1.getRuns() * 100.0 / playerScore1.getBalls();
        playerScore1.setStrikeRate(strikerRate);
        finalrate = strikerRate;
    } else {
        nonRate = playerScore1.getRuns() * 100.0 / playerScore1.getBalls();
        playerScore1.setStrikeRate(nonRate);
        finalrate = nonRate;
    }
    playerScore1.setStrikeRate(finalrate);

    if (run == 4)
        playerScore1.setFours(playerScore1.getFours() + 1);
    if (run == 6)
        playerScore1.setSixes(playerScore1.getSixes() + 1);
    p1 = new DBHelper(this).onUpdatePlayersData(n, playerScore1);

    tvStrikerRun.setText(String.valueOf(p1.getRuns()));
    tvStriker.setText(p1.getName());
    strikerFours.setText(String.valueOf(p1.getFours()));
    strikerSixes.setText(String.valueOf(p1.getSixes()));
    tvStrikerBalls.setText(String.valueOf(p1.getBalls()));
    tvStrikerRate.setText(String.valueOf(p1.getStrikeRate()));
    //}


            playerScore2 = new DBHelper(this).getPlayersData(bowler, bowlId);

        if(b && nb){
            totalRuns = totalRuns + run + 1;
            playerScore2.setRuns(playerScore2.getRuns());
            b = false;
            nb = false;
            rb0.setEnabled(true);
            rb0.setBackgroundResource(R.drawable.ripple_background);
            functionSwap();
        }
        else if(wb && b){
            totalRuns = totalRuns + run + 1;
            playerScore2.setRuns(playerScore2.getRuns() + run + 1);
            /*wb = false;
            b = false;*/
            rb0.setEnabled(true);
            rb0.setBackgroundResource(R.drawable.ripple_background);
        }
        else if(wb || nb){
                totalRuns = totalRuns + run + 1;
                playerScore2.setRuns(playerScore2.getRuns() + run + 1);
                nb = false;
            }
            else if(b || lb){
                totalRuns = totalRuns + run;
                playerScore2.setRuns(playerScore2.getRuns());
            }

            else{
                totalRuns = totalRuns + run;
                playerScore2.setRuns(playerScore2.getRuns() + run);
            }



            double fi;
            int x,y;
            overB = playerScore2.getBalls();
             x = overB / 6;
            y = overB % 6;
            fi = x + y * 0.1;
            playerScore2.setStrikeRate(playerScore2.getRuns() / fi );

            overB++;
            playerScore2.setBalls(overB);
             x = overB / 6;
             y = overB % 6;
            fi = x + y * 0.1;
            tvBowlerOvers.setText(String.valueOf(fi));



            p2 = new DBHelper(this).onUpdatePlayersData(bowlId, playerScore2);
            tvBowler.setText(p2.getName());
            tvBowlerRuns.setText(String.valueOf(p2.getRuns()));
            tvBowlerER.setText(String.valueOf(p2.getStrikeRate()));

            if(remainder == 1){
                viewOverRuns.clear();
            }
            viewOverRuns.add(new ViewOverRuns(String.valueOf(overView)));
            adapterViewRun = new AdapterViewRun(this, viewOverRuns);
            rvView.setAdapter(adapterViewRun);

            updateTeamRuns(totalRuns);
        if(b || lb){
            if(run % 2 != 0){
                functionSwap();
            }
            b = false;
            lb = false;
            rb0.setEnabled(true);
            rb0.setBackgroundResource(R.drawable.ripple_background);
        }


    }

    private void updateWicket(int wicket, int i){

        PlayerScore p, p1 = null;
        p = new DBHelper(this).getPlayersData(bowler, i);
        p1 = new DBHelper(this).onUpdatePlayersWicket(wicket, p.getId());
        tvBowlerWickets.setText(String.valueOf(p1.getWickets()));

        totalWickets = wicket + totalWickets;

        updateTeamWickets(totalWickets);
    }

    private void updateTeamRuns(int totalRuns) {
        //to update team details

        TeamDetails details1 = null;
        if(tvName.getText().toString().equals(nameA)) {
            details1 = new DBHelper(this).updateTeamTossRuns(nameA, idA, totalRuns+teamDetails1.getRuns());

            if(wb)
            {
                details1.setRuns(details1.getRuns()+1);
                wb = false;
            }
            else if(nb){
                nb = false;
            }
            else if(b || lb){
                details1.setRuns(details1.getRuns());
            }
            tvRun.setText(String.valueOf(details1.getRuns()));
            details1.setCurrentOvers(val);
            details1 = new DBHelper(this).updateTeamDetails(idA, details1);
            tvOvers.setText(String.valueOf(details1.getCurrentOvers()));
        }
        if(tvName.getText().toString().equals(nameB)) {
            details1 = new DBHelper(this).updateTeamTossRuns(nameB, idB, totalRuns+teamDetails2.getRuns());
            tvRun.setText(String.valueOf(details1.getRuns()));
            details1.setCurrentOvers(val);
            details1 = new DBHelper(this).updateTeamDetails(idB, details1);
            tvOvers.setText(String.valueOf(details1.getCurrentOvers()));
        }

    }

    private void updateTeamWickets(int totalWickets) {
        //to update team details
        TeamDetails details1 = null;
        if(tvName.getText().toString().equals(nameA)) {
            details1 = new DBHelper(this).updateTeamTossWickets(nameA, idA, totalWickets);
            tvWicket.setText(String.valueOf(details1.getWickets()));
        }
        if(tvName.getText().toString().equals(nameB)) {
            details1 = new DBHelper(this).updateTeamTossWickets(nameB, idB, totalWickets);
            tvWicket.setText(String.valueOf(details1.getWickets()));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100)
        {
            bowlerWickets++;

            String who;
            playerToBe = data.getStringExtra("oldS");
            retire = data.getStringExtra("newS");
            who = data.getStringExtra("who");
            if(who.equals("str")) {
                strikeID = data.getIntExtra("idSout", 0);
            }
            else if(who.equals("non")) {
                nonId = data.getIntExtra("idSout", 0);
            }
            currOvers++;
            tvOvers.setText(String.valueOf(currOvers/6));
            setData(retire);
            updateWicket(bowlerWickets, idBowl);
        }

    }
}
