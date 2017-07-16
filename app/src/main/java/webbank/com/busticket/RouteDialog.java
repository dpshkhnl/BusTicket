package webbank.com.busticket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import webbank.com.busticket.Bus.BusCardAdapter;
import webbank.com.busticket.Routes.RouteAdapter;

/**
 * Created by Dpshkhnl on 2017-04-07.
 */

public class RouteDialog extends DialogFragment  implements View.OnClickListener {

    private ListView list;
    RouteAdapter cardArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.dialog_list_route,container,false);
        getDialog().setTitle("Route");
     EditText   filterText = (EditText) mView.findViewById(R.id.EditBox);
        filterText.addTextChangedListener(filterTextWatcher);
        list = (ListView) mView.findViewById(R.id.List);
        cardArrayAdapter = new RouteAdapter(mView.getContext(), R.layout.card_source_destination);

        list.setAdapter(cardArrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Log.d("", "Selected Item is = "+list.getItemAtPosition(position));
            }
        });

        return mView;
    }

    View.OnClickListener DoneAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onClick(View view) {

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            cardArrayAdapter.getFilter().filter(s);
        }
    };
}
