package webbank.com.busticket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;

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

public class RegisterActivity  extends AppCompatActivity {
    private final String TAG = "LoginActivity";


    TinyDB tinyDB;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText confirmPasswordView;
    private EditText phone;
    private View mProgressView;
    private View mLoginFormView;
    View focusView = null;
    private String email;
    private String password;
    private String confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

tinyDB = new TinyDB(getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Register");

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        confirmPasswordView = (EditText) findViewById(R.id.confirmPassword);
        phone = (EditText) findViewById(R.id.phone);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin(){
        boolean mCancel = this.loginValidation();
        if (mCancel) {
            focusView.requestFocus();
        } else {
            String mobile = phone.getText().toString();
            tinyDB.putString("email", email);
            register(email, password,mobile);
        }
    }
    private boolean loginValidation() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        confirmPassword = confirmPasswordView.getText().toString();
        boolean cancel = false;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!password.equals(confirmPassword))
        {
            mPasswordView.setError(getString(R.string.password_doesnt_match));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        return cancel;
    }
    private void populateAutoComplete(){
        //  String[] countries = getResources().getStringArray(R.array.autocomplete);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        //  mEmailView.setAdapter(adapter);
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if(!password.equals(confirmPassword))
        {
            return false;
        }
        return password.length() > 8;
    }

    private LoginInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final LoginInterface mInterfaceService = retrofit.create(LoginInterface.class);
        return mInterfaceService;
    }

    private void register(final String email, String password,String phone){
        LoginInterface mApiService = this.getInterfaceService();
        String ipAddress = getIPAddress(true);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(RegisterActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Registering...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<UserRest> mService = mApiService.registration(email, password,phone,ipAddress);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                UserModel mLoginObject = response.body().getResult().get(0);

                    Toast.makeText(RegisterActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                if (mLoginObject.getMessage().contains("check email")) {
                    Intent loginIntent = new Intent(RegisterActivity.this, VerifyRegister.class);
                    loginIntent.putExtra("email", email);

                    startActivity(loginIntent);
                }
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(RegisterActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }



   public static String getIPAddress(boolean verbose) {
        String stringUrl = "https://ipinfo.io/ip";

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            if(verbose) {
                int responseCode = conn.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);
            }

            StringBuffer response = new StringBuffer();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if(verbose) {
                //print result
                System.out.println("My Public IP address:" + response.toString());
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



}
