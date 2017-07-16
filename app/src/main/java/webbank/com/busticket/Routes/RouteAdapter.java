package webbank.com.busticket.Routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import webbank.com.busticket.R;

/**
 * Created by Dpshkhnl on 2017-04-03.
 */

public class RouteAdapter  extends ArrayAdapter<RoutesModel> {
    private List<RoutesModel> boardingPoints = new ArrayList<RoutesModel>();
    public List<RoutesModel> lstTemp = new ArrayList<>();
    static class CardViewHolder {
        TextView sourceDest;

    }

    public RouteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(RoutesModel object) {
        boardingPoints.add(object);

        super.add(object);
    }

    @Override
    public int getCount() {
        return this.boardingPoints.size();
    }

    @Override
    public RoutesModel getItem(int index) {
        return this.boardingPoints.get(index);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        RouteAdapter.CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.card_source_destination, parent, false);
            viewHolder = new RouteAdapter.CardViewHolder();
            viewHolder.sourceDest = (TextView) row.findViewById(R.id.txtRoute);
            row.setTag(viewHolder);
        } else {
            viewHolder = (RouteAdapter.CardViewHolder)row.getTag();

        }
        final RoutesModel card = getItem(position);
        viewHolder.sourceDest.setText(card.getFrom());

       /* row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
*/

        return row;
    }

    @Override
    public Filter getFilter() {
        if (lstTemp.size()==0){
            lstTemp  = new ArrayList<>(boardingPoints);
        }
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) { // if your editText field is empty, return full list of FriendItem
                    results.count = lstTemp.size();
                    results.values = lstTemp;
                } else {
                    List<RoutesModel> filteredList = new ArrayList<>();

                    constraint = constraint.toString().toLowerCase(); // if we ignore case
                    for (RoutesModel item : lstTemp) {
                        String firstName = item.getFrom().toLowerCase(); // if we ignore case

                        if (firstName.contains(constraint.toString())) {
                            filteredList.add(item); // added item witch contains our text in EditText
                        }
                    }

                    results.count = filteredList.size(); // set count of filtered list
                    results.values = filteredList; // set filtered list
                }
                return results; // return our filtered list
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                boardingPoints = (List<RoutesModel>) results.values; // replace list to filtered list
                notifyDataSetChanged(); // refresh adapter
            }
        };
        return filter;
    }
}
