package id.ac.umn.nutrimo.periksa;
import id.ac.umn.nutrimo.ChildModel;
import id.ac.umn.nutrimo.Login;
import id.ac.umn.nutrimo.MainActivity;
import id.ac.umn.nutrimo.R;
import id.ac.umn.nutrimo.RoomDB;
import id.ac.umn.nutrimo.Setting;
import id.ac.umn.nutrimo.article.Article;
import id.ac.umn.nutrimo.menu.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Periksa extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView setting,beranda,artikel,menu;
    RecyclerView history_rv;
    TextView childrenProfile;

    String activeChildId,gender;
    FloatingActionButton addHistory;
    LineChart chartHaz, chartWhz;
    RoomDB db;
    HAZDao hazDao;
    WHZDao whzDao;
    DatabaseReference historyRef, mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periksa);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        Intent intent = getIntent();
        activeChildId = intent.getStringExtra("Child");
        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting.class);
                intent.putExtra("Child",activeChildId);
                startActivity(intent);
            }
        });
        beranda = findViewById(R.id.beranda);
        beranda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Child",activeChildId);
                startActivity(intent);
                finish();
            }
        });
        artikel = findViewById(R.id.artikel);
        artikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Article.class);
                intent.putExtra("Child",activeChildId);
                startActivity(intent);
            }
        });
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                intent.putExtra("Child",activeChildId);
                startActivity(intent);
                finish();
            }
        });
        db = RoomDB.getInstance(getApplicationContext());
        hazDao = db.hazDao();
        whzDao = db.whzDao();
        childrenProfile = findViewById(R.id.childrenProfile);
        mRef=  FirebaseDatabase.getInstance().getReference().child("Childs").child(user.getUid());
        historyRef = FirebaseDatabase.getInstance().getReference().child("History").child(activeChildId);
        updateDataAndGraph();



        //Recycle View For Check History
        history_rv = findViewById(R.id.history_rv);
        history_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if (user != null && activeChildId != null) {
            FirebaseRecyclerOptions<HistoryModel> options =
                    new FirebaseRecyclerOptions.Builder<HistoryModel>()
                            .setQuery(historyRef.orderByChild("age"), HistoryModel.class)
                            .build();
            FirebaseRecyclerAdapter<HistoryModel, Periksa.historyViewHolder> adapter = new FirebaseRecyclerAdapter<HistoryModel, historyViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull historyViewHolder holder, int position, @NonNull HistoryModel model) {
                    holder.history.setText(model.getDate());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String id_check = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                            Intent detail = new Intent(Periksa.this, detailHistory.class);
                            detail.putExtra("id_check",id_check);
                            detail.putExtra("Child",activeChildId);
                            startActivity(detail);
                        }
                    });
                }

                @NonNull
                @Override
                public historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);
                    Periksa.historyViewHolder viewHolder = new Periksa.historyViewHolder(view);
                    return viewHolder;
                }
            };
            history_rv.setAdapter(adapter);
            adapter.startListening();
        }
        //FAB to add new Check History
        addHistory = findViewById(R.id.addHistory);
        addHistory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent history = new Intent(getApplicationContext(),NewHistory.class);
            history.putExtra("Child",activeChildId);
            startActivity(history);
        }
    });
    }
    private void updateDataAndGraph(){
        mRef.orderByKey().equalTo(activeChildId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChildModel chi = new ChildModel();
                for(DataSnapshot childs: snapshot.getChildren()){
                    chi = childs.getValue(ChildModel.class);
                }
                childrenProfile.setText(chi.getName());
                gender = chi.getGender();
                //HAZ Graph
                chartHaz = findViewById(R.id.graphHAZ);
                drawHazGraph();
                //WHZ Graph
                chartWhz = findViewById(R.id.graphWHZ);
                drawWhzGraph();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected  void onResume(){
        super.onResume();
        updateDataAndGraph();
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public class historyViewHolder extends  RecyclerView.ViewHolder{
        TextView history;
        public historyViewHolder(@NonNull View itemView) {
            super(itemView);
            history = itemView.findViewById(R.id.history);
        }
    }
    private void  drawHazGraph(){
        chartHaz.setBackgroundColor(Color.parseColor("#9DC08B"));
        chartHaz.getDescription().setEnabled(false);
        chartHaz.clear();
        List<HazEntity> listHaz = hazDao.getHazByGender(gender);
        if (listHaz == null) {
            return;
        }
        ArrayList<Entry> nsd3 = new ArrayList<>();
        ArrayList<Entry> nsd2 = new ArrayList<>();
        ArrayList<Entry> nsd1 = new ArrayList<>();
        ArrayList<Entry> median = new ArrayList<>();
        ArrayList<Entry> psd1 = new ArrayList<>();
        ArrayList<Entry> psd2 = new ArrayList<>();
        ArrayList<Entry> psd3 = new ArrayList<>();
        ArrayList<Entry> history = new ArrayList<>();

        for(HazEntity haz : listHaz){
            int usia = haz.getUsia();
            nsd3.add(new Entry(usia,(float) haz.getNsd3()));
            nsd2.add(new Entry(usia,(float) haz.getNsd2()));
            nsd1.add(new Entry(usia,(float) haz.getNsd1()));
            median.add(new Entry(usia,(float) haz.getMedian()));
            psd1.add(new Entry(usia,(float) haz.getPsd1()));
            psd2.add(new Entry(usia,(float) haz.getPsd2()));
            psd3.add(new Entry(usia,(float) haz.getPsd3()));
        }
        //add history
        historyRef.orderByChild("age").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        HistoryModel hM = dataSnapshot.getValue(HistoryModel.class);
                        int age = hM.getAge();
                        float height = (float) hM.getHeight();
                        history.add(new Entry(age,height));
                    }
                }
                //Input to Graph
                LineDataSet line1 = new LineDataSet(nsd3,"-3 SD");
                LineDataSet line2 = new LineDataSet(nsd2,"-2 SD");
                LineDataSet line3 = new LineDataSet(nsd1,"-1 SD");
                LineDataSet line4 = new LineDataSet(median,"Median");
                LineDataSet line5 = new LineDataSet(psd1,"+1 SD");
                LineDataSet line6 = new LineDataSet(psd2,"+2 SD");
                LineDataSet line7 = new LineDataSet(psd3,"+3 SD");
                LineDataSet lineUtama = new LineDataSet(history, "Riwayat");
                Log.d("Status Fungsi: ", "Input Graph");
                line1.setDrawCircles(false);
                line1.setColor(Color.parseColor("#7d120a"));
                line2.setDrawCircles(false);
                line2.setColor(Color.parseColor("#8a3c0b"));
                line3.setDrawCircles(false);
                line3.setColor(Color.parseColor("#736f0e"));
                line4.setDrawCircles(false);
                line4.setColor(Color.GREEN);
                line5.setDrawCircles(false);
                line5.setColor(Color.parseColor("#736f0e"));
                line6.setDrawCircles(false);
                line6.setColor(Color.parseColor("#8a3c0b"));
                line7.setDrawCircles(false);
                line7.setColor(Color.parseColor("#7d120a"));
                lineUtama.setColor(Color.BLACK);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(line1);
                dataSets.add(line2);
                dataSets.add(line3);
                dataSets.add(line4);
                dataSets.add(line5);
                dataSets.add(line6);
                dataSets.add(line7);
                dataSets.add(lineUtama);

                LineData data = new LineData(dataSets);
                data.setDrawValues(false);
                chartHaz.setData(data);
                chartHaz.getAxisLeft().setDrawGridLines(false);
                chartHaz.getXAxis().setDrawGridLines(false);
                chartHaz.setDrawGridBackground(false);
                chartHaz.setTouchEnabled(true);
                chartHaz.setPinchZoom(true);
                chartHaz.setDragEnabled(true);
                chartHaz.setScaleEnabled(true);
                chartHaz.setVisibleYRangeMinimum(20f, YAxis.AxisDependency.LEFT);
                chartHaz.setVisibleXRangeMinimum(3f);
                chartHaz.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chartHaz.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void drawWhzGraph(){
        chartWhz.setBackgroundColor(Color.parseColor("#9DC08B"));
        chartWhz.getDescription().setEnabled(false);
        chartWhz.clear();
        List<WhzEntity> listWhz = whzDao.getWhzByGender(gender);
        if (listWhz == null) {
            return;
        }
        ArrayList<Entry> nsd3 = new ArrayList<>();
        ArrayList<Entry> nsd2 = new ArrayList<>();
        ArrayList<Entry> nsd1 = new ArrayList<>();
        ArrayList<Entry> median = new ArrayList<>();
        ArrayList<Entry> psd1 = new ArrayList<>();
        ArrayList<Entry> psd2 = new ArrayList<>();
        ArrayList<Entry> psd3 = new ArrayList<>();
        ArrayList<Entry> history = new ArrayList<>();

        for(WhzEntity whz : listWhz){
            double tinggi = whz.getTinggi();
            nsd3.add(new Entry((float) tinggi,(float) whz.getNsd3()));
            nsd2.add(new Entry((float) tinggi,(float) whz.getNsd2()));
            nsd1.add(new Entry((float) tinggi,(float) whz.getNsd1()));
            median.add(new Entry((float) tinggi,(float) whz.getMedian()));
            psd1.add(new Entry((float) tinggi,(float) whz.getPsd1()));
            psd2.add(new Entry((float) tinggi,(float) whz.getPsd2()));
            psd3.add(new Entry((float) tinggi,(float) whz.getPsd3()));
        }
        //add history
        historyRef.orderByChild("height").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        HistoryModel hM = dataSnapshot.getValue(HistoryModel.class);
                        float weight = (float) hM.getWeight();
                        float height = (float) hM.getHeight();
                       history.add(new Entry(height,weight));
                    }
                }
                //Input to Graph
                LineDataSet line1 = new LineDataSet(nsd3,"-3 SD");
                LineDataSet line2 = new LineDataSet(nsd2,"-2 SD");
                LineDataSet line3 = new LineDataSet(nsd1,"-1 SD");
                LineDataSet line4 = new LineDataSet(median,"Median");
                LineDataSet line5 = new LineDataSet(psd1,"+1 SD");
                LineDataSet line6 = new LineDataSet(psd2,"+2 SD");
                LineDataSet line7 = new LineDataSet(psd3,"+3 SD");
                LineDataSet lineUtama = new LineDataSet(history,"Riwayat");
                line1.setDrawCircles(false);
                line1.setColor(Color.parseColor("#7d120a"));
                line2.setDrawCircles(false);
                line2.setColor(Color.parseColor("#8a3c0b"));
                line3.setDrawCircles(false);
                line3.setColor(Color.parseColor("#736f0e"));
                line4.setDrawCircles(false);
                line4.setColor(Color.GREEN);
                line5.setDrawCircles(false);
                line5.setColor(Color.parseColor("#736f0e"));
                line6.setDrawCircles(false);
                line6.setColor(Color.parseColor("#8a3c0b"));
                line7.setDrawCircles(false);
                line7.setColor(Color.parseColor("#7d120a"));
                lineUtama.setColor(Color.BLACK);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(line1);
                dataSets.add(line2);
                dataSets.add(line3);
                dataSets.add(line4);
                dataSets.add(line5);
                dataSets.add(line6);
                dataSets.add(line7);
                dataSets.add(lineUtama);
                LineData data = new LineData(dataSets);
                data.setDrawValues(false);
                chartWhz.setData(data);
                chartWhz.getAxisLeft().setDrawGridLines(false);
                chartWhz.getXAxis().setDrawGridLines(false);
                chartWhz.setDrawGridBackground(false);
                chartWhz.setTouchEnabled(true);
                chartWhz.setPinchZoom(true);
                chartWhz.setDragEnabled(true);
                chartWhz.setScaleEnabled(true);
                chartWhz.setVisibleYRangeMinimum(1f, YAxis.AxisDependency.LEFT);
                chartWhz.setVisibleXRangeMinimum(3f);
                chartWhz.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chartWhz.invalidate();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}