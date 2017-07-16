package webbank.com.busticket.Bus;

import java.util.List;

/**
 * Created by Dpshkhnl on 2017-04-04.
 */

public class BusSearchModel {

    public int getSch_id() {
        return sch_id;
    }

    public void setSch_id(int sch_id) {
        this.sch_id = sch_id;
    }

    public void setNetfare(String netfare) {
        this.netfare = netfare;
    }

    int sch_id;
    String bus_id;
    String bus_name;
    String departure;
    String arrival;
    String departuretime;
    String arrivaltime;
    int from;
    int to;
    String discount;
    String netfare;
    String boardingpoint;
    String droppingpoint;
    String shift;
    String boardingtime;
    int company;
    String addinnfo;
    String user;
    String is_Active;
    String first_boradigpoint;
    List<String> features;

    public String getFirst_boradigpoint() {
        return first_boradigpoint;
    }

    public void setFirst_boradigpoint(String first_boradigpoint) {
        this.first_boradigpoint = first_boradigpoint;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparturetime() {
        return departuretime;
    }

    public void setDeparturetime(String departuretime) {
        this.departuretime = departuretime;
    }

    public String getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(String arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNetfare() {
        return netfare;
    }

    public void setNetFare(String netfare) {
        this.netfare = netfare;
    }

    public String getBoardingpoint() {
        return boardingpoint;
    }

    public void setBoardingpoint(String boardingpoint) {
        this.boardingpoint = boardingpoint;
    }

    public String getDroppingpoint() {
        return droppingpoint;
    }

    public void setDroppingpoint(String droppingpoint) {
        this.droppingpoint = droppingpoint;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getBoardingtime() {
        return boardingtime;
    }

    public void setBoardingtime(String boardingtime) {
        this.boardingtime = boardingtime;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public String getAddinnfo() {
        return addinnfo;
    }

    public void setAddinnfo(String addinnfo) {
        this.addinnfo = addinnfo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIs_Active() {
        return is_Active;
    }

    public void setIs_Active(String is_Active) {
        this.is_Active = is_Active;
    }

    public String getBus_name() {
        return bus_name;
    }

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
