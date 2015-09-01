package lolbellum.druglog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class DoseFragment extends Fragment {
    private static  DrugManager drugManager = Calendar.drugManager;

    ListView dosesListView;
    ListAdapter dosesList;

    private  Drug m_drug;
    private View m_view;


    public static  DoseFragment newInstance() {
        DoseFragment fragment = new DoseFragment();
        return fragment;
    }

    public DoseFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dose, container, false);
        m_view = rootView;

        m_drug = drugManager.getDrug(getArguments().getLong("drugid"));

        TextView textViewDrugName = (TextView) rootView.findViewById(R.id.doses_fragment_drug_name);
        dosesListView = (ListView) rootView.findViewById(R.id.notification_list);

        textViewDrugName.setText(m_drug.getDrugName());

        updateDoses(rootView);

       dosesListView.setOnItemClickListener(
               new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Intent i = new Intent(view.getContext(),DoseView.class);
                       Dose dose = (Dose) dosesListView.getItemAtPosition(position);
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

        return rootView;
    }

    public void updateDoses(View view){
        TextView emptyList = (TextView) view.findViewById(R.id.emptyList);

        ArrayList<Dose> doses = new ArrayList<Dose>();

        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            if(tempDose.getDrug().getDrugId() == m_drug.getDrugId()){
                doses.add(tempDose);
            }
        }

        if(doses.size() > 0){

            if(emptyList.getVisibility() == View.VISIBLE){
                emptyList.setVisibility(View.INVISIBLE);
            }

            if(dosesListView.getVisibility() == View.INVISIBLE){
                dosesListView.setVisibility(View.VISIBLE);
            }
        } else {
            if (dosesListView.getVisibility() == View.VISIBLE) {
                dosesListView.setVisibility(View.INVISIBLE);
            }

            if (emptyList.getVisibility() == View.INVISIBLE) {
                emptyList.setVisibility(View.VISIBLE);
            }
            return;
        }

        dosesList = new DoseListAdapter(view.getContext(),doses);
        dosesListView.setAdapter(dosesList);

    }



    public View getView(){
        return m_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ((DrugView) activity).onSectionAttached(getArguments().getInt("SectionNumber"));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
       // inflater.inflate(R.menu.menu_do, menu);
    }
}
