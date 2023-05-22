package com.beyzanur.expiration_date_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity {
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("users"). document(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .collection("urunler");

    EditText product_name;
    String update_id;
    ImageView addBack;
    Button sumitButton,vegetablesCat,medicineCat,fruitCat,beverageCat,othersCat,dessertCat;
    TextView product_cat;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        product_name=findViewById(R.id.product_name);
        product_cat=findViewById(R.id.enter_category);
        sumitButton=findViewById(R.id.submit_button);
        vegetablesCat=findViewById(R.id.add_product_vegetables);
        medicineCat=findViewById(R.id.add_product_medicine);
        beverageCat=findViewById(R.id.add_product_beverage);
        othersCat=findViewById(R.id.add_product_others);
        dessertCat=findViewById(R.id.add_product_dessert);
        fruitCat=findViewById(R.id.add_product_fruit);
        addBack=findViewById(R.id.addback);

        addBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String receivedName = intent.getStringExtra("name");
        String receivedCat = intent.getStringExtra("cat");

        product_name.setText(receivedName);
        product_cat.setText(receivedCat);


        collectionReference.whereEqualTo("productName", product_name.getText().toString())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                update_id = document.getId();

                            }
                        }
                    }
                });

        vegetablesCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_cat.setText("Vegetables");
            }
        });

        fruitCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_cat.setText("Fruit");
            }
        });

        medicineCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_cat.setText("Medicine");
            }
        });

        beverageCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_cat.setText("Beverage");
            }
        });

        dessertCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_cat.setText("Dessert");
            }
        });

        othersCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_cat.setText("Others");
            }
        });


        sumitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("productCat", product_cat.getText().toString());
                updates.put("productName", product_name.getText().toString());

                collectionReference.document(update_id).update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateProductActivity.this, "Update Successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateProductActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateProductActivity.this, "Update Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }




}