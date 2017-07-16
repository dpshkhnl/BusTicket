package webbank.com.busticket.Seat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import webbank.com.busticket.R;

public class AirplaneAdapter extends SelectableAdapter<RecyclerView.ViewHolder> {

    private OnSeatSelected mOnSeatSelected;

    private static class EdgeViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeat;
        private final ImageView imgSeatSelected;
        ImageView imgBooked;
        ImageView imgReserved;
        TextView seatNo;


        public EdgeViewHolder(View itemView) {
            super(itemView);
            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgBooked = (ImageView) itemView.findViewById(R.id.img_seat_booked);
            imgReserved = (ImageView) itemView.findViewById(R.id.img_seat_reserved);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);
            seatNo = (TextView) itemView.findViewById(R.id.item_text);
        }

    }

    private static class CenterViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeat;
        private final ImageView imgSeatSelected;
        TextView seatNo;
        ImageView imgBooked;
        ImageView imgReserved;

        public CenterViewHolder(View itemView) {
            super(itemView);
            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);
            seatNo = (TextView) itemView.findViewById(R.id.item_text);
            imgBooked = (ImageView) itemView.findViewById(R.id.img_seat_booked);
            imgReserved = (ImageView) itemView.findViewById(R.id.img_seat_reserved);

        }

    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }

    }

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public List<AbstractItem> mItems;

    public AirplaneAdapter(Context context, List<AbstractItem> items) {
        mOnSeatSelected = (OnSeatSelected) context;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AbstractItem.TYPE_CENTER) {
            View itemView = mLayoutInflater.inflate(R.layout.list_item_seat, parent, false);
            return new CenterViewHolder(itemView);
        } else if (viewType == AbstractItem.TYPE_EDGE) {
            View itemView = mLayoutInflater.inflate(R.layout.list_item_seat, parent, false);
            return new EdgeViewHolder(itemView);
        } else {
            View itemView = new View(mContext);
            return new EmptyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        int type = mItems.get(position).getType();
        String label = mItems.get(position).getLabel();
        if(getSelectedItems().size() > 6)
        {
            Toast.makeText(mContext.getApplicationContext(), "Sorry,You cannot book more than 6 Seats", Toast.LENGTH_LONG).show();
            return;
        }

        if (type == AbstractItem.TYPE_CENTER) {
            final CenterItem item = (CenterItem) mItems.get(position);
            CenterViewHolder holder = (CenterViewHolder) viewHolder;
            int status = mItems.get(position).getSeatStatus();

            if(status==1) {
                holder.imgBooked.setVisibility(View.VISIBLE );
            }else {

                holder.imgSeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        toggleSelection(position);
                        if (isSelected(position))
                        {
                            mItems.get(position).setSeatStatus(2);
                        }else
                        {
                            mItems.get(position).setSeatStatus(0);
                        }

                        mOnSeatSelected.onSeatSelected(getSelectedItemCount());
                    }
                });

                holder.imgSeatSelected.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
            }
            holder.seatNo.setText(label);
        } else if (type == AbstractItem.TYPE_EDGE) {
            final EdgeItem item = (EdgeItem) mItems.get(position);
            int status = mItems.get(position).getSeatStatus();
            EdgeViewHolder holder = (EdgeViewHolder) viewHolder;
            if(status==1) {
                holder.imgBooked.setVisibility(View.VISIBLE );

            }else {
                holder.imgSeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        toggleSelection(position);
                        if (isSelected(position))
                        {
                            mItems.get(position).setSeatStatus(2);
                        }else
                        {
                            mItems.get(position).setSeatStatus(0);
                        }
                        mOnSeatSelected.onSeatSelected(getSelectedItemCount());


                    }
                });

                holder.imgSeatSelected.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
            }
            holder.seatNo.setText(label);
        }
    }

}
