package webbank.com.busticket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Login.LoginInterface;
import webbank.com.busticket.Login.UserModel;
import webbank.com.busticket.Login.UserRest;

/**
 * Created by Dpshkhnl on 2017-04-16.
 */

public class VerifyRegister extends AppCompatActivity {


    // UI references.
    String email;
    private EditText code;
    View focusView = null;
    TinyDB tinyDB;
    EditText txtEmail;
    Button sendCode;
    Button verify;
    Button verifyForgetPswrd;
    TextInputLayout layoutEmail;
    TextInputLayout layoutCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        tinyDB= new TinyDB(getApplicationContext());
        code = (EditText) findViewById(R.id.code);
        txtEmail = (EditText) findViewById(R.id.email);
        sendCode = (Button) findViewById(R.id.sendCode);
         verify = (Button) findViewById(R.id.verify);
        verifyForgetPswrd= (Button) findViewById(R.id.verifyFogetPassword);
        layoutCode =(TextInputLayout) findViewById(R.id.layoutCode);
        layoutEmail =(TextInputLayout) findViewById(R.id.layoutEmail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        boolean forgetPassword = intent.getBooleanExtra("ForgotPassword",false);
        if(forgetPassword)
        {
            setTitle("Forget Password");
            layoutCode.setVisibility(View.INVISIBLE);
            layoutEmail.setVisibility(View.VISIBLE);
            txtEmail.setVisibility(View.VISIBLE);
            sendCode.setVisibility(View.VISIBLE);
            code.setVisibility(View.INVISIBLE);
            verify.setVisibility(View.INVISIBLE);
            verifyForgetPswrd.setVisibility(View.INVISIBLE);

        }
        else {
            txtEmail.setVisibility(View.INVISIBLE);
            sendCode.setVisibility(View.INVISIBLE);
            code.setVisibility(View.VISIBLE);
            verify.setVisibility(View.VISIBLE);
            verifyForgetPswrd.setVisibility(View.INVISIBLE);

            layoutCode.setVisibility(View.VISIBLE);
            layoutEmail.setVisibility(View.INVISIBLE);
            setTitle("Verify");
        }

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptVerify();
            }
        });

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = txtEmail.getText().toString();
                if (mail.equals(""))
                {
                    Toast.makeText(VerifyRegister.this, "Please Insert email Address", Toast.LENGTH_LONG).show();
                    return;

                }
                sendCode(mail);
            }
        });
        verifyForgetPswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code1 = code.getText().toString();
                if (code1.equals(""))
                {
                    Toast.makeText(VerifyRegister.this, "Please Insert email Address", Toast.LENGTH_LONG).show();
                    return;

                }
                Intent intent = new Intent(VerifyRegister.this,ChangePasswordActivity.class);
                intent.putExtra("code",code1);
                startActivity(intent);
            }
        });

    }

    private void attemptVerify() {

        Intent intent = getIntent();
        email = tinyDB.getString("email");

        String code1 = code.getText().toString();
        verify( code1,email);

    }


    private LoginInterface getInterfaceService() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
      /* Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/
        final LoginInterface mInterfaceService = retrofit.create(LoginInterface.class);
        return mInterfaceService;
    }

    private void verify(String code1,String email1) {
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(VerifyRegister.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Verifying...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<UserRest> mService = mApiService.verify(email1, code1);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                if (response.body().getResult() != null) {
                    UserModel mLoginObject = response.body().getResult().get(0);
               /* String returnedResponse = mLoginObject.isLogin;*/
                    if (mLoginObject.getMessage().contains("verified")) {
                        Toast.makeText(VerifyRegister.this, "Welcome "+email, Toast.LENGTH_LONG).show();
                        progressDoalog.hide();

                        Intent loginIntent = new Intent(VerifyRegister.this, MainActivity.class);
                        loginIntent.putExtra("EMAIL", email);
                        tinyDB.putString("email",email);
                        tinyDB.putString("name",mLoginObject.getFname() + " " + mLoginObject.getLname());
                        tinyDB.putInt("cUserId", Integer.valueOf(mLoginObject.getId()));
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(VerifyRegister.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                        progressDoalog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(VerifyRegister.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendCode(String email1) {
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(VerifyRegister.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("We Are sending you code...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<UserRest> mService = mApiService.forgetPassword(email1);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                if (response.body().getResult() != null) {
                    UserModel mLoginObject = response.body().getResult().get(0);
               /* String returnedResponse = mLoginObject.isLogin;*/
                    if (mLoginObject.getMessage().contains("Success")) {

                        txtEmail.setVisibility(View.INVISIBLE);
                        sendCode.setVisibility(View.INVISIBLE);
                        code.setVisibility(View.VISIBLE);
                        verify.setVisibility(View.INVISIBLE);
                        verifyForgetPswrd.setVisibility(View.VISIBLE);
                        layoutCode.setVisibility(View.VISIBLE);
                        layoutEmail.setVisibility(View.INVISIBLE);
                        setTitle("Verify");
                        progressDoalog.hide();
                    } else {
                        Toast.makeText(VerifyRegister.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                        progressDoalog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(VerifyRegister.this, "Some Error Occured", Toast.LENGTH_LONG).show();
            }
        });
    }


}
