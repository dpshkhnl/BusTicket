package webbank.com.busticket;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Bus.BusCard;
import webbank.com.busticket.Bus.BusCardAdapter;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Login.LoginInterface;
import webbank.com.busticket.Login.UserModel;
import webbank.com.busticket.Login.UserRest;
import webbank.com.busticket.TravelHistory.TravelAdapter;
import webbank.com.busticket.TravelHistory.TravelHistApiModel;
import webbank.com.busticket.TravelHistory.TravelHistoryModel;

/**
 * Created by Dpshkhnl on 2017-04-27.
 */

public class TravelHistoryActivity extends HomeActivity {

    List<TravelHistoryModel> lstTravelHistory = new ArrayList<>();
    TravelAdapter travelAdapter;
    ListView listView;
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_travel_hist, frameLayout);

        tinyDB = new TinyDB(getApplicationContext());
        travelAdapter = new TravelAdapter(getApplicationContext(), R.layout.card_travel_history);
        listView = (ListView) findViewById(R.id.travelHistory);
        setTitle("Travel History");
        int cUserId = tinyDB.getInt("cUserId");
        load(String.valueOf(cUserId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        //navigationView.getMenu().getItem(5).setChecked(true);
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
        progressDoalog = new ProgressDialog(TravelHistoryActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<TravelHistApiModel> mService = mApiService.findTravelHistory(id);
        mService.enqueue(new Callback<TravelHistApiModel>() {
            @Override
            public void onResponse(Call<TravelHistApiModel> call, Response<TravelHistApiModel> response) {
                lstTravelHistory = response.body().getResult();

                for (TravelHistoryModel card1 : lstTravelHistory) {
                    travelAdapter.add(card1);
                }
                listView.setAdapter(travelAdapter);
               // Toast.makeText(TravelHistoryActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<TravelHistApiModel> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(TravelHistoryActivity.this, "Unable To connect to server", Toast.LENGTH_LONG).show();
            }
        });
    }
}
