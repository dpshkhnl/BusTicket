package webbank.com.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
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
import webbank.com.busticket.Bus.BusSearchAPIModel;
import webbank.com.busticket.Bus.BusSearchInterface;
import webbank.com.busticket.Bus.BusSearchModel;
import webbank.com.busticket.Data.MessageModel;
import webbank.com.busticket.Data.TicketModel;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Traveller.BookSeat;
import webbank.com.busticket.Traveller.BookSeatInterface;
import webbank.com.busticket.Traveller.MainTraveller;
import webbank.com.busticket.Traveller.TravellerAdapter;
import webbank.com.busticket.Traveller.TravellerDetails;
import webbank.com.busticket.Traveller.TravellerRecycleAdapter;

/**
 * Created by Dpshkhnl on 2017-03-18.
 */

public class TravellerActivity extends AppCompatActivity {

    List listView;
    RecyclerView recyclerView;
    List<TravellerDetails> lstBusCard = new ArrayList<>();
    TravellerAdapter cardArrayAdapter;
    TravellerRecycleAdapter cardRecycleAdapter;
    Button btnContinue;
    TinyDB tinydb ;
    EditText txtEmail,txtPhone,txtCoupon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        txtEmail = (EditText) findViewById(R.id.et_email);
        txtPhone = (EditText) findViewById(R.id.et_mobile_number);
        txtCoupon = (EditText) findViewById(R.id.et_coupon_code);

