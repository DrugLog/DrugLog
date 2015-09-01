package lolbellum.druglog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;


public class SetTime extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        Bundle calendarData = getIntent().getExtras();
        if(calendarData == null){
            return;
        }
        timePicker.setCurrentHour(calendarData.getInt("hour"));
        timePicker.setCurrentMinute(calendarData.getInt("minute"));
    }

    public void clickSetTime(View view){
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        Intent i = getIntent();
        i.putExtra("hour", timePicker.getCurrentHour());
        i.putExtra("minute", timePicker.getCurrentMinute());
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_time, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
