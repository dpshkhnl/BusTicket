package webbank.com.busticket.Seat;

import java.util.List;

/**
 * Created by Dpshkhnl on 2017-04-05.
 */

public class BusDetailModel {

int id ;
    String bus_no;
    int bus_name_id;
    String bus_name;
    String bus_category;
    int company;
    String mobile_no;
    String bus_image;
    int total_sheet_in_a_side;
    int total_sheet_in_b_side;
    int cabin;
    int special;
    int last_row;
    String type;
    String hices;
    String force;
    String is_active;
    List<String> booked_seat;
    String rev_female;
    String rev_student;
    String rev_old;
    String rev_staff;
    String rev_handicap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public int getBus_name_id() {
        return bus_name_id;
    }

    public void setBus_name_id(int bus_name_id) {
        this.bus_name_id = bus_name_id;
    }

    public String getBus_name() {
        return bus_name;
    }

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }


    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getBus_image() {
        return bus_image;
    }

    public void setBus_image(String bus_image) {
        this.bus_image = bus_image;
    }

    public int getTotal_sheet_in_a_side() {
        return total_sheet_in_a_side;
    }

    public void setTotal_sheet_in_a_side(int total_sheet_in_a_side) {
        this.total_sheet_in_a_side = total_sheet_in_a_side;
    }

    public int getTotal_sheet_in_b_side() {
        return total_sheet_in_b_side;
    }

    public void setTotal_sheet_in_b_side(int total_sheet_in_b_side) {
        this.total_sheet_in_b_side = total_sheet_in_b_side;
    }

    public int getCabin() {
        return cabin;
    }

    public void setCabin(int cabin) {
        this.cabin = cabin;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public int getLast_row() {
        return last_row;
    }

    public void setLast_row(int last_row) {
        this.last_row = last_row;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHices() {
        return hices;
    }

    public void setHices(String hices) {
        this.hices = hices;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public List<String> getBooked_seat() {
        return booked_seat;
    }

    public void setBooked_seat(List<String> booked_seat) {
        this.booked_seat = booked_seat;
    }

    public String getRev_female() {
        return rev_female;
    }

    public void setRev_female(String rev_female) {
        this.rev_female = rev_female;
    }

    public String getRev_student() {
        return rev_student;
    }

    public void setRev_student(String rev_student) {
        this.rev_student = rev_student;
    }

    public String getRev_old() {
        return rev_old;
    }

    public void setRev_old(String rev_old) {
        this.rev_old = rev_old;
    }

    public String getRev_staff() {
        return rev_staff;
    }

    public void setRev_staff(String rev_staff) {
        this.rev_staff = rev_staff;
    }

    public String getRev_handicap() {
        return rev_handicap;
    }

    public void setRev_handicap(String rev_handicap) {
        this.rev_handicap = rev_handicap;
    }

    public String getBus_category() {
        return bus_category;
    }

    public void setBus_category(String bus_category) {
        this.bus_category = bus_category;
    }
}
