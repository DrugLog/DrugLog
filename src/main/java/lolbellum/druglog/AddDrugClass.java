package lolbellum.druglog;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class AddDrugClass extends Activity {

    private DrugManager drugManager = Calendar.drugManager;

    private int defaultColor;
    ListView drugListView;
    ListAdapter drugsToAdd;
    ArrayList<Drug> checkedDrugs = new ArrayList<Drug>();

   // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug_class);

        String[] drugList = new String[drugManager.getDrugs().size()];
        for(int x = 0;x<drugManager.getDrugs().size();x++){
            drugList[x] = drugManager.getDrugs().get(x).getDrugName();
        }

        int[] drugImageIds = new int[drugManager.getDrugs().size()];
        for(int x = 0;x < drugManager.getDrugs().size();x++){
            drugImageIds[x] = drugManager.getDrugs().get(x).getImageId();
        }

       this.setTitle(R.string.title_add_drug_class);
        drugsToAdd = new DrugListAdapter(this, drugManager.getDrugs());
        drugListView = (ListView) findViewById(R.id.drugs_to_add);
        drugListView.setAdapter(drugsToAdd);
        defaultColor = drugListView.getDrawingCacheBackgroundColor();

        drugListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Drug clickedDrug = (Drug) drugListView.getItemAtPosition(position);
                        for(int x = 0;x < checkedDrugs.size();x++){
                            if(checkedDrugs.get(x).getDrugName().equalsIgnoreCase(clickedDrug.getDrugName())){
                                view.setBackgroundColor(defaultColor);
                                checkedDrugs.remove(clickedDrug);
                                return;
                            }
                        }
                        view.setBackgroundColor(Color.LTGRAY);
                        checkedDrugs.add(clickedDrug);
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_drug_class, menu);
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

    public void clickAddClass(View view){
        EditText classNameText = (EditText) findViewById(R.id.class_name_text);
        String className = classNameText.getText().toString();

        if(className.equalsIgnoreCase("")){
            Toast.makeText(this, "The Class name field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int x = 0;x < drugManager.getDrugClasses().size();x++){
            if(className.equalsIgnoreCase(drugManager.getDrugClasses().get(x).getDrugClassName())){
                Toast.makeText(this, "That class already exists",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //Intent i = new Intent(this, drugs.class);
        if(checkedDrugs.size() == 0){
            Toast.makeText(this, "You have to add at least one drug to make a class",Toast.LENGTH_LONG).show();
            return;
        }


        DrugClass newClass = new DrugClass(className, checkedDrugs);
        Toast.makeText(this, "Class " + className + " added!",Toast.LENGTH_LONG).show();
        finish();


    }
  /*  public void clickLayout(View view){
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox_add_drug);
        TextView viewText = (TextView) view.findViewById(R.id.drug_name_text);
        String drugName = viewText.toString();
        checkedDrugs.remove(drugManager.getDrugFromName(drugName));
        if(checkBox.isChecked()){
            checkBox.setChecked(false);
            view.setBackgroundColor(Color.WHITE);

        } else {
            checkBox.setChecked(true);
            view.setBackgroundColor(Color.LTGRAY);
            checkedDrugs.add(drugManager.getDrugFromName(drugName));
        }
    }*/
}
