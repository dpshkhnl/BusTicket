package webbank.com.busticket;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Dashboard.DashBoardModel;
import webbank.com.busticket.Dashboard.DashboardAPIModel;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Login.LoginInterface;
import webbank.com.busticket.Login.UserModel;
import webbank.com.busticket.Login.UserRest;
import webbank.com.busticket.R;

/**
 * Created by Dpshkhnl on 2017-04-16.
 */

public class DashboardActivity extends HomeActivity {

    TextView txtReward;
    TextView txtTicket;
    TextView txtPayment;
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.frag_dashboard, frameLayout);
        tinyDB = new TinyDB(getApplicationContext());
        String email = tinyDB.getString("email");
        int userId = tinyDB.getInt("cUserId");

        txtPayment =(TextView) findViewById(R.id.txtPayment);
        txtReward =(TextView) findViewById(R.id.txtReward);
        txtTicket =(TextView) findViewById(R.id.txtTicketNo);

        setTitle("Dashboard");
        loadData(userId);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
       // navigationView.getMenu().getItem(1).setChecked(true);
    }

    private LoginInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final LoginInterface mInterfaceService = retrofit.create(LoginInterface.class);
        return mInterfaceService;
    }

    private void loadData(final int user_id){
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(DashboardActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<DashboardAPIModel> mService = mApiService.getDashboard(Integer.valueOf(user_id));
        mService.enqueue(new Callback<DashboardAPIModel>() {
            @Override
            public void onResponse(Call<DashboardAPIModel> call, Response<DashboardAPIModel> response) {
                DashBoardModel dashBoardModel = response.body().getResult().get(0);
                txtPayment.setText(dashBoardModel.getPayment());
                txtTicket.setText(dashBoardModel.getTickets());
                txtReward.setText(dashBoardModel.getReward());
                Toast.makeText(DashboardActivity.this, dashBoardModel.getMessage(), Toast.LENGTH_LONG).show();
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<DashboardAPIModel> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(DashboardActivity.this, "Unable to connect to server", Toast.LENGTH_LONG).show();
            }
        });
    }
}

