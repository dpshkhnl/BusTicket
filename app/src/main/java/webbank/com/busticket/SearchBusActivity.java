package webbank.com.busticket;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Bus.BusCard;
import webbank.com.busticket.Bus.BusCardAdapter;
import webbank.com.busticket.Bus.BusSearchAPIModel;
import webbank.com.busticket.Bus.BusSearchInterface;
import webbank.com.busticket.Bus.BusSearchModel;
import webbank.com.busticket.Bus.PostSearchModel;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Routes.ApiRoutes;
import webbank.com.busticket.Routes.RouteAdapter;
import webbank.com.busticket.Routes.RoutesAPI;
import webbank.com.busticket.Routes.RoutesModel;

import static java.util.Locale.*;

/**
 * Created by Dpshkhnl on 2017-03-15.
 */

public class SearchBusActivity extends AppCompatActivity {

    ListView listView;
    List<BusCard> lstBusCard = new ArrayList<>();
    TinyDB tinyDB ;
    TextView txtFrom,txtTo,txtDate;
    TextView txtAvailableBus,tripNotFound;
    ImageView tripNF;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    BusCardAdapter cardArrayAdapter;
    private List<BusSearchModel> lstAvailableBuses = new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_bus);
            txtFrom = (TextView) findViewById(R.id.txtFrom);
            txtTo =(TextView) findViewById(R.id.txtTo);
            txtDate =(TextView) findViewById(R.id.txtDate);
            txtAvailableBus =(TextView) findViewById(R.id.txtAvailableBus);
            tripNotFound =(TextView) findViewById(R.id.notFound);
            tripNF = (ImageView) findViewById(R.id.imgTripNF);

            tinyDB = new TinyDB(getApplicationContext());

          int sourceId=  tinyDB.getInt("SourceId");
          int destId =  tinyDB.getInt("DestinationId");
            String date = tinyDB.getString("SearchDate");
           String sourceName= tinyDB.getString("SourceName");
            String destName= tinyDB.getString("DestinationName");
            String date3= tinyDB.getString("date");
            txtFrom.setText(sourceName);
            txtTo.setText(destName);
            txtDate.setText(date);


            BusSearchInterface mApiService = this.getInterfaceService();
            PostSearchModel postSearchModel = new PostSearchModel();
            postSearchModel.setFrom(sourceId);
            postSearchModel.setTo(destId);
            postSearchModel.setDate(date3);
            cardArrayAdapter = new BusCardAdapter(getApplicationContext(), R.layout.list_bus_card);


            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(SearchBusActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Its loading....");
            progressDoalog.setTitle("Searching bus for your route ");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // show it
            progressDoalog.show();
            listView = (ListView) findViewById(R.id.bus_listView);
            Call<BusSearchAPIModel> mService = mApiService.searchBuses(postSearchModel.getFrom(),postSearchModel.getTo(),postSearchModel.getDate());
            mService.enqueue(new Callback<BusSearchAPIModel>() {
                @Override
                public void onResponse(Call<BusSearchAPIModel> call, Response<BusSearchAPIModel> response) {
                    if(response.body()!=null) {
                        lstAvailableBuses = response.body().getResult();
                        for (BusSearchModel card1 : lstAvailableBuses) {
                            BusCard busCard = new BusCard();
                            busCard.setCompanyName(card1.getBus_name());
                            busCard.setBusId(card1.getSch_id());
                            busCard.setBusNo(card1.getBus_id());
                            busCard.setBoardingPoint(card1.getFirst_boradigpoint());
                            busCard.setAllBoardingPoint(card1.getBoardingpoint());
                            busCard.setDateTime(card1.getDeparture() + " " + card1.getDeparturetime());
                            busCard.setFare(card1.getNetfare());
                            busCard.setLstFeatures(card1.getFeatures());
                            busCard.setAllDroppingPoint(card1.getDroppingpoint());
                            busCard.setBoardingTime(card1.getBoardingtime());
                            busCard.setDeportingTime(card1.getDeparturetime());
                            // busCard.set
                            busCard.setArrivalDateTime(card1.getDeparture() + " " + card1.getDeparturetime());
                            lstBusCard.add(busCard);
                        }
                        if (lstAvailableBuses.size() == 0) {
                            txtAvailableBus.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            tripNotFound.setVisibility(View.VISIBLE);
                            tripNF.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            txtAvailableBus.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.VISIBLE);
                            tripNotFound.setVisibility(View.INVISIBLE);
                            tripNF.setVisibility(View.INVISIBLE);
                        }


                        for (BusCard card1 : lstBusCard) {
                            cardArrayAdapter.add(card1);
                        }
                        listView.setAdapter(cardArrayAdapter);
                        progressDoalog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<BusSearchAPIModel> call, Throwable t) {
                    call.cancel();
                    progressDoalog.dismiss();
                    Toast.makeText(SearchBusActivity.this, "No Results Found", Toast.LENGTH_LONG).show();
                }
            });




        }
    private BusSearchInterface getInterfaceService() {

        String base_url = getResources().getString(R.string.BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final BusSearchInterface mInterfaceService = retrofit.create(BusSearchInterface.class);
        return mInterfaceService;
    }

}
