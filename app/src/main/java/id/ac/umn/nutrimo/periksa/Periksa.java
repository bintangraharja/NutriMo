package id.ac.umn.nutrimo.periksa;
import id.ac.umn.nutrimo.MainActivity;
import id.ac.umn.nutrimo.R;
import id.ac.umn.nutrimo.Setting;
import id.ac.umn.nutrimo.article.Article;
import id.ac.umn.nutrimo.menu.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Periksa extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView setting,beranda,artikel,menu;
    RecyclerView history_rv;
    TextView childrenProfile;
    Integer usia;
    String statusTinggi, statusGizi, activeChildId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periksa);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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

        childrenProfile = findViewById(R.id.childrenProfile);
        usia = 13;
        statusTinggi = "Sangat Pendek";
        statusGizi = "Beresiko Gizi Lebih";
        history_rv = findViewById(R.id.history_rv);
        history_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if (user != null && activeChildId != null) {
            DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("History").child(activeChildId);
            FirebaseRecyclerOptions<HistoryModel> options =
                    new FirebaseRecyclerOptions.Builder<HistoryModel>()
                            .setQuery(historyRef, HistoryModel.class)
                            .build();
            FirebaseRecyclerAdapter<HistoryModel, Periksa.historyViewHolder> adapter = new FirebaseRecyclerAdapter<HistoryModel, historyViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull historyViewHolder holder, int position, @NonNull HistoryModel model) {
                    holder.history.setText(model.getDate());
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
            Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
        }
        forwardChaining(usia,statusTinggi,statusGizi);
    }
    public void forwardChaining(Integer usia, String statusTinggi, String statusGizi){
        if(usia <= 6){
            childrenProfile.setText("ASI Eksklusif");
        } else if (usia > 6 && usia <= 9) {
            if(statusGizi.equals("Gizi Buruk") || statusGizi.equals("Gizi Kurang") || statusTinggi.equals("Sangat Pendek")){
                childrenProfile.setText("MP-ASI Tinggi Kalori dan Protein");
            }else{
                childrenProfile.setText("MP-ASI Gizi Seimbang");
            }
        } else if (usia > 9 && usia <= 12) {
            if(statusGizi.equals("Gizi Buruk") || statusGizi.equals("Gizi Kurang")){
                childrenProfile.setText("MP-ASI Tinggi Kalori dan Protein");
            }else{
                childrenProfile.setText("MP-ASI Gizi Seimbang");
            }
        } else if (usia >12 && usia <=24) {
            if(statusGizi.equals("Gizi Baik")){
                childrenProfile.setText("Menu Makanan Gizi Seimbang");
            }else if (statusGizi.equals("Gizi Buruk") || statusGizi.equals("Gizi Kurang")){
                childrenProfile.setText("Menu Makanan Tinggi Kalori dan Protein");
            }else {
                childrenProfile.setText("Kurangi Makanan dan Minuman Manis");
            }
        }
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
}