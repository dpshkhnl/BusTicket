package webbank.com.busticket.Seat;

public abstract class AbstractItem {

    public static final int TYPE_CENTER = 0;
    public static final int TYPE_EDGE = 1;
    public static final int TYPE_EMPTY = 2;
    public static final int TYPE_DRIVER = 3;

    private String label;

    public int getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(int seatStatus) {
        this.seatStatus = seatStatus;
    }

    private int seatStatus;


    public AbstractItem(String label,int seatStatus) {

        this.label = label;
        this.seatStatus = seatStatus;
    }


    public String getLabel() {
        return label;
    }

    abstract public int getType();




}
