package webbank.com.busticket;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Complain.ComplainAPIModel;
import webbank.com.busticket.Complain.ComplainAdapter;
import webbank.com.busticket.Complain.ComplainModel;
import webbank.com.busticket.Login.LoginInterface;
import webbank.com.busticket.TravelHistory.TravelAdapter;
import webbank.com.busticket.TravelHistory.TravelHistApiModel;
import webbank.com.busticket.TravelHistory.TravelHistoryModel;

/**
 * Created by Dpshkhnl on 2017-04-27.
 */

public class ComplainActivity extends HomeActivity {

    List<ComplainModel> lstComplains = new ArrayList<>();
    ComplainAdapter complainAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_complain, frameLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                boolean wrapInScrollView = true;
                new MaterialDialog.Builder(ComplainActivity.this)
                        .title("Add Complain")
                        .positiveText("Submit")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getCustomView();
                                EditText txtSubject = (EditText) view.findViewById(R.id.subject);
                                Spinner txtServices = (Spinner) view.findViewById(R.id.txtService);
                                EditText txtMessage = (EditText) view.findViewById(R.id.txtMessage);
                                String subject =txtSubject.getText().toString();
                                String services = txtServices.getSelectedItem().toString();
                                String message = txtMessage.getText().toString();
                               // Toast.makeText(ComplainActivity.this, "Hello!! "+subject+","+services+","+message, Toast.LENGTH_LONG).show();

                                int cUserId = tinyDB.getInt("cUserId");

                                addComplain(String.valueOf(cUserId),message,subject,services);


                            }
                        })
                        .customView(R.layout.activity_add_complain, wrapInScrollView)
                        .show();
            }
        });


        complainAdapter = new ComplainAdapter(getApplicationContext(), R.layout.card_complain);
        listView = (ListView) findViewById(R.id.List);
        int cUserId = tinyDB.getInt("cUserId");
        load(String.valueOf(cUserId));
        setTitle("Complain");

    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        //navigationView.getMenu().getItem(6).setChecked(true);
    }

    private LoginInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final LoginInterface mInterfaceService = retrofit.create(LoginInterface.class);
        return mInterfaceService;
    }

    private void load(String id){
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ComplainActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<ComplainAPIModel> mService = mApiService.findAllComplian(id);
        mService.enqueue(new Callback<ComplainAPIModel>() {
            @Override
            public void onResponse(Call<ComplainAPIModel> call, Response<ComplainAPIModel> response) {
                lstComplains = response.body().getResult();
                for (ComplainModel card1 : lstComplains) {
                    complainAdapter.add(card1);
                }
                listView.setAdapter(complainAdapter);
                // Toast.makeText(TravelHistoryActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<ComplainAPIModel> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(ComplainActivity.this, "Unable To connect to server", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addComplain(final String id, String message,String subject,String services){
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ComplainActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<ComplainAPIModel> mService = mApiService.addCompains(id,subject,services,message);
        mService.enqueue(new Callback<ComplainAPIModel>() {
            @Override
            public void onResponse(Call<ComplainAPIModel> call, Response<ComplainAPIModel> response) {
                lstComplains = response.body().getResult();
                if(lstComplains.get(0).getMessage().contains("Success"))
                {
                    load(id);
                }
                 Toast.makeText(ComplainActivity.this,lstComplains.get(0).getMessage(), Toast.LENGTH_LONG).show();
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<ComplainAPIModel> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(ComplainActivity.this, "Unable To connect to server", Toast.LENGTH_LONG).show();
            }
        });
    }
}
