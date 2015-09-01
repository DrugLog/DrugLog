package lolbellum.druglog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DrugView extends ActionBarActivity implements DrugViewNavigationBar.NavigationDrawerCallbacks {

    private static final int ADD_REMINDER = 1;
    private static final int EDIT_DRUG = 2;

    private DrugManager drugManager = Calendar.drugManager;

    private Drug m_drug;
    private String m_drugName;
    private long m_drugId = -1;
    private PendingIntent m_pendingIntent;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrugViewNavigationBar mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //super.onCreate(savedInstanceState);

        Bundle drugData = getIntent().getExtras();
        if(drugData == null){
            return;
        }

        Log.i("testing","Setting m_drugName as " + drugData.getString("DrugName"));
        m_drugName = drugData.getString("DrugName");
        m_drugId = drugData.getLong("DrugId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_view);

        ActionBar actionBar = getSupportActionBar();


       // m_drug = drugManager.getDrugFromName(m_drugName);
        m_drug = drugManager.getDrug(m_drugId);
        mNavigationDrawerFragment = (DrugViewNavigationBar) getSupportFragmentManager().findFragmentById(R.id.drug_view_fragment);
        mTitle = m_drug.getDrugName();

       // ImageView drugImageView = (ImageView) findViewById(R.id.drug_view_image);
        //TextView textDrugName = (TextView) findViewById(R.id.drug_view_drug_name);
       // TextView textDrugNicknames = (TextView) findViewById(R.id.drug_view_nick_names);

        //drugImageView.setImageResource(drug.getImageId());
        //textDrugName.setText(drug.getDrugName());

        //setting up nicknames
        /*if(drug.getDrugNickNames().size() == 0){
            textDrugNicknames.setText("Nicknames: None");
        } else {
            String nickNames = "";
            for(int x = 0;x < drug.getDrugNickNames().size();x++){
                if(x == drug.getDrugNickNames().size() - 1){
                    nickNames += drug.getDrugNickNames().get(x);
                } else {
                    nickNames += drug.getDrugNickNames().get(x) + ", ";
                }
            }
            textDrugNicknames.setText("Nicknames: " + nickNames);
        }*/

        //actionBar.setTitle(drug.getDrugName());

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.drug_view_fragment, (DrawerLayout) findViewById(R.id.drug_view_drawyer));
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.i("testing","The item's selected position is " + position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        Log.i("testing", "Putting the string " + m_drugName + " into the bundle");
        bundle.putString("drugname", m_drugName);
        bundle.putLong("drugid",m_drugId);

        if(position == 0){
            mTitle = "Drug Information";
            DrugInfoFragment drugInfoFragment = new DrugInfoFragment();
            //Drug Information
            Log.i("testing", "Setting drugInfoFragment's arguments");
            drugInfoFragment.setArguments(bundle);
           ft.replace(R.id.drug_view_container, drugInfoFragment).commit();
        } else if(position == 1){
            mTitle = "Doses";
            DoseFragment doseFragment = new DoseFragment();
            doseFragment.setArguments(bundle);
            ft.replace(R.id.drug_view_container,doseFragment).commit();
        } else if(position == 2){
            mTitle = "Notes";
            //Notes
            DrugNotesFragment drugNotesFragment = new DrugNotesFragment();
            drugNotesFragment.setArguments(bundle);
            ft.replace(R.id.drug_view_container, drugNotesFragment).commit();
        } else if(position == 3){
            mTitle = "Statistics";
            //Statistics
            StatisticsFragment statisticsFragment = new StatisticsFragment();
            statisticsFragment.setArguments(bundle);
            ft.replace(R.id.drug_view_container, statisticsFragment).commit();
        } else if(position == 4){
            mTitle = "Goals";
            //Goals
        } else if(position == 5){
            mTitle = "Reminders";
            //Reminders
            ReminderFragment reminderFragment = new ReminderFragment();
            reminderFragment.setArguments(bundle);
            ft.replace(R.id.drug_view_container, reminderFragment).commit();
           // changeToReminderMenu(this.get);
        } else  {
            return;
        }
    }

    public void changeToReminderMenu(Menu menu){
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
    }

    public void onSectionAttached(int number) {
        if(number == 0){
            //Drug Information

        } else if(number == 1){
            //Notes
        } else if(number == 2){
            //Statistics
        } else if(number == 3){
            //Goals
        } else if(number == 4){
            //Resources
        } else {
            return;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.drug_view, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        if(id == R.id.action_add_reminder){
            Log.i("testing", "Add Reminder clicked, going to AddReminder");
            Intent i = new Intent(this, AddReminder.class);
            i.putExtra("DrugName", m_drugName);
            startActivityForResult(i, ADD_REMINDER);
            return true;
        }
        if(id == R.id.edit_drug){
            Log.i("testing","Edit Drug clicked, going to EditDrug.class");
            Intent i = new Intent(this,EditDrug.class);
            i.putExtra("DrugName",m_drugName);
            i.putExtra("DrugId",m_drugId);
            startActivityForResult(i,EDIT_DRUG);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_REMINDER && resultCode == Activity.RESULT_OK){
           // ReminderFragment reminderFragment = new ReminderFragment();
            //reminderFragment.updateReminders(reminderFragment.getView());

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            Bundle bundle = new Bundle();
            Log.i("testing", "Putting the string " + m_drugName + " into the bundle");
            bundle.putString("drugname", m_drugName);

            mTitle = "Reminders";
            ReminderFragment reminderFragment = new ReminderFragment();
            reminderFragment.setArguments(bundle);
            ft.replace(R.id.drug_view_container, reminderFragment).commit();

        }
        if(requestCode == EDIT_DRUG && resultCode == Activity.RESULT_OK){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            Bundle bundle = new Bundle();
            Log.i("testing", "Putting the drug id " + m_drugId + " into the bundle");
            bundle.putString("drugname", m_drugName);
            bundle.putLong("drugid", m_drugId);

            DrugInfoFragment drugInfoFragment = new DrugInfoFragment();
            drugInfoFragment.setArguments(bundle);
            ft.replace(R.id.drug_view_container,drugInfoFragment).commit();
        }
    }

    public PendingIntent getPendingIntent(){
        return m_pendingIntent;
    }

    public void clickErowid(View view){
        if(m_drug.getErowidLink().equalsIgnoreCase("")){
            Toast.makeText(this, "No link available!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(m_drug.getErowidLink()));
        startActivity(browserIntent);
    }
    public void clickWiki(View view){
        if(m_drug.getWikiLink().equalsIgnoreCase("")){
            Toast.makeText(this, "No link available!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(m_drug.getWikiLink()));
        startActivity(browserIntent);
    }
    public void clickReddit(View view){
        if(m_drug.getRedditLink().equalsIgnoreCase("")){
            Toast.makeText(this, "No link available!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(m_drug.getRedditLink()));
        startActivity(browserIntent);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
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

        /*@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_drug_view, container, false);
            return rootView;
        }*/

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((DrugView) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
