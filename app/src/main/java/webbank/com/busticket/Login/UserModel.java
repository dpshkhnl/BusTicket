package webbank.com.busticket.Login;

/**
 * Created by Dpshkhnl on 2017-02-13.
 */

public class UserModel {


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    String email;
    String fname;
    String lname;
    String address;
    String mobile_no;
    String id;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
