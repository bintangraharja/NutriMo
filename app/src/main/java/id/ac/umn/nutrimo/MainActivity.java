package id.ac.umn.nutrimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.nutrimo.article.Article;
import id.ac.umn.nutrimo.menu.Menu;
import id.ac.umn.nutrimo.menu.MenuDetail;
import id.ac.umn.nutrimo.menu.MenuModel;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView setting,periksa,artikel,menu;
    ImageSlider artikelSlider;
    Button children;
    RecyclerView child_rv;
    String activeChildId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        children = findViewById(R.id.childrenProfile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if( user != null)
        {
            changeActiveChild();
        }
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
                Intent intent = new Intent(getApplicationContext(),Setting.class);
                startActivity(intent);
            }
        });
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
            }
        });
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                finish();
            }
        });
        artikelSlider = findViewById(R.id.artikelSlider);
        final List<SlideModel> sliderArticle = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Slider").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    sliderArticle.add(new SlideModel(data.child("url").getValue().toString(),ScaleTypes.FIT));
                }
                artikelSlider.setImageList(sliderArticle,ScaleTypes.FIT);
                artikelSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {

                        Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }
        private void showBottomDialog(){
            final Dialog dialog = new Dialog(this);
            LayoutInflater inflater = getLayoutInflater();
            View newView = (View) inflater.inflate(R.layout.child_profile, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(newView);
            child_rv = newView.findViewById(R.id.childProfile_rv);
            child_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            ImageView cancelButton = newView.findViewById(R.id.closeDialog);
            AppCompatButton addChild = newView.findViewById(R.id.addChild);
            if(user != null){
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
                    Intent intent = new Intent(getApplicationContext(),AddChildProfile.class);
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
    public static class childViewHolder extends  RecyclerView.ViewHolder{
        TextView childName;
        public childViewHolder(@NonNull View itemView)
        {
            super(itemView);
            childName= itemView.findViewById(R.id.list_child_name);
        }
    }
    public void changeActiveChild(){
        DatabaseReference mRef=  FirebaseDatabase.getInstance().getReference().child("Childs").child(user.getUid());
        if(activeChildId == null){
            mRef.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ChildModel chi = new ChildModel();
                    for(DataSnapshot childs: snapshot.getChildren()){
                        chi = childs.getValue(ChildModel.class);
                        activeChildId = childs.getKey();
                    }
                    children.setText(chi.getName());
                    Log.d("DEbug","Child ID:"+activeChildId);
                    if(chi.getGender().toString().equals("Perempuan")){
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.girl_32);
                        children.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                    }else{
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.boy_32);
                        children.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            mRef.orderByKey().equalTo(activeChildId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ChildModel chi = new ChildModel();
                    for(DataSnapshot childs: snapshot.getChildren()){
                        chi = childs.getValue(ChildModel.class);
                    }
                    children.setText(chi.getName());
                    Log.d("DEbug","Child ID:"+activeChildId);
                    if(chi.getGender().toString().equals("Perempuan")){
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.girl_32);
                        children.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                    }else{
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.boy_32);
                        children.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        //Update Grafik
    }
}

