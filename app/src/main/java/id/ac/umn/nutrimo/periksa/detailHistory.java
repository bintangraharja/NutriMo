package id.ac.umn.nutrimo.periksa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import id.ac.umn.nutrimo.R;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detailHistory extends AppCompatActivity {
    ImageButton back;
    TextView dateCheck, heightCheck, weightCheck, ageCheck, hazCheck,whzCheck,growthStat,resultCheck;
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
    private void RetrieveCheckDetail(){
        checkRef.child(checkId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}