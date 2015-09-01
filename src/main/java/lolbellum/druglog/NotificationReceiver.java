package lolbellum.druglog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("testing","Notification received in NotificationReceiver");
        Bundle reminderData = intent.getExtras();

        if(reminderData == null){
            Log.i("testing","The intent extras are null in the NotificationReceiver");
            return;
        }
        String reminderText = reminderData.getString("text");


        Intent i = new Intent(context,NotificationService.class);
        i.putExtra("text",reminderText);
        context.startService(i);
    }

    public void cancelAlarm(Context context, Intent intent){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
