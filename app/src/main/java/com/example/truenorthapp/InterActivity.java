package com.example.truenorthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.truenorthapp.R;

// the information display!
// fully dynamic -- date and time change accurately.
public class InterActivity extends AppCompatActivity {

    private TextView timeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter);
        getSupportActionBar().hide();

        // assigns the empty textview from the xml file to a new one.
        // opens and assigns a new simple date format, storing only the time and time zone.
        // then casts the time to a string and assigns it to the textview.
        timeView = findViewById(R.id.timeTextView);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss z");

        String timeSet = simpleDateFormat.format(new Date());
        timeView.setText(timeSet);


        // a simple button that goes from one activity to another, letting us into the main functionalities.
        Button startButton = (Button) findViewById(R.id.entryButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InterActivity.this, DailyTaskActivity.class));
            }
        });
    }
}