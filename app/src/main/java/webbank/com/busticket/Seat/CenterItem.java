package webbank.com.busticket.Seat;

public class CenterItem extends AbstractItem {

    public CenterItem(String label,int status) {
        super(label,status);
    }


    @Override
    public int getType() {
        return TYPE_CENTER;
    }

}
