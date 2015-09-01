package lolbellum.druglog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DrugListEditDeleteAdapter extends ArrayAdapter<Drug>{

    private DrugManager drugManager = Calendar.drugManager;

    private ArrayList<Drug> m_drugs = new ArrayList<Drug>();
    //private int[] m_drugImageIds;



    public DrugListEditDeleteAdapter(Context context, ArrayList<Drug> drugs){
        super(context, R.layout.drug_list_listview_edit_delete, drugs);
        m_drugs = drugs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.drug_list_listview_edit_delete, parent, false);

        final Drug drug = getItem(position);
        TextView drugNameText = (TextView) customView.findViewById(R.id.drug_name_text);
        ImageView drugImage = (ImageView) customView.findViewById(R.id.drug_image);
        ImageView buttonEditDrug = (ImageView) customView.findViewById(R.id.button_edit_drug);
        ImageView buttonDeleteDrug = (ImageView) customView.findViewById(R.id.button_delete_drug);

        drugNameText.setText(drug.getDrugName());

        buttonEditDrug.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), EditDrug.class);
                        i.putExtra("DrugId", drug.getDrugId());
                        getContext().startActivity(i);
                    }
                }
        );

        buttonDeleteDrug.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                        alertDialog.setTitle("Confirm deletion");
                        alertDialog.setMessage("Are you sure you want to delete " + drug.getDrugName() + "?");
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("testing", "No");
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("testing", "Yes");
                                drugManager.removeDrug(drug);
                                Toast.makeText(alertDialog.getContext(), "Drug " + drug.getDrugName() + " deleted", Toast.LENGTH_LONG).show();
                            }
                        });
                        alertDialog.show();
                    }
                }
        );


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
