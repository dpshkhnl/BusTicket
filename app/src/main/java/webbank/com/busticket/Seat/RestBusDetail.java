package webbank.com.busticket.Seat;

import java.util.List;

/**
 * Created by Dpshkhnl on 2017-04-05.
 */

public class RestBusDetail {
    public List<BusDetailModel> getResult() {
        return result;
    }

    public void setResult(List<BusDetailModel> result) {
        this.result = result;
    }

    List<BusDetailModel> result;

}
