package webbank.com.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Login.LoginInterface;
import webbank.com.busticket.Login.UserModel;
import webbank.com.busticket.Login.UserRest;

import static webbank.com.busticket.R.id.frameLayout;

/**
 * Created by Dpshkhnl on 2017-04-26.
 */

public class UpdateActivity extends HomeActivity {

    EditText txtfName,txtlName,txtAddress,txtPhone,txtEmail;
    Button btnUpdate;
    UserModel user;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_update_profile, frameLayout);
        txtfName = (EditText) findViewById(R.id.fname);
        txtlName = (EditText) findViewById(R.id.lname);
        txtAddress = (EditText) findViewById(R.id.address);
        txtPhone = (EditText) findViewById(R.id.phone);
        txtEmail = (EditText) findViewById(R.id.email);
        btnUpdate = (Button) findViewById(R.id.updProfile);
        tinyDB = new TinyDB(getApplicationContext());
String email = tinyDB.getString("email");
        findUser(email);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtEmail.getText().equals("")||txtPhone.getText().equals(""))
                {
                    Toast.makeText(UpdateActivity.this, "Please Enter Email and Phone No", Toast.LENGTH_LONG).show();
return;
                }else
                {
                    update(txtEmail.getText().toString(),txtAddress.getText().toString(),txtPhone.getText().toString(),"45",txtfName.getText().toString(),txtlName.getText().toString());
                }
            }
        });

        setTitle("Update Profile");



    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
       // navigationView.getMenu().getItem(2).setChecked(true);
    }

    private LoginInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final LoginInterface mInterfaceService = retrofit.create(LoginInterface.class);
        return mInterfaceService;
    }

    private void update(final String email, String address,String phone,String id,String fName, String lName){
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(UpdateActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Updating...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<UserRest> mService = mApiService.updateProfile(email, address,phone,fName,lName,id);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                UserModel mLoginObject = response.body().getResult().get(0);
                Toast.makeText(UpdateActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                findUser(email);
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(UpdateActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findUser(final String email){
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(UpdateActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<UserRest> mService = mApiService.findUserByEmail(email);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                UserModel mLoginObject = response.body().getResult().get(0);
                {
                    txtfName.setText(mLoginObject.getFname());
                    txtlName.setText(mLoginObject.getLname());
                    txtAddress.setText(mLoginObject.getAddress());
                    txtPhone.setText(mLoginObject.getMobile_no());
                    txtEmail.setText(mLoginObject.getEmail());
                }

                Toast.makeText(UpdateActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(UpdateActivity.this, "Unable to connect to server", Toast.LENGTH_LONG).show();
            }
        });
    }
}
