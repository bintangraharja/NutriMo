package id.ac.umn.nutrimo.menu;
import id.ac.umn.nutrimo.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MenuDetail extends AppCompatActivity {

    private String menuId;
    private ImageButton back;
    private ImageView menuImg;
    private TextView title, cook, item;

    private DatabaseReference menuRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        menuRef = FirebaseDatabase.getInstance().getReference().child("Menu");
        menuId = getIntent().getExtras().get("id_menu").toString();

        menuImg = findViewById(R.id.imageMenu);
        title = findViewById(R.id.nameMenu);
        cook = findViewById(R.id.cookMenu);
        item = findViewById(R.id.itemMenu);
        back = findViewById(R.id.backMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RetrieveMenuDetail();

    }

    private void RetrieveMenuDetail() {
        menuRef.child(menuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String image = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String cooks = snapshot.child("cook").getValue().toString().replace("/n", System.getProperty("line.separator"));
                    String items = snapshot.child("item").getValue().toString().replace("/n", System.getProperty("line.separator"));

                    Picasso.get().load(image).into(menuImg);
                    title.setText(name);
                    cook.setText(cooks);
                    item.setText(items);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}