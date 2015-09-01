package lolbellum.druglog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle reminderData = intent.getExtras();

        if(reminderData == null){
            Log.i("testing","The intent extras are null in the ReminderReceiver");
            return;
        }
        String reminderText = reminderData.getString("ReminderText");
        long timeInterval = reminderData.getLong("TimeInterval");
        long timeFor = reminderData.getLong("TimeFor");
        long endTime = reminderData.getLong("EndTime");

        if(System.currentTimeMillis() > endTime){
            cancelAlarm(context, intent);
            Log.i("testing", "Reminder alarm canceled");
        }

        Intent i = new Intent(context,ReminderService.class);
        i.putExtra("ReminderText",reminderText);
        i.putExtra("TimeInterval", timeInterval);
        i.putExtra("TimeFor", timeFor);
        context.startService(i);
    }

    public void cancelAlarm(Context context, Intent intent){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
