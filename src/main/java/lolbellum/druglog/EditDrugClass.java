package lolbellum.druglog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EditDrugClass extends ActionBarActivity {

    private DrugManager drugManager = Calendar.drugManager;
    DrugClass drugClass;
    private ArrayList<DrugClass> checkedClasses = new ArrayList<DrugClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_drug_class);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            Log.i("testing","Bundle in EditDrugClass onCreate() is null. Returning");
            return;
        }
        drugClass = drugManager.getDrugClass(bundle.getLong("DrugClassId"));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Editing " + drugClass.getDrugClassName());

        EditText editTextDrugClassName = (EditText) findViewById(R.id.edit_text_drug_class_name);
        ListView listViewDrugsInClass = (ListView) findViewById(R.id.list_view_drugs_in_class);
        ListView listViewBadCombinations = (ListView) findViewById(R.id.list_view_bad_combinations);

        editTextDrugClassName.setText(drugClass.getDrugClassName());
        checkedClasses = drugClass.getBadCombinationList();
        ListAdapter adapterDrugsInClass = new DrugListAdapter(this,drugClass.getDrugs());
        ListAdapter adapterBadCombinations = new DrugClassWithCheckAdapter(this,drugManager.getDrugClasses(),drugClass.getBadCombinationList());


        listViewDrugsInClass.setAdapter(adapterDrugsInClass);
        listViewBadCombinations.setAdapter(adapterBadCombinations);

       // for(int x = 0;x < drugClass.getBadCombinationList().size();x++){
        //   DrugClass badCombinationClass = drugClass.getBadCombinationList().get(x);
        //}

        listViewBadCombinations.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DrugClass selectedClass = (DrugClass) parent.getSelectedItem();
                        CheckBox checkBoxDrugClassSelected = (CheckBox) view.findViewById(R.id.check_box_drug_class_checked);
                        checkBoxDrugClassSelected.performClick();
                        if(checkBoxDrugClassSelected.isChecked()){
                            checkedClasses.add(selectedClass);
                        } else {
                            checkedClasses.remove(selectedClass);
                        }
                    }
                }
        );
    }

    public void clickEditDrugClass(View view){
        EditText editTextDrugClassName = (EditText) findViewById(R.id.edit_text_drug_class_name);
        ListView listViewBadCombinations = (ListView) findViewById(R.id.list_view_bad_combinations);

        drugClass.setDrugClassName(editTextDrugClassName.getText().toString());
        drugClass.setBadCombinationList(checkedClasses);
        drugManager.updateDrugClass(drugClass);
        Log.i("testing", "DrugClass Updated");
        Intent i = new Intent();
        i.putExtra("DrugClassId",drugClass.getClassId());
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_drug_class, menu);
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
        if(id == R.id.delete_drug_class){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Confirm deletion");
            alertDialog.setMessage("Are you sure you want to delete " + drugClass.getDrugClassName() + "?");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("testing", "No");
                    alertDialog.dismiss();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("testing", "Yes");
                    drugManager.removeDrugClass(drugClass);
                    Toast.makeText(alertDialog.getContext(),"DrugClass " + drugClass.getDrugClassName() + " deleted",Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
