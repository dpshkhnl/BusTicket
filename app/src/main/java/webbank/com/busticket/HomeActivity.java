package webbank.com.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;

import webbank.com.busticket.Data.TinyDB;

/**
 * Created by Dpshkhnl on 2017-04-26.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    TinyDB tinyDB ;
    View header;
    TextView txtName;
    TextView txtEmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home);;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tinyDB = new TinyDB(getApplicationContext());
        frameLayout = (FrameLayout) findViewById(R.id.frameNew);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         header = navigationView.getHeaderView(0);
        String email = tinyDB.getString("email");
        String name = tinyDB.getString("name");

        if (!email.equals("")) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
            navigationView.getMenu().findItem(R.id.logout).setVisible(false);
        }


        // View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
         txtName = (TextView) header.findViewById(R.id.name);
         txtEmail = (TextView) header.findViewById(R.id.email);
        /*SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);

        String name = "", email = "", logo = "";

        name = prefs.getString("name", "Not Logged In");//"No name defined" is the default value.
        email = prefs.getString("email", ""); //0 is the default value.
        logo = prefs.getString("logo", "No Logo");*/

      /*  Glide.with(this).load(logo)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);*/
      if(!name.equals("")|| !email.equals("")) {
          txtName.setText(name);
          txtEmail.setText(email);
      }
    }


        //navigationView.getMenu().performIdentifierAction(R.id.home, 0);



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);

        startActivity(intent);
    }


    @Override
    protected void onResume(){
        super.onResume();
        // put your code here...

        String email = tinyDB.getString("email");
        String name = tinyDB.getString("name");
        if(!name.equals("")|| !email.equals("")) {
            txtName.setText(name);
            txtEmail.setText(email);
        }
        if (!email.equals("")) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
            navigationView.getMenu().findItem(R.id.logout).setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //to prevent current item select over and over
       /* if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }*/
        String email = tinyDB.getString("email");

            if (id == R.id.home) {
                if (email.equals(""))
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                else
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                // fragment = new DashboardActivity();
            } else if (id == R.id.dashboard) {
                if (email.equals(""))
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                else
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            } else if (id == R.id.profileupd) {
                if (email.equals(""))
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                else
                    startActivity(new Intent(getApplicationContext(), UpdateActivity.class));
            } else if (id == R.id.nav_change_pwd) {
                if (email.equals(""))
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                else
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
            } else if (id == R.id.travelHistory) {
                if (email.equals(""))
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                else
                    startActivity(new Intent(getApplicationContext(), TravelHistoryActivity.class));
            } else if (id == R.id.nav_complain) {
                if (email.equals(""))
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                else
                    startActivity(new Intent(getApplicationContext(), ComplainActivity.class));
            } else if (id == R.id.nav_event) {
                startActivity(new Intent(getApplicationContext(), NewsAndEvent.class));
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
            } else if (id == R.id.nav_hook) {
                startActivity(new Intent(getApplicationContext(), BookProcess.class));
            } else if (id == R.id.nav_process) {
                startActivity(new Intent(getApplicationContext(), OurProcess.class));
            } else if (id == R.id.contactus) {
                startActivity(new Intent(getApplicationContext(), ContactUs.class));

            } else if (id == R.id.logout) {
                if(!email.equals(""))
                {
                    CountDownTimer mTimer;
                    final ProgressDialog progressDoalog;
                    progressDoalog = new ProgressDialog(HomeActivity.this);
                    progressDoalog.setMax(100);
                    progressDoalog.setMessage("Logging you Out...");
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it
                    progressDoalog.show();


                    mTimer = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            tinyDB.remove("email");
                            tinyDB.remove("name");
                            tinyDB.remove("cUserId");
                            LoginManager.getInstance().logOut();
                            progressDoalog.hide();
                            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(in);
                            finish();
                        }
                    };
                    mTimer.start();

                }else
                {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

                }

            } else if (id == R.id.login)  {
                if(!email.equals(""))
                {
                    CountDownTimer mTimer;
                    final ProgressDialog progressDoalog;
                    progressDoalog = new ProgressDialog(HomeActivity.this);
                    progressDoalog.setMax(100);
                    progressDoalog.setMessage("Logging you Out...");
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it
                    progressDoalog.show();


                    mTimer = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            tinyDB.remove("email");
                            tinyDB.remove("name");
                            tinyDB.remove("cUserId");
                            LoginManager.getInstance().logOut();
                            progressDoalog.hide();
                            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(in);
                            finish();
                        }
                    };
                    mTimer.start();



                }else
                {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

                }

                }



        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    public void logOut() {
    }
}

