package com.example.covid19treatmentdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

public class DatabaseActivity extends AppCompatActivity {

    TableLayout tl;
    TableRow tr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        tl = findViewById(R.id.CaseInformation);
        createHeader();
    }


    /**
     * Creates the header for the table
     */
    void createHeader() {
        /** Create a TableRow dynamically **/
        tr = new TableRow(this);

        /** Creating a TextView to add to the row **/
        TextView label = new TextView(this);
        label.setText("Case ID");
        label.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        label.setPadding(5, 5, 5, 5);

        LinearLayout Ll = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        Ll.addView(label,params);

        /** TODO: For some reason I can't get this textView to show up **/
        TextView label2 = new TextView(this);
        label2.setText("YEYEYYEYEYEY");
        label2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        label2.setPadding(5, 5, 5, 5);

        Ll.addView(label2);

        tr.addView(Ll); // Adding textView to tablerow.
        tl.addView(tr);

    }
    void createNewEntry() {

    }


}
