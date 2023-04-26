package id.ac.umn.nutrimo.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import id.ac.umn.nutrimo.MainActivity;
import id.ac.umn.nutrimo.R;
import id.ac.umn.nutrimo.article.Article;

public class Menu extends AppCompatActivity {
    ImageView beranda,periksa,artikel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        periksa = findViewById(R.id.periksa);
        periksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        artikel = findViewById(R.id.artikel);
        artikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Article.class);
                startActivity(intent);
                finish();
            }
        });
        beranda = findViewById(R.id.beranda);
        beranda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}