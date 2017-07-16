package webbank.com.busticket.Bus;

import java.util.List;

/**
 * Created by Dpshkhnl on 2017-03-17.
 */

public class BusCard {
    int busId;
    String companyName;
    String busNo;
    String fare;
    String dateTime;
    String BoardingPoint;
    String allBoardingPoint;
    String allDroppingPoint;
    String arrivalDateTime;
    String boardingTime;
    String deportingTime;
    List<String> lstFeatures;

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBoardingPoint() {
        return BoardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        BoardingPoint = boardingPoint;
    }

    public List<String> getLstFeatures() {
        return lstFeatures;
    }

    public void setLstFeatures(List<String> lstFeatures) {
        this.lstFeatures = lstFeatures;
    }

    public String getAllBoardingPoint() {
        return allBoardingPoint;
    }

    public void setAllBoardingPoint(String allBoardingPoint) {
        this.allBoardingPoint = allBoardingPoint;
    }

    public String getAllDroppingPoint() {
        return allDroppingPoint;
    }

    public void setAllDroppingPoint(String allDroppingPoint) {
        this.allDroppingPoint = allDroppingPoint;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getBoardingTime() {
        return boardingTime;
    }

    public void setBoardingTime(String boardingTime) {
        this.boardingTime = boardingTime;
    }

    public String getDeportingTime() {
        return deportingTime;
    }

    public void setDeportingTime(String deportingTime) {
        this.deportingTime = deportingTime;
    }
}
