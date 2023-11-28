package com.beyzanur.expiration_date_reminder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Classification {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Products> categoryArrayList;
    public void CatClass(RecyclerView recyclerView, Context context, String category){
        categoryArrayList=new ArrayList<>();
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("urunler").whereEqualTo("productCat",category).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String product_name = (String) document.getData().get("productName");
                                String product_cat = (String) document.getData().get("productCat");
                                String product_date = (String) document.getData().get("productDate");
                                String product_notification = (String) document.getData().get("productNotification");
                                Products products = new Products(product_name, product_cat, product_date, product_notification);
                                categoryArrayList.add(products);
                            }
                            if (categoryArrayList.isEmpty()){   Toast.makeText(context, "NO ITEM", Toast.LENGTH_SHORT).show();  }
                        }
                        else { Log.d(TAG, "Error getting documents: ", task.getException()); }
                        recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
                        recyclerView.setAdapter(new ProductsAdapter(context, categoryArrayList));
                    }
                });
    }
}
