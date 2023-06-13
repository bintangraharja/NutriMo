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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class edit_child_profile extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText childName, childBirth, height, weight;
    ImageButton back;
    AppCompatButton save,edit;
    RadioButton gender;
    RadioGroup chooseGender;
    RadioButton boy, girl;
    DatePickerDialog dpDialog;
    boolean isAllFieldsChecked = false;
    String activeChildId;
    DatabaseReference childRef;
    ChildModel activeChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child_profile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        activeChildId = getIntent().getStringExtra("Child");

        childName = findViewById(R.id.childName);
        height = findViewById(R.id.childHeight);
        weight = findViewById(R.id.childWeight);
        childBirth = findViewById(R.id.childBirth);
        chooseGender = findViewById(R.id.chooseGender);
        boy = findViewById(R.id.chooseBoy);
        girl = findViewById(R.id.chooseGirl);

        back = findViewById(R.id.backSetting);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if(activeChildId == null){
            Intent intent = new Intent(getApplicationContext(),AddChildProfile.class);
            startActivity(intent);
            finish();
        }else{
            childRef = FirebaseDatabase.getInstance().getReference().child("Childs").child(user.getUid()).child(activeChildId);
            RetrieveChildData();
        }
        edit = findViewById(R.id.editChild);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childName.setEnabled(true);
                height.setEnabled(true);
                weight.setEnabled(true);
                childBirth.setEnabled(true);
                boy.setEnabled(true);
                girl.setEnabled(true);
                save.setVisibility(View.VISIBLE);
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
                    dpDialog = new DatePickerDialog(edit_child_profile.this, new DatePickerDialog.OnDateSetListener() {
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
                    activeChild.setName(childName.getText().toString());
                    activeChild.setBirthdate(childBirth.getText().toString());
                    activeChild.setGender(gender.getText().toString());
                    activeChild.setHeight(height.getText().toString());
                    activeChild.setWeight(weight.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("Childs").child(user.getUid()).child(activeChildId).setValue(activeChild).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Profil anak berhasil disimpan",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("Child",activeChildId);
                            intent.putExtra("Refresh",true);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
    private void RetrieveChildData(){
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activeChild = snapshot.getValue(ChildModel.class);
                if(activeChild != null){
                    childName.setText(activeChild.getName());
                    height.setText(activeChild.getHeight());
                    weight.setText(activeChild.getWeight());
                    childBirth.setText(activeChild.getBirthdate());
                    if(activeChild.getGender().equals("Perempuan")){
                        girl.setChecked(true);
                        boy.setChecked(false);
                    }else{
                        boy.setChecked(true);
                        girl.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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