package lolbellum.druglog;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class DateView extends ActionBarActivity {

    DrugManager drugManager = Calendar.drugManager;

    ListView doseListView;
    ListAdapter doseList;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_view);

        Bundle dateData = getIntent().getExtras();

        if(dateData == null){
            return;
        }

        date = dateData.getString("date");

        ActionBar actionBar = getSupportActionBar();

        //get date from datepicker activity

        //set the title to the date
        actionBar.setTitle(date);

        //get all the doses on this date
        ArrayList<Dose> doses = new ArrayList<Dose>();

        for(int x = 0; x < drugManager.getAllDoses().size();x++){
            String doseDate = drugManager.getAllDoses().get(x).getDate().substring(0,10);
            if(doseDate.equalsIgnoreCase(date)){
                doses.add(drugManager.getAllDoses().get(x));
            }
        }

        String[] drugNames = new String[doses.size()];
        String[] roas = new String[doses.size()];
        String[] doseTimes = new String[doses.size()];

        //get info for each dose
        for(int x = 0; x < doses.size();x++){
            drugNames[x] = doses.get(x).getDrug().getDrugName();
            roas[x] = doses.get(x).getRoa().getRoaName();
            doseTimes[x] = doses.get(x).getDate().substring(11,15);
          //  doseTimes[x] = doses.get(x).getDate();
        }

        doseList = new DoseListAdapter(this, doses);
        doseListView = (ListView) findViewById(R.id.dose_list_view);
        doseListView.setAdapter(doseList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_view, menu);
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
