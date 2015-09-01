package lolbellum.druglog;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrugclassListAdapter extends ArrayAdapter<DrugClass>{

    private DrugManager drugManager = Calendar.drugManager;

    private ArrayList<DrugClass> m_drugClasses = new ArrayList<DrugClass>();

   /* public DrugclassListAdapter(Context context, String[] drugNames) {
        super(context,R.layout.add_drug_listview, drugNames);
    }

    public DrugclassListAdapter(Context context, String[] drugNames, int[] drugImageIds) {
        super(context,R.layout.add_drug_listview, drugNames);
        m_drugImageIds = drugImageIds;
    }*/

    public DrugclassListAdapter(Context context, ArrayList<DrugClass> drugClasses){
        super(context, R.layout.add_drug_listview, drugClasses);
        m_drugClasses = drugClasses;
      //  m_drugClasses.add(new DrugClass("All Drugs", drugManager.getDrugs()));
       // for(int x = 0; x < drugClasses.size();x++){
           // m_drugClasses.add(x, drugClasses.get(x - 1));
        //    m_drugClasses.add(drugClasses.get(x));
       // }

        if(!getItem(0).getDrugClassName().equalsIgnoreCase("All Drugs")){
            insert(new DrugClass("All Drugs", drugManager.getDrugs(), R.drawable.ic_alldrugs), 0);
            notifyDataSetChanged();
        }
    }

    public void add(DrugClass drugClass){

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.drug_class_list_row, parent, false);

        String[] drugClassNames = new String[m_drugClasses.size() + 1];
        int[] drugImageIds = new int[m_drugClasses.size() + 1];

        //String[] drugClassNames = new String[drugManager.getDrugClasses().size() + 1];
       // int[] drugImageIds = new int[drugManager.getDrugClasses().size() + 1];

        drugClassNames[0] = "All Drugs";
        drugImageIds[0] = R.drawable.ic_alldrugs;

       /* for(int x = 0;x < m_drugClasses.size();x++){
            drugClassNames[x] = m_drugClasses.get(x).getDrugClassName();
            drugImageIds[x] = m_drugClasses.get(x).getImageId();
        }*/

        Log.i("testing","There are " + drugManager.getDrugClasses().size() + " drug classes");
        Log.i("testing","m_drugClasses has " + m_drugClasses.size() + " classes in it");
        Log.i("testing","The Length of drugClassNames[] is " + drugClassNames.length);

        for(int x = 1;x < m_drugClasses.size() + 1;x++){
            drugClassNames[x] = m_drugClasses.get(x - 1).getDrugClassName();
            drugImageIds[x] = m_drugClasses.get(x - 1).getImageId();
        }

        //DrugClass drugClass = getItem(position);
        String drugClassName = drugClassNames[position];
        int drugImageId = drugImageIds[position];
        TextView drugNameText = (TextView) customView.findViewById(R.id.drug_class_name_text);
        ImageView drugImage = (ImageView) customView.findViewById(R.id.drug_class_image);

      //  drugNameText.setText(drugClassName);
        //default image
       // if(position == 0) {
        //    drugImage.setImageResource(R.drawable.ic_alldrugs);
       // } else {
          //  drugImage.setImageResource(drugImageId);

       // if(position == 0){
           // drugNameText.setText("All Drugs");
           // drugImage.setImageResource(R.drawable.ic_alldrugs);
       // } else {
        DrugClass drugClass = null;
       // if(position == 0){
         //    drugClass = getItem(position);

      //  } else {
             drugClass = getItem(position);
            drugNameText.setText(drugClass.getDrugClassName());
            drugImage.setImageResource(drugClass.getImageId());
       // }



       // }

       //}
        return customView;
    }
}
