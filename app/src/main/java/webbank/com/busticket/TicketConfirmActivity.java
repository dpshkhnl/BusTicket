package webbank.com.busticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esewa.fonepay.esewapaymentsdk.ESewaConfiguration;
import com.esewa.fonepay.esewapaymentsdk.ESewaPayment;
import com.esewa.fonepay.esewapaymentsdk.ESewaPaymentActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Data.MessageModel;
import webbank.com.busticket.Data.TicketModel;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Traveller.BookSeatInterface;
import webbank.com.busticket.Traveller.ConfirmTicketAdapter;
import webbank.com.busticket.Traveller.MainTraveller;
import webbank.com.busticket.Traveller.TravellerDetails;


/**
 * Created by Dpshkhnl on 2017-03-23.
 */

public class TicketConfirmActivity  extends AppCompatActivity {

    ListView listView;
    TextView txtFrom, txtDepatureAt, txtBoarding;
    TextView txtTo, txtArrivalAt, txtDeporting;
    TextView txtBusNo, txtBusCat;
    TextView txtSubTotal, txtDiscount, txtTotal;
    Button btnConfirm, btnCancel;
    ESewaConfiguration esewa_configuration = new ESewaConfiguration().clientId("ITIxNiMyJS47KjwuPi80RSY2KEkBFhUSCQQXDg==")
            .secretKey("BhwIWQUSHwQbBB0OWRYWCQcYCxg=")
            .environment(ESewaConfiguration.ENVIRONMENT_PRODUCTION);

    List<TravellerDetails> lstBusCard = new ArrayList<>();
    ConfirmTicketAdapter cardArrayAdapter;
    TinyDB tinydb;
    MainTraveller mainTraveller = new MainTraveller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ticket);
        tinydb = new TinyDB(getApplicationContext());
        listView = (ListView) findViewById(R.id.lv_passengerList1);

        txtFrom = (TextView) findViewById(R.id.txtFrom);
        txtDepatureAt = (TextView) findViewById(R.id.txtDeparatue);
        txtBoarding = (TextView) findViewById(R.id.txtBoardingPoint);

        txtTo = (TextView) findViewById(R.id.txtTo);
        txtArrivalAt = (TextView) findViewById(R.id.txtArrivalTime);
        txtDeporting = (TextView) findViewById(R.id.txtDeporting);

        txtBusNo = (TextView) findViewById(R.id.txtBusNo);
        txtBusCat = (TextView) findViewById(R.id.txtBusCat);

        txtSubTotal = (TextView) findViewById(R.id.txtSubtotal);
        txtDiscount = (TextView) findViewById(R.id.txtDiscount);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ticketID = tinydb.getString("ticketID");

               ESewaPayment eSewaPayment = new ESewaPayment(mainTraveller.getTotal(), "Databank Bus Ticket", ticketID, "www.databankbooking.com/Api/verifypayment");
                Intent intent = new Intent(getApplicationContext(), ESewaPaymentActivity.class);
                intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, esewa_configuration);
                intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
                startActivityForResult(intent, 1);


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        String sourceName = tinydb.getString("SourceName");
        String destName = tinydb.getString("DestinationName");
        String depatureTime = tinydb.getString("depatureTime");
        String arrivalTime = tinydb.getString("arrivalTime");
        String busName = tinydb.getString("busName");
        String busCategory = tinydb.getString("busCategory");
        String firstPoint = tinydb.getString("firstPoint");
        String lastPoint = tinydb.getString("lastPoint");
        String ticketID = tinydb.getString("ticketID");
        String rate = tinydb.getString("rate");

        txtFrom.setText(sourceName);
        txtTo.setText(destName);
        txtDepatureAt.setText(depatureTime);
        txtArrivalAt.setText(arrivalTime);
        txtBusNo.setText(busName);
        txtBusCat.setText(busCategory);
        txtBoarding.setText(firstPoint);
        txtDeporting.setText(lastPoint);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("Traveller");
        mainTraveller = gson.fromJson(strObj, MainTraveller.class);
        cardArrayAdapter = new ConfirmTicketAdapter(getApplicationContext(), R.layout.passenger_confirm_body);


        int i = 0;
        for (TravellerDetails card1 : mainTraveller.getLstTravellers()) {
           /* busCard= new TravellerDetails();
            busCard.setName(card1.getName());
            busCar*/
            i++;
            card1.setRate(rate);
            card1.setId(i);
            lstBusCard.add(card1);
        }
        txtTotal.setText("Rs."+mainTraveller.getTotal());
        txtDiscount.setText("Rs."+mainTraveller.getDiscount());
        txtSubTotal.setText("Rs."+mainTraveller.getSubTotal());
        for (TravellerDetails card1 : lstBusCard) {
            cardArrayAdapter.add(card1);
        }
        listView.setAdapter(cardArrayAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Log.d("Message",s);


                Map jsonJavaRootObject = new Gson().fromJson(s, Map.class);
                System.out.println(jsonJavaRootObject.get("transactionDetails"));
                Map<String,String> details= (Map<String, String>) jsonJavaRootObject.get("transactionDetails");

                Map jsonJavaRootObject2 = new Gson().fromJson(s, Map.class);
                String ref_code = (String) details.get("referenceId");
              //  Toast.makeText(this,s,Toast.LENGTH_LONG).show();
                String ticketID = tinydb.getString("ticketID");
                updatePayment(ticketID,ref_code);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this,"Canceled By User",Toast.LENGTH_LONG).show();
            }
            if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID){
                String ssa = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Toast.makeText(this,"Payment Failed",Toast.LENGTH_LONG).show();
            }

        }
    }

    public  void updatePayment(String ticketId,String refID)
    {
        BookSeatInterface mApiService = getInterfaceService();
        Call<MessageModel> mService = mApiService.updatePayment(ticketId,refID);
        mService.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.body() !=null) {
                    String ticketID = tinydb.getString("ticketID");

                    MaterialDialog dialog = new MaterialDialog.Builder(TicketConfirmActivity.this)
                            .title("Databank Booking")
                            .content("Payment Done!!Your Ticket No is "+ticketID+".Please check your mail for Ticket .Thank you for Travelling with us!!")
                            .positiveText("Ok")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                    return;


                }
                else
                {
                    Toast.makeText(TicketConfirmActivity.this, "Some error occured", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                call.cancel();
                Toast.makeText(TicketConfirmActivity.this, "Payment Not updated,Please contact us for support", Toast.LENGTH_LONG).show();
            }
        });


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

