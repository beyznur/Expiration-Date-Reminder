package com.beyzanur.expiration_date_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    EditText addProductName,addNotificationSelect;
    Button sumitButton,vegetablesCat,medicineCat,fruitCat,beverageCat,othersCat,dessertCat;
    ImageView selectDateImage,addBack;
    DatePicker pickerDate;
    TextView enterCategory,selectDateTxt;
    Calendar cal;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference dbproducts = firebaseFirestore.collection("users").
            document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    String productName,productCat,productDate,productNotification;
    static int RQS_1 = 1;
    public static ArrayList<Products> productsArrayList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        createNotificationChannel();


        addProductName=findViewById(R.id.product_name);
        addNotificationSelect=findViewById(R.id.add_notification_select);
        sumitButton=findViewById(R.id.submit_button);
        vegetablesCat=findViewById(R.id.add_product_vegetables);
        medicineCat=findViewById(R.id.add_product_medicine);
        beverageCat=findViewById(R.id.add_product_beverage);
        othersCat=findViewById(R.id.add_product_others);
        dessertCat=findViewById(R.id.add_product_dessert);
        fruitCat=findViewById(R.id.add_product_fruit);
        pickerDate=findViewById(R.id.pickerdate);
        selectDateImage=findViewById(R.id.add_select_date_image);
        addBack=findViewById(R.id.addback);
        productsArrayList=new ArrayList<>();


        enterCategory=findViewById(R.id.enter_category);
        selectDateTxt=findViewById(R.id.add_selectdate_textview);

        addBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        vegetablesCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCategory.setText("Vegetables");
            }
        });

        fruitCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCategory.setText("Fruit");
            }
        });

        medicineCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCategory.setText("Medicine");
            }
        });

        beverageCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCategory.setText("Beverage");
            }
        });

        dessertCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCategory.setText("Dessert");
            }
        });

        othersCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCategory.setText("Others");
            }
        });



        selectDateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDate.setVisibility(View.VISIBLE);
            }
        });

        addNotificationSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDate.setVisibility(View.GONE);
            }
        });

        Calendar now = Calendar.getInstance();
        long t=now.getTimeInMillis();
        pickerDate.setMinDate(t);

        pickerDate.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);

        addNotificationSelect.setText("0");
        sumitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar current = Calendar.getInstance();

                cal = Calendar.getInstance();
                cal.set(pickerDate.getYear(),
                        pickerDate.getMonth(),
                        pickerDate.getDayOfMonth()-Integer.parseInt(addNotificationSelect.getText().toString()));



                int year=pickerDate.getYear();
                int month=pickerDate.getMonth()+1;
                int day=pickerDate.getDayOfMonth();
                if (month<10)
                {
                    if(day<10)
                    {
                        String month1="0"+month;
                        String day1="0"+day;
                        selectDateTxt.setText(day1 + "/" +(month1) + "/"+year);

                    }
                    else
                    {
                        String month1="0"+month;
                        selectDateTxt.setText(day + "/" +(month1) + "/"+year);
                    }

                }
                else
                {
                    if (day<10)
                    {
                        String day1="0"+day;
                        selectDateTxt.setText(day1 + "/" +(month) + "/"+year);
                    }
                    else
                    {
                        selectDateTxt.setText(day + "/" +(month) + "/"+year);
                    }
                }

                addProduct();


            }
        });
    }

    private void setAlarm(Calendar targetCal){

        Intent intent = new Intent(getBaseContext(), ReminderBroadcast1.class);
        intent.putExtra("category",productCat);
        intent.putExtra("name",productName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        RQS_1++;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Expiry Notifications";
            String description = "Dont Disable it otherwise Expiry Notification will not be delivered to you properly";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ExpiryNotification", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void addProduct(){

        productName=addProductName.getText().toString().trim();
        productCat=enterCategory.getText().toString().trim();
        productDate=selectDateTxt.getText().toString().trim();
        productNotification=addNotificationSelect.getText().toString().trim();


        Products product = new Products( productName, productCat, productDate, productNotification);

        Query query = dbproducts.collection("urunler")
                .whereEqualTo("productName", productName);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty())
                    {
                        Toast.makeText(AddProductActivity.this, "A product with the same name already exists.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else{
                        if(!TextUtils.isEmpty(productName) && !TextUtils.isEmpty(productCat)){
                            Map<String,Object> products =new HashMap<>();
                            products.put("ProductNotification",productNotification);
                            products.put("ProductDate",productDate);
                            products.put("Category",productCat);
                            products.put("ProductName",productName);

                            dbproducts.collection("urunler").document()
                                    .set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            setAlarm(cal);
                                            Toast.makeText(AddProductActivity.this,"Added",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddProductActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddProductActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });}
                        else{   Toast.makeText(AddProductActivity.this, "You should enter name and category", Toast.LENGTH_LONG).show();   }
                    }
                }

            }
        });




    }




}