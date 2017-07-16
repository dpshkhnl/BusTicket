package webbank.com.busticket.Traveller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import webbank.com.busticket.R;

/**
 * Created by Dpshkhnl on 2017-03-23.
 */

public class ConfirmTicketAdapter  extends ArrayAdapter<TravellerDetails> {
    private List<TravellerDetails> travellerDetailsList = new ArrayList<TravellerDetails>();

    static class CardViewHolder {
        TextView lblSno;
        TextView lblName;
        TextView lblRate;
        TextView seatNo;

    }

    public ConfirmTicketAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(TravellerDetails object) {
        travellerDetailsList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.travellerDetailsList.size();
    }

    @Override
    public TravellerDetails getItem(int index) {
        return this.travellerDetailsList.get(index);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        ConfirmTicketAdapter.CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.passenger_confirm_body, parent, false);
            viewHolder = new ConfirmTicketAdapter.CardViewHolder();
            viewHolder.lblSno = (TextView) row.findViewById(R.id.lblSno);
            viewHolder.lblName = (TextView) row.findViewById(R.id.txtName);
            viewHolder.seatNo = (TextView) row.findViewById(R.id.txtSeatNo);
            viewHolder.lblRate = (TextView) row.findViewById(R.id.txtRate);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ConfirmTicketAdapter.CardViewHolder)row.getTag();

        }
        final TravellerDetails card = getItem(position);
        viewHolder.lblSno.setText(String.valueOf(card.getId()));
        viewHolder.lblName.setText(card.getName());
        viewHolder.seatNo.setText(card.getSeatNo());
        viewHolder.lblRate.setText(card.getRate());


        return row;
    }

}


