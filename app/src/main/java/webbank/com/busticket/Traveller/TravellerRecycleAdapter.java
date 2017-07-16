package webbank.com.busticket.Traveller;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import webbank.com.busticket.R;

/**
 * Created by Dpshkhnl on 2017-03-27.
 */

public class TravellerRecycleAdapter extends RecyclerView.Adapter<TravellerRecycleAdapter.MyViewHolder> {

    private List<TravellerDetails> travellerDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText name, age;
        public Spinner gender;
        public Button uploadImg;
        public TextView seatNo;
        public TextView seatCategory;

        public MyViewHolder(View view) {
            super(view);
            name = (EditText) view.findViewById(R.id.et_name);
            age = (EditText) view.findViewById(R.id.et_age);
            gender = (Spinner) view.findViewById(R.id.gender);
           // uploadImg = (Button)view.findViewById(R.id.btnUpload);
            seatNo = (TextView) view.findViewById(R.id.txtSeatN0);
            seatCategory = (TextView) view.findViewById(R.id.txtSeatCat);



        }
    }


    public TravellerRecycleAdapter(List<TravellerDetails> travellerDetailses) {
        this.travellerDetailsList = travellerDetailses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_traveller_detail_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TravellerDetails travellerDetails = travellerDetailsList.get(position);

        holder.seatNo.setText(travellerDetails.getSeatNo());
        holder.seatCategory.setText(travellerDetails.getSeatCategory());
        travellerDetailsList.get(position).setGender(holder.gender.getSelectedItem().toString());
        holder.gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int po, long id) {
                // your code here
                travellerDetailsList.get(position).setGender(holder.gender.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                travellerDetailsList.get(position).setGender(holder.gender.getSelectedItem().toString());
            }

        });


        holder.name.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    travellerDetailsList.get(position).setName(charSequence.toString());

            }
        });

        holder.age.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    travellerDetailsList.get(position).setAge(charSequence.toString());

            }
        });

        /*holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());*/
    }

    @Override
    public int getItemCount() {
        return travellerDetailsList.size();
    }


    public TravellerDetails getItem(int position)
    {
        TravellerDetails travellerDetails = travellerDetailsList.get(position);

        return travellerDetails;
    }


}