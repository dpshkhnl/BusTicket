package webbank.com.busticket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Image.CircleTransform;
import webbank.com.busticket.Routes.ApiRoutes;
import webbank.com.busticket.Routes.RouteAdapter;
import webbank.com.busticket.Routes.RoutesAPI;
import webbank.com.busticket.Routes.RoutesModel;


public class MainActivity extends HomeActivity
        implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {

    private ImageView btnTicket;
    private Button searchBus;
    private TextView txtDate;
    private TextView txtDay;
    private TextView txtFrom;
    private TextView txtTo;
    private TextView txtFromSub;
    private TextView txtToSub;
    private LinearLayout linearDate;
    private LinearLayout linearSource;
    private LinearLayout linearDest;
    private LinearLayout linearFAQ;
    private ImageButton swapButton;
    Handler uiHandler = new Handler();
    WebView webview;
    ProgressDialog progressBar;
    private int sourceId;
    private int destId;
    TinyDB tinyDB;
    private List<RoutesModel> lstRoutes = new ArrayList<>();
    RouteAdapter cardArrayAdapter;

    DrawerLayout drawerLayout;


    @Override
    public void onBackPressed() {
       /* if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
*/
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();
      /*  */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.app_bar_main);
        getLayoutInflater().inflate(R.layout.content_main, frameLayout);

        tinyDB = new TinyDB(getApplicationContext());
        btnTicket = (ImageView) findViewById(R.id.btnTicket);
        searchBus = (Button) findViewById(R.id.btnSearch);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDay = (TextView) findViewById(R.id.txtDay);
        txtFrom = (TextView) findViewById(R.id.title1);
        txtTo = (TextView) findViewById(R.id.title2);
        txtFromSub = (TextView) findViewById(R.id.subtitle1);
        txtToSub = (TextView) findViewById(R.id.subtitle2);
        swapButton = (ImageButton) findViewById(R.id.btnSwap);

        Calendar now = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
        txtDate.setText(sdf.format(now.getTime()));
        txtDay.setText(sdfDay.format(now.getTime()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tinyDB.putString("date",dateFormat.format(now.getTime()));

        linearDate = (LinearLayout) findViewById(R.id.txtDateLinerar);
        linearSource = (LinearLayout) findViewById(R.id.linearSource);
        linearDest = (LinearLayout) findViewById(R.id.linearDest);
        linearFAQ = (LinearLayout) findViewById(R.id.linearFAQ);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        cardArrayAdapter = new RouteAdapter(getApplicationContext(), R.layout.card_source_destination);
        RoutesAPI mApiService = this.getInterfaceService();
        Call<ApiRoutes> mService = mApiService.loadAllRoutes();
        mService.enqueue(new Callback<ApiRoutes>() {
            @Override
            public void onResponse(Call<ApiRoutes> call, Response<ApiRoutes> response) {
                if(response.body() != null) {
                    lstRoutes = response.body().getResult();
                    for (RoutesModel card1 : lstRoutes) {
                        cardArrayAdapter.add(card1);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiRoutes> call, Throwable t) {
                call.cancel();
                Toast.makeText(MainActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });


        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtFrom.getText().toString().equals("Select Source"))
                {
                    Toast.makeText(v.getContext(), // <- Line changed
                            "Please select Source",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (txtTo.getText().toString().equals("Select Destination"))
                {
                    Toast.makeText(v.getContext(), // <- Line changed
                            "Please select Source",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isNetworkAvailable())
                {
                    MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                            .title("Databank Booking")
                            .content("No Internet Available")
                            .positiveText("Ok")
                            .show();
                    return;
                }


                Intent in = new Intent(getApplicationContext(), SearchBusActivity.class);
                startActivity(in);
            }
        });

        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = txtFrom.getText().toString();
                String subTitleText = txtFromSub.getText().toString();
                txtFrom.setText(txtTo.getText().toString());
                txtFromSub.setText(txtToSub.getText().toString());
                txtTo.setText(titleText);
                txtToSub.setText(subTitleText);
                int temp = sourceId;
                sourceId = destId;
                destId = temp;

            }
        });


        linearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                // dpd.setVersion(DatePickerDialog.Version.VERSION_2);

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        linearFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("FAQ");


                webview = new WebView(MainActivity.this);


                alert.setView(webview);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();

                WebSettings settings = webview.getSettings();
                settings.setJavaScriptEnabled(true);
                webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                progressBar = ProgressDialog.show(MainActivity.this, "", "Loading...");
                String primeDiv = "content_display";
                String html = new String();


                new MainActivity.BackgroundWorker().execute();


            }
        });

        linearSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.search_dialog, null);


                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.search);
                userInput.addTextChangedListener(filterTextWatcher);

                ListView list = (ListView) promptsView.findViewById(R.id.route_list);

                 AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);

                builderSingle.setTitle("Source");
                builderSingle.setView(promptsView);
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog ad = builderSingle.show();
                list.setAdapter(cardArrayAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        String strName = cardArrayAdapter.getItem(position).getFrom();
                        sourceId = cardArrayAdapter.getItem(position).getId();
                        txtFrom.setText(strName);
                        txtFromSub.setText(strName);
                        cardArrayAdapter.getFilter().filter("");
                        ad.dismiss();

                    }
                });

            }
        });


        linearDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.search_dialog, null);


                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.search);
                userInput.addTextChangedListener(filterTextWatcher);

               ListView list = (ListView) promptsView.findViewById(R.id.route_list);

                builderSingle.setView(promptsView);
                builderSingle.setTitle("Destination");
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog ad = builderSingle.show();

                list.setAdapter(cardArrayAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        String strName = cardArrayAdapter.getItem(position).getFrom();
                        txtTo.setText(strName);
                        txtToSub.setText(strName);
                        destId = cardArrayAdapter.getItem(position).getId();
                        cardArrayAdapter.getFilter().filter("");
                        ad.dismiss();
                    }
                });



            }
        });

        searchBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtFrom.getText().toString().equals("Select Source"))
                {
                    Toast.makeText(v.getContext(), // <- Line changed
                            "Please select Source",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (txtTo.getText().toString().equals("Select Destination"))
                {
                    Toast.makeText(v.getContext(), // <- Line changed
                            "Please select Destination",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isNetworkAvailable())
                {
                    MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                            .title("Databank Booking")
                            .content("No Internet Available")
                            .positiveText("Ok")
                            .show();
                    return;
                }

                tinyDB.putInt("SourceId", sourceId);
                tinyDB.putInt("DestinationId", destId);
                tinyDB.putString("SearchDate", txtDate.getText().toString());
                tinyDB.putString("SourceName", txtFrom.getText().toString());
                tinyDB.putString("DestinationName", txtTo.getText().toString());

                Intent in = new Intent(getApplicationContext(), SearchBusActivity.class);

                startActivity(in);
            }
        });



        View header = navigationView.getHeaderView(0);

        TextView txtName = (TextView) header.findViewById(R.id.name);
        TextView txtEmail = (TextView) header.findViewById(R.id.email);
        String email = tinyDB.getString("email");
        String name = tinyDB.getString("name");
        if(!name.equals("")|| !email.equals("")) {
            txtName.setText(name);
            txtEmail.setText(email);
        }
