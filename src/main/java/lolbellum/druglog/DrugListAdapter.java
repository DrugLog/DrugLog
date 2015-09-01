package lolbellum.druglog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrugListAdapter extends ArrayAdapter<Drug>{

    private DrugManager drugManager = Calendar.drugManager;

    private ArrayList<Drug> m_drugs = new ArrayList<Drug>();
    //private int[] m_drugImageIds;



    public DrugListAdapter(Context context, ArrayList<Drug> drugs){
        super(context, R.layout.drug_list_listview, drugs);
        m_drugs = drugs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.drug_list_listview, parent, false);

        final Drug drug = getItem(position);
        TextView drugNameText = (TextView) customView.findViewById(R.id.drug_name_text);
        ImageView drugImage = (ImageView) customView.findViewById(R.id.drug_image);

        drugNameText.setText(drug.getDrugName());

        if(!drug.getImagePath().equalsIgnoreCase("")){
            Bitmap imageBitmap = BitmapFactory.decodeFile(drug.getImagePath());
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 100,100,true);
            drugImage.setImageBitmap(resizedBitmap);
        } else {
            drugImage.setImageResource(drug.getImageId());
        }
        return customView;
    }

  /*  public void clickEditDrug(View view){
        Log.i("testing", "Clicked edit drug in drug list view");
    }

    public void clickDeleteDrug(View view){
        Log.i("testing","Clicked delete drug in drug list view");
    }*/

}
