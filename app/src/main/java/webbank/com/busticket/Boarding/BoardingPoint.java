package webbank.com.busticket.Boarding;

/**
 * Created by Dpshkhnl on 2017-03-18.
 */

public class BoardingPoint {
    int boardingId;
    String boardingPoint;
    String time;

    public String getBoardOrDeport() {
        return boardOrDeport;
    }

    public void setBoardOrDeport(String boardOrDeport) {
        this.boardOrDeport = boardOrDeport;
    }

    String boardOrDeport;

    public int getBoardingId() {
        return boardingId;
    }

    public void setBoardingId(int boardingId) {
        this.boardingId = boardingId;
    }

    public String getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
