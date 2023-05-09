package id.ac.umn.nutrimo;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.nutrimo.article.Article;
import id.ac.umn.nutrimo.menu.Menu;
import id.ac.umn.nutrimo.periksa.HAZDao;
import id.ac.umn.nutrimo.periksa.HazEntity;
import id.ac.umn.nutrimo.periksa.HistoryModel;
import id.ac.umn.nutrimo.periksa.Periksa;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView setting, periksa, artikel, menu;
    ImageSlider artikelSlider;
    Button children;
    RecyclerView child_rv;
    String activeChildId, gender;
    LineChart graphMain;
    RoomDB db;
    HAZDao hazDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = RoomDB.getInstance(getApplicationContext());
        hazDao = db.hazDao();

        children = findViewById(R.id.childrenProfile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        graphMain = findViewById(R.id.graphMain);
        Intent intent = getIntent();
        activeChildId = intent.getStringExtra("Child");
        changeActiveChild();

        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting.class);
                intent.putExtra("Child", activeChildId);
                startActivity(intent);
            }
        });
        periksa = findViewById(R.id.periksa);
        periksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Periksa.class);
                intent.putExtra("Child", activeChildId);
                startActivity(intent);
                finish();
            }
        });
        artikel = findViewById(R.id.artikel);
        artikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Article.class);
                intent.putExtra("Child", activeChildId);
                startActivity(intent);

            }
        });
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                intent.putExtra("Child", activeChildId);
                startActivity(intent);
                finish();
            }
        });
        artikelSlider = findViewById(R.id.artikelSlider);
        final List<SlideModel> sliderArticle = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Slider").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    sliderArticle.add(new SlideModel(data.child("url").getValue().toString(), ScaleTypes.FIT));
                }
                artikelSlider.setImageList(sliderArticle, ScaleTypes.FIT);
                artikelSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View newView = (View) inflater.inflate(R.layout.child_profile, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(newView);
        child_rv = newView.findViewById(R.id.childProfile_rv);
        child_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ImageView cancelButton = newView.findViewById(R.id.closeDialog);
        AppCompatButton addChild = newView.findViewById(R.id.addChild);
        if (user != null) {
            DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("Childs").child(user.getUid());
            FirebaseRecyclerOptions<ChildModel> options =
                    new FirebaseRecyclerOptions.Builder<ChildModel>()
                            .setQuery(childRef, ChildModel.class)
                            .build();
            FirebaseRecyclerAdapter<ChildModel, MainActivity.childViewHolder> adapter = new FirebaseRecyclerAdapter<ChildModel, childViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull childViewHolder holder, int position, @NonNull ChildModel model) {
                    holder.childName.setText((model.getName()));

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activeChildId = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                            changeActiveChild();
                            dialog.dismiss();
                        }
                    });
                }

                @NonNull
                @Override
                public MainActivity.childViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child, parent, false);
                    MainActivity.childViewHolder viewHolder = new MainActivity.childViewHolder(view);
                    return viewHolder;
                }
            };
            child_rv.setAdapter(adapter);
            adapter.startListening();
        }

        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), AddChildProfile.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public static class childViewHolder extends RecyclerView.ViewHolder {
        TextView childName;

        public childViewHolder(@NonNull View itemView) {
            super(itemView);
            childName = itemView.findViewById(R.id.list_child_name);
        }
    }

    public void changeActiveChild() {
        if (user == null) {
            drawGraph();
            return;
        }
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Childs").child(user.getUid());

        if (activeChildId == null) {
            mRef.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ChildModel chi = new ChildModel();
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot childs : snapshot.getChildren()) {
                            chi = childs.getValue(ChildModel.class);
                            activeChildId = childs.getKey();
                        }
                        children.setText(chi.getName());
                        gender = chi.getGender();
                        if (gender.equals("Perempuan")) {
                            Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.girl_32);
                            children.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        } else {
                            Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.boy_32);
                            children.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

                        }
                    }
                    drawGraph();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            mRef.orderByKey().equalTo(activeChildId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ChildModel chi = new ChildModel();
                    for (DataSnapshot childs : snapshot.getChildren()) {
                        chi = childs.getValue(ChildModel.class);
                    }
                    children.setText(chi.getName());
                    gender = chi.getGender();
                    if (gender.equals("Perempuan")) {
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.girl_32);
                        children.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    } else {
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.boy_32);
                        children.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

                    }
                    drawGraph();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Update Grafik

    }

    private void drawGraph() {
        graphMain.setBackgroundColor(Color.parseColor("#9DC08B"));
        graphMain.getDescription().setEnabled(false);

        if(user == null){
            graphMain.clear();
            graphMain.setNoDataText("Silahkan Login");
            graphMain.setNoDataTextColor(Color.BLACK);
            graphMain.invalidate();
            return;
        }
        if (activeChildId == null) {
            graphMain.clear();
            graphMain.setNoDataText("Pilih Profik Anak");
            graphMain.setNoDataTextColor(Color.BLACK);
            graphMain.invalidate();
            return;
        }

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

        for (HazEntity haz : listHaz) {
            int usia = haz.getUsia();
            nsd3.add(new Entry(usia, (float) haz.getNsd3()));
            nsd2.add(new Entry(usia, (float) haz.getNsd2()));
            nsd1.add(new Entry(usia, (float) haz.getNsd1()));
            median.add(new Entry(usia, (float) haz.getMedian()));
            psd1.add(new Entry(usia, (float) haz.getPsd1()));
            psd2.add(new Entry(usia, (float) haz.getPsd2()));
            psd3.add(new Entry(usia, (float) haz.getPsd3()));
        }
        //add history
        FirebaseDatabase.getInstance().getReference()
                .child("History").child(activeChildId).
                orderByChild("age").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                HistoryModel hM = dataSnapshot.getValue(HistoryModel.class);
                                int age = hM.getAge();
                                float height = (float) hM.getHeight();
                                history.add(new Entry(age, height));
                            }
                        }
                        //Input to Graph
                        LineDataSet line1 = new LineDataSet(nsd3, "-3 SD");
                        LineDataSet line2 = new LineDataSet(nsd2, "-2 SD");
                        LineDataSet line3 = new LineDataSet(nsd1, "-1 SD");
                        LineDataSet line4 = new LineDataSet(median, "Median");
                        LineDataSet line5 = new LineDataSet(psd1, "+1 SD");
                        LineDataSet line6 = new LineDataSet(psd2, "+2 SD");
                        LineDataSet line7 = new LineDataSet(psd3, "+3 SD");
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
                        graphMain.setData(data);
                        graphMain.getAxisLeft().setDrawGridLines(false);
                        graphMain.getXAxis().setDrawGridLines(false);
                        graphMain.setDrawGridBackground(false);
                        graphMain.setTouchEnabled(true);
                        graphMain.setPinchZoom(true);
                        graphMain.setDragEnabled(true);
                        graphMain.setScaleEnabled(true);
                        graphMain.setVisibleYRangeMinimum(20f, YAxis.AxisDependency.LEFT);
                        graphMain.setVisibleXRangeMinimum(3f);
                        graphMain.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        graphMain.invalidate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}

