package webbank.com.busticket.Seat;

public class EdgeItem extends AbstractItem {

    public EdgeItem(String label,int status) {
        super(label,status);
    }



    @Override
    public int getType() {
        return TYPE_EDGE;
    }

}
