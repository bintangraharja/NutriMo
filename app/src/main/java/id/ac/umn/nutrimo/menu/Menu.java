package id.ac.umn.nutrimo.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import id.ac.umn.nutrimo.MainActivity;
import id.ac.umn.nutrimo.R;
import id.ac.umn.nutrimo.Setting;
import id.ac.umn.nutrimo.article.Article;

public class Menu extends AppCompatActivity {
    ImageView beranda,periksa,artikel;
    RecyclerView menu_rv;
    MenuAdapter menuAdapter;
    DatabaseReference menuRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuRef = FirebaseDatabase.getInstance().getReference().child("Menu");
        menu_rv = findViewById(R.id.menu_rv);
        menu_rv.setLayoutManager(new GridLayoutManager(this,2));
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
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerOptions<MenuModel> options =
                new FirebaseRecyclerOptions.Builder<MenuModel>()
                        .setQuery(menuRef, MenuModel.class)
                        .build();
        FirebaseRecyclerAdapter<MenuModel, MenuViewHolder > adapter = new FirebaseRecyclerAdapter<MenuModel, MenuViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull MenuModel model) {
                        holder.menuName.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.menuImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String id_menu = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                                Intent detail = new Intent(Menu.this, MenuDetail.class);
                                detail.putExtra("id_menu", id_menu);
                                startActivity(detail);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                        MenuViewHolder viewHolder = new MenuViewHolder(view);
                        return viewHolder;
                    }
                };

        menu_rv.setAdapter(adapter);
        adapter.startListening();
    }
    public static class MenuViewHolder extends  RecyclerView.ViewHolder{
        TextView menuName;
        ImageView menuImage;
        public MenuViewHolder(@NonNull View itemView)
        {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuName);
            menuImage = itemView.findViewById(R.id.menuImg);
        }
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}