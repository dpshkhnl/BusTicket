package webbank.com.busticket.TravelHistory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import webbank.com.busticket.R;
import webbank.com.busticket.Traveller.ConfirmTicketAdapter;

/**
 * Created by Dpshkhnl on 2017-05-01.
 */

public class TravelHistoryDetailsAdapter  extends RecyclerView.Adapter<TravelHistoryDetailsAdapter.MyViewHolder> {

    private List<TravelHistoryDetailAPI> travellerDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lblSno;
        TextView lblName;
        TextView lblRate;
        TextView seatNo;

        public MyViewHolder(View view) {
            super(view);


            lblSno = (TextView) view.findViewById(R.id.lblSno);
            lblName = (TextView) view.findViewById(R.id.txtName);
            lblRate = (TextView) view.findViewById(R.id.txtRate);
            seatNo = (TextView) view.findViewById(R.id.txtSeatNo);



        }
    }


    public TravelHistoryDetailsAdapter(List<TravelHistoryDetailAPI> travellerDetailses) {
        this.travellerDetailsList = travellerDetailses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_confirm_body , parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TravelHistoryDetailsAdapter.MyViewHolder holder, final int position) {

        final TravelHistoryDetailAPI card = getItem(position);
        holder.lblSno.setText(String.valueOf(card.getId()));
        holder.lblName.setText(card.getName());
        holder.seatNo.setText(card.getSeat());
        holder.lblRate.setText(card.getRate());


    }

    @Override
    public int getItemCount() {
        return travellerDetailsList.size();
    }


    public TravelHistoryDetailAPI getItem(int position)
    {
        TravelHistoryDetailAPI travellerDetails = travellerDetailsList.get(position);

        return travellerDetails;
    }


}
