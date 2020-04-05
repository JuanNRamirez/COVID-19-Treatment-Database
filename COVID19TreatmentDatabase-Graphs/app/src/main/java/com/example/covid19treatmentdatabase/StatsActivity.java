package com.example.covid19treatmentdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
    private int counter = 0;
    private Button btStats;
    private Spinner graphSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        barChart = findViewById(R.id.bChart1);
        yVals = new ArrayList<>();
        xLabel = new ArrayList<>();
        btStats = findViewById(R.id.btStat);
        graphSpinner = findViewById(R.id.graphSpinner);

        graphSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    casesDatabase.collection("casesCOVID").document("counts").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
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
                                    String[] medicines = new String[7];

                                    for (Map.Entry<String, Long> en : orderedMap.entrySet()) {
                                        yVals.add(new BarEntry(counter, en.getValue()));
                                        medicines[counter] = en.getKey();
                                        counter++;
                                    }

                                    XAxis xAxis = barChart.getXAxis();
                                    xAxis.setValueFormatter(new MyXAxisValueFormatter(medicines));
                                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                    xAxis.setDrawGridLines(false);
                                    BarDataSet set = new BarDataSet(yVals, "Treatments");
                                    BarData data = new BarData(set);
                                    barChart.setData(data);
                                    barChart.invalidate();
                                    barChart.getXAxis().setTextSize(7);
                                    barChart.setHorizontalScrollBarEnabled(true);

                                } else {
                                    Toast.makeText(StatsActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();
                                }
                            } else {

                                Toast.makeText(StatsActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(StatsActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();

                        }
                    });
                } else if (i == 1) {
                    casesDatabase.collection("casesCOVID").document("counts").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> map = document.getData();
                                    String[] lists = new String[map.size()];

                                    /*

                                    for (String key: map.keySet()) {
                                        casesDatabase.collection("casesCOVID").d
                                    }


                                     */

                                    /*

                                    XAxis xAxis = barChart.getXAxis();
                                    xAxis.setValueFormatter(new MyXAxisValueFormatter(medicines));
                                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                    xAxis.setDrawGridLines(false);
                                    BarDataSet set = new BarDataSet(yVals, "Treatments");
                                    BarData data = new BarData(set);
                                    barChart.setData(data);
                                    barChart.invalidate();
                                    barChart.getXAxis().setTextSize(7);
                                    barChart.setHorizontalScrollBarEnabled(true);

                                    */

                                } else {
                                    Toast.makeText(StatsActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();
                                }
                            } else {

                                Toast.makeText(StatsActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(StatsActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
