package com.beyzanur.expiration_date_reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class MedicineActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        recyclerView = findViewById(R.id.medicine_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Classification classification = new Classification();
        classification.CatClass(recyclerView,getApplicationContext(),"Medicine");



    }
}