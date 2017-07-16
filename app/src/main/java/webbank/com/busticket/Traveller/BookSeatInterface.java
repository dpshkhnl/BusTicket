package webbank.com.busticket.Traveller;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import webbank.com.busticket.Bus.BusSearchAPIModel;
import webbank.com.busticket.Data.MessageModel;
import webbank.com.busticket.TravelHistory.TravelHistoryDetailsModel;

/**
 * Created by Dpshkhnl on 2017-04-17.
 */

public interface BookSeatInterface {
    @FormUrlEncoded
    @POST("passengersdtls")
    Call<MessageModel> bookSeat(@Field("sid") int sid, @Field("coupon") String coupon, @Field("rate") String rate, @Field("buscompany") int buscompany, @Field("depature") String depature,
                                @Field("boarding") int boarding, @Field("dropping") int dropping, @Field("selectedseats") String selectedseats,
                                @Field("email") String email, @Field("contact") String contact, @Field("cuserid") int cuserid, @Field("name[]") List<String> name
            , @Field("age[]") List<String> age, @Field("seat[]") List<String> seat, @Field("gender[]") List<String> gender);

    @FormUrlEncoded
    @POST("updatestatus")
    Call<MessageModel> updatePayment(@Field("ticketid") String ticketid,@Field("ref_code") String  ref_code);

    @FormUrlEncoded
    @POST("passengerdetails")
    Call<TravelHistoryDetailsModel> getTravelHistDet(@Field("info_id") String info_id);
}