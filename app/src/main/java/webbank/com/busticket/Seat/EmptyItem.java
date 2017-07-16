package webbank.com.busticket.Seat;

public class EmptyItem extends AbstractItem {

    public EmptyItem(String label,int status) {
        super(label,status);
    }


    @Override
    public int getType() {
        return TYPE_EMPTY;
    }

}
