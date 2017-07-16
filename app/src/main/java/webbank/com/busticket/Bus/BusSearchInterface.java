package webbank.com.busticket.Bus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import webbank.com.busticket.Routes.ApiRoutes;
import webbank.com.busticket.Seat.BusDetailModel;
import webbank.com.busticket.Seat.RestBusDetail;

/**
 * Created by Dpshkhnl on 2017-04-04.
 */

public interface BusSearchInterface {

    @FormUrlEncoded
    @POST("bussearch")
    Call<BusSearchAPIModel> searchBuses(@Field("from") int from,@Field("to") int to,@Field("date") String date);

    @FormUrlEncoded
    @POST("busdetailsBybusid")
    Call<RestBusDetail> getBusDetailByBusId(@Field("sch_id") String schId,@Field("bus_id") String busId);


}
