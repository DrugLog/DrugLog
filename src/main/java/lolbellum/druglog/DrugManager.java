package lolbellum.druglog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import  android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;
import java.util.Collections;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class DrugManager {

    MyDBHandler dbHandler;
    private  int m_drugManagerId;

    private ArrayList<Drug> m_drugList = new ArrayList<Drug>();
    private ArrayList<DrugClass> m_drugClassList = new ArrayList<DrugClass>();
    private ArrayList<Dose> m_allDoses = new ArrayList<Dose>();
    private ArrayList<Metric> m_allMetrics = new ArrayList<Metric>();
    private ArrayList<RouteOfAdministration> m_allRoas = new ArrayList<RouteOfAdministration>();
    private ArrayList<Note> m_allNotes = new ArrayList<Note>();
    private ArrayList<Reminder> m_allReminders = new ArrayList<Reminder>();
    private ArrayList<MetricConversion> m_metricConversions = new ArrayList<MetricConversion>();
    private ArrayList<Notification> m_notifications = new ArrayList<Notification>();


   // public DrugManager(){
       // Random rand = new Random();
      //  int randomNumber = rand.nextInt(10000) + 1;
       // m_drugManagerId = randomNumber;
  //  }

    public DrugManager(int id, MyDBHandler dbHandler){
        m_drugManagerId = id;
        this.dbHandler = dbHandler;
    }
    public DrugManager(MyDBHandler dbHandler){;
        this.dbHandler = dbHandler;
    }

    public DrugManager getDrugManager(int id){
        if(m_drugManagerId == id){
            return this;
        }
        //if it returns null, the drugManager doesn't exist
        return null;
    }

    public void testSetUp(){
        m_drugList.clear();
        m_drugClassList.clear();
        m_allRoas.clear();
        m_allMetrics.clear();
        m_allDoses.clear();
        m_allNotes.clear();
        m_allReminders.clear();
        m_metricConversions.clear();
        m_notifications.clear();

        m_drugList = dbHandler.getDrugs();
        m_drugClassList = dbHandler.getDrugClasses();
        m_allRoas =  dbHandler.getRoas();
        m_allMetrics = dbHandler.getMetrics();
        m_allDoses = dbHandler.getDoses();
        m_allNotes = dbHandler.getNotes();
        m_allReminders = dbHandler.getReminders();
        m_metricConversions = dbHandler.getMetricConversions();
        m_notifications = dbHandler.getNotifications();
    }

    public void setUp(){
        m_drugList.clear();
        m_drugClassList.clear();
        m_allDoses.clear();
        m_allMetrics.clear();
        m_allRoas.clear();
        m_allNotes.clear();
        m_allReminders.clear();
        m_metricConversions.clear();

        Log.i("testing", "Setting up drugclasses");
        m_drugClassList = dbHandler.getDrugClasses();
        Log.i("testing", "Drug class setup finished");
        m_drugList = dbHandler.getDrugs();
        Log.i("testing", "Setting up doses");
        m_allDoses = dbHandler.getDoses();
        Log.i("testing", " Done setting up doses. There are " + m_allDoses.size() + " doses in the database");
        Log.i("testing", "Setting up metrics");
        m_allMetrics = dbHandler.getMetrics();
        Log.i("testing", "Done setting up metrics");
        Log.i("testing", "Setting up Roas");
        m_allRoas = dbHandler.getRoas();
        Log.i("testing", "Roas finished setting up");
        m_allNotes = dbHandler.getNotes();
        m_allReminders = dbHandler.getReminders();
        Log.i("testing", "There are " + dbHandler.getMetricConversions().size() + " metric conversions in the database") ;
        m_metricConversions = dbHandler.getMetricConversions();
    }

   /* public void addDrugToClass(DrugClass drugClass, Drug drug){
        dbHandler.addDrugToClass(drugClass, drug);
    }*/

    public void addDrugToClass(DrugClass drugClass, Drug drug){
//        DrugClass drugClass = getDrugClass(Class.getClassId());
        //dbHandler.addDrugToClass(drugClass,drug);
        dbHandler.addDrugClass(drugClass);
    }

    public void removeDrugFromClass(DrugClass drugClass, Drug drug){
        dbHandler.addDrugClass(drugClass);
    }

    public void createDrugClass(String className, ArrayList<Drug> drugstoAddToClass){
        DrugClass newClass = new DrugClass(className, drugstoAddToClass);
    }

    public void addDrug(Drug drug){
        //checks if the drug already exists, if so, it returns and doesn't add it to m_drugList or the SQL database
        for(int x = 0; x < getDrugs().size();x++){
            if(getDrugs().get(x).getDrugId() == drug.getDrugId()){
                Log.i("testing", "DrugManager - Drug already exists, returning");
                return;
            }
        }

        m_drugList.add(drug);
        dbHandler.addDrug(drug);
        //Collections.sort(m_drugList);
       //assignDrugIds();
    }

    public void updateDrug(Drug drug, long originalId){
        Log.i("testing","DrugManager - updateDrug(drug)");
       // Drug updatedDrug = getDrug(originalId);
       // if(updatedDrug == null){
       //     Log.i("testing","drug is null in updateDrug(drug). Returning");
        //    return;
        //}
       /* updatedDrug.setDrugName(drug.getDrugName());
        updatedDrug.setImageId(drug.getImageId());
        updatedDrug.setImagePath(drug.getImagePath());
        updatedDrug.setDrugNicknames(drug.getDrugNickNames());
        updatedDrug.setErowidLink(drug.getErowidLink());
        updatedDrug.setWikiLink(drug.getWikiLink());
        updatedDrug.setRedditLink(drug.getRedditLink());*/


        for(int x = 0;x < getDrugClasses().size();x++){
            DrugClass tempClass = getDrugClasses().get(x);
            for(int i = 0;i < tempClass.getDrugs().size();i++){
                Drug tempDrug = tempClass.getDrugs().get(i);
                if(tempDrug.getDrugId() == originalId){
                    dbHandler.addDrugClass(tempClass);
                }
            }
        }

        for(int x = 0;x < getAllDoses().size();x++){
            Dose tempDose = getAllDoses().get(x);
            if(drug.getDrugId() == tempDose.getDrug().getDrugId()){
                dbHandler.addDose(tempDose);
            }
        }

        dbHandler.addDrug(drug);
    }

    public void updateDrugClass(DrugClass drugClass){
        dbHandler.addDrugClass(drugClass);
    }

    public void addReminder(Reminder reminder){
        for(int x = 0; x < m_allReminders.size();x++){
            Reminder tempReminder = m_allReminders.get(x);
            if(reminder.getDrug() == null){
                if(reminder.getDrugClass().getDrugClassName().equalsIgnoreCase(tempReminder.getDrugClass().getDrugClassName()) && (reminder.getTime() == tempReminder.getTime() && reminder.getTitle().equalsIgnoreCase(tempReminder.getTitle()))){
                    return;
                }
                if(reminder.getDrugClass() == null){
                    if(reminder.getDrug().getDrugName().equalsIgnoreCase(tempReminder.getDrug().getDrugName()) && (reminder.getTime() == tempReminder.getTime() && reminder.getTitle().equalsIgnoreCase(tempReminder.getTitle()))){
                        return;
                    }
                }
            }
        }
        m_allReminders.add(reminder);
        dbHandler.addReminder(reminder);
    }

    public void removeReminder(Reminder reminder){
        m_allReminders.remove(reminder);
    }


    public void addNotification(Notification notification){

        for(int x = 0;x < getNotifications().size();x++){
            if(notification.getId() == getNotifications().get(x).getId()){
                m_notifications.remove(notification);
            }
        }

        m_notifications.add(notification);
        dbHandler.addNotification(notification);
    }

    public ArrayList<Reminder> getReminders(){
        return m_allReminders;
    }
    public void setReminders(ArrayList<Reminder> allReminders){
        m_allReminders = allReminders;
    }

    public ArrayList<Notification> getNotifications(){
        return m_notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications){
        m_notifications = notifications;
    }

    public Reminder getReminder(String drugName, String text, int timeInterval, int timeFor){
        for(int x = 0;x < getReminders().size();x++){
            Reminder reminder = getReminders().get(x);
            if(reminder.getDrug().getDrugName().equalsIgnoreCase(drugName) && reminder.getText().equalsIgnoreCase(text) && reminder.getTime() == timeInterval && reminder.getTimeFor() == timeFor){
                return reminder;
            }
        }
        return null;
    }

    public void addDrugClass(DrugClass drugClass){
        for(int x = 0;x < getDrugClasses().size();x++){
            if(getDrugClasses().get(x).getDrugClassName().equalsIgnoreCase(drugClass.getDrugClassName())){
                return;
            }
            if(drugClass.getDrugClassName().equalsIgnoreCase("All Drugs")){
                return;
            }
        }


        //adds the class to the list of drug classes in DrugManager
        m_drugClassList.add(drugClass);
        //adds the class to the SQL database
        dbHandler.addDrugClass(drugClass);
       // alphabetizeDrugClasses();

        //update the order in the layout

       // assignDrugClassIds();
    }

    public ArrayList<RouteOfAdministration> getAllRoas(){
        return m_allRoas;
    }
    public void setAllRoas(ArrayList<RouteOfAdministration> allRoas){
        m_allRoas = allRoas;
    }
    public void addRoa(RouteOfAdministration roa){

        for(int x = 0;x < getAllRoas().size();x++){
            if(getAllRoas().get(x).getRoaName().equalsIgnoreCase(roa.getRoaName())){
                Log.i("testing","DrugManager - Roa already exists, returning");
                return;
            }
        }

        m_allRoas.add(roa);
        dbHandler.addRoa(roa);

    }
    public void removeRoa(RouteOfAdministration roa){
        m_allRoas.remove(roa);
    }
    public RouteOfAdministration getRoa(String roaName){
        for(int x = 0;x<m_allRoas.size();x++){
            if(m_allRoas.get(x).getRoaName().equalsIgnoreCase(roaName)){
                return m_allRoas.get(x);
            }
        }
        return null;
    }
    public void alphabetizeDrugClasses(){

        //Sorting the list of classes in alphabetical order
        List<String> listOfNames = new LinkedList<String>();
        //assigning the drugclass arraylist to a List<String> so that it can be sorted by Collections.sort(List list);
        for(int x = 0;x < m_drugClassList.size();x++){
            DrugClass classAtX = m_drugClassList.get(x);
            String nameAtX = classAtX.getDrugClassName();
            listOfNames.add(x,nameAtX);
        }
        //sorting the list of drugclass by alphabetical order of each class's name
        Collections.sort(listOfNames);

        m_drugClassList.clear();
        //assigning the list of drugclasses in the order of the new Sorted List<String>
        for(int x = 0;x < listOfNames.size();x++){
            m_drugClassList.add(getDrugClass(listOfNames.get(x)));
        }
    }

    public DrugClass getDrugClass(String className){
        for(int x = 0; x < m_drugClassList.size();x++){
            DrugClass tempClass = m_drugClassList.get(x);
            if(className.equalsIgnoreCase(tempClass.getDrugClassName())){
                return tempClass;
            }
        }

        return null;
    }

    public void removeDrug(Drug drug){
        m_drugList.remove(drug);
        dbHandler.removeDrug(drug);

        for(int x = 0;x < getDrugClasses().size();x++){
            DrugClass tempClass = getDrugClasses().get(x);
            for(int i = 0;i < tempClass.getDrugs().size();i++){
                Drug tempDrug = tempClass.getDrugs().get(i);
                if(tempDrug.getDrugId() == drug.getDrugId()){
                    tempClass.removeDrug(drug);
                    //dbHandler.addDrugClass(tempClass);
                }
            }
        }
    }

    public void removeDrugClass(DrugClass drugClass){
        m_drugClassList.remove(drugClass);
        dbHandler.removeDrugClass(drugClass);
    }

    public ArrayList<Drug> getDrugs(){
       // assignDrugIds();
        return m_drugList;
    }

    public void setDrugs(ArrayList<Drug> drugs){
        m_drugList = drugs;
    }

    public ArrayList<DrugClass> getDrugClasses(){
       // assignDrugClassIds();
    return m_drugClassList;
    }

    public void setDrugClasses(ArrayList<DrugClass> drugClasses){
        m_drugClassList = drugClasses;
    }

    public ArrayList<Dose> getAllDoses(){
        return m_allDoses;
    }

    public void setAllDoses(ArrayList<Dose> allDoses){
        m_allDoses = allDoses;
    }
    public Dose getDose(Drug drug, long date){
        for(int x = 0; x < m_allDoses.size();x++){
            Dose tempDose = m_allDoses.get(x);
            if(tempDose.getDrug() == drug && tempDose.getDateMilliseconds() == date){
                return tempDose;
            }
        }
        return null;
    }
    public Dose getDose(String drugName, String roaName, String date){
        for(int x = 0;x < m_allDoses.size();x++){
            Dose tempDose = m_allDoses.get(x);
            if(tempDose.getDrug().getDrugName().equalsIgnoreCase(drugName) && tempDose.getRoa().getRoaName().equalsIgnoreCase(roaName) && tempDose.getDate().equalsIgnoreCase(date)){
                return tempDose;
            }
        }
        return null;
    }

    public Dose getDose(int id){
        return dbHandler.getDose(id);
    }

    public void addDose(Dose dose){

       /* for(int x = 0 ;x < getAllDoses().size();x++){
            if(getAllDoses().get(x).getDrug().getDrugName().equalsIgnoreCase(dose.getDrug().getDrugName()) && getAllDoses().get(x).getDate().equalsIgnoreCase(dose.getDate())){
                Log.i("testing", "DrugManager - dose already exists, returning");
                return;
            }
        }*/

        for(int x = 0; x < getAllDoses().size();x++){
            Dose tempDose = getAllDoses().get(x);
            if(dose.getDrug().getDrugName().equalsIgnoreCase(tempDose.getDrug().getDrugName()) && dose.getDate().equalsIgnoreCase(tempDose.getDate())){
                Log.i("testing", "DrugManager - dose already exists, returning");
                return;
            }
        }

        m_allDoses.add(dose);
        dbHandler.addDose(dose);
    }

    public void removeDose(Dose dose){
        m_allDoses.remove(dose);
    }

    public ArrayList<Note> getNotes(Dose dose){
       if(dose != null){
           return dose.getNotes();
       }
        return null;
    }


    public void addNote(Note note){

        for(int x = 0; x < m_allNotes.size();x++){
            Note tempNote = m_allNotes.get(x);
            if(tempNote.getNoteDose().getDrug().getDrugName().equalsIgnoreCase(note.getNoteDose().getDrug().getDrugName()) && tempNote.getNoteDose().getRoa().getRoaName().equalsIgnoreCase(note.getNoteDose().getRoa().getRoaName()) && tempNote.getNoteDose().getDate().equalsIgnoreCase(note.getNoteDose().getDate()) && tempNote.getNoteTitle().equalsIgnoreCase(note.getNoteTitle())){
                return;
            }
        }

        m_allNotes.add(note);
       // note.getNoteDose().addNote(note);
        dbHandler.addNote(note);
    }

    public ArrayList<Dose> getDrugDoses(String drugName){
        ArrayList<Dose> drugDoses = new ArrayList<Dose>();
        if(drugExists(drugName)){
            for(int x = 0;x < m_allDoses.size();x++){
                Dose tempDose = m_allDoses.get(x);
                if(tempDose.getDrug().equals(getDrugFromName(drugName))){
                    drugDoses.add(tempDose);
                }
            }
            return drugDoses;
        }
        return null;
    }

    public int getTimesDosed(String drugName){
        if(drugExists(drugName)){
            return getDrugDoses(drugName).size();
        }
        return 0;
    }

    public Drug getDrugFromName(String drugName){
        for(int x = 0;x < m_drugList.size();x++){
            Drug tempDrug = m_drugList.get(x);
            if(drugName.equalsIgnoreCase(tempDrug.getDrugName())){
                return tempDrug;
            }
            for(int i = 0; i < tempDrug.getDrugNickNames().size();i++){
                if(drugName.equalsIgnoreCase(tempDrug.getDrugNickNames().get(i))){
                    return tempDrug;
                }
            }
        }
        //return null if nothing is found
        return null;
    }

    public Drug getDrug(long id){
        for(int x = 0;x < m_drugList.size();x++){
            Drug tempDrug = m_drugList.get(x);
            if(id == tempDrug.getDrugId()){
                return tempDrug;
            }
        }
        return null;
    }

    public boolean drugExists(String drugName){
        if(getDrugFromName(drugName) != null){
            return true;
        }
        return false;
    }

    public void addMetric(Metric metric){

        for(int x = 0;x < getAllMetrics().size();x++){
            if(getAllMetrics().get(x).getMetricName().equalsIgnoreCase(metric.getMetricName())){
                Log.i("testing", "DrugManager - Metric already exists, returning");
                return;
            }
        }

        m_allMetrics.add(metric);
        dbHandler.addMetric(metric);
    }
    public void removeMetric(Metric metric){
        dbHandler.deleteMetric(metric.getMetricName());
        m_allMetrics.remove(metric);
    }
    public Metric getMetric(Drug drug, String metricName){
        for(int x = 0; x < m_allMetrics.size();x++){
            Metric tempMetric = m_allMetrics.get(x);
            if(tempMetric.getMetricDrug() == drug && tempMetric.getMetricName() == metricName){
                return tempMetric;
            }
        }
        return null;
    }

    public Metric getMetric(String metricName){
        for(int x = 0;x < m_allMetrics.size();x++){
            Metric tempMetric = m_allMetrics.get(x);
            if(tempMetric.getMetricName().equalsIgnoreCase(metricName)){
                return tempMetric;
            }
        }
        return null;
    }

    public Metric getMetric(long id){
        for(int x = 0;x < getAllMetrics().size();x++){
            Metric tempMetric = getAllMetrics().get(x);
            if(tempMetric.getId() == id){
                return tempMetric;
            }
        }
        return null;
    }

    public boolean drugClassExists(String className){
        if(getDrugClass(className) != null){
            return true;
        }
        return false;
    }

    public void assignDrugIds(){
       // alphabetizeDrugs();
        for(int x = 0; x < m_drugList.size();x++){
            Drug tempDrug = m_drugList.get(x);
            tempDrug.setDrugId(x);
        }
    }

    public void assignDrugClassIds(){
        alphabetizeDrugClasses();
        for(int x = 0; x < m_drugClassList.size();x++){
            DrugClass tempClass = m_drugClassList.get(x);
            //tempClass.setClassId(x);
        }
    }

    public void alphabetizeDrugs(){
        ArrayList<String> drugNames = new ArrayList<String>();

        for(int x = 0;x < m_drugClassList.size();x++){
            DrugClass drugClass = m_drugClassList.get(x);
            // tempClass = drugClass;
           // if(drugClass != null) {
                String drugClassName = drugClass.getDrugClassName();
                drugNames.add(drugClassName);
          //  }

        }

        Collections.sort(drugNames);

        for(int x = 0; x < drugNames.size(); x++){
            m_drugList.set(x, getDrugFromName(drugNames.get(x)));
        }
    }



    public int getdrugManagerId(){
        return m_drugManagerId;
    }

    public void setDrugManagerId(int id){
        m_drugManagerId = id;
    }

    public static String formattedDate(long milliseconds, String dateFormat){

        //In another place your code would look like: System.out.println(getDate(82233213123L, "dd/MM/yyyy hh:mm:ss.SSS"));
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());
    }

    public ArrayList<Metric> getAllMetrics(){
        return m_allMetrics;
    }
    public void setM_allMetrics(ArrayList<Metric> allMetrics){
        m_allMetrics = allMetrics;
    }

    public ArrayList<Note> getAllNotes(){
        return m_allNotes;
    }
    public void setAllNotes(ArrayList<Note> allNotes){
        m_allNotes = allNotes;
    }

    public ArrayList<MetricConversion> getMetricConversions(){
        return m_metricConversions;
    }
    public void setMetricConversions(ArrayList<MetricConversion> metricConversions){
        m_metricConversions = metricConversions;
    }

    public void addMetricConversion(MetricConversion metricConversion){
       /* String metricToConvertName = metricConversion.getMetricToConvert().getMetricName();
        String metricToConvertToName = metricConversion.getMetricToConvertTo().getMetricName();
        for(int x = 0;x < m_metricConversions.size();x++){
            MetricConversion tempMetricConversion = m_metricConversions.get(x);
            String tempMetricToConvertName = tempMetricConversion.getMetricToConvert().getMetricName();
            String tempMetricToConvertToName = tempMetricConversion.getMetricToConvertTo().getMetricName();

            if(metricToConvertName.equalsIgnoreCase(tempMetricToConvertName) && metricToConvertToName.equalsIgnoreCase(tempMetricToConvertToName) && metricConversion.getConversionRate() == tempMetricConversion.getConversionRate()){
              //  Log.i("testing", "DrugManager - This metric conversion already exists. Returning");
                return;
            }
        }*/


        m_metricConversions.add(metricConversion);
        dbHandler.addMetricConversion(metricConversion);
    }

    public double convertMetrics(double amount, Metric metric1, Metric metric2){
        String metricOneName = metric1.getMetricName();
        String metricTwoName = metric2.getMetricName();

        if(metricOneName.equalsIgnoreCase(metricTwoName)){
            Log.i("testing", "The units are the same");
            return amount;
        }

        if(!metric2.getMetricName().equalsIgnoreCase("Grams")){
            Log.i("testing","Converting to Grams");
            amount = convertMetrics(amount,metric1,getMetric("Grams"));
            metricOneName = "Grams";
        }

        for(int x = 0;x < m_metricConversions.size();x++){
            MetricConversion tempMetricConversion = m_metricConversions.get(x);
            Log.i("testing","The temp conversion rate is " + tempMetricConversion.getConversionRate());
            Metric tempMetricToConvert = tempMetricConversion.getMetricToConvert();
            Metric tempMetricToConvertTo = tempMetricConversion.getMetricToConvertTo();

            String metricToConvertName = tempMetricToConvert.getMetricName();
            String metricToConvertToName = tempMetricToConvertTo.getMetricName();

            if(metricOneName.equalsIgnoreCase(metricToConvertName) && metricTwoName.equalsIgnoreCase(metricToConvertToName)){
                Log.i("testing", "Normal metric conversion");
                double conversionRate = tempMetricConversion.getConversionRate();
                Log.i("testing","The conversion rate is " + conversionRate);
                double newAmount = amount * conversionRate;
                Log.i("testing","The original amount(" + metricOneName + ") was " + amount + " The new amount(" + metricTwoName + ") is " + newAmount);
                return newAmount;
            }

            if(metricOneName.equalsIgnoreCase(metricToConvertToName) && metricTwoName.equalsIgnoreCase(metricToConvertName)){
                Log.i("testing", "Inverse metric conversion");
               // double conversionRate =  (1 / (tempMetricConversion.getConversionRate()));
                double conversionRate = tempMetricConversion.getConversionRate();
                Log.i("testing","The conversion rate is " +  1/conversionRate);
                double newAmount = amount / conversionRate;
                Log.i("testing","The original amount(" + metricOneName + ") was " + amount + " The new amount(" + metricTwoName + ") is " + newAmount);
                return newAmount;
            }

        }
        Log.i("testing", "No metric conversion exists for these two metrics. Returning");
        return amount;
    }

    public double convertToGrams(double amount, Metric metric){
        for(int x = 0;x < m_metricConversions.size();x++){

        }

        return 0;
    }

    public DrugClass getDrugClass(Drug drug){
        String drugName = drug.getDrugName();
        for(int x = 0;x < getDrugClasses().size();x++){
            DrugClass tempDrugClass = getDrugClasses().get(x);
            for(int i = 0;i < tempDrugClass.getDrugs().size();x++){
                Drug tempDrug = tempDrugClass.getDrugs().get(i);
                if(tempDrug.getDrugName().equalsIgnoreCase(drugName)){
                   return tempDrugClass;
                }
            }
        }
        return null;
    }

    public DrugClass getDrugClass(long id){
        for(int x = 0;x < m_drugClassList.size();x++){
            DrugClass tempClass = m_drugClassList.get(x);
            if(id == tempClass.getClassId()){
                return tempClass;
            }
        }
        return null;
    }

    public ArrayList<DrugClass> getDrugsClasses(Drug drug){
        ArrayList<DrugClass> drugsClasses = new ArrayList<DrugClass>();
        String drugName = drug.getDrugName();
        for(int x = 0;x < getDrugClasses().size();x++){
            DrugClass tempDrugClass = getDrugClasses().get(x);
            for(int i = 0;i < tempDrugClass.getDrugs().size();x++){
                Drug tempDrug = tempDrugClass.getDrugs().get(i);
                if(tempDrug.getDrugName().equalsIgnoreCase(drugName)){
                    drugsClasses.add(tempDrugClass);
                }
            }
        }
        return drugsClasses;
    }
}
