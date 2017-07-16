package webbank.com.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import webbank.com.busticket.Bus.BusCard;
import webbank.com.busticket.Bus.BusSearchAPIModel;
import webbank.com.busticket.Bus.BusSearchInterface;
import webbank.com.busticket.Bus.BusSearchModel;
import webbank.com.busticket.Data.TinyDB;
import webbank.com.busticket.Seat.AbstractItem;
import webbank.com.busticket.Seat.AirplaneAdapter;
import webbank.com.busticket.Seat.BusDetailModel;
import webbank.com.busticket.Seat.CenterItem;
import webbank.com.busticket.Seat.EdgeItem;
import webbank.com.busticket.Seat.EmptyItem;
import webbank.com.busticket.Seat.OnSeatSelected;
import webbank.com.busticket.Seat.RestBusDetail;

/**
 * Created by Dpshkhnl on 2017-03-16.
 */

public class SeatActivity extends AppCompatActivity implements OnSeatSelected {

    private static final int COLUMNS = 5;
    private Button txtSeatSelected;
    private TextView featureMore;
    private ArrayList<String> selectedSeatNo = new ArrayList<>();
    private RelativeLayout relative_layout_main;
    TinyDB tinydb;
    int selectedBusId;
    String busNo;
    List<BusDetailModel>  busDetail = new ArrayList<>();
    final List<AbstractItem> items = new ArrayList<>();
   // final List<AbstractItem> itemsCabin = new ArrayList<>();

    AirplaneAdapter adapter = null;
    //AirplaneAdapter adapterCabin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Seats");

        relative_layout_main = (RelativeLayout) findViewById(R.id.relative_layout_main);

        txtSeatSelected = (Button) findViewById(R.id.txt_seat_selected);
        featureMore = (TextView) findViewById(R.id.featureMore);
        tinydb = new TinyDB(getApplicationContext());
        adapter = new AirplaneAdapter(this, items);
       // adapterCabin = new AirplaneAdapter(this, itemsCabin);


        selectedBusId = tinydb.getInt("selectedBusId");
        busNo = tinydb.getString("busNo");

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(SeatActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("We are loading seats for you");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        BusSearchInterface mApiService = this.getInterfaceService();
        Call<RestBusDetail> mService = mApiService.getBusDetailByBusId(String.valueOf(selectedBusId),busNo);

        mService.enqueue(new Callback<RestBusDetail>() {
            @Override
            public void onResponse(Call<RestBusDetail> call, Response<RestBusDetail> response) {
                busDetail  = response.body().getResult();
                BusDetailModel busSeat = busDetail.get(0);
                tinydb.putString("busCategory",busSeat.getBus_category());
                tinydb.putInt("busCompanyId",busSeat.getCompany());
                designBusSeat(busSeat);

                //Toast.makeText(SeatActivity.this, "Ah Rececived"+busDetail.get(0).getBus_name(), Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
                progressDoalog.dismiss();
                //adapterCabin.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<RestBusDetail> call, Throwable t) {
                call.cancel();
                progressDoalog.dismiss();
                Toast.makeText(SeatActivity.this, "Error,Something seems not right", Toast.LENGTH_LONG).show();
            }
        });


        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);


        recyclerView.setAdapter(adapter);

        /*RecyclerView recyclerViewCabin = (RecyclerView) findViewById(R.id.lst_items_cabin);
        recyclerViewCabin.setLayoutManager(manager);


        recyclerViewCabin.setAdapter(adapterCabin);*/

        txtSeatSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtSeatSelected.getText().equals("Book")){
                    Toast.makeText(SeatActivity.this, "Please select at least one seat", Toast.LENGTH_LONG).show();
                    return;
                }

                selectedSeatNo = null;
                selectedSeatNo = new ArrayList<String>();
                for (AbstractItem item : adapter.mItems)
                {
                    if (item.getSeatStatus()==2)
                    {
                       selectedSeatNo.add(item.getLabel());

                    }
                }
                tinydb.putListString("SelectedSeat", selectedSeatNo);
                Intent intent=new Intent(getApplicationContext(), BoardingActivity.class);
                startActivity(intent);


            }
        });

        featureMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wrapInScrollView = true;
                new MaterialDialog.Builder(SeatActivity.this)
                        .title("Seat Type")
                        .customView(R.layout.seat_type, wrapInScrollView)
                        .positiveText("Ok")
                        .show();

            }
        });
    }

    @Override
    public void onSeatSelected(int count) {
        if (count >6)
        {
            return;
        }
        RelativeLayout items = (RelativeLayout)findViewById(R.id.mainRelative);
        LinearLayout child1 = (LinearLayout)findViewById(R.id.lin_price_container);
        items.removeView(child1);
        if(count >0) {
            selectedSeatNo = null;
            selectedSeatNo = new ArrayList<String>();
            for (AbstractItem item : adapter.mItems) {
                if (item.getSeatStatus() == 2) {
                    selectedSeatNo.add(item.getLabel());

                }
            }
            String rate = tinydb.getString("rate");
            RelativeLayout item = (RelativeLayout) findViewById(R.id.mainRelative);
            View child = getLayoutInflater().inflate(R.layout.selected_seats, null);
            TextView txtSeats = (TextView) child.findViewById(R.id.tv_selected_bus);
            TextView txtPrice = (TextView) child.findViewById(R.id.tv_selected_price);

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            p.addRule(RelativeLayout.ABOVE, R.id.txt_seat_selected);
            LinearLayout child2 = (LinearLayout)findViewById(R.id.lin_price_container);
            child.setLayoutParams(p);
            txtSeats.setPadding(0, 5, 0, 0);
            String seats = "";
            String price = "";
            for (String str : selectedSeatNo) {
                int space=0;
                if(seats.equals(""))
                {
                    seats =  str;
                    price =  "Rs." + rate;
                    space=price.length()-seats.length();
                }
                else
                {
                    int blank = rate.length()-str.length()+space+6+(rate.length()+1)/2;
                    for(int j = 1; j<=blank;j++)
                    {
                        seats+= "\u00A0";
                    }
                    seats +=  str;
                    price += "\u00A0\u00A0" + "Rs." + rate;
                }

            }
            txtSeats.setText(seats);
            txtPrice.setText(price);
            item.addView(child);


        }
        txtSeatSelected.setText("Book " + count + " seats");
    }
    private BusSearchInterface getInterfaceService() {

        String base_url = getResources().getString(R.string.BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final BusSearchInterface mInterfaceService = retrofit.create(BusSearchInterface.class);
        return mInterfaceService;
    }

    public void designBusSeat(BusDetailModel busDetailModel) {

        int sidea = busDetailModel.getTotal_sheet_in_a_side();
        int sideb = busDetailModel.getTotal_sheet_in_b_side();
        int cabinSeat = busDetailModel.getCabin();
        int lastSeat = busDetailModel.getLast_row();
        int specialSeat = busDetailModel.getSpecial();
        List<String> bookedSeat = busDetailModel.getBooked_seat();
        String rev_female = busDetailModel.getRev_female();
        String rev_Student = busDetailModel.getRev_student();
        String rev_old = busDetailModel.getRev_old();
        String rev_staff = busDetailModel.getRev_staff();
        String rev_handicap = busDetailModel.getRev_handicap();
        String type = busDetailModel.getType();
        String hiaces = busDetailModel.getHices();
        String force = busDetailModel.getForce();
        if(bookedSeat == null)
        {
            bookedSeat = new ArrayList<>();
        }
        if (type.equals("hice"))
        {
            loadHiaceSeats(hiaces,bookedSeat);
        }else if (type.equals("sumo"))
        {
            loadSumoSeats(bookedSeat);
        }else
        {
            loadBusAndForceSeat(bookedSeat,cabinSeat,sidea,sideb,lastSeat,specialSeat,rev_female,rev_Student,rev_old,rev_staff,rev_handicap);
        }

    }

    public void loadHiaceSeats(String hiaces,List<String> bookedSeat)
    {
        String[] parts = hiaces.split(",");
        int aCount = 2;

        int seats = COLUMNS-Integer.valueOf(parts[0])%COLUMNS;
        for (int i = 0; i < Integer.valueOf(parts[0])+seats; i++) {
            int status = 0;
            String seat = "";
            int a = i % (COLUMNS);
            if (a == 1 || a == 2) {
                seat = "S" + aCount;
                aCount--;
            }
    for (String bookedSt : bookedSeat) {
        if (seat.equals(bookedSt)) {
            status = 1;
        }
    }

            if (i % COLUMNS == 1) {
                items.add(new CenterItem(String.valueOf(seat), status));
                    /*items.add(new EdgeItem(String.valueOf(seat), 0));*/
            } else if (i % COLUMNS == 2) {
                items.add(new CenterItem(String.valueOf(seat), status));
            } else {
                items.add(new EmptyItem(String.valueOf(seat), status));
            }
        }

        seats = COLUMNS-Integer.valueOf(parts[1])%COLUMNS;
        int  firstRow = 3;
        for (int i = 0; i < Integer.valueOf(parts[1])+seats; i++) {

            String seat = "";
            int status=0;
            int a = i % (COLUMNS);
            if (a == 2 || a == 3||a == 4) {
                seat = "A" + firstRow;
                firstRow--;
            }

            for(String bookedSt: bookedSeat) {
                if (seat.equals(bookedSt)) {
                    status = 1;
                    break;
                }
            }
            if (i % COLUMNS == 4) {

                items.add(new EdgeItem(String.valueOf(seat), status));
            } else if (i % COLUMNS == 3||i % COLUMNS == 2) {
                items.add(new CenterItem(String.valueOf(seat), status));
            } else {
                items.add(new EmptyItem(String.valueOf(seat), status));
            }
        }

        seats = COLUMNS-Integer.valueOf(parts[2])%COLUMNS;
        seats += COLUMNS-Integer.valueOf(parts[3])%COLUMNS;
        int  bakiRow = 3;
        int  bakiRow2 = 3;
        for (int i = 0; i < Integer.valueOf(parts[2])+Integer.valueOf(parts[3])+seats; i++) {

            String seat = "";
            int status =0;
            int a = i % (COLUMNS);
            if (a == 1 || a == 3||a == 4) {
                if(i <5) {
                    seat = "B" + bakiRow;
                    bakiRow--;
                }
                else
                {
                    seat = "C" + bakiRow2;
                    bakiRow2--;
                }


            }

            for(String bookedSt: bookedSeat) {
                if (seat.equals(bookedSt)) {
                    status = 1;
                    break;
                }
            }
            if (i % COLUMNS == 4) {

                items.add(new EdgeItem(String.valueOf(seat), status));
            } else if (i % COLUMNS == 3||i % COLUMNS == 1) {
                items.add(new CenterItem(String.valueOf(seat), status));
            } else {
                items.add(new EmptyItem(String.valueOf(seat), status));
            }
        }

        seats = COLUMNS-Integer.valueOf(parts[4])%COLUMNS;
        int  lastRow = 4;
        for (int i = 0; i < Integer.valueOf(parts[4])+seats; i++) {

            String seat = "";
            int status  =0;
            int a = i % (COLUMNS);
            if (a == 1 ||a == 2 || a == 3||a == 4) {
                seat = "D" + lastRow;
                lastRow--;
            }

            for(String bookedSt: bookedSeat) {
                if (seat.equals(bookedSt)) {
                    status = 1;
                    break;
                }
            }
            if (i % COLUMNS == 4) {

                items.add(new EdgeItem(String.valueOf(seat), status));
            } else if (i % COLUMNS == 3||i % COLUMNS == 1||i % COLUMNS == 2) {
                items.add(new CenterItem(String.valueOf(seat), status));
            } else {
                items.add(new EmptyItem(String.valueOf(seat), status));
            }
        }

    }

    public void loadSumoSeats(List<String> bookedSeat)
    {
        int aCount = 2;
        int seats = 3;

        for (int i = 0; i < COLUMNS; i++) {

            String seat = "";
            int status = 0;
            int a = i % (COLUMNS);
            if (a == 1 || a == 2) {
                seat = "F" + aCount;
                aCount--;
            }

            for (String bookedSt : bookedSeat) {
                if (seat.equals(bookedSt)) {
                    status = 1;
                }
            }
            if (i % COLUMNS == 1) {
                items.add(new CenterItem(String.valueOf(seat), status));
                    /*items.add(new EdgeItem(String.valueOf(seat), 0));*/
            } else if (i % COLUMNS == 2) {
                items.add(new CenterItem(String.valueOf(seat), status));
            } else {
                items.add(new EmptyItem(String.valueOf(seat), status));
            }
        }

        int  lastRowA = 4;
        int  lastRowB = 4;
        int  lastRowC= 4;
        for (int i = 0; i < COLUMNS*3; i++) {

            String seat = "";
            int status = 0;
            int a = i % (COLUMNS);
            if (a == 1 ||a == 2 || a == 3||a == 4) {
                if(i<5) {
                    seat = "A" + lastRowA;
                    lastRowA--;
                }else if(i<10){
                    seat = "B" + lastRowB;
                    lastRowB--;
                }else
                {
                    seat = "C" + lastRowC;
                    lastRowC--;
                }
            }
            for (String bookedSt : bookedSeat) {
                if (seat.equals(bookedSt)) {
                    status = 1;
                }
            }

            if (i % COLUMNS == 4) {

                items.add(new EdgeItem(String.valueOf(seat), status));
            } else if (i % COLUMNS == 3||i % COLUMNS == 1||i % COLUMNS == 2) {
                items.add(new CenterItem(String.valueOf(seat), status));
            } else {
                items.add(new EmptyItem(String.valueOf(seat), status));
            }
        }

    }
public  void loadBusAndForceSeat(List<String>bookedSeat,int cabinSeat,int sidea,int sideb ,int lastSeat,int sepical,String  rev_female,String rev_Student,String rev_old,String rev_staff,String rev_handicap )
{

    //CabinSeat
    int cabinRows = 1;
    if (cabinSeat>2)
        cabinRows = (cabinSeat + 1) / 2;
    int emptyCabin =COLUMNS- cabinSeat%COLUMNS;
    if (cabinSeat > 0) {
    int aCount = 1;
    for (int i = 0; i < (cabinSeat+emptyCabin)*cabinRows; i++) {


        String seat = "";
        int status =0;
        int a = i % (COLUMNS);
        if (a == 1 || a == 0) {
            seat = "C" + aCount;
            aCount++;
        }
        if (sepical >0)
        {

        }
        for (String bookedSt : bookedSeat) {
            if (seat.equals(bookedSt)) {
                status = 1;
            }
        }
        if (i % COLUMNS == 0) {
            items.add(new EdgeItem(String.valueOf(seat), status));
        } else if (i % COLUMNS == 1) {
            items.add(new CenterItem(String.valueOf(seat), status));
        } else {
            items.add(new EmptyItem(String.valueOf(seat), status));
        }
    }

}

    int specials = 1;
    if (sepical>0)
        specials = (sepical + 1) / 2;
    int emptySpecial =COLUMNS- sepical%COLUMNS;
    if (sepical > 0) {
        int aCount = 1;
        for (int i = 0; i < (sepical+emptySpecial)*specials; i++) {


            String seat = "";
            int status =0;
            int a = i % (COLUMNS);
            if (a == 3 || a == 4) {
                seat = "S" + aCount;
                aCount++;
            }
            for (String bookedSt : bookedSeat) {
                if (seat.equals(bookedSt)) {
                    status = 1;
                }
            }
            if (i % COLUMNS == 4) {
                items.add(new EdgeItem(String.valueOf(seat), status));
            } else if (i % COLUMNS == 3) {
                items.add(new CenterItem(String.valueOf(seat), status));
            } else {
                items.add(new EmptyItem(String.valueOf(seat), status));
            }
        }

    }


       /* int remainSeat = COLUMNS-cabinSeat%COLUMNS;
            for (int i = 0 ; i<remainSeat ;i++)
            {
                items.add(new EmptyItem(String.valueOf(""), 0));
            }*/

    //Side A
    int seatRem = COLUMNS-(sidea+sideb)%COLUMNS;
    if (sidea+sideb > 0) {
        int aCount = 1;
        int bCount = 1;
        final String[] female = rev_female.split(",");
        final String[] student = rev_Student.split(",");
        final String[] staff = rev_staff.split(",");
        final String[] handicaff = rev_handicap.split(",");
        final String[] oldCiti = rev_old.split(",");
        for (int i = 0; i < sidea+sideb+seatRem; i++) {

            String seat = "";
            int status =0;
            int a = i % (COLUMNS);
            if (a== 1 ||a== 0)
            {
                seat="A"+aCount;
                aCount++;
            }else if(a== 4 ||a == 3)
            {
                seat ="B"+bCount;
                bCount++;
            }

            for (String bookedSt : bookedSeat) {
                if (seat.equals(bookedSt)) {
                    status = 1;
                }
            }
            if(seat.equals(female[2]+female[1])||seat.equals(female[2]+female[0]))
            {
                seat = "F"+seat;
            }else if(seat.equals(oldCiti[2]+oldCiti[1])||seat.equals(oldCiti[2]+oldCiti[0]))
            {
                seat = "O"+seat;
            }else if(seat.equals(handicaff[2]+handicaff[1])||seat.equals(handicaff[2]+handicaff[0]))
            {
                seat = "H"+seat;
            }else if(seat.equals(staff[2]+staff[1])||seat.equals(staff[2]+staff[0]))
            {
                seat = "St"+seat;
            }else if(seat.equals(student[2]+student[1])||seat.equals(student[2]+student[0]))
            {
                seat = "S"+seat;
            }

            if (i % COLUMNS == 0 || i % COLUMNS == 4) {
                items.add(new EdgeItem(String.valueOf(seat),status));
            } else if (i % COLUMNS == 1 || i % COLUMNS == 3) {
                items.add(new CenterItem(String.valueOf(seat),status));
            } else {
                items.add(new EmptyItem(String.valueOf(seat),status));
            }
        }
    }

    //Last Seat

    if (lastSeat > 0) {
        int aCount = lastSeat;

        for (int i = 0; i < lastSeat; i++) {

            String seat = "";
            int status =0;
            int a = i % (COLUMNS);

            seat = "L" + aCount;
            aCount--;
            for (String bookedSt : bookedSeat) {
                if (seat.equals(bookedSt)) {
                    status = 1;
                }
            }

            if (i % COLUMNS == 0) {
                items.add(new EdgeItem(String.valueOf(seat), status));
            } else if (i % COLUMNS == 1) {
                items.add(new CenterItem(String.valueOf(seat), status));
            } else {
                items.add(new CenterItem(String.valueOf(seat), status));
            }
        }

    }

}
}