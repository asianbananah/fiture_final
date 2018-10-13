package com.lim.fiture.fiture.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.fragments.DatePickerFragment;
import com.lim.fiture.fiture.models.User;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private User mUser;
    private CircleImageView userPic;
    private TextView userName, userBirthDate;
    private Button cancelBtn, submitBtn;
    private EditText userfirstName, userlastName, userEmail, heightTxt, weightTxt;
    private Spinner userGender;

    DatabaseReference databaseUser;
    private String action = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //Your reference to the database ex. https://console.firebase.google.com/project/fiturefinal/database/fiturefinal/data/User
        databaseUser = FirebaseDatabase.getInstance().getReference("Users");

        mUser = (User) getIntent().getSerializableExtra("user");
        Log.d("testUser" ,mUser.toString());

        if(getIntent().getStringExtra("action") != null)
            action = getIntent().getStringExtra("action");

        findViews();
        initializeViews();

        if(action.equals("edit")){
            resumeValues();
        }
    }

    private void findViews(){
        userPic = findViewById(R.id.userPic);
        userName = findViewById(R.id.userName);
        userfirstName = findViewById(R.id.userFirstName);
        userlastName = findViewById(R.id.userLastName);
        userEmail = findViewById(R.id.userEmail);
        heightTxt = findViewById(R.id.heightTxt);
        weightTxt = findViewById(R.id.weightTxt);
        userGender = findViewById(R.id.userGender);

        userBirthDate = findViewById(R.id.userBirthDate);
        userBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "datePickerFragment");
            }
        });

        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:
            }
        });
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(view -> {
            mUser.setFirstName(userfirstName.getText().toString());
            mUser.setLastName(userlastName.getText().toString());
            mUser.setEmail(userEmail.getText().toString());
            mUser.setDateOfBirth(userBirthDate.getText().toString());
            mUser.setHeightInCm(Integer.parseInt(heightTxt.getText().toString()));
            mUser.setWeightInKg(Integer.parseInt(weightTxt.getText().toString()));
            mUser.setGender(userGender.getSelectedItem().toString());
            mUser.setBmi(calculateBMI((float)mUser.getWeightInKg(),(float)mUser.getHeightInCm()/100));
            if(!action.equals("edit"))
                mUser.setPoints(0); //this means that if this is a new user, set the default points to 0

            databaseUser.child(mUser.getiD()).setValue(mUser);
            startActivity(new Intent(EditProfileActivity.this,FitnessLevelActivity.class).putExtra("user",mUser));
        });
    }

    private void initializeViews(){
        Glide.with(this).load(mUser.getPhotoUrl()).into(userPic);
        userfirstName.setText(mUser.getFirstName());
        userlastName.setText(mUser.getLastName());
        userEmail.setText(mUser.getEmail());
        userName.setText(mUser.getFirstName() + " " + mUser.getLastName());
    }

    private void resumeValues(){
        userfirstName.setText(mUser.getFirstName());
        userlastName.setText(mUser.getLastName());
        userEmail.setText(mUser.getEmail());
        userBirthDate.setText(mUser.getDateOfBirth());
        setSpinnerValues(R.array.gender_choices, userGender, mUser.getGender());
        heightTxt.setText(String.valueOf(mUser.getHeightInCm()));
        weightTxt.setText(String.valueOf(mUser.getWeightInKg()));
    }

    private void setSpinnerValues(int resource, Spinner spinner, String compareValue) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, resource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = new GregorianCalendar(i, i1, i2);
        setDate(cal);
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        userBirthDate.setText(dateFormat.format(calendar.getTime()));
    }

    private float calculateBMI (float weight, float height) {
        return (float) (weight / (height * height));
    }
}
