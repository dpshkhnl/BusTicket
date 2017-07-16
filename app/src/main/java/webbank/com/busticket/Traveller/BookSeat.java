package webbank.com.busticket.Traveller;

import java.util.List;

/**
 * Created by Dpshkhnl on 2017-04-17.
 */

public class BookSeat {

  int sid;
int buscompany;
    String coupon;
   String depature;// (date matra)
    int boarding;
      int  dropping;
   String selectedseats;
            String email;
    String contact;
         int  cuserid;
    String name;
    String age;
    String seat;
    String gender;
    String rate;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getBuscompany() {
        return buscompany;
    }

    public void setBuscompany(int buscompany) {
        this.buscompany = buscompany;
    }

    public String getDepature() {
        return depature;
    }

    public void setDepature(String depature) {
        this.depature = depature;
    }

    public int getBoarding() {
        return boarding;
    }

    public void setBoarding(int boarding) {
        this.boarding = boarding;
    }

    public int getDropping() {
        return dropping;
    }

    public void setDropping(int dropping) {
        this.dropping = dropping;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getCuserid() {
        return cuserid;
    }

    public void setCuserid(int cuserid) {
        this.cuserid = cuserid;
    }


    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getSelectedseats() {
        return selectedseats;
    }

    public void setSelectedseats(String selectedseats) {
        this.selectedseats = selectedseats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
