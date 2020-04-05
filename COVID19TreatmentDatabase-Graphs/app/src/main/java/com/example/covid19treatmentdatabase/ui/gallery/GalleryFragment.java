package com.example.covid19treatmentdatabase.ui.gallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.covid19treatmentdatabase.ConfirmationActivity;
import com.example.covid19treatmentdatabase.MedicNavDrawer;
import com.example.covid19treatmentdatabase.MultiSpinner;
import com.example.covid19treatmentdatabase.R;
import com.example.covid19treatmentdatabase.RegisterActivity;
import com.example.covid19treatmentdatabase.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment implements MultiSpinner.MultiSpinnerListener {

    private FirebaseFirestore casesDatabase = FirebaseFirestore.getInstance();
    private GalleryViewModel galleryViewModel;
    private EditText age;
    private RadioGroup sex;
    private EditText hospital;
    private RadioGroup hospitalized;
    private RadioGroup status;
    private RadioButton radioButton;
    private Button btSubmit;
    private String selected1, selected2, selected3, selected4;
    private Spinner raceSpinner, countrySpinner;
    //private AlertDialog.Builder alert;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        age = root.findViewById(R.id.etAge);
        sex = root.findViewById(R.id.mF);
        hospital = root.findViewById(R.id.etHospital);
        hospitalized = root.findViewById(R.id.hospitalized);
        status = root.findViewById(R.id.status);
        btSubmit = root.findViewById(R.id.btSub);
        raceSpinner = root.findViewById(R.id.raceSpinner);
        countrySpinner = root.findViewById(R.id.countrySpinner);

        //alert = new AlertDialog.Builder(getActivity());
        //final EditText edittext = new EditText(getActivity());

        raceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected1 = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected4 = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final List<String> arraySpinner = new ArrayList<String>(Arrays.asList("Diabetes", "Hypertension",
                "Heart disease", "Atrial fibrillation", "Cancer", "Kidney diseases", "Bronchitis",
                "Chronic emphysema", "Stroke", "Dementia", "Liver disease", "Depression", "Anxiety"));

        MultiSpinner multiSpinner = (MultiSpinner) root.findViewById(R.id.multi);
        multiSpinner.setItems(arraySpinner, "Test", this);

        multiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected2 = (String) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final List<String> arraySpinner2 = new ArrayList<String>(Arrays.asList("Ibuprofen", "Chloroquine",
                "Hydroxychloroquine", "Remdesivir", "Favilavir", "Lopinavir", "Ritonavir",
                "Interferon Beta"));

        MultiSpinner multiMed = root.findViewById(R.id.multiMed);
        multiMed.setItems(arraySpinner2, "Test", this);

        multiMed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected3 = (String) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strAge = age.getText().toString();

                int radioID = sex.getCheckedRadioButtonId();
                radioButton = root.findViewById(radioID);
                String strSex = radioButton.getText().toString();

                String hospitalName = hospital.getText().toString();

                radioID = hospitalized.getCheckedRadioButtonId();
                radioButton = root.findViewById(radioID);
                String strHospit = radioButton.getText().toString();

                radioID = status.getCheckedRadioButtonId();
                radioButton = root.findViewById(radioID);
                String strStatus = radioButton.getText().toString();

                Map<String, Object> data = new HashMap<>();
                data.put("age", strAge);
                data.put("sex", strSex);
                data.put("race", selected1);
                data.put("country", selected4);
                data.put("hospital", hospitalName);
                data.put("preHealth", selected2);
                data.put("wasHospitalized", strHospit);
                data.put("medicine", selected3);
                data.put("status", strStatus);

                casesDatabase.collection("casesCOVID").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //Up counts for popular treatments

                        List<String> items = Arrays.asList(selected3.split("\\s*,\\s*"));
                        for (final String item: items) {
                            casesDatabase.collection("casesCOVID").document("counts").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Long treatmentCount = documentSnapshot.getLong(item);
                                    if (treatmentCount != null) {
                                        Map<String, Object> newDat = new HashMap<>();
                                        newDat.put(item, treatmentCount + 1);
                                        casesDatabase.collection("casesCOVID").document("counts").set(newDat, SetOptions.merge());
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_LONG).show();

                                }
                            });
                        }

                        //End, rest of normal method

                        Toast.makeText(getActivity(), "Case sent!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MedicNavDrawer.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onItemsSelected(boolean[] selected) {

    }
}