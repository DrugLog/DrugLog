package lolbellum.druglog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;


public class SetDate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);

        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        Bundle calendarData = getIntent().getExtras();
        if(calendarData == null){
            return;
        }

        datePicker.updateDate(calendarData.getInt("year"), calendarData.getInt("month"), calendarData.getInt("day"));
    }

    public void clickSetDate(View view){
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Intent i = getIntent();
        i.putExtra("day", datePicker.getDayOfMonth());
        i.putExtra("month", datePicker.getMonth());
        i.putExtra("year", datePicker.getYear());
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_date, menu);
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
