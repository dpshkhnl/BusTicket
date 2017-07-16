package webbank.com.busticket.Boarding;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import webbank.com.busticket.BoardingActivity;
import webbank.com.busticket.Bus.BusCard;
import webbank.com.busticket.Bus.BusCardAdapter;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.DeportingActivity;
import webbank.com.busticket.R;
import webbank.com.busticket.SeatActivity;
import webbank.com.busticket.TravellerActivity;

/**
 * Created by Dpshkhnl on 2017-03-18.
 */

public class BoardingCardAdapter  extends ArrayAdapter<BoardingPoint> {
    private List<BoardingPoint> boardingPoints = new ArrayList<BoardingPoint>();

    static class CardViewHolder {
        TextView boardingPoint;
        TextView boardingTime;;
        TextView boardOrDeport;

    }

    public BoardingCardAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(BoardingPoint object) {
        boardingPoints.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.boardingPoints.size();
    }

    @Override
    public BoardingPoint getItem(int index) {
        return this.boardingPoints.get(index);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        BoardingCardAdapter.CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.boarding_card, parent, false);
            viewHolder = new BoardingCardAdapter.CardViewHolder();
            viewHolder.boardingPoint = (TextView) row.findViewById(R.id.txtBoardingPoint);
            viewHolder.boardingTime = (TextView) row.findViewById(R.id.txtBoardingTime);
            viewHolder.boardOrDeport =(TextView) row.findViewById(R.id.boardOrDeport);
            row.setTag(viewHolder);
        } else {
            viewHolder = (BoardingCardAdapter.CardViewHolder)row.getTag();

        }
        final BoardingPoint card = getItem(position);
        viewHolder.boardingPoint.setText(card.getBoardingPoint());
        viewHolder.boardingTime.setText(card.getTime());
        viewHolder.boardOrDeport.setText(card.getBoardOrDeport());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.getBoardOrDeport().equals("Boarding")) {

                    TinyDB tinyDB = new TinyDB(parent.getContext());
                    tinyDB.putString("firstPoint",card.getBoardingPoint());
                    tinyDB.putInt("boardingId",card.getBoardingId());
                    Intent intent = new Intent(parent.getContext(), DeportingActivity.class);
                    parent.getContext().startActivity(intent);
                }
                else
                {
                    TinyDB tinyDB = new TinyDB(parent.getContext());
                    tinyDB.putString("lastPoint",card.getBoardingPoint());
                    tinyDB.putInt("deportingId",card.getBoardingId());
                    Intent intent = new Intent(parent.getContext(), TravellerActivity.class);
                    parent.getContext().startActivity(intent);
                }

            }
        });


        return row;
    }


}

