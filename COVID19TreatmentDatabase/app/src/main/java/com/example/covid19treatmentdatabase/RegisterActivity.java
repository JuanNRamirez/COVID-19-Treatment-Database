package com.example.covid19treatmentdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore medicsDatabase = FirebaseFirestore.getInstance();

    private EditText etName, etBirthday, etEducation, etCareer, etCareer2, etCellphone;
    private EditText etEmail, etPassword;
    private EditText etExperience, etCountry, etCity;
    private Button btSubmit;
    private FirebaseAuth mAuth;
    EditText etUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getSupportActionBar().setTitle("Register");

        etName = findViewById(R.id.etName);
        etBirthday = findViewById(R.id.etBirthday);
        etEducation = findViewById(R.id.etEducation);
        etCareer = findViewById(R.id.etCareer);
        etCareer2 = findViewById(R.id.etCareer2);
        etCellphone = findViewById(R.id.etCellphone);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.etPassword);
        etExperience = findViewById(R.id.etExperiences);
        etCountry = findViewById(R.id.etCountry);
        etCity = findViewById(R.id.etCity);
        btSubmit = findViewById(R.id.btSubmit);

        btClick();

    }

    private void btClick() {

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = etName.getText().toString();
                final String birthday = etBirthday.getText().toString();
                final String education = etEducation.getText().toString();
                final String career = etCareer.getText().toString();
                final String career2 = etCareer2.getText().toString();
                final String cellphone = etCellphone.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String experience = etExperience.getText().toString();
                final String country = etCountry.getText().toString();
                final String city = etCity.getText().toString();


                if (name.isEmpty() || education.isEmpty() || career.isEmpty() || cellphone.isEmpty() || password.isEmpty() || city.isEmpty() || birthday.isEmpty()
                        || email.isEmpty() || career2.isEmpty() || experience.isEmpty() || country.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Fill out the missing fields", Toast.LENGTH_LONG).show();

                    if (name.isEmpty()) {
                        etName.setError("Required");
                    }
                    if (birthday.isEmpty()) {
                        etBirthday.setError("Required");
                    }
                    if (education.isEmpty()) {
                        etEducation.setError("Required");
                    }
                    if (career.isEmpty()) {
                        etCareer.setError("Required");
                    }
                    if (career2.isEmpty()) {
                        etCareer2.setError("Required");
                    }
                    if (cellphone.isEmpty()) {
                        etCellphone.setError("Required");
                    }
                    if (password.isEmpty()) {
                        etPassword.setError("Required");
                    }
                    if (experience.isEmpty()) {
                        etExperience.setError("Required");
                    }
                    if (country.isEmpty()) {
                        etCountry.setError("Required");
                    }
                    if (email.isEmpty()) {
                        etEmail.setError("Required");
                    }
                    if (city.isEmpty()) {
                        etCity.setError("Required");
                    }

                } else {

                    //Authentication registration starts here...

                    //TESTING ONLY

                    etUserID = findViewById(R.id.etUserID);

                    //TEST END

                    mAuth = FirebaseAuth.getInstance();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String uid = task.getResult().getUser().getUid();
                                registerUser(uid, name, birthday, education, career, career2,
                                        cellphone, password, experience, email, country, city);


                            } else {

                                Toast.makeText(RegisterActivity.this, "Check that your password is longer than 6 characters", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }

        });

        //Se cierra el onClick

    }

    public void registerUser (final String uidComp, String name, String birthday, String education,
                              String career, String career2, String cellphone, String password,
                              String experience, String email, String country, String city){

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("birthday", birthday);
        data.put("education", education);
        data.put("career", career);
        data.put("career2", career2);
        data.put("cellphone", cellphone);
        data.put("password", password);
        data.put("experience", experience);
        data.put("email", email);
        data.put("country", country);
        data.put("city", city);
        data.put("approvalStatus", "D");

        medicsDatabase.collection("usersMedics").document(uidComp).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(RegisterActivity.this, "Application sent!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, ConfirmationActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(RegisterActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }

}
