package lolbellum.druglog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import android.util.Log;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Calendar extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final static String TAG = "testing";

    final static int ADD_LOG = 0;
    final static int DONATE = 1;
    final static int SPLASH_SCREEN = 2;

   public static DrugManager drugManager;
    MyDBHandler dbHandler;

    ListView doseListView;
    ListAdapter doseList;

    private static final String PREF_USER_FIRST_TIME = "firstTime";
    private static final String PREF_PASSCODE_ENABLED = "passcodeEnabled";
    private static final String PREF_TRY_LOGIN = "tryLogin";
    private static final String PREF_USER_PASSCODE = "userPasscode";
    private String passcode;
    private boolean passcodeExists;
    private boolean tryLogin = true;
    private boolean passcodeEnabled = true;
    private boolean loginDialogOpen = false;
    boolean firstTime;
    private static final String PREF_ALERT_BAD_COMBINATIONS = "alertBadCombinations";
    boolean alertBadCombinations;
    ArrayList<String> activeDates = new ArrayList<String>();

    private int selectedMonth;
    private int selectedDay;
    private int selectedYear;
    private String dateString;


    private static boolean devMode = true;

    private NavigationDrawerFragment mNavigationDrawerFragment;


    private CharSequence mTitle;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        doseListView = (ListView) findViewById(R.id.list_view_doses);

        firstTime = sp.getBoolean(PREF_USER_FIRST_TIME, true);
        passcodeEnabled = sp.getBoolean(PREF_PASSCODE_ENABLED,true);
        alertBadCombinations = sp.getBoolean(PREF_ALERT_BAD_COMBINATIONS, true);
        tryLogin = sp.getBoolean(PREF_TRY_LOGIN,true);
        passcode = sp.getString(PREF_USER_PASSCODE,"");

        //dbHandler = new MyDBHandler(this, null, null, 16);
        dbHandler = MyDBHandler.getInstance(this, null, null, 25);
        drugManager = new DrugManager(dbHandler);

        if(passcode.equalsIgnoreCase("")){
            passcodeExists = false;
        } else {
            passcodeExists = true;
        }

        if(passcodeEnabled) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (firstTime) {
                        Log.i(TAG, "First time running");
                        onFirst();

                    } else {
                        Log.i(TAG, "Setting up variables from database");
                        //drugManager.setUp();
                        drugManager.testSetUp();
                        Log.i(TAG, "Finished setting up");
                    }
                }
            }).start();
        } else {
            if (firstTime) {
                Log.i(TAG, "First time running");
                onFirst();

            } else {
                Log.i(TAG, "Setting up variables from database");
                //drugManager.setUp();
                drugManager.testSetUp();
                Log.i(TAG, "Finished setting up");
            }
        }

        String title = "";
        String buttonText = "";
        if(passcodeExists){
            title = "Enter passcode";
            buttonText = "Login";
        } else {
            title = "Set passcode";
            buttonText = "Confirm";
        }


        //warning dialog
        final AlertDialog warningDialog = new AlertDialog.Builder(this).create();
        warningDialog.setTitle("Warning");
        warningDialog.setMessage("If you forget your passcode, you may need to reset all of your data. If you do not want to worry about this, it is recommended that you disable passcodes");
        warningDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        warningDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog loginDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.login_dialog, null);
        loginDialog.setView(layout);
        loginDialog.setTitle(title);
        loginDialog.setCancelable(false);
        final EditText editTextPasscode = (EditText) layout.findViewById(R.id.password);
        loginDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        if(!passcodeExists){
            loginDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Disable Passcode", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        if(passcodeEnabled) {
            loginDialogOpen = true;
            loginDialog.show();


            Button positive = (Button) loginDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negative = (Button) loginDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String enteredPasscode = editTextPasscode.getText().toString();
                    if (enteredPasscode.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "You must enter a passcode!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (passcodeExists) {
                        if (enteredPasscode.equals(passcode)) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            loginDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect passcode!", Toast.LENGTH_LONG).show();
                            editTextPasscode.setText("");
                            return;
                        }
                    } else {
                        warningDialog.show();
                        Button okButton = (Button) warningDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button cancelButton = (Button) warningDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String enteredPasscode = editTextPasscode.getText().toString();
                                warningDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Passcode set!", Toast.LENGTH_LONG).show();
                                sp.edit().putString(PREF_USER_PASSCODE, enteredPasscode).apply();
                                passcodeExists = true;
                                loginDialog.dismiss();
                                loginDialogOpen = false;
                            }
                        });
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                warningDialog.dismiss();
                                return;
                            }
                        });


                    }
                }
            });

            if (negative != null) {
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Toast.makeText(getApplicationContext(), "Passcode disabled. You can re-enable this in settings", Toast.LENGTH_LONG).show();
                        sp.edit().putBoolean(PREF_PASSCODE_ENABLED, false).apply();
                        loginDialog.dismiss();
                        loginDialogOpen = false;
                    }
                });
            }

        }

      /*  final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.login_dialog, null);
        final EditText editTextPasscode = (EditText) layout.findViewById(R.id.password);
        builder.setTitle(title);
       // builder.setView(inflater.inflate(R.layout.login_dialog,null));
        builder.setView(layout);
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                final String enteredPasscode = editTextPasscode.getText().toString();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(passcodeExists){
                    if(enteredPasscode.equals(passcode)){
                        Toast.makeText(getApplicationContext(),"Passcode entered successfully!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(),"Passcode entered incorrectly!",Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    if(enteredPasscode.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"You must enter a passcode!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Passcode set!", Toast.LENGTH_LONG).show();
                    sp.edit().putString(PREF_USER_PASSCODE, enteredPasscode);
                    sp.edit().putBoolean(PREF_PASSCODE_ENABLED,true);
                    passcodeExists = true;
                    dialog.dismiss();
                }
            }
        });
        if(!passcodeExists){
            builder.setNegativeButton("Disbale passcode",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                   Toast.makeText(getApplicationContext(), "Passcode disabled!", Toast.LENGTH_LONG).show();
                    sp.edit().putBoolean(PREF_PASSCODE_ENABLED, false).commit();
                }
            });
        }
        builder.create();
        if(passcodeEnabled){
            builder.show();
        }*/





        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            if(tempDose.getDate().length() > 0) {
                String tempDate = tempDose.getDate().substring(0, 10);
                //Toast.makeText(this, tempDate,Toast.LENGTH_SHORT).show();
                //  Toast.makeText(this,tempDate + " added to active dates!" ,Toast.LENGTH_SHORT).show();
                activeDates.add(tempDate);
                Log.i("testing","Adding date: " + tempDate + " to activeDates");
            }
           // Toast.makeText(this, tempDate + " is an active date",Toast.LENGTH_SHORT).show();
        }



        //initializing the datepicker and getting the currently selected date
        final DatePicker dosePicker = (DatePicker)findViewById(R.id.dose_calendar);

       selectedMonth = dosePicker.getMonth();
        selectedDay = dosePicker.getDayOfMonth();
        selectedYear = dosePicker.getYear();

        updateDoseList(selectedMonth, selectedDay, selectedYear);


       doseListView.setOnItemClickListener(
               new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Intent i = new Intent(parent.getContext(), DoseView.class);

                       Dose dose = (Dose) doseListView.getItemAtPosition(position);
                       i.putExtra("DrugName", dose.getDrug().getDrugName());
                       i.putExtra("RoaName", dose.getRoa().getRoaName());
                       i.putExtra("Date", dose.getDate());
                       i.putExtra("Time", dose.getDate().substring(11, 15));
                       i.putExtra("MetricName", dose.getMetric().getMetricName());
                       i.putExtra("DoseAmount", dose.getDoseAmount());

                       startActivity(i);
                   }
               }
       );

        dosePicker.setOnClickListener(
                new DatePicker.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "Datepicker clicked");
                        selectedYear = dosePicker.getYear();
                        selectedMonth = dosePicker.getMonth();
                        selectedDay = dosePicker.getDayOfMonth();
                        updateDoseList(selectedMonth, selectedDay, selectedYear);
                    }
                }
        );

        //Called whenever the date is changed on the datepicker
      //dosePicker.init(dosePicker.getYear(), dosePicker.getMonth(), dosePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
        dosePicker.init(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR), java.util.Calendar.getInstance().get(java.util.Calendar.MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                   @Override
                   public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                       Log.i(TAG, "Date changed");
                       updateDoseList(monthOfYear, dayOfMonth, year);
                   }
               }
       );




        //find which dates have doses, and maybe mark them somehow


        //CalendarView logCalendar = (CalendarView) findViewById(R.id.log_calendar);
       // long currentTime = System.currentTimeMillis();
       /* String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(currentTime));
        int currentMonth = Integer.parseInt(dateString.substring(0,1));
        int currentDay = Integer.parseInt();*/
      //  logCalendar.setDate(currentTime);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
          mTitle = getTitle();
       // mTitle = "Calendar";

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        sp.edit().putBoolean(PREF_TRY_LOGIN,true).commit();
    }

    public void clickDatePicker(View view){
        DatePicker dosePicker = (DatePicker) findViewById(R.id.dose_calendar);
        //Log.i(TAG, "Datepicker clicked");
        selectedYear = dosePicker.getYear();
        selectedMonth = dosePicker.getMonth();
        selectedDay = dosePicker.getDayOfMonth();
        updateDoseList(selectedMonth, selectedDay, selectedYear);
    }

    public void updateDoseList(int monthOfYear, int dayOfMonth, int year){

        Log.i(TAG, "Updating dose list");

        TextView emptyList = (TextView) findViewById(R.id.emptyList);
        //ArrayList<Dose> doses = new ArrayList<Dose>();

        //set the selected date variable to the new selected date
        selectedMonth = monthOfYear;
        selectedDay = dayOfMonth;
        selectedYear = year;

        //create a MM/dd/YYYY string of the date for comparison
        String month;
        String day;
        String Year;
        if(selectedMonth + 1 < 10){
            month =  "0" + String.valueOf(selectedMonth + 1);
        } else {
            month = String.valueOf(selectedMonth + 1);
        }
        if(selectedDay < 10){
            day = "0" + String.valueOf(selectedDay);
        } else {
            day = String.valueOf(selectedDay);
        }
        Year = String.valueOf(selectedYear);
        dateString = month + "/" + day + "/" + Year;
       // Toast.makeText(this, "dateString is " + dateString,Toast.LENGTH_LONG).show();
        Log.i(TAG, "the current dateString is: " + dateString);

        ArrayList<Dose> doses = new ArrayList<Dose>();

        //for loop that runs through all of the dates with doses
       /* for(int i = 0;i < activeDates.size();i++) {
            Log.i("testing", "Active date: " + activeDates.get(i));
            if (activeDates.get(i).equalsIgnoreCase(dateString)) {
                for (int x = 0; x < drugManager.getAllDoses().size(); x++) {
                    Dose tempDose = drugManager.getAllDoses().get(x);
                    Log.i("testing", "getAllDoses has " + drugManager.getAllDoses().size() + " items");
                    if (tempDose.getDate().length() > 0) {
                        String doseDate = tempDose.getDate().substring(0, 10);
                        Log.i("testing", "doseDate = " + doseDate);
                        if (doseDate.equalsIgnoreCase(dateString)) {
                            doses.add(drugManager.getAllDoses().get(x));
                        }
                    }
                }
            }
        }*/
        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            String doseDate = tempDose.getDate().substring(0,10);
            Log.i("testing","Dose date is " + doseDate);
            if(doseDate.equalsIgnoreCase(dateString)){
                doses.add(tempDose);
            }
        }


        Log.i("testing", "There are " + doses.size() + " doses under this date");
                doseList = new DoseListAdapter(this,doses);
                //doseListView = (ListView) findViewById(R.id.list_view_doses);
                doseListView.setAdapter(doseList);

        if(doses.size() > 0){
            if(doseListView.getVisibility() == View.INVISIBLE){
                doseListView.setVisibility(View.VISIBLE);
            }

            if(emptyList.getVisibility() == View.VISIBLE){
                emptyList.setVisibility(View.INVISIBLE);
            }
        } else {
            if(doseListView.getVisibility() == View.VISIBLE){
                doseListView.setVisibility(View.INVISIBLE);
            }

            if(emptyList.getVisibility() == View.INVISIBLE){
                emptyList.setVisibility(View.VISIBLE);
            }
        }

      //  if(doseListView.getVisibility() == View.INVISIBLE){
        //    doseListView.setVisibility(View.VISIBLE);
       // }

       // if(emptyList.getVisibility() == View.VISIBLE){
       //     emptyList.setVisibility(View.INVISIBLE);
       // }

            }

   /* @Override
    public void onBackPressed() {
        if(loginDialogOpen){
            return;
        }
        super.onBackPressed();
    }*/

    public void onFirst(){
       // Intent i = new Intent(this,SplashScreen.class);
        //startActivityForResult(i,SPLASH_SCREEN);
        //Roas
        RouteOfAdministration oral = new RouteOfAdministration("Oral");
        RouteOfAdministration sublingual = new RouteOfAdministration("Sublingual");
        RouteOfAdministration intranasal = new RouteOfAdministration("Insufflated");
        RouteOfAdministration vaporized = new RouteOfAdministration("Vaporized");
        RouteOfAdministration smoke = new RouteOfAdministration("Smoked");
        RouteOfAdministration plugged = new RouteOfAdministration("Plugged");

        //metrics
        Metric grams = new Metric("Grams");
        Metric kilograms = new Metric("Kilograms");
        Metric milligrams = new Metric("Milligrams");
        Metric micrograms = new Metric("Micrograms");
        Metric pounds = new Metric("Pounds");
        Metric ounces = new Metric("Ounces");
        Metric liters = new Metric("Liters");
        Metric milliliters = new Metric("Milliliters");

        //metric conversions
        //to grams
        //MetricConversion microgramsToGrams = new MetricConversion(micrograms, grams, (1/1000000));
       // MetricConversion milligramsToGrams = new MetricConversion(milligrams, grams, (1/1000));
        //MetricConversion kilogramsToGrams = new MetricConversion(kilograms, grams, 1000);
        //grams to others
        MetricConversion gramsToKilograms = new MetricConversion(grams, kilograms, (1/1000));
        MetricConversion gramsToMilligrams = new MetricConversion(grams, milligrams, 1000);
        MetricConversion gramsToMicrograms = new MetricConversion(grams, micrograms, 1000000);
        MetricConversion gramsToOunces = new MetricConversion(grams, ounces, 0.035274);
        MetricConversion gramsToPounds = new MetricConversion(grams, pounds, 0.00220462);
        MetricConversion gramsToLiters = new MetricConversion(grams,liters,(1/1000));
        MetricConversion gramsToMilliliters = new MetricConversion(grams,milliliters,1);

        //Drug nicknames
        ArrayList<String> methNicknames = new ArrayList<String>(Arrays.asList("Meth", "Ice", "Shard", "Tina", "Crystal", "Glass", "Yaba"));
        ArrayList<String> amphNicknames = new ArrayList<String>(Arrays.asList("Speed","Dex","Adderall","Dexamphetamine","Vyvanse"));
        ArrayList<String> cocaineNicknames = new ArrayList<String>(Arrays.asList("Coke","Blow","Snow","Nose Candy"));
        ArrayList<String> caffeineNicknames = new ArrayList<String>(Arrays.asList(""));
        ArrayList<String> nicotineNicknames = new ArrayList<String>(Arrays.asList(""));
        ArrayList<String> methylphenidateNicknames = new ArrayList<String>(Arrays.asList("Ritalin","Concerta","Tranquilyn"));
        ArrayList<String> ketamineNicknames = new ArrayList<String>(Arrays.asList("K", "Special K", "Ket", "Horse Tranquilizer"));
        ArrayList<String> mxeNicknames = new ArrayList<String>(Arrays.asList("MXE", "Mexxy","Roflcopter"));
        ArrayList<String> dxmNicknames = new ArrayList<String>(Arrays.asList("Dextromethorphan","DM","Robo"));
        ArrayList<String> lsdNicknames = new ArrayList<String>(Arrays.asList("Acid", "L", "Lucy"));
        ArrayList<String> dmtNicknames = new ArrayList<String>(Arrays.asList("N,N-DMT","Dimethyltryptamine","Dimitri"));
        ArrayList<String> shroomNicknames = new ArrayList<String>(Arrays.asList("Psilocybin Mushrooms","Magic Mushrooms","Boomers"));
        ArrayList<String> nbomb25iNicknames = new ArrayList<String>(Arrays.asList("2C-I-NBOME","N-Bomb","Smiles","Wizard"));
        ArrayList<String> mdmaNicknames = new ArrayList<String>(Arrays.asList("Ecstasty", "E", "XTC", "Rolls", "Molly", "Adam", "3,4-methylenedioxymethamphetamine"));
        ArrayList<String> alprazolamNicknames = new ArrayList<String>(Arrays.asList("Xanax"));
        ArrayList<String> clonazepamNicknames = new ArrayList<String>(Arrays.asList("Klonopin","Rivotril","Clonitrazepam","CPam"));
        ArrayList<String> clonazolamNicknames = new ArrayList<String>(Arrays.asList("Clonitrazolam","CLam"));
        ArrayList<String> ethanolNicknames = new ArrayList<String>(Arrays.asList("Ethanol", "Ethyl Alcohol","Liquor","Spirits","Beer","Wine"));
        ArrayList<String> heroinNicknames = new ArrayList<String>(Arrays.asList("Dope","H","Junk","Smack"));
        ArrayList<String> tramadolNicknames = new ArrayList<String>(Arrays.asList("Mapatrix"));
        ArrayList<String> cannabisNicknames = new ArrayList<String>(Arrays.asList("Marijuana","Pot","Weed","Grass","Mary Jane"));

        //Drugs
        Drug Methamphetamine = new Drug("Methamphetamine", R.drawable.ic_methamphetamine, methNicknames, "https://www.erowid.org/chemicals/meth/meth.shtml", "https://en.wikipedia.org/wiki/Methamphetamine", "https://www.reddit.com/user/420tazeit/m/drugs/search?q=Meth&restrict_sr=on");
        Drug Amphetamine = new Drug("Amphetamine",R.drawable.ic_2000px_d_amphetamine,amphNicknames,"https://www.erowid.org/chemicals/amphetamines/amphetamines.shtml","https://en.wikipedia.org/wiki/Amphetamine","https://www.reddit.com/user/420tazeit/m/drugs/search?q=Amphetamine%2C+adderall%2C+Vyvanse%2C+speed&restrict_sr=on&sort=relevance&t=all");
        Drug Cocaine = new Drug("Cocaine",R.drawable.ic_cocaine,cocaineNicknames,"https://www.erowid.org/chemicals/cocaine/cocaine.shtml","https://en.wikipedia.org/wiki/Cocaine","https://www.reddit.com/user/420tazeit/m/drugs/search?q=Cocaine&restrict_sr=on&sort=relevance&t=all");
        Drug Caffeine = new Drug("Caffeine",R.drawable.ic_caffeine,caffeineNicknames,"https://www.erowid.org/chemicals/caffeine/caffeine.shtml","https://en.wikipedia.org/wiki/Caffeine","https://www.reddit.com/user/420tazeit/m/drugs/search?q=caffeine&restrict_sr=on&sort=relevance&t=all");
        Drug Nicotine = new Drug("Nicotine",R.drawable.ic_nicotine,nicotineNicknames,"https://www.erowid.org/chemicals/nicotine/","https://en.wikipedia.org/wiki/Nicotine","https://www.reddit.com/user/420tazeit/m/drugs/search?q=nicotine&restrict_sr=on");
        Drug Methylphenidate = new Drug("Methylphenidate",R.drawable.ic_methylphenidate,methylphenidateNicknames,"https://www.erowid.org/pharms/methylphenidate/methylphenidate.shtml","https://en.wikipedia.org/wiki/Methylphenidate","https://www.reddit.com/user/420tazeit/m/drugs/search?q=methylphenidate+ritalin+mph&restrict_sr=on");
        Drug Ketamine = new Drug("Ketamine", R.drawable.ic_ketamine, ketamineNicknames,"https://www.erowid.org/chemicals/ketamine/ketamine.shtml","https://en.wikipedia.org/wiki/Ketamine", "https://www.reddit.com/user/420tazeit/m/drugs/search?q=Ketamine&restrict_sr=on&sort=relevance&t=all");
        Drug Methoxetamine = new Drug("Methoxetamine",R.drawable.ic_methoxetamine,mxeNicknames,"https://www.erowid.org/chemicals/methoxetamine/methoxetamine.shtml","https://en.wikipedia.org/wiki/Methoxetamine","https://www.reddit.com/user/420tazeit/m/drugs/search?q=MXE+methoxetamine&restrict_sr=on");
        Drug Dxm = new Drug("DXM",R.drawable.ic_dxm,dxmNicknames,"https://www.erowid.org/chemicals/dxm/dxm.shtml","https://en.wikipedia.org/wiki/Dextromethorphan","https://www.reddit.com/user/420tazeit/m/drugs/search?q=DXM&restrict_sr=on&sort=relevance&t=all");
        Drug LSD = new Drug("LSD", R.drawable.ic_lsd, lsdNicknames, "https://www.erowid.org/chemicals/lsd/lsd.shtml", "https://en.wikipedia.org/?title=Lysergic_acid_diethylamide", "https://www.reddit.com/user/420tazeit/m/drugs/search?q=LSD&restrict_sr=on&sort=relevance&t=all");
        Drug Shrooms = new Drug("Shrooms",R.drawable.ic_psilocybin,shroomNicknames,"https://www.erowid.org/plants/mushrooms/mushrooms.shtml","https://en.wikipedia.org/wiki/Psilocybin_mushroom","https://www.reddit.com/user/420tazeit/m/drugs/search?q=shrooms&restrict_sr=on");
        Drug nbomb25i = new Drug("25i-NBOMe",R.drawable.ic_25i,nbomb25iNicknames,"https://www.erowid.org/chemicals/2ci_nbome/2ci_nbome.shtml","https://en.wikipedia.org/wiki/25I-NBOMe","https://www.reddit.com/user/420tazeit/m/drugs/search?q=25i+Nbome+&restrict_sr=on&sort=relevance&t=all");
        Drug Dmt = new Drug("DMT",R.drawable.ic_dmt,dmtNicknames,"https://www.erowid.org/chemicals/dmt/dmt.shtml","https://en.wikipedia.org/wiki/N,N-Dimethyltryptamine","https://www.reddit.com/user/420tazeit/m/drugs/search?q=DMT&restrict_sr=on&sort=relevance&t=all");
        Drug MDMA = new Drug("MDMA", R.drawable.ic_mdma, mdmaNicknames, "https://www.erowid.org/chemicals/mdma/mdma.shtml", "https://en.wikipedia.org/wiki/MDMA", "https://www.reddit.com/user/420tazeit/m/drugs/search?q=MDMA&restrict_sr=on&sort=relevance&t=all");
        Drug alprazolam = new Drug("Alprazolam", R.drawable.ic_alprazolam, alprazolamNicknames, "https://www.erowid.org/pharms/alprazolam/alprazolam.shtml", "https://en.wikipedia.org/wiki/Alprazolam","https://www.reddit.com/user/420tazeit/m/drugs/search?q=alprazolam+xanax&restrict_sr=on&sort=relevance&t=all");
        Drug clonazepam = new Drug("Clonazepam",R.drawable.ic_clonazepam,clonazepamNicknames,"https://www.erowid.org/pharms/clonazepam/","https://en.wikipedia.org/wiki/Clonazepam","https://www.reddit.com/user/420tazeit/m/drugs/search?q=clonazepam+klonopin&restrict_sr=on&sort=relevance&t=all");
        Drug clonazolam = new Drug("Clonazolam",R.drawable.ic_clonazolam,clonazolamNicknames,"https://www.erowid.org/experiences/subs/exp_Clonazolam.shtml","https://en.wikipedia.org/wiki/Clonazolam","https://www.reddit.com/user/420tazeit/m/drugs/search?q=clonazolam&restrict_sr=on&sort=relevance&t=all");
        Drug ethanol = new Drug("Alcohol",R.drawable.ic_800px_ethanol_2d_skeletal,ethanolNicknames,"https://www.erowid.org/chemicals/alcohol/","https://en.wikipedia.org/wiki/Alcohol","https://www.reddit.com/user/420tazeit/m/drugs/search?q=Alcohol&restrict_sr=on");
        Drug heroin = new Drug("Heroin",R.drawable.ic_heroin,heroinNicknames,"https://www.erowid.org/chemicals/heroin/heroin.shtml","https://en.wikipedia.org/wiki/Heroin","https://www.reddit.com/user/420tazeit/m/drugs/search?q=heroin&restrict_sr=on&sort=relevance&t=all");
        Drug tramadol = new Drug("Tramadol",R.drawable.ic_tramadol,tramadolNicknames,"https://www.erowid.org/pharms/tramadol/","https://en.wikipedia.org/wiki/Tramadol","https://www.reddit.com/user/mapatrix");
        Drug Cannabis = new Drug("Cannabis",R.drawable.ic_cannabis,cannabisNicknames,"https://www.erowid.org/plants/cannabis/cannabis.shtml","https://en.wikipedia.org/wiki/Cannabis","https://www.reddit.com/user/420tazeit/m/drugs/search?q=weed+OR+cannabis+OR+marijuana&restrict_sr=on&sort=relevance&t=all");
        //list of drugs
        ArrayList<Drug> hallucinogenList = new ArrayList<Drug>(Arrays.asList(LSD,Shrooms,Dmt,nbomb25i));
        ArrayList<Drug> stimulantList = new ArrayList<Drug>(Arrays.asList(Amphetamine,Methamphetamine, MDMA, Cocaine,Caffeine,Methylphenidate,Nicotine));
        ArrayList<Drug> dissocistiveList = new ArrayList<>(Arrays.asList(Ketamine,Methoxetamine,Dxm));
        ArrayList<Drug> benzodiazepineList = new ArrayList<Drug>(Arrays.asList(alprazolam,clonazepam,clonazolam));
        ArrayList<Drug> depressantList = new ArrayList<Drug>(Arrays.asList(ethanol));
        ArrayList<Drug> opiateList = new ArrayList<Drug>(Arrays.asList(heroin,tramadol));


        //list of bad drug combinations

        //Drug Classes
       // DrugClass allDrugs = new DrugClass("Stimulants", drugManager.getDrugs());
        DrugClass Stimulants = new DrugClass("Stimulants", stimulantList);
        DrugClass Hallucinogens = new DrugClass("Hallucinogens", hallucinogenList);
        DrugClass Dissociatives = new DrugClass("Dissociatives", dissocistiveList);
        DrugClass Depressants = new DrugClass("CNS Depressants(minus Benzos)", depressantList);
        ArrayList<DrugClass> benzoCombinations = new ArrayList<DrugClass>(Arrays.asList(Depressants));
        DrugClass Benzodiazepines = new DrugClass("Benzodiazepines", benzodiazepineList,benzoCombinations);
        DrugClass Opiates = new DrugClass("Opiates",opiateList);


        firstTime = false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean(PREF_USER_FIRST_TIME, false).commit();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        if(position == 1){
            Intent i = new Intent(this, drugs.class);
            startActivity(i);
            return;
        } else if(position == 2){
            Intent i = new Intent(this,GeneralStatistics.class);
            startActivity(i);
        } else if(position == 3){
            //for now
            Toast.makeText(this,"Goals not implemented yet!",Toast.LENGTH_LONG).show();
            return;
        } else if(position == 4){
            Intent i = new Intent(this,Reminders.class);
            startActivity(i);
        } else if(position == 5){
            Intent i = new Intent(this,Notifications.class);
            startActivity(i);
        } else if(position == 6){
            Intent i = new Intent(this, Donate.class);
            startActivityForResult(i, DONATE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
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
        if(number == 1){
            mTitle = getString(R.string.title_section1);
        } else if(number == 2){
            mTitle = getString(R.string.title_section2);
        } else if(number == 3){
            mTitle = "Statistics";
        } else if(number == 4){
            mTitle = "Goals";
        } else if(number == 5){
            mTitle = "Reminders";
        } else if(number == 6){
            mTitle = "Support Me";
        } else {
            mTitle = "Calendar";
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
            getMenuInflater().inflate(R.menu.calendar, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

   // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.action_add_dose){
           // CalendarView logCalendar = (CalendarView)findViewById(R.id.log_calendar);
            DatePicker logCalendar = (DatePicker) findViewById(R.id.dose_calendar);
            Intent i = new Intent(this, AddDose.class);


           // if(logCalendar != null) {
              //  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
               // String selectedDate = sdf.format(new Date(logCalendar.getDate()));
                int month = logCalendar.getMonth();
                int day = logCalendar.getDayOfMonth();
                int year = logCalendar.getYear();

               // String selectedDate = month + "/" + day + "/" + year;

               // i.putExtra("Date", selectedDate);
            i.putExtra("day", day);
            i.putExtra("month", month);
            i.putExtra("year", year);
           // }
           // startActivity(i);
            startActivityForResult(i, ADD_LOG);
        }
        if(id == R.id.action_enable_passcode){
            if(passcodeEnabled){
                Toast.makeText(this,"You already have passcode enabled!",Toast.LENGTH_LONG).show();
                return true;
            }
            sp.edit().putBoolean(PREF_PASSCODE_ENABLED,true).commit();
            Toast.makeText(this,"Passcode Enabled!",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_LOG && resultCode == Activity.RESULT_OK) {
            for (int x = 0; x < drugManager.getAllDoses().size(); x++) {
                Dose tempDose = drugManager.getAllDoses().get(x);
                if (tempDose.getDate().length() > 0) {
                    String tempDate = tempDose.getDate().substring(0, 10);
                    // Log.i(TAG, "tempDate of added dose is: " + tempDate);
                    // Toast.makeText(this,"tempDate is " + tempDate ,Toast.LENGTH_LONG).show();
                    for (int i = 0; i < activeDates.size(); i++) {
                        Log.i(TAG, "Running through activedates on activityresult");
                        if (activeDates.get(i).equalsIgnoreCase(tempDate)) {
                            Log.i("testing", "this is already an activedate, returning");
                            return;
                        }
                    }
                    // Log.i(TAG,"Date: " + tempDate + " added to activedates!");
                    activeDates.add(tempDate);
                    //updateDoseList();
                }
            }
        }
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Calendar) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