        tinydb = new TinyDB(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Traveller Details");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view) ;
        //listView = (RecyclerView) findViewById(R.id.lv_passengerList);
        View header = getLayoutInflater().inflate(R.layout.card_passenger_header, null);
        View footer = getLayoutInflater().inflate(R.layout.card_passenger_footer, null);
       // listView.addHeaderView(header);
      //  listView.addFooterView(footer);

        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isValidMail(txtEmail.getText().toString()))
                {
                    Toast.makeText(v.getContext(), // <- Line changed
                            "Enter Valid Email Address",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isValidMobile(txtPhone.getText().toString()))
                {
                    Toast.makeText(v.getContext(), // <- Line changed
                            "Enter Valid Phone Number",
                            Toast.LENGTH_LONG).show();
                }
                SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
                int userId = prefs.getInt("cUserId", 0);
                int boardingId = tinydb.getInt("boardingId");
                int deportingId = tinydb.getInt("deportingId");
                int busCompanyId = tinydb.getInt("busCompanyId");
                int selectedBusId = tinydb.getInt("selectedBusId");
                String date = tinydb.getString("SearchDate");
                String rate = tinydb.getString("rate");
                List<String>lstSeletectSeat= tinydb.getListString("SelectedSeat");


                final MainTraveller traveller = new MainTraveller();
                traveller.setEmail(txtEmail.getText().toString());
                traveller.setPhone(txtPhone.getText().toString());
                traveller.setCouponCode(txtCoupon.getText().toString());
                traveller.setLstTravellers(new ArrayList<TravellerDetails>());
                int size = ((TravellerRecycleAdapter) recyclerView.getAdapter()).getItemCount();

                if( size ==0)
                {
                    Toast.makeText(v.getContext(), // <- Line changed
                            "Please Enter primary name",
                            Toast.LENGTH_LONG).show();
                }
                for (int i = 0; i < size; i++) {

                        TravellerDetails carItem = cardRecycleAdapter.getItem(i);
                    traveller.getLstTravellers().add(carItem);
                        // Do something with the item like save it to a selected items array.
                }

               BookSeat bookSeat = new BookSeat();
                bookSeat.setSid(selectedBusId);
                bookSeat.setEmail(traveller.getEmail());
                bookSeat.setContact(traveller.getPhone());
                bookSeat.setBoarding(boardingId);
                bookSeat.setDropping(deportingId);
                bookSeat.setDepature(date);
                bookSeat.setBuscompany(busCompanyId);
                bookSeat.setCuserid(userId);
                bookSeat.setCoupon(traveller.getCouponCode());
                bookSeat.setRate(rate);
                bookSeat.setSelectedseats("");

                List<String> age = new ArrayList<String>();
                List<String> name = new ArrayList<String>();
                List<String> gender = new ArrayList<String>();
                List<String> seat = new ArrayList<String>();
                for(TravellerDetails travellerDetails : traveller.getLstTravellers())
                {
                    age.add(travellerDetails.getAge());
                    seat.add(travellerDetails.getSeatNo());
                    name.add(travellerDetails.getName());
                    gender.add(travellerDetails.getGender());
                    if(bookSeat.getSelectedseats()=="")
                    {
                        bookSeat.setSelectedseats(travellerDetails.getSeatNo());
                    }else
                    {
                        bookSeat.setSelectedseats(bookSeat.getSelectedseats()+","+travellerDetails.getSeatNo());

                    }

                }
              /*  Gson gson = new Gson();
                traveller.setDiscount("500");
                traveller.setTotal("1500");
                traveller.setSubTotal("2000");
                Intent intent = new Intent(getApplicationContext(), TicketConfirmActivity.class);
                intent.putExtra("Traveller", gson.toJson(traveller));
                startActivity(intent);*/

              final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(TravellerActivity.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Its loading....");
                progressDoalog.setTitle("We are booking you seats");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // show it
                progressDoalog.show();
                BookSeatInterface mApiService = getInterfaceService();
                Call<MessageModel> mService = mApiService.bookSeat(
                        bookSeat.getSid(),bookSeat.getCoupon(),bookSeat.getRate(),bookSeat.getBuscompany(),bookSeat.getDepature(),bookSeat.getBoarding(),bookSeat.getDropping(),
                        bookSeat.getSelectedseats(),bookSeat.getEmail(),bookSeat.getContact(),bookSeat.getCuserid(),name,age,seat,
                        gender);
                mService.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.body() !=null) {
                            Toast.makeText(TravellerActivity.this, response.body().getResult().get(0).getMessage(), Toast.LENGTH_LONG).show();


                            TicketModel msgModel = response.body().getResult().get(0);
                            tinydb.putString("ticketID",msgModel.getTicketid());
                            Gson gson = new Gson();
                            traveller.setDiscount(msgModel.getDiscount());
                            traveller.setTotal(msgModel.getTotal());
                            traveller.setSubTotal(String.valueOf(Double.valueOf(msgModel.getTotal()) + Double.valueOf(msgModel.getDiscount())));
                            Intent intent = new Intent(getApplicationContext(), TicketConfirmActivity.class);
                            intent.putExtra("Traveller", gson.toJson(traveller));
                            startActivity(intent);


                        }
                        else
                        {
                            Toast.makeText(TravellerActivity.this, "Some error occured", Toast.LENGTH_LONG).show();
                        }
                        progressDoalog.dismiss();
                    }
                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        call.cancel();
                        progressDoalog.dismiss();
                        Toast.makeText(TravellerActivity.this, "Unable To book", Toast.LENGTH_LONG).show();
                    }
                });





               /* Bundle bundle = new Bundle();
                Intent in = new Intent(getApplicationContext(),TicketConfirmActivity.class);
                bundle.putSerializable("traveller", traveller);
                in.putExtras(bundle);
                startActivity(in);*/
            }
        });
        cardArrayAdapter = new TravellerAdapter(getApplicationContext(), R.layout.card_traveller_detail_new);

        TravellerDetails busCard= new TravellerDetails();

        List<String>lstSeletectSeat= tinydb.getListString("SelectedSeat");
        for (String card1 : lstSeletectSeat) {
            busCard= new TravellerDetails();
            busCard.setSeatNo(card1);
            busCard.setSeatCategory("Normal");
            lstBusCard.add(busCard);
        }


        for (TravellerDetails card1 : lstBusCard) {
            cardArrayAdapter.add(card1);
        }
        cardRecycleAdapter = new TravellerRecycleAdapter(lstBusCard);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardRecycleAdapter);


    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    private BookSeatInterface getInterfaceService() {

        String base_url = getResources().getString(R.string.BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final BookSeatInterface mInterfaceService = retrofit.create(BookSeatInterface.class);
        return mInterfaceService;
    }


}
