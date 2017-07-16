package webbank.com.busticket.TravelHistory;

import java.util.List;

import webbank.com.busticket.Traveller.TravellerDetails;

/**
 * Created by Dpshkhnl on 2017-05-02.
 */

public class TravelHistoryDetailsModel {

    List<TravelHistoryDetailAPI> result;

    public List<TravelHistoryDetailAPI> getResult() {
        return result;
    }

    public void setResult(List<TravelHistoryDetailAPI> result) {
        this.result = result;
    }
}
