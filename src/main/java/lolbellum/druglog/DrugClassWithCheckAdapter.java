package lolbellum.druglog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eliot on 7/31/2015.
 */
public class DrugClassWithCheckAdapter extends ArrayAdapter<DrugClass> {

    private ArrayList<DrugClass> m_drugClasses = new ArrayList<DrugClass>();
    private ArrayList<DrugClass> m_drugClassesChecked = new ArrayList<DrugClass>();
    private boolean[] drugClassChecked;

    public DrugClassWithCheckAdapter(Context context, ArrayList<DrugClass> drugClasses, ArrayList<DrugClass> drugClassesChecked){
        super(context, R.layout.drug_class_with_check_row, drugClasses);
        m_drugClasses = drugClasses;
        m_drugClassesChecked = drugClassesChecked;
        drugClassChecked = new boolean[drugClasses.size()];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.drug_class_with_check_row, parent, false);

        TextView textViewDrugClassName = (TextView) customView.findViewById(R.id.text_view_drug_class_name);
        ImageView imageViewDrugClassImage = (ImageView) customView.findViewById(R.id.image_view_drug_class_image);
        final CheckBox checkBoxDrugClassSelected = (CheckBox) customView.findViewById(R.id.check_box_drug_class_checked);

        final DrugClass drugClass = getItem(position);
        textViewDrugClassName.setText(drugClass.getDrugClassName());
        imageViewDrugClassImage.setImageResource(drugClass.getImageId());

        for(int x = 0;x > m_drugClassesChecked.size();x++){
            DrugClass badCombinationClass = m_drugClassesChecked.get(x);
            if(badCombinationClass.getClassId() == drugClass.getClassId()){
                checkBoxDrugClassSelected.setChecked(true);
                drugClassChecked[position] = true;
            }
        }

        if(drugClassChecked[position]){
            checkBoxDrugClassSelected.setChecked(true);
        } else {
            checkBoxDrugClassSelected.setChecked(false);
        }

        checkBoxDrugClassSelected.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("testing","Check Box clicked on Drug Class");
                        if (checkBoxDrugClassSelected.isChecked()) {
                            drugClassChecked[position] = true;
                        } else {
                            drugClassChecked[position] = false;
                        }
                    }
                }
        );


        return customView;
    }
}
