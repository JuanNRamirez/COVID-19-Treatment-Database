package com.example.covid19treatmentdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private FirebaseAuth mAuth;
    private Button signInBt;
    private Button directBt;
    private FirebaseFirestore generalDatabase = FirebaseFirestore.getInstance();
    private TextView tvNoAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbarF);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign in");

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        signInBt = findViewById(R.id.btSignIn);
        directBt = findViewById(R.id.btDirect);
        tvNoAccount = findViewById(R.id.tvNoAccount);

        tvNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        directBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, MedicNavDrawer.class);
                LoginActivity.this.startActivity(intent);

            }
        });

        signInBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {

                    if (email.isEmpty()) {
                        mEmailView.setError("Required field");
                    }
                    if (password.isEmpty()) {
                        mPasswordView.setError("Required field");
                    }
                } else {

                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = user.getUid();

                                DocumentReference priceRef = generalDatabase.collection("usersMedics").document(uid);
                                priceRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        String approvalStatus = documentSnapshot.getString("approvalStatus");
                                        checkApprovalStatus(approvalStatus);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();

                                    }
                                });

                            } else {

                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }

            }
        });


    }

    private void checkApprovalStatus(String approvalStatus) {

        if(approvalStatus.equals("A")) {

            Intent intent = new Intent(LoginActivity.this, MedicNavDrawer.class);
            startActivity(intent);

        } else {

            FirebaseAuth.getInstance().signOut();
            Intent intentReload = new Intent (LoginActivity.this, LoginActivity.class);
            startActivity(intentReload);
            Toast.makeText(LoginActivity.this, "Account not approved!", Toast.LENGTH_LONG).show();

        }

    }
}
