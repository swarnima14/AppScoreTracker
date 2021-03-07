package com.app.appnext.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.appnext.R;
import com.app.appnext.adapter.AdapterOne;
import com.app.appnext.modelclasses.ModelOne;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterOne adapterOne;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ModelOne> modelOneArrayList;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navView);

        drawerToggle=new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.openNavDrawer,
                R.string.closeNavDrawer);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(navigationView);
        navigationView.bringToFront();

      /*  recyclerView = findViewById(R.id.rvName);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        modelOneArrayList = new ArrayList<ModelOne>();
        modelOneArrayList.add(new ModelOne("abc"));
        modelOneArrayList.add(new ModelOne("def"));
        modelOneArrayList.add(new ModelOne("ghi"));
        modelOneArrayList.add(new ModelOne("xyz"));
        modelOneArrayList.add(new ModelOne("www"));
        modelOneArrayList.add(new ModelOne("mmm"));

        adapterOne=new AdapterOne(this,modelOneArrayList);
        recyclerView.setAdapter(adapterOne);*/

    }

  /*  @Override
    public void onItemClicked(int index) {

        String name = modelOneArrayList.get(index).getName();
        Intent intent = new Intent(MainActivity.this, AddAndViewDetails.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }*/

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.

        if (drawerToggle.onOptionsItemSelected(item))
            return true;


        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        //Fragment fragment = null;
        // Class fragmentClass;

        menuItem.setChecked(true);

        drawerLayout.closeDrawers();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent = null;

                    switch (item.getItemId()) {
                        case R.id.nav_1:
                            intent = new Intent(MainActivity.this, EnterDetails.class);
                            break;
                        case R.id.nav_2:
                            intent = new Intent(MainActivity.this, AddAndViewDetails.class);
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "illegal", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(intent);
                    return true;
                }
            };
}