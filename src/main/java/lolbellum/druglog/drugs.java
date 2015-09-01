package lolbellum.druglog;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class drugs extends ActionBarActivity implements DrugsNavigationBar.NavigationDrawerCallbacks {

    private final static int ADD_DRUG = 0;
    private final static int VIEW_DRUG = 1;
    private final static int EDIT_DRUG_CLASS = 2;

    private DrugManager drugManager = Calendar.drugManager;

    DrugClass selectedClass;
    ListAdapter drugAdapter;
    ListView drugListView;

   // public static DrugManager drugManager;
    // MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
   //  public  DrugManager drugManager = new DrugManager(1, dbHandler);

   // DrugManager drugManager;


    private DrugsNavigationBar mNavigationDrawerFragment;
    private CharSequence m_title;
    private int m_position = 0;
    private String selectedClassName = "";



   // public final static DrugManager drugManager = MainActivity.drugManager;
    // public MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //drugManager.readFromDatabase(dbHandler);



        setContentView(R.layout.activity_drugs);
        //final RelativeLayout drugsLayout = (RelativeLayout) findViewById(R.id.drugsLayout);

        m_title = "All Drugs";

       /* String[] firstClass = new String[drugManager.getDrugs().size()];
        for(int x = 0;x<drugManager.getDrugs().size();x++){
            firstClass[x] = drugManager.getDrugs().get(x).getDrugName();
        }

        int[] firstImages = new int[drugManager.getDrugs().size()];
                for(int x = 0;x < drugManager.getDrugs().size();x++){
                    firstImages[x] = drugManager.getDrugs().get(x).getImageId();
                };*/

        ListAdapter initialAdapter = new DrugListEditDeleteAdapter(this,drugManager.getDrugs());
       drugListView = (ListView)findViewById(R.id.drugsList);
        drugListView.setAdapter(initialAdapter);

        drugListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Drug drug = (Drug) drugListView.getItemAtPosition(position);
                            Intent i = new Intent(parent.getContext(), DrugView.class);
                            i.putExtra("DrugName", drug.getDrugName());
                            i.putExtra("DrugId",drug.getDrugId());
                            Log.i("testing", "putting " + drug.getDrugName() + " as extra");
                            startActivity(i);
                    }
                }
        );

        mNavigationDrawerFragment = (DrugsNavigationBar) getSupportFragmentManager().findFragmentById(R.id.navigation_drawers);
        //m_title = getTitle();


        //Sets up Navigation Drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawers, (DrawerLayout) findViewById(R.id.drugs_drawer));
    }

    public void updateDrugList(int position){
        DrugClass selectedClass;
        ArrayList<Drug> drugList = new ArrayList<Drug>();
        //String[] drugList = null;
        int[] imageIds = null;

        if(position == 0){
            m_title = "All Drugs";
            drugList = drugManager.getDrugs();

        } else {

            selectedClass = drugManager.getDrugClasses().get(position - 1);
            m_title = selectedClass.getDrugClassName();
            drugList = selectedClass.getDrugs();


        }

        drugAdapter = new DrugListEditDeleteAdapter(this, drugList);
        // ListView drugListView = (ListView)findViewById(R.id.drugsList);
        drugListView.setAdapter(drugAdapter);
    }

   @Override
    public void onNavigationDrawerItemSelected(int position) {

       m_position = position;
       ArrayList<Drug> drugList = new ArrayList<Drug>();
       //String[] drugList = null;
       int[] imageIds = null;

       if(position == 0){
           m_title = "All Drugs";
          drugList = drugManager.getDrugs();

       } else {

            selectedClass = drugManager.getDrugClasses().get(position - 1);
           m_title = selectedClass.getDrugClassName();
           drugList = selectedClass.getDrugs();


       }

        drugAdapter = new DrugListEditDeleteAdapter(this, drugList);
      // ListView drugListView = (ListView)findViewById(R.id.drugsList);
       drugListView.setAdapter(drugAdapter);

       // FragmentManager fragmentManager = getSupportFragmentManager();
       // fragmentManager.beginTransaction().replace(R.id.drugs_container, PlaceholderFragment.newInstance(position + 1)).commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */


   public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(m_title);
    }

    public void onSectionAttached(int number) {
       /* switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }*/
       /* if (number == 1) {
            m_title = getString(R.string.title_section1);
        } else if (number == 2) {
            m_title = getString(R.string.title_section2);
        } else if (number == 3) {
            m_title = getString(R.string.title_section3);
        } else if (number == 4) {
            m_title = getString(R.string.title_section4);
        }*/

       /* for(int x = 0; x < drugManager.getDrugClasses().size();x++){
            if(number == x){
                m_title = drugManager.getDrugClasses().get(x).getDrugClassName();
            }
        }*/

        m_title = drugManager.getDrugClasses().get(number - 1).getDrugClassName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_drugs, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionBar actionBar = getSupportActionBar();
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_home) {
            Intent i = new Intent(this, Calendar.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.add_drug){
            Intent i = new Intent(this, AddDrug.class);
           // i.putExtra("Position", m_position);
            i.putExtra("SelectedClassName", actionBar.getTitle().toString());
            startActivityForResult(i, ADD_DRUG);
            return true;
        }
        if(id == R.id.add_class){
            Intent i = new Intent(this, AddDrugClass.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.edit_drug_class){
            Intent i = new Intent(this,EditDrugClass.class);
            i.putExtra("DrugClassId",selectedClass.getClassId());
            startActivityForResult(i, EDIT_DRUG_CLASS);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_DRUG && resultCode == RESULT_OK) {
            Bundle classData = data.getExtras();
            if(classData.getString("ClassName").equalsIgnoreCase("All Drugs")){
                drugAdapter = new DrugListEditDeleteAdapter(this, drugManager.getDrugs());
            } else {
                DrugClass drugClass = drugManager.getDrugClass(classData.getString("ClassName"));
            drugAdapter = new DrugListEditDeleteAdapter(this, drugClass.getDrugs());
        }
            drugListView.setAdapter(drugAdapter);
        }
        if(requestCode == EDIT_DRUG_CLASS && resultCode == RESULT_OK){
            Bundle classData = data.getExtras();
            if(classData == null){
                Log.i("testing","extras bundle passed from EditDrugClass is null. Returning");
                return;
            }
            DrugClass drugClass = drugManager.getDrugClass(classData.getLong("DrugClassId"));
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(drugClass.getDrugClassName());
            drugAdapter = new DrugListEditDeleteAdapter(this, drugClass.getDrugs());
            drugListView.setAdapter(drugAdapter);
        }
    }

    public void clickEditDrug(View view){
        Log.i("testing","Clicked edit drug in drug list view");
    }

    public void clickDeleteDrug(View view){
        Log.i("testing","Clicked delete drug in drug list view");
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

      /*  @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_drugs, container, false);
            return rootView;
        }*/

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((drugs) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}

