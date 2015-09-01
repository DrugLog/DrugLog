package lolbellum.druglog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class DoseListAdapter extends ArrayAdapter<Dose> {

    private ArrayList<Dose> m_doses = new ArrayList<Dose>();


   /* public DoseListAdapter(Context context, String[] drugNames, String[] roas, String[] doseTimes) {
        super(context,R.layout.add_drug_listview ,drugNames);
        m_roas = roas;
        m_doseTimes = doseTimes;
        m_drugNames = drugNames;
    }*/

    public DoseListAdapter(Context context, ArrayList<Dose> doses){
        super(context, R.layout.add_drug_listview, doses);
        m_doses = doses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //final String DOUBLE_BYTE_SPACE = "\u3000";
        String fixString = "";
       // textView.append(DOUBLE_BYTE_SPACE);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.dose_row, parent, false);

        String[] m_roas =  new String[m_doses.size()];
        String[] m_doseTimes = new String[m_doses.size()];
        String[] m_drugNames = new String[m_doses.size()];

        for(int x = 0;x < m_doses.size();x++){
            Dose tempDose = m_doses.get(x);
            m_drugNames[x] = tempDose.getDrug().getDrugName();
            m_roas[x] = tempDose.getRoa().getRoaName();

            int lastIndex = tempDose.getDate().length();
            int hour = Integer.valueOf(tempDose.getDate().substring(11,13));
           // Log.i("testing", "The value for hour is " +  hour);

            int minute = Integer.valueOf(tempDose.getDate().substring(14,lastIndex));
           // Log.i("testing", "The value of minute is " + minute);

            String timeString = "";
            String leHour;
            String leMinute;
            String amOrPm;

            if(hour > 12){
                amOrPm = "PM";
                if(hour - 12 < 10){
                    leHour = "0" + String.valueOf(hour - 12);
                } else {
                    leHour = String.valueOf(hour - 12);
                }
            } else {
                amOrPm = "AM";
                if(hour < 10){
                    leHour = "0" + String.valueOf(hour);
                    if(hour == 0){
                        leHour = "12";
                    }
                } else {
                    leHour = String.valueOf(hour);
                }
            }
            if(minute < 10){
                leMinute = "0" + String.valueOf(minute);
            } else {
                leMinute = String.valueOf(minute);
            }

            timeString = leHour + ":" + leMinute + " " + amOrPm;
            Log.i("testing", "timeString is " + timeString);

           // m_doseTimes[x] = tempDose.getDate().substring(11,lastIndex);
            //Log.i("testing", "'Thde dose time is " + m_doseTimes[x]);
            m_doseTimes[x] = timeString;
        }

        Dose dose = getItem(position);
        String drugName = m_drugNames[position];
        String roa = m_roas[position];
        String doseTime = m_doseTimes[position];

        TextView textDrugName = (TextView) customView.findViewById(R.id.text_drug_name);
        TextView textRoa = (TextView) customView.findViewById(R.id.text_roa_name);
        TextView textDoseTime = (TextView) customView.findViewById(R.id.text_time_dose);

        textDrugName.setText(drugName);
        textRoa.setText(roa);
        textDoseTime.setText(doseTime);

        return customView;
    }
}
