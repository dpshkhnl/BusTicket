package webbank.com.busticket.Bus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.media.RatingCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.MainActivity;
import webbank.com.busticket.R;
import webbank.com.busticket.SearchBusActivity;
import webbank.com.busticket.SeatActivity;

/**
 * Created by Dpshkhnl on 2017-03-17.
 */

public class BusCardAdapter extends ArrayAdapter<BusCard> {
    private List<BusCard> busList = new ArrayList<BusCard>();

    static class CardViewHolder {
        TextView companyName;
        TextView busNo;
        TextView fare;
        TextView dateTime;
        TextView BoardingPoint;
        TextView feature1;
        TextView feature2;
        TextView feature3;
        TextView feature4;
        TextView featureMore;

        List<String> lstFeatures;

    }

    public BusCardAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(BusCard object) {
        busList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.busList.size();
    }

    @Override
    public BusCard getItem(int index) {
        return this.busList.get(index);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_bus_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.companyName = (TextView) row.findViewById(R.id.txtCompanyName);
            viewHolder.BoardingPoint = (TextView) row.findViewById(R.id.txtBoardingPoint);
            viewHolder.dateTime = (TextView) row.findViewById(R.id.txtDate);
            viewHolder.fare = (TextView) row.findViewById(R.id.txtFare);
            viewHolder.feature1 =(TextView) row.findViewById(R.id.feature1);
            viewHolder.feature2 =(TextView) row.findViewById(R.id.feature2);
            viewHolder.feature3 =(TextView) row.findViewById(R.id.feature3);
            viewHolder.feature4 =(TextView) row.findViewById(R.id.feature4);
            viewHolder.featureMore =(TextView) row.findViewById(R.id.featureMore);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();

        }
        final BusCard card = getItem(position);
        viewHolder.companyName.setText(card.getCompanyName());
        viewHolder.BoardingPoint.setText(card.getBoardingPoint());
        viewHolder.dateTime.setText(card.getDateTime());
        viewHolder.fare.setText("Rs."+card.getFare());
        if (card.getLstFeatures().size() > 4)
        {
            viewHolder.featureMore.setVisibility(View.VISIBLE);
        }
        for (int i= 0;i< card.getLstFeatures().size();i++)
        {
            if (i==0){
                viewHolder.feature1.setVisibility(View.VISIBLE);
                viewHolder.feature1.setText(card.getLstFeatures().get(0));
            }
            if (i==1){
                viewHolder.feature2.setVisibility(View.VISIBLE);
                viewHolder.feature2.setText(card.getLstFeatures().get(1));
            }
            if (i==2){
                viewHolder.feature3.setVisibility(View.VISIBLE);
                viewHolder.feature3.setText(card.getLstFeatures().get(2));
            }
            if (i==3){
                viewHolder.feature4.setVisibility(View.VISIBLE);
                viewHolder.feature4.setText(card.getLstFeatures().get(3));
            }
        }
        viewHolder.featureMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(parent.getContext())
                        .title("Features")
                        .items(card.getLstFeatures())
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            }
                        })
                        .show();

            }
        });;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), // <- Line changed
                        "Bus seats for "+card.getBusId(),
                        Toast.LENGTH_LONG).show();
                TinyDB tinyDB = new TinyDB(parent.getContext());
                Intent intent=new Intent(parent.getContext(), SeatActivity.class);
                tinyDB.putInt("selectedBusId",card.getBusId());
                tinyDB.putString("busNo",card.getBusNo());
                tinyDB.putString("boardingPoint",card.getAllBoardingPoint());
                tinyDB.putString("droppingPoint",card.getAllDroppingPoint());
                tinyDB.putString("depatureTime",card.getDateTime());
                tinyDB.putString("arrivalTime",card.getArrivalDateTime());
                tinyDB.putString("boardingTime",card.getBoardingTime());
                tinyDB.putString("deportingTime",card.getDeportingTime());
                tinyDB.putString("busName",card.getCompanyName());
                tinyDB.putString("rate",card.getFare());
                parent.getContext().startActivity(intent);


            }
        });


        return row;
    }


}
