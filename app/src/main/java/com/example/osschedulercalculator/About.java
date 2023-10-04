package com.example.osschedulercalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class About extends AppCompatActivity {

    LinearLayout redirect;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        redirect = findViewById(R.id.mygitlink);
        redirect.setOnClickListener(view -> {
            String ur =  getResources().getString(R.string.githuburl);
            Uri url = Uri.parse(ur);
            Intent intent = new Intent(Intent.ACTION_VIEW, url);
            startActivity(intent);

        });
    }
}