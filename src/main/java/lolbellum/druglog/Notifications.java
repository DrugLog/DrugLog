package lolbellum.druglog;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Notifications extends ActionBarActivity {

    private final static int ADD_NOTIFICATION = 0;

    private DrugManager drugManager = Calendar.drugManager;
    ListView listViewNotifications;
    ArrayAdapter adapterNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("All Custom Notifications");

        listViewNotifications = (ListView) findViewById(R.id.notification_list);

        updateNotifications();
    }

    public void updateNotifications(){
        TextView emptyList = (TextView) findViewById(R.id.emptyList);
        ArrayList<Notification> notifications = new ArrayList<Notification>();
        for(int x = 0;x < drugManager.getNotifications().size();x++){
            notifications.add(drugManager.getNotifications().get(x));
        }

        if(notifications.size() > 0){

            if(emptyList.getVisibility() == View.VISIBLE){
                emptyList.setVisibility(View.INVISIBLE);
            }

            if(listViewNotifications.getVisibility() == View.INVISIBLE){
                listViewNotifications.setVisibility(View.VISIBLE);
            }
        } else {
            if(listViewNotifications.getVisibility() == View.VISIBLE) {
                listViewNotifications.setVisibility(View.INVISIBLE);
            }

            if(emptyList.getVisibility() == View.INVISIBLE) {
                emptyList.setVisibility(View.VISIBLE);
            }
        }

        adapterNotifications = new NotificationListAdapter(this,notifications);
        listViewNotifications.setAdapter(adapterNotifications);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_add_notification){
            Intent i = new Intent(this,AddNotification.class);
            startActivityForResult(i,ADD_NOTIFICATION);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == ADD_NOTIFICATION && resultCode == RESULT_OK){
            updateNotifications();
        }

    }
}
