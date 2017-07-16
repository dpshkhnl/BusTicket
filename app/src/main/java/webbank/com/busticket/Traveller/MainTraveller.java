package webbank.com.busticket.Traveller;

import java.util.List;

/**
 * Created by Dpshkhnl on 2017-04-17.
 */

public class MainTraveller {
    int id;
    String email;
    String phone;
    String couponCode;
    String from;
    String to;
    String total;
    String discount;
    String subTotal;

    List<TravellerDetails> lstTravellers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public List<TravellerDetails> getLstTravellers() {
        return lstTravellers;
    }

    public void setLstTravellers(List<TravellerDetails> lstTravellers) {
        this.lstTravellers = lstTravellers;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }
}
