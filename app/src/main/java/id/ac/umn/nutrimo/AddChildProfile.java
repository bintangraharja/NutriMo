package id.ac.umn.nutrimo;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddChildProfile extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText childName, childBirth, height, weight;
    AppCompatButton save;
    RadioButton gender;
    RadioGroup chooseGender;
    DatePickerDialog dpDialog;
    boolean isAllFieldsChecked = false;
    ImageButton back;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_profile);
        childName = findViewById(R.id.childName);
        height = findViewById(R.id.childHeight);
        weight = findViewById(R.id.childWeight);
        childBirth = findViewById(R.id.childBirth);
        chooseGender = findViewById(R.id.chooseGender);
        back = findViewById(R.id.backMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        childBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    dpDialog = new DatePickerDialog(AddChildProfile.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            childBirth.setText(i2 + "/" + (i1 + 1) + "/" + i);
                        }
                    }, mYear, mMonth, mDay);
                    dpDialog.show();
                }

            }
        });

        save = findViewById(R.id.saveChild);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = chooseGender.getCheckedRadioButtonId();

                gender = findViewById(radioId);
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){
                    Map<String, Object> child = new HashMap<String,Object>();
                    child.put("name", childName.getText().toString());
                    child.put("birthdate",childBirth.getText().toString());
                    child.put("gender",gender.getText().toString());
                    child.put("weight",weight.getText().toString());
                    child.put("height",height.getText().toString());

                    final String pushId = FirebaseDatabase.getInstance().getReference().push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("Childs").child(user.getUid()).child(pushId).setValue(child).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Profil anak berhasil ditambahkan",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }

    public void chooseGender(View view) {
        int radioId = chooseGender.getCheckedRadioButtonId();
        gender = findViewById(radioId);

    }
    private boolean CheckAllFields() {
        if (childName.length() == 0) {
            childName.setError("This field is required");
            return false;
        }
        if (childBirth.length() == 0) {
            childBirth.setError("This field is required");
            return false;
        }else{
            childBirth.setError(null);
        }
        int radioId = chooseGender.getCheckedRadioButtonId();
        if(radioId == -1){
            gender = findViewById(R.id.chooseGirl);
            gender.setError("This field is required");
            return false;
        }else{
            gender.setError(null);
        }
        if (weight.length() == 0) {
            weight.setError("This field is required");
            return false;
        }else{
            weight.setError(null);
        }
        if (height.length() == 0) {
            height.setError("This field is required");
            return false;
        }else{
            height.setError(null);
        }
        return true;
    }
}