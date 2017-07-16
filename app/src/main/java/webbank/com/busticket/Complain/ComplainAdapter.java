package webbank.com.busticket.Complain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import webbank.com.busticket.ComplainActivity;
import webbank.com.busticket.R;

/**
 * Created by Dpshkhnl on 2017-05-02.
 */

public class ComplainAdapter extends ArrayAdapter<ComplainModel> {
    private List<ComplainModel> travelHistLst = new ArrayList<ComplainModel>();

    static class CardViewHolder {
        TextView txtDate;
        TextView txtMonth;
        TextView txtSubject;
        TextView txtService;
        TextView txtStatus;


    }

    public ComplainAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(ComplainModel object) {
        travelHistLst.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.travelHistLst.size();
    }

    @Override
    public ComplainModel getItem(int index) {
        return this.travelHistLst.get(index);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        ComplainAdapter.CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.card_complain, parent, false);
            viewHolder = new ComplainAdapter.CardViewHolder();
            viewHolder.txtMonth = (TextView) row.findViewById(R.id.txtMonth);
            viewHolder.txtDate = (TextView) row.findViewById(R.id.txtDate);
            viewHolder.txtSubject = (TextView) row.findViewById(R.id.txtSubject);
            viewHolder.txtService = (TextView) row.findViewById(R.id.txtService);
            viewHolder.txtStatus =(TextView) row.findViewById(R.id.txtStatus);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ComplainAdapter.CardViewHolder)row.getTag();

        }
        final ComplainModel card = getItem(position);
        viewHolder.txtStatus.setText(card.getIs_active().equals("N")?"Closed":"Open");
        viewHolder.txtStatus.setBackgroundColor(card.getIs_active().equals("N")?Color.RED:Color.GREEN);
        viewHolder.txtMonth.setText(card.getMonth());
        viewHolder.txtDate.setText(card.getDay());
        viewHolder.txtSubject.setText(card.getSubject());
        viewHolder.txtService.setText(card.getService());


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wrapInScrollView = true;
                MaterialDialog dialog =  new MaterialDialog.Builder(parent.getContext())
                        .title("Complain Details")
                        .positiveText("Ok")
                        .customView(R.layout.activity_add_complain, wrapInScrollView)
                        .show();

                View dialogView = dialog.getCustomView();
                EditText txtSubject = (EditText) dialogView.findViewById(R.id.subject);
                Spinner txtServices = (Spinner) dialogView.findViewById(R.id.txtService);
                EditText txtMessage = (EditText) dialogView.findViewById(R.id.txtMessage);
                txtSubject.setFocusable(false);
                txtServices.setEnabled(false);
                txtMessage.setFocusable(false);
               txtSubject.setText(card.getSubject());
                String[] array = parent.getResources().getStringArray(R.array.brew_array);
                for(int i=0;i<array.length;i++) {
                    if(array[i].equals(card.getService())) {
                        txtServices.setSelection(i);
                        break;
                    }
                }

                txtMessage.setText(card.getMessage());

            }
        });


        return row;
    }

    public void SetSpinnerSelection(Spinner spinner,String[] array,String text) {
        for(int i=0;i<array.length;i++) {
            if(array[i].equals(text)) {
                spinner.setSelection(i);
            }
        }
    }
}
