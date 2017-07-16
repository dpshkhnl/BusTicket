package webbank.com.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Dpshkhnl on 2017-04-27.
 */

public class ChangePasswordActivity extends HomeActivity {

    EditText txtOldPassword,txtNewPsswrd,txtConfirm;
    Button btnChange;
    TinyDB tinyDB;
    Button changePasswordForget;
    TextInputLayout inputLayoutOldPswrd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_change_password, frameLayout);

        txtOldPassword = (EditText) findViewById(R.id.oldPassword);
        txtNewPsswrd = (EditText) findViewById(R.id.password);
        txtConfirm = (EditText) findViewById(R.id.confirmPassword);
        btnChange = (Button) findViewById(R.id.changePassword);
        changePasswordForget = (Button) findViewById(R.id.changePasswordForget);
        inputLayoutOldPswrd  = (TextInputLayout) findViewById(R.id.inputLayoutOldPswrd);

        tinyDB = new TinyDB(getApplicationContext());
       final String email = tinyDB.getString("email");
        final String id = String.valueOf(tinyDB.getInt("cUserId"));

        Intent intent = getIntent();
        final String code = intent.getStringExtra("code");
        if(code!= null && !code.equals(""))
        {
            txtOldPassword.setVisibility(View.INVISIBLE);
            inputLayoutOldPswrd.setVisibility(View.INVISIBLE);
            changePasswordForget.setVisibility(View.VISIBLE);
            btnChange.setVisibility(View.INVISIBLE);
        }
        else
        {
            txtOldPassword.setVisibility(View.VISIBLE);
            inputLayoutOldPswrd.setVisibility(View.VISIBLE);
            changePasswordForget.setVisibility(View.INVISIBLE);
            btnChange.setVisibility(View.VISIBLE);
        }


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!txtNewPsswrd.getText().toString().equals(txtConfirm.getText().toString()))
                {
                    Toast.makeText(ChangePasswordActivity.this, "Password and Confirm password doesnt match", Toast.LENGTH_LONG).show();
                    return;
                }else
                {
                    changePassword(email,txtOldPassword.getText().toString(),txtNewPsswrd.getText().toString(),id);
                }
            }
        });

        changePasswordForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tinyDB.getString("email");
                if (!txtNewPsswrd.getText().toString().equals(txtConfirm.getText().toString()))
                {
                    Toast.makeText(ChangePasswordActivity.this, "Password and Confirm password doesnt match", Toast.LENGTH_LONG).show();
                    return;
                }else
                {
                    setChangePasswordForget(email,code,txtNewPsswrd.getText().toString(),id);
                }
            }
        });

        setTitle("Change Password");

    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
       // navigationView.getMenu().getItem(4).setChecked(true);
    }

    private LoginInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final LoginInterface mInterfaceService = retrofit.create(LoginInterface.class);
        return mInterfaceService;
    }

    private void changePassword(final String email, String oldPassword,String newPassword,String id){
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ChangePasswordActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Updating...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<UserRest> mService = mApiService.changePassword(email, oldPassword,newPassword,id);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                UserModel mLoginObject = response.body().getResult().get(0);
                Toast.makeText(ChangePasswordActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                if (mLoginObject.getMessage().contains("successfully"))
                {
                    Intent in  = new Intent(ChangePasswordActivity.this,MainActivity.class);
                    startActivity(in);
                }
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(ChangePasswordActivity.this, "Unable to Connect to Server", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setChangePasswordForget(final String email, String code,String newPassword,String id){
        LoginInterface mApiService = this.getInterfaceService();


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ChangePasswordActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Updating...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Call<UserRest> mService = mApiService.changeForgetPassword(code,newPassword);
        mService.enqueue(new Callback<UserRest>() {
            @Override
            public void onResponse(Call<UserRest> call, Response<UserRest> response) {
                UserModel mLoginObject = response.body().getResult().get(0);
                Toast.makeText(ChangePasswordActivity.this, mLoginObject.getMessage(), Toast.LENGTH_LONG).show();
                if (mLoginObject.getMessage().contains("successfully"))
                {
                    Intent in  = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                    startActivity(in);
                }
                progressDoalog.hide();
            }
            @Override
            public void onFailure(Call<UserRest> call, Throwable t) {
                call.cancel();
                progressDoalog.hide();
                Toast.makeText(ChangePasswordActivity.this, "Unable to Connect to Server", Toast.LENGTH_LONG).show();
            }
        });
    }
}

/*
public  boolean validatePassword()
{
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

}*/
