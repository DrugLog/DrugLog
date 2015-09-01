package lolbellum.druglog;

import android.annotation.TargetApi;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.view.ViewGroup;
import android.content.Intent;


import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

  //  public static DrugManager drugManager = null;
    //MyDBHandler dbHandler;

    //stims
  /*  public final static Drug Methamphetamine = new Drug("Methamphetamine");
    public final static Drug Cocaine = new Drug("Cocaine");
    public final static Drug Amphetamine = new Drug("Amphetamine");
    public final static Drug Caffeine = new Drug("Caffeine");

    //Benzodiazepines
    public final static Drug Diazepam = new Drug("Diazepam");
    public final static Drug Clonazepam = new Drug("Clonazepam");

    public ArrayList<Drug> stimulantList = new ArrayList<Drug>(Arrays.asList(Methamphetamine, Cocaine, Amphetamine, Caffeine));
    public ArrayList<Drug> benzoList = new ArrayList<Drug>(Arrays.asList(Diazepam, Clonazepam));

    public   DrugClass Stimulants = new DrugClass("Stimulants",stimulantList);
    public  DrugClass Benzodiazepines = new DrugClass("Benzodiazepines", benzoList);*/

    public void findDrugManagerById(int id){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // dbHandler = new MyDBHandler(this, null, null, 1);
        //drugManager = new DrugManager(1,dbHandler);

        //Event Handling for button that will move the user to the drugs page
         final RelativeLayout myLayout = (RelativeLayout)findViewById(R.id.myLayout);
        Button drugsButton = (Button)findViewById(R.id.buttonDrugs);
        TextView myText = (TextView) findViewById(R.id.mainText);
       // myText.setText(Stimulants.getDrugClassName());

       // printDatabase();

        drugsButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        clickButton(myLayout);
                    }
                }
        );
    }

  /*  public void printDatabase(){
        String dbString = dbHandler.databaseToString();
    }*/

  //  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void clickButton(RelativeLayout layout){

        Intent i = new Intent(this, drugs.class);
        startActivity(i);

       /* for(int x = 0;x < Stimulants.getDrugs().size();x++){

            TextView myText = (TextView) findViewById(R.id.mainText);
            //myText.setId(100);

            Drug tempDrug = Stimulants.getDrugs().get(x);
            Button newButton = new Button(this);
            newButton.setId(x);
            newButton.setText(tempDrug.getDrugName());

            RelativeLayout.LayoutParams buttonDetails = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
           // buttonDetails.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

            buttonDetails.setMargins(0,0,0, 50);

            if(x > 0){
                Button test = (Button) findViewById(x-1);
                buttonDetails.addRule(RelativeLayout.BELOW, (x-1));
             //   buttonDetails.setMargins(0,0,50,0);
            } else if(x == 0) {
                buttonDetails.addRule(RelativeLayout.LEFT_OF, myText.getId());
                buttonDetails.addRule(RelativeLayout.BELOW, myText.getId());
            }

            layout.addView(newButton, buttonDetails);
            */



        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //MY EDITS
        if(item.getItemId() == R.id.action_home){
            setContentView(R.layout.activity_main);
            return true;
        }
        if(item.getItemId() == R.id.action_drugs){
            setContentView(R.layout.activity_drugs);
            return true;
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
