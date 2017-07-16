package webbank.com.busticket.Login;

/**
 * Created by Dpshkhnl on 2017-02-13.
 */

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import webbank.com.busticket.Complain.ComplainAPIModel;
import webbank.com.busticket.Dashboard.DashboardAPIModel;
import webbank.com.busticket.TravelHistory.TravelHistApiModel;

public interface LoginInterface {

    @FormUrlEncoded
    @POST("login")
    Call<UserRest> authenticate(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("sociallogin")
    Call<UserRest> authenticateSocial(@Field("oauth_provider") String oauth_provider, @Field("oauth_uid") String oauth_uid,@Field("fname") String fname,@Field("lname") String lname,@Field("email") String email,@Field("ip") String ip);

    @FormUrlEncoded
    @POST("userchangepassword")
    Call<UserRest> changePassword(@Field("email") String email,@Field("oldpassword") String oldPassword, @Field("newpassword") String newPassword,@Field("id") String id);

    @FormUrlEncoded
    @POST("changeforgetpassword")
    Call<UserRest> changeForgetPassword(@Field("code") String code, @Field("password") String newPassword);



    @FormUrlEncoded
    @POST("register")
    Call<UserRest> registration(@Field("email") String email, @Field("password") String password,@Field("mobile_no") String phone,@Field("ip") String ip);

    @FormUrlEncoded
    @POST("update")
    Call<UserRest> updateProfile(@Field("email") String email, @Field("address") String address,@Field("mobile_no") String phone,@Field("fname") String fName,@Field("lname") String lName,@Field("id") String id);


    @FormUrlEncoded
    @POST("userverify")
    Call<UserRest> verify(@Field("email") String email, @Field("code") String code);

    @FormUrlEncoded
    @POST("finduserbyemail")
    Call<UserRest> findUserByEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST("forgetpassword")
    Call<UserRest> forgetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("travelhistory")
    Call<TravelHistApiModel> findTravelHistory(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("complains")
    Call<ComplainAPIModel> findAllComplian(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("addcomplains")
    Call<ComplainAPIModel> addCompains(@Field("user_id") String userId,@Field("subject") String subject,@Field("service") String service,@Field("message") String message);

    @FormUrlEncoded
    @POST("dashboard")
    Call<DashboardAPIModel> getDashboard(@Field("user_id") int user_id);


}
