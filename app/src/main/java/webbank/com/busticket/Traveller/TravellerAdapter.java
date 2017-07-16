package webbank.com.busticket.Traveller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import webbank.com.busticket.Boarding.BoardingCardAdapter;
import webbank.com.busticket.Boarding.BoardingPoint;
import webbank.com.busticket.R;
import webbank.com.busticket.TravellerActivity;

/**
 * Created by Dpshkhnl on 2017-03-18.
 */

public class TravellerAdapter  extends ArrayAdapter<TravellerDetails> {
    public List<TravellerDetails> travellerDetailsList = new ArrayList<TravellerDetails>();

    static class CardViewHolder {
        EditText txtName;
        EditText age;
        Spinner gender;

    }

    public TravellerAdapter(Context context, int textViewResourceId) {
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
        TravellerAdapter.CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.card_traveller_detail_new, parent, false);
            viewHolder = new TravellerAdapter.CardViewHolder();
          /*  viewHolder.boardingPoint = (TextView) row.findViewById(R.id.etName);
            viewHolder.boardingTime = (TextView) row.findViewById(R.id.txtBoardingTime);*/

          viewHolder.gender = (Spinner) row.findViewById(R.id.gender);
            viewHolder.age = (EditText) row.findViewById(R.id.et_age);
            viewHolder.txtName = (EditText) row.findViewById(R.id.et_name);

            row.setTag(viewHolder);
        } else {
            viewHolder = (TravellerAdapter.CardViewHolder)row.getTag();

        }
        /*final TravellerDetails card = getItem(position);
        viewHolder.txtName.setText(card.);
        viewHolder.boardingTime.setText(card.getTime());*/

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Toast.makeText(view.getContext(), // <- Line changed
                        "Boarding Point  "+card.getBoardingPoint(),
                        Toast.LENGTH_LONG).show();
                Intent intent=new Intent(parent.getContext(), TravellerActivity.class);
                parent.getContext().startActivity(intent);*/


            }
        });


        return row;
    }


}

