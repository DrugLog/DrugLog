package lolbellum.druglog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Eliot on 7/7/2015.
 */
public class NotificationService extends Service {
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);

        if(intent == null){
            return;
        }

        Bundle reminderData = intent.getExtras();
        if(reminderData == null){
            Log.i("testing", "Intent extras are null in ReminderService intent");
            return;
        }

        String reminderText = reminderData.getString("text");



        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        //Intent intent1 = new Intent(this.getApplicationContext(),DrugView.class);
        Intent intent1 = new Intent();

        Notification notification = new Notification(R.mipmap.ic_launcher,reminderText, System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(),"Drug Log Reminder", reminderText, pendingNotificationIntent);


        mManager.notify(0, notification);
        Log.i("testing", "Reminder notification notified");
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