/*

        //  View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView txtName = (TextView) header.findViewById(R.id.name);
        TextView txtEmail = (TextView) header.findViewById(R.id.email);
        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);

        String name = "", email = "", logo = "";

        name = prefs.getString("name", "Not Logged In");//"No name defined" is the default value.
        email = prefs.getString("email", ""); //0 is the default value.
        logo = prefs.getString("logo", "No Logo");

      *//*  Glide.with(this).load(logo)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);*//*
        txtName.setText(name);
        txtEmail.setText(email);*/
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            cardArrayAdapter.getFilter().filter(s);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
        txtDate.setText(sdf.format(cal.getTime()));
        tinyDB.putString("date",cal.getTime().toString());
        txtDay.setText(sdfDay.format(cal.getTime()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tinyDB.putString("date",dateFormat.format(cal.getTime()));
        datePickerDialog.dismiss();
        //Toast.makeText(MainActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
    }



    private RoutesAPI getInterfaceService() {

        String base_url = getResources().getString(R.string.BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RoutesAPI mInterfaceService = retrofit.create(RoutesAPI.class);
        return mInterfaceService;
    }


    public void logOut()
    {
        SharedPreferences.Editor editor = getSharedPreferences("LoginPref", MODE_PRIVATE).edit();
        editor.putString("name", "");
        editor.putString("email", "");
        editor.putString("logo", "");
        editor.putString("token", "");
        editor.commit();
        Intent in = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(in);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class BackgroundWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            getDarewod();
            return null;
        }

        public void getDarewod() {

            try {
                Document htmlDocument = Jsoup.connect("http://www.databankbooking.com/information/archive/faqs").get();
                Element element = htmlDocument.select("#content_display").first();

                // replace body with selected element
                htmlDocument.body().empty().append(element.toString());
                final String html = htmlDocument.toString();

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        webview.loadData(html, "text/html", "UTF-8");
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

