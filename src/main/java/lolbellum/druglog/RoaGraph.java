package lolbellum.druglog;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class RoaGraph extends ActionBarActivity {

    private DrugManager drugManager = Calendar.drugManager;
    Drug drug;
    String timeFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roa_graph);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("RouteOfAdministration(ROA) graph");

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }

        drug = drugManager.getDrugFromName(bundle.getString("DrugName"));
        timeFrame = bundle.getString("TimeFrame");

        graph(timeFrame);
    }

    private void graph(String timeFrame){
        GraphView graphView = (GraphView) findViewById(R.id.roa_graph);
        graphView.setTitle(drug.getDrugName() + " ROAs used/" + timeFrame);
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("ROA");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("# of Times ROA used");
        //graphView.getGridLabelRenderer().setLabelFormatter(new BetterDateLabelFormatter(this));
        StaticLabelsFormatter roaLabelFormatter = new StaticLabelsFormatter(graphView);

        //set ROAs as horizontal labels
        String roaLabel[] = new String[drugManager.getAllRoas().size()];
        for(int x = 0;x < drugManager.getAllRoas().size();x++){
            roaLabel[x] = drugManager.getAllRoas().get(x).getRoaName();
        }
        roaLabelFormatter.setHorizontalLabels(roaLabel);
        graphView.getGridLabelRenderer().setLabelFormatter(roaLabelFormatter);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
       // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        long time = -1;
        if(timeFrame.equalsIgnoreCase("Today")){
            time = TimeUnit.DAYS.toMillis(1);
        } else if(timeFrame.equalsIgnoreCase("This Week")){
            time = TimeUnit.DAYS.toMillis(7);
        } else if(timeFrame.equalsIgnoreCase("This Month")){
            time = TimeUnit.DAYS.toMillis(30);
        } else if(timeFrame.equalsIgnoreCase("This Year")){
            time = TimeUnit.DAYS.toMillis(365);
        } else if(timeFrame.equalsIgnoreCase("All Time")){
            time = -1;
        }

        ArrayList<Dose> doses = new ArrayList<Dose>();
        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            if(tempDose.getDrug().getDrugId() == drug.getDrugId()){
                if(time == -1){
                    doses.add(tempDose);
                } else {
                    try {
                        long startTime = System.currentTimeMillis() - time;
                        Date doseDate = sdf.parse(tempDose.getDate());
                        long doseTime = doseDate.getTime();
                        if(doseTime <= System.currentTimeMillis() && doseTime >= startTime){
                            doses.add(tempDose);
                        }
                    } catch(Exception e){

                    }
                }
            }
        }
        Integer roaCount[] = new Integer[drugManager.getAllRoas().size()];

        for(int x = 0;x < roaCount.length;x++){
            roaCount[x] = 0;
        }

        for(int x = 0; x < doses.size();x++){
            Dose dose = doses.get(x);
            Long roaId = dose.getRoa().getId();
            for(int i = 0; i < drugManager.getAllRoas().size();i++){
                if(roaId == drugManager.getAllRoas().get(i).getId()){
                    roaCount[i] += 1;
                }
            }
        }
        DataPoint[] dataPoints = new DataPoint[roaCount.length];
        for(int x = 0;x < roaCount.length;x++){
           dataPoints[x] = new DataPoint(x,roaCount[x]);
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
        series.setSpacing(25);
        graphView.addSeries(series);

        graphView.getGridLabelRenderer().setNumHorizontalLabels(roaCount.length);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setPadding(75);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_roa_graph, menu);
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
