package com.sistec.sistecstudents;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AlertDialog.Builder builder;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_container, new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            logout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Top Right menu items
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_Logout) {
            logout();
            return true;
        }

        if (id == R.id.action_Feedback) {
            //TODO: get phone and user info, Intent to mail app and send feedback
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        //TODO:use different fragments to switch between there nav options
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (id) {
            case R.id.nav_home:
                fragmentTransaction.replace(R.id.home_container, new HomeFragment());
                break;
            case R.id.nav_acadmic:
                fragmentTransaction.replace(R.id.home_container, new AcadmicRecordFragment());
                break;
            case R.id.nav_attendance:
                fragmentTransaction.replace(R.id.home_container, new ViewAttendanceFragment());
                break;
            case R.id.nav_fee:
                fragmentTransaction.replace(R.id.home_container, new FeeStatusFragment());
                break;
            case R.id.nav_assignment:
                fragmentTransaction.replace(R.id.home_container, new AssignmentMarkFragment());
                break;
            case R.id.nav_midsem:
                fragmentTransaction.replace(R.id.home_container, new MidsemMarkFragment());
                break;
            case R.id.nav_change_pass:
                //ChangePassword Activity
                break;
            case R.id.nav_about:
                //About Activity
                break;
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(HomeActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setIcon(R.drawable.ic_log_out_red_24dp)
                .setTitle("Log Out?")
                .setMessage("Are you sure to want to log out...")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: clear cache and close app
                        HomeActivity.this.finish();
                    }
                })
                .setNegativeButton("Canle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
