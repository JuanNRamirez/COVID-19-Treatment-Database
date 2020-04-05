package com.example.covid19treatmentdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatsActivity extends AppCompatActivity {

    private BarChart barChart;
    private FirebaseFirestore casesDatabase = FirebaseFirestore.getInstance();
    private ArrayList<BarEntry> yVals;
    private ArrayList<String> xLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        barChart = findViewById(R.id.bChart1);
        yVals = new ArrayList<>();
        xLabel = new ArrayList<>();

        casesDatabase.collection("casesCOVID").document("counts").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(StatsActivity.this, "We in", Toast.LENGTH_LONG).show();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        HashMap<String, Long> newMap = new HashMap<>();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if(entry.getValue() instanceof Object){
                                newMap.put(entry.getKey(), (Long) entry.getValue());
                            }
                        }
                        HashMap<String, Long> orderedMap = sortByValue(newMap);
                        int counter = 0;
                        for (Map.Entry<String, Long> en : orderedMap.entrySet()) {
                            yVals.add(new BarEntry(counter, en.getValue()));
                            xLabel.add(en.getKey());
                            counter++;
                        }
                        BarDataSet set = new BarDataSet(yVals, "BarDataSet");
                        BarData data = new BarData(set);
                        data.setBarWidth(0.9f); // set custom bar width
                        barChart.setData(data);
                        barChart.setFitBars(true); // make the x-axis fit exactly all bars
                        barChart.invalidate();

                    } else {
                        Toast.makeText(StatsActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();
                    }
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static HashMap<String, Long> sortByValue(HashMap<String, Long> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Long> > list =
                new LinkedList<Map.Entry<String, Long> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Long> >() {
            public int compare(Map.Entry<String, Long> o1,
                               Map.Entry<String, Long> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Long> temp = new LinkedHashMap<String, Long>();
        for (Map.Entry<String, Long> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
