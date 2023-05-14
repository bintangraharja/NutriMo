package id.ac.umn.nutrimo.periksa;

import id.ac.umn.nutrimo.ChildModel;
import id.ac.umn.nutrimo.R;
import id.ac.umn.nutrimo.RoomDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewHistory extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton back;
    EditText childName, childGender, dateToday, weightToday, heightToday;
    String activeChildId,dateBirth;
    DatePickerDialog dpDialog;
    LocalDate birth, now;
    AppCompatButton save;
    DateTimeFormatter formatter;
    RoomDB db;
    HAZDao hazDao;
    WHZDao whzDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_history);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getIntent();
        activeChildId = intent.getStringExtra("Child");
        formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        db = RoomDB.getInstance(getApplicationContext());
        hazDao = db.hazDao();
        whzDao = db.whzDao();
        DatabaseReference childRef=  FirebaseDatabase.getInstance().getReference().child("Childs").child(user.getUid());
        childRef.orderByKey().equalTo(activeChildId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChildModel chi = new ChildModel();
                for(DataSnapshot childs: snapshot.getChildren()){
                    chi = childs.getValue(ChildModel.class);
                }
                childName.setText(chi.getName());
                childGender.setText(chi.getGender());
                dateBirth = chi.getBirthdate();
                birth = LocalDate.parse(dateBirth, formatter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        back = findViewById(R.id.backPeriksa);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        childName = findViewById(R.id.childName);
        childGender = findViewById(R.id.childGender);
        dateToday = findViewById(R.id.dateToday);
        weightToday = findViewById(R.id.weightToday);
        heightToday = findViewById(R.id.heightToday);

        dateToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                dpDialog = new DatePickerDialog(NewHistory.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateToday.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                dpDialog.show();
            }
        });
        save = findViewById(R.id.saveHistory);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String heightStatus, nutrition, result;
               if(CheckAllFields()){
                   //Calculate Child's Age
                   now = LocalDate.parse(dateToday.getText(),formatter);
                   int ageYear = Period.between(birth.withDayOfMonth(birth.getDayOfMonth()),now.withDayOfMonth(now.getDayOfMonth())).getYears();
                   int ageMonth = Period.between(birth.withDayOfMonth(birth.getDayOfMonth()),now.withDayOfMonth(now.getDayOfMonth())).getMonths();
                   int age = (12 * ageYear) + ageMonth;
                   if(age > 24){
                       Toast.makeText(NewHistory.this, "Maaf, usia maksimal 24 bulan", Toast.LENGTH_SHORT).show();
                       finish();
                       return;
                   }
                   double medianHaz = hazDao.getMedian(age, String.valueOf(childGender.getText()));
                   double height = Double.parseDouble(String.valueOf(heightToday.getText()));
                   double haz;
                   if(height > medianHaz){
                       double psd1 = hazDao.getPsd1(age,String.valueOf(childGender.getText()));
                       haz = (height-medianHaz)/(psd1-medianHaz);
                   }else{
                       double nsd1 = hazDao.getNsdp1(age,String.valueOf(childGender.getText()));
                       haz = (height-medianHaz)/(medianHaz-nsd1);
                   }
                   if(haz < -3){
                       heightStatus = "Sangat Pendek";
                   }else if(haz < -2){
                       heightStatus = "Pendek";
                   } else if (haz <= 3) {
                       heightStatus = "Normal";
                   }else {
                       heightStatus = "Tinggi";
                   }
                   double heightRounded = Math.ceil(height *2)/2 ;
                   if(heightRounded < 45){
                       heightRounded = 45;
                   } else if (heightRounded > 110) {
                       heightRounded = 110;
                   }
                   double medianWhz = whzDao.getMedian(heightRounded,String.valueOf(childGender.getText()));
                   double weight = Double.parseDouble(String.valueOf(weightToday.getText()));
                   double whz;
                   if(weight > medianWhz){
                       double psd1Whz = whzDao.getPsd1(heightRounded, String.valueOf(childGender.getText()));
                       whz = (weight - medianWhz)/(psd1Whz-medianWhz);
                   }else{
                       double nsd1Whz = whzDao.getNsdp1(heightRounded, String.valueOf(childGender.getText()));
                       whz = (weight - medianWhz)/(medianWhz - nsd1Whz);
                   }
                   if(whz < -3){
                       nutrition = "Gizi Buruk";
                   }else if(whz < -2){
                       nutrition = "Gizi Kurang";
                   }else if(whz <= 1){
                       nutrition = "Gizi Baik";
                   }else if(whz <= 2){
                       nutrition = "Berisiko Gizi Lebih";
                   } else if (whz <= 3) {
                       nutrition = "Gizi Lebih";
                   }else{
                       nutrition = "Obesitas";
                   }
                   result = forwardChaining(age,heightStatus,nutrition);
                   HistoryModel history = new HistoryModel(
                           age,dateToday.getText().toString(),
                           haz,Double.parseDouble(heightToday.getText().toString()),
                           heightStatus,nutrition,result,
                           Double.parseDouble(weightToday.getText().toString()),whz);

                   final String pushID = FirebaseDatabase.getInstance().getReference().push().getKey();
                   FirebaseDatabase.getInstance().getReference().child("History").child(activeChildId).child(pushID).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           finish();
                       }
                   });
               }
            }
        });
    }
    public String forwardChaining(Integer usia, String statusTinggi, String statusGizi){
        String result = null;
        if(usia <= 6){
            result = "ASI Eksklusif";
        } else if (usia <= 9) {
            if(statusGizi.equals("Gizi Buruk") || statusGizi.equals("Gizi Kurang") || statusTinggi.equals("Sangat Pendek")){
                result = "MP-ASI Tinggi Kalori dan Protein";
            }else{
                result = "MP-ASI Gizi Seimbang";
            }
        } else if (usia <= 12) {
            if(statusGizi.equals("Gizi Buruk") || statusGizi.equals("Gizi Kurang")){
                result = "MP-ASI Tinggi Kalori dan Protein";
            }else{
                result = "MP-ASI Gizi Seimbang";
            }
        } else if (usia <=24) {
            if(statusGizi.equals("Gizi Baik")){
                result = "Menu Makanan Gizi Seimbang";
            }else if (statusGizi.equals("Gizi Buruk") || statusGizi.equals("Gizi Kurang")){
                result = "Menu Makanan Tinggi Kalori dan Protein";
            }else {
                result = "Kurangi Makanan dan Minuman Manis";
            }
        }
        return result;
    }
    private boolean CheckAllFields() {
        if (dateToday.length() == 0) {
            dateToday.setError("This field is required");
            return false;
        }else{
            dateToday.setError(null);
        }

        if (weightToday.length() == 0) {
            weightToday.setError("This field is required");
            return false;
        }else{
            weightToday.setError(null);
        }
        if (heightToday.length() == 0) {
            heightToday.setError("This field is required");
            return false;
        }else{
            heightToday.setError(null);
        }
        return true;
    }
}