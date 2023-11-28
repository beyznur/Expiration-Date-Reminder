package com.beyzanur.expiration_date_reminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFragAdapter extends RecyclerView.Adapter<HomeFragAdapter.MyViewHolder> {

    Context mContext;
    List<Categories> mData;

    public HomeFragAdapter(Context mContext, List<Categories> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_home_frag,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragAdapter.MyViewHolder holder, int position) {

        holder.categoryName.setText(mData.get(position).getCategories());
        holder.categoryImage.setImageResource(mData.get(position).getIcon());


        holder.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.categoryName.getText().toString()){

                    case "FRUIT":
                        Intent intent1 = new Intent(mContext, FruitActivity.class);
                        mContext.startActivity(intent1);
                        break;

                    case "VEGETABLE":
                        Intent intent2 = new Intent(mContext, VegetablesActivity.class);
                        mContext.startActivity(intent2);
                        break;

                    case "BEVERAGE":
                        Intent intent3 = new Intent(mContext, BeverageActivity.class);
                        mContext.startActivity(intent3);
                        break;

                    case "DESSERT":
                        Intent intent4 = new Intent(mContext, DessertActivity.class);
                        mContext.startActivity(intent4);
                        break;

                    case "MEDICINE":
                        Intent intent5 = new Intent(mContext, MedicineActivity.class);
                        mContext.startActivity(intent5);
                        break;

                    case "OTHER":
                        Intent intent6 = new Intent(mContext, OthersActivity.class);
                        mContext.startActivity(intent6);
                        break;
                }}
        });


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


public static class MyViewHolder extends RecyclerView.ViewHolder{

    private TextView categoryName;
    private ImageView categoryImage;

    private LinearLayout recyclerView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryName = (TextView) itemView.findViewById(R.id.categoryName);
        categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
        recyclerView=(LinearLayout) itemView.findViewById(R.id.linear_recycler);
    }
}
}