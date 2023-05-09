package id.ac.umn.nutrimo.article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.ac.umn.nutrimo.MainActivity;
import id.ac.umn.nutrimo.Setting;
import id.ac.umn.nutrimo.menu.Menu;
import id.ac.umn.nutrimo.R;
import id.ac.umn.nutrimo.periksa.Periksa;

public class Article extends AppCompatActivity {
    ImageView beranda,periksa,menu,artikel;
    String activeChildId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        getSupportFragmentManager().beginTransaction().replace(R.id.articleWrapper,new recfragment()).commit();
        Intent intent = getIntent();
        activeChildId = intent.getStringExtra("Child");

        artikel = findViewById(R.id.artikel);
        artikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Article.class);
                intent.putExtra("Child",activeChildId);
                startActivity(intent);
                finish();
            }
        });
        periksa = findViewById(R.id.periksa);
        periksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Periksa.class);
                intent.putExtra("Child",activeChildId);
                startActivity(intent);
                finish();
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


    }
    @Override
    public void onBackPressed(){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


}