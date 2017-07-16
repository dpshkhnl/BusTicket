package webbank.com.busticket.TravelHistory;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webbank.com.busticket.Data.MessageModel;
import webbank.com.busticket.Data.TicketModel;
import webbank.com.busticket.R;
import webbank.com.busticket.Traveller.BookSeatInterface;
import webbank.com.busticket.Traveller.ConfirmTicketAdapter;
import webbank.com.busticket.Traveller.TravellerDetails;

/**
 * Created by Dpshkhnl on 2017-04-28.
 */

public class TravelAdapter extends ArrayAdapter<TravelHistoryModel> {
    private List<TravelHistoryModel> travelHistLst = new ArrayList<TravelHistoryModel>();

    static class CardViewHolder {
        TextView txtDate;
        TextView txtName;
        TextView txtPnr;
        TextView txtCoupon;
        TextView txtTotal;
        TextView txtReward;
        Button btnTravellers;
        Button btnPrint;
        Button btnDetails;

    }

    public TravelAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(TravelHistoryModel object) {
        travelHistLst.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.travelHistLst.size();
    }

    @Override
    public TravelHistoryModel getItem(int index) {
        return this.travelHistLst.get(index);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        TravelAdapter.CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.card_travel_history, parent, false);
            viewHolder = new TravelAdapter.CardViewHolder();
            viewHolder.txtDate = (TextView) row.findViewById(R.id.txtDate);
            viewHolder.txtPnr = (TextView) row.findViewById(R.id.txtPnr);
            viewHolder.btnPrint =(Button) row.findViewById(R.id.btnPrint);
            viewHolder.btnTravellers =(Button) row.findViewById(R.id.btnTravellers);
            viewHolder.btnDetails =(Button) row.findViewById(R.id.btnDetails);

            row.setTag(viewHolder);
        } else {
            viewHolder = (TravelAdapter.CardViewHolder)row.getTag();

        }
        final TravelHistoryModel card = getItem(position);
        viewHolder.txtDate.setText(card.getDate());
        viewHolder.txtPnr.setText(card.getPnr());


        viewHolder.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printTicket(card.getPnr(),card.getRef_code(),parent);
            }
        });
        viewHolder.btnTravellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(parent,card.getId());
            }
        });;
        viewHolder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean wrapInScrollView = true;
                MaterialDialog dialog =  new MaterialDialog.Builder(parent.getContext())
                        .title("Traveller Details")
                        .positiveText("Ok")
                        .customView(R.layout.card_travel_details, wrapInScrollView)
                        .show();

                View dialogView = dialog.getCustomView();

                TextView txtName = (TextView) dialogView.findViewById(R.id.txtName);
                TextView txtDate = (TextView) dialogView.findViewById(R.id.txtDate);
                TextView txtCoupon = (TextView) dialogView.findViewById(R.id.txtCoupon);
                TextView txtPnr = (TextView) dialogView.findViewById(R.id.txtPnr);
                TextView txtTotal =(TextView) dialogView.findViewById(R.id.txtTotal);
                TextView txtReward =(TextView) dialogView.findViewById(R.id.txtReward);

                txtDate.setText(card.getDate());
                txtPnr.setText(card.getPnr());
                txtName.setText(card.getName());
                txtTotal.setText(card.getTotal());
                txtCoupon.setText(card.getCoupon());
                txtReward.setText(card.getReward());

            }
        });;


        /*row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/


        return row;
    }
    public  void printTicket(String ticketId,String refID,final ViewGroup parent)
    {
        BookSeatInterface mApiService = getInterfaceService();
        Call<MessageModel> mService = mApiService.updatePayment(ticketId,refID);
        mService.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.body() !=null) {
                    TicketModel ticketModel = response.body().getResult().get(0);

                    MaterialDialog dialog = new MaterialDialog.Builder(parent.getContext())
                            .title("Databank Booking")
                            .content("Your Ticket with ticket No "+ticketModel.getTicketid()+"is send in mail.Please check your mail ")
                            .positiveText("Ok")
                            .show();
                    return;


                }
                else
                {
                    Toast.makeText(parent.getContext(), "Some error occured", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                call.cancel();
                Toast.makeText(parent.getContext(), "Some error occured while printing ticket print", Toast.LENGTH_LONG).show();
            }
        });


    }

    private BookSeatInterface getInterfaceService() {

        String base_url = getContext().getResources().getString(R.string.BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final BookSeatInterface mInterfaceService = retrofit.create(BookSeatInterface.class);
        return mInterfaceService;
    }

    private void showDialog(final ViewGroup parent,String info_ID){


        boolean wrapInScrollView = true;
       MaterialDialog dialog = new MaterialDialog.Builder(parent.getContext())
                .title("Passenger Details")
                .customView(R.layout.passenger_details_body, wrapInScrollView)
                .positiveText("Ok")
                .show();

        final ListView lv = (ListView) dialog.findViewById(R.id.lv_passengerList);
        final ConfirmTicketAdapter cardArrayAdapter  = new ConfirmTicketAdapter(parent.getContext(), R.layout.passenger_confirm_body);


        BookSeatInterface mApiService = getInterfaceService();
        Call<TravelHistoryDetailsModel> mService = mApiService.getTravelHistDet(info_ID);
        mService.enqueue(new Callback<TravelHistoryDetailsModel>() {
            @Override
            public void onResponse(Call<TravelHistoryDetailsModel> call, Response<TravelHistoryDetailsModel> response) {
                if (response.body() !=null) {

                   List<TravelHistoryDetailAPI> travellerDetailses = response.body().getResult();
                   for (TravelHistoryDetailAPI travelHistLst:travellerDetailses) {
                        TravellerDetails card1 = new TravellerDetails();
                        card1.setName(travelHistLst.getName());
                        card1.setGender(travelHistLst.getGender());
                        card1.setRate(travelHistLst.getRate());
                        card1.setSeatNo(travelHistLst.getSeat());
                        card1.setId(travelHistLst.getId());
                        cardArrayAdapter.add(card1);

                    }
                    lv.setAdapter(cardArrayAdapter);

                }
                else
                {
                    Toast.makeText(parent.getContext(), "Some error occured", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<TravelHistoryDetailsModel> call, Throwable t) {
                call.cancel();
                Toast.makeText(parent.getContext(), "Some error occured while printing ticket print", Toast.LENGTH_LONG).show();
            }
        });

       /* lv.setAdapter(cardArrayAdapter);


        dialog.setContentView(promptsView);

        dialog.show();*/

    }

}
