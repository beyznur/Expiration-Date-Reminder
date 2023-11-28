package com.beyzanur.expiration_date_reminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>{

    Context context;
    ArrayList<Products> products;
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("urunler");
    public ProductsAdapter(){}

    public ProductsAdapter(Context context, ArrayList<Products> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.product_recycler,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.productName.setText(products.get(position).getProductName());
        holder.productDate.setText(products.get(position).getProductDate());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_product= products.get(position).getProductName();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete "+name_product+"?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        collectionReference.whereEqualTo("productName",products.get(position).getProductName())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            String deleteId;
                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                deleteId = document.getId();
                                                collectionReference.document(deleteId).delete();

                                            }
                                            Toast.makeText(context, "DELETED", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(v.getContext(),MainActivity.class);
                                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "WRONG", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }


        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(),UpdateProductActivity.class);

                intent.putExtra("name", products.get(position).getProductName());
                intent.putExtra("cat", products.get(position).getProductCat());
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView productName, productDate;
        ImageButton deleteButton,editButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            productDate=itemView.findViewById(R.id.product_date_recyler);
            productName=itemView.findViewById(R.id.product_name_recyler);
            deleteButton=itemView.findViewById(R.id.deletebtn_recycler);
            editButton=itemView.findViewById(R.id.editbtn_recycler);


        }
    }
}

