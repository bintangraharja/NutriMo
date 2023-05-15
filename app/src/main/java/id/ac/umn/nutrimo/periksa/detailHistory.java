package id.ac.umn.nutrimo.periksa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import id.ac.umn.nutrimo.R;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detailHistory extends AppCompatActivity {
    ImageButton back;
    TextView dateCheck, heightCheck, weightCheck, ageCheck, hazCheck,whzCheck,growthStat,resultCheck;
    AppCompatButton delete;
    private String checkId, childId;
    private DatabaseReference checkRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        checkId = getIntent().getExtras().get("id_check").toString();
        childId = getIntent().getExtras().get("Child").toString();
        checkRef = FirebaseDatabase.getInstance().getReference().child("History").child(childId);

        back = findViewById(R.id.backHistory);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        delete = findViewById(R.id.deleteHistory);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation();
            }
        });
        dateCheck = findViewById(R.id.dateCheck);
        heightCheck = findViewById(R.id.heightCheck);
        weightCheck = findViewById(R.id.weightCheck);
        ageCheck = findViewById(R.id.ageCheck);
        hazCheck = findViewById(R.id.hazCheck);
        whzCheck = findViewById(R.id.whzCheck);
        growthStat = findViewById(R.id.growthStat);
        resultCheck = findViewById(R.id.resultCheck);

        RetrieveCheckDetail();
    }

    private void confirmation() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View newView = (View) inflater.inflate(R.layout.confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(newView);
        AppCompatButton noBtn = newView.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        AppCompatButton yesBtn = newView.findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRef.child(checkId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(detailHistory.this, "Riwayat Periksa berhasil dihapus", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(detailHistory.this, "Riwayat Periksa gagal dihapus", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    private void RetrieveCheckDetail(){
        checkRef.child(checkId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                int age =  snapshot.child("age").getValue(Integer.class);
                String date = snapshot.child("date").getValue().toString();
                double haz_temp = (double) snapshot.child("haz").getValue(Double.class);
                double height_temp = (double) snapshot.child("height").getValue(Double.class);
                String heightStat = snapshot.child("heightStatus").getValue().toString();
                String nutri = snapshot.child("nutrition").getValue().toString();
                String result = snapshot.child("result").getValue().toString();
                double weight_temp = (double) snapshot.child("weight").getValue(Double.class);
                double whz_temp = (double) snapshot.child("whz").getValue(Double.class);

                double haz = Math.ceil(haz_temp *100)/100 ;
                double height = Math.ceil(height_temp *100)/100 ;
                double weight = Math.ceil(weight_temp *100)/100 ;
                double whz = Math.ceil(whz_temp *100)/100 ;

                dateCheck.setText(date);
                heightCheck.setText(String.valueOf(height) + " Cm");
                weightCheck.setText(String.valueOf(weight) + " Kg");
                ageCheck.setText(String.valueOf(age) + " Bulan");
                hazCheck.setText(String.valueOf(haz));
                whzCheck.setText(String.valueOf(whz));
                growthStat.setText(heightStat +" \u0026 "+nutri);
                resultCheck.setText(result);
            }
        });
    }
}