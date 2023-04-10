package com.beyzanur.expiration_date_reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReminderBroadcast1 extends BroadcastReceiver {
    String getCategory;
    String getName;
    String NotificationText;


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent=new Intent(context, MainActivity.class);

         getCategory=intent.getStringExtra("category");
         getName= intent.getStringExtra("name");
        NotificationText = "The product in your " + getCategory + " category named " + getName +" is approaching its expiration date.";

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ExpiryNotification")
                .setSmallIcon(R.drawable.calendar_icon)
                .setContentTitle("Expiry Approaching")
                .setLargeIcon(BitmapFactory. decodeResource (context.getResources(), R.drawable.ic_blue ))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(NotificationText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        builder.setOngoing(true);
        builder.setColor(ContextCompat.getColor(context, R.color.purple_200));
        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmsound);



        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        builder.setContentIntent(PendingIntent.getActivity(context,0,myIntent,0));
        final NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(240,builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 1000000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(240);
            }

        }, delayInMilliseconds);

        addBildirim();


    }

    public void addBildirim(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference dbproducts = firebaseFirestore.collection("users").
                document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("notification");

        Map<String,Object> notification =new HashMap<>();
        notification.put("notificationCategory",getCategory);
        notification.put("notificationProductName",getName);

        dbproducts.document().set(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("bildirim eklendi");
            }
        });


    }


}

