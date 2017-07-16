package webbank.com.busticket;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Boarding.BoardingCardAdapter;
import webbank.com.busticket.Boarding.BoardingPoint;
import webbank.com.busticket.Bus.BusCard;
import webbank.com.busticket.Bus.BusCardAdapter;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Routes.ApiRoutes;
import webbank.com.busticket.Routes.RoutesAPI;
import webbank.com.busticket.Routes.RoutesModel;

/**
 * Created by Dpshkhnl on 2017-03-18.
 */

public class BoardingActivity extends AppCompatActivity {

    ListView listView;
    List<BoardingPoint> lstBusCard = new ArrayList<>();
    BoardingCardAdapter cardArrayAdapter;
    private List<RoutesModel> lstRoutes = new ArrayList<>();
    TinyDB tinyDB ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);

        listView = (ListView) findViewById(R.id.lstBoarding);
        tinyDB = new TinyDB(getApplicationContext());
      String allBoardingPoint=  tinyDB.getString("boardingPoint");
        String allBoardingtime =tinyDB.getString("boardingTime");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Boarding Point");

        final String[] parts = allBoardingPoint.split(",");
        final String[] depart = allBoardingtime.split(",");


        cardArrayAdapter = new BoardingCardAdapter(getApplicationContext(), R.layout.boarding_card);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(BoardingActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading Boarding point");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        RoutesAPI mApiService = this.getInterfaceService();
        Call<ApiRoutes> mService = mApiService.loadAllRoutes();
        mService.enqueue(new Callback<ApiRoutes>() {
            @Override
            public void onResponse(Call<ApiRoutes> call, Response<ApiRoutes> response) {
                lstRoutes = response.body().getResult();
                int i= 0;
                for (RoutesModel card1 : lstRoutes) {
                    for (String part : parts)
                    {
                        if (part.equals(String.valueOf(card1.getId())))
                        {
                           BoardingPoint busCard= new BoardingPoint();
                            busCard.setBoardingId(card1.getId());
                            busCard.setBoardingPoint(card1.getFrom());
                            busCard.setTime(depart[i]);
                            busCard.setBoardOrDeport("Boarding");
                            cardArrayAdapter.add(busCard);
                            i++;
                        }
                    }

                }
                progressDoalog.hide();
            }

            @Override
            public void onFailure(Call<ApiRoutes> call, Throwable t) {
                call.cancel();
                Toast.makeText(BoardingActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
                progressDoalog.hide();
            }
        });


        for (BoardingPoint card1 : lstBusCard) {
            cardArrayAdapter.add(card1);
        }
        listView.setAdapter(cardArrayAdapter);
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


}
