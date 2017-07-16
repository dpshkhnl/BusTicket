package webbank.com.busticket.Routes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Dpshkhnl on 2017-04-03.
 */

public interface RoutesAPI {
    @GET("allrouts")
    Call<ApiRoutes> loadAllRoutes();

}
