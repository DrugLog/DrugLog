package lolbellum.druglog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.CheckBox;

public class AddDrugAdapter extends ArrayAdapter<String>{

    private DrugManager drugManager = Calendar.drugManager;

    private int[] m_drugImageIds;

    public AddDrugAdapter(Context context, String[] drugNames) {
        super(context,R.layout.add_drug_listview, drugNames);
    }

    public AddDrugAdapter(Context context, String[] drugNames, int[] drugImageIds) {
        super(context,R.layout.add_drug_listview, drugNames);
        m_drugImageIds = drugImageIds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.add_drug_listview, parent, false);

        String drugName = getItem(position);
        int drugImageId = m_drugImageIds[position];
        TextView drugNameText = (TextView) customView.findViewById(R.id.drug_name_text);
        ImageView drugImage = (ImageView) customView.findViewById(R.id.drug_image);
        CheckBox addToClass = (CheckBox) customView.findViewById(R.id.checkbox_add_drug);





        drugNameText.setText(drugName);
        //default image
        drugImage.setImageResource(drugImageId);
        return customView;
    }



}
