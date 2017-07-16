package webbank.com.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{
    private final String TAG = "LoginActivity";


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    View focusView = null;
    private String email;
    private String password;
    TextView txtForgetPassword;
    TinyDB tinyDB;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    GoogleApiClient  mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 007;
    private static final int RC_Facebook_IN = 9;
    private ImageView loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tinyDB = new TinyDB(getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Login");

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        ImageView signInButton = (ImageView) findViewById(R.id.sign_in_button);
        // signInButton.setSize(SignInButton.SIZE_WIDE);


        loginButton = (ImageView) findViewById(R.id.login_button);




        // If using in a fragment

        // Other app specific specialization

        // Callback registration


        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               // deleteFacebookApplication();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","email"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Log.d("Success", "Login");

                                if (AccessToken.getCurrentAccessToken() != null) {


                                    GraphRequest request = GraphRequest.newMeRequest(
                                            loginResult.getAccessToken(),
                                            new GraphRequest.GraphJSONObjectCallback() {

                                                @Override
                                                public void onCompleted(JSONObject object, GraphResponse response) {
                                                    Log.v("Main", response.toString());

                                                    try {
                                                        //  Profile acct = Profile.getCurrentProfile();


                                                        String name = object.getString("name");

                                                        String lastName = "";
                                                        String firstName = "";
                                                        if (name.split("\\w+").length > 1) {

                                                            lastName = name.substring(name.lastIndexOf(" ") + 1);
                                                            firstName = name.substring(0, name.lastIndexOf(' '));
                                                        } else {
                                                            firstName = name;
                                                        }


                                                        //  String personPhotoUrl = acct.getProfilePictureUri(20,20).toString();
                                                        String authId = object.getString("id");
                                                        String emailAd = "";
                                                        if (object.has("email")) {
                                                            emailAd = object.getString("email");
                                                        } else {
                                                            Toast.makeText(LoginActivity.this, "Please Make your Email Address Public", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        //Log.d()

                                                        saveSocialLogin("facebook", authId, firstName, lastName, emailAd, getIPAddress(true));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "id,name,email");
                                    request.setParameters(parameters);
                                    request.executeAsync();

                                }
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException exception) {

                                    AccessToken.setCurrentAccessToken(null);
                                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));

                                Toast.makeText(LoginActivity.this, "Error to Login Facebook", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        txtForgetPassword = (TextView)findViewById(R.id.forgetPassword) ;
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
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();
            }
        });

        txtForgetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,VerifyRegister.class);
                intent.putExtra("ForgotPassword",true);
                startActivity(intent);

            }
        });


        TextView signUp = (TextView) findViewById(R.id.btnSignUp);
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(loginIntent);
            }
        });
        Button Guest = (Button) findViewById(R.id.btnGuest);
        Guest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginIntent);
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin(){
        boolean mCancel = this.loginValidation();
        if (mCancel) {
            focusView.requestFocus();
        } else {
            loginProcessWithRetrofit(email, password);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private boolean loginValidation() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        boolean cancel = false;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
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
        return password.length() > 4;
    }

    private LoginInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final LoginInterface mInterfaceService = retrofit.create(LoginInterface.class);
        return mInterfaceService;
    }
    private void loginProcessWithRetrofit(final String email, String password){
        LoginInterface mApiService = this.getInterfaceService();
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Logging you in...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        Call<UserRest> mService = mApiService.authenticate(email, password);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                if (response.body().getResult()!=null) {
                    UserModel mLoginObject = response.body().getResult().get(0);
               /* String returnedResponse = mLoginObject.isLogin;*/
                    if (mLoginObject.getMessage().equals("success")) {
                        Toast.makeText(LoginActivity.this, "Welcome " + mLoginObject.getFname() + " " + mLoginObject.getLname(), Toast.LENGTH_LONG).show();
                        progressDoalog.hide();
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        loginIntent.putExtra("EMAIL", email);
                        tinyDB.putString("email",email);
                        tinyDB.putString("name",mLoginObject.getFname() + " " + mLoginObject.getLname());
                        tinyDB.putInt("cUserId", Integer.valueOf(mLoginObject.getId()));
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(LoginActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                        progressDoalog.hide();
                    }
                }
            }
            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(LoginActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void signIn() {
        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        // checkCache();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        // hideProgressDialog();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String fName = acct.getGivenName();
            String lName = acct.getFamilyName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String authId = acct.getId();
            String id = acct.getIdToken();


            String token = acct.getServerAuthCode();
            String ipAddress = getIPAddress(true);

            saveSocialLogin("google", authId, fName, lName, email, ipAddress);

        } else {
            // Signed out, show unauthenticated UI.

        }
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




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            checkCache();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }
    public  void checkCache()
    {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            hideProgressDialog();
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                    handleSignInResult(googleSignInResult);
                    hideProgressDialog();
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void saveSocialLogin(String oauth_provider, String oauth_uid, String fname,String lname ,String email,String ip)
    {
        LoginInterface mApiService = this.getInterfaceService();


        Call<UserRest> mService = mApiService.authenticateSocial(oauth_provider, oauth_uid,fname,lname,email,ip);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                if (response.body().getResult()!=null) {
                    UserModel mLoginObject = response.body().getResult().get(0);
               /* String returnedResponse = mLoginObject.isLogin;*/
                    if (mLoginObject.getMessage().contains("success")) {
                        tinyDB.putString("email",mLoginObject.getEmail());
                        tinyDB.putString("name",mLoginObject.getFname() + " " + mLoginObject.getLname());
                        tinyDB.putInt("cUserId", Integer.valueOf(mLoginObject.getId()));

                        CountDownTimer mTimer;
                        final ProgressDialog progressDoalog;
                        progressDoalog = new ProgressDialog(LoginActivity.this);
                        progressDoalog.setMax(100);
                        progressDoalog.setMessage("Logging you In...");
                        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        // show it
                        progressDoalog.show();


                        mTimer = new CountDownTimer(3000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                Toast.makeText(LoginActivity.this, "Welcome " + tinyDB.getString("name"), Toast.LENGTH_LONG).show();
                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                progressDoalog.hide();
                                startActivity(loginIntent);

                                finish();
                            }
                        };
                        mTimer.start();


                    } else {
                        Toast.makeText(LoginActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }
            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                Toast.makeText(LoginActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*void deleteFacebookApplication(){
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                boolean isSuccess = false;
                try {
                    isSuccess = response.getJSONObject().getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isSuccess && response.getError()==null){
                    // Application deleted from Facebook account
                }

            }
        }).executeAsync();
    }*/

}