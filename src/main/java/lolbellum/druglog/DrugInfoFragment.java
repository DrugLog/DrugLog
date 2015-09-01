package lolbellum.druglog;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;


public  class DrugInfoFragment extends Fragment {

    private static  DrugManager drugManager = Calendar.drugManager;

    private  Drug m_drug;


    public static  DrugInfoFragment newInstance() {
        DrugInfoFragment fragment = new DrugInfoFragment();
        return fragment;
    }

    public DrugInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drug_info, container, false);

        setHasOptionsMenu(true);
       // m_drug = drugManager.getDrugFromName(getArguments().getString("DrugName"));
        Log.i("testing", "Getting arguments");
        String drugName = getArguments().getString("drugname");
        Long drugId = getArguments().getLong("drugid");
        Log.i("testing","getArguments.getString(DrugName) is " + drugName);
        Log.i("testing","getArguments.getLong(drugid) is " + drugId);
        //m_drug = drugManager.getDrugFromName(drugName);
        m_drug = drugManager.getDrug(drugId);

        ImageView drugImageView = (ImageView) rootView.findViewById(R.id.drug_view_image);
        TextView textDrugName = (TextView) rootView.findViewById(R.id.drug_view_drug_name);
         TextView textDrugNicknames = (TextView) rootView.findViewById(R.id.drug_view_nick_names);

        //drugImageView.setImageResource(m_drug.getImageId());
        if(m_drug.getImagePath() == null){
            Log.i("testing","m_drug.getImagePath() is null. Returning");
            return rootView;
        }
        if(!m_drug.getImagePath().equalsIgnoreCase("")){
            Bitmap imageBitmap = BitmapFactory.decodeFile(m_drug.getImagePath());
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 100,100,true);
            drugImageView.setImageBitmap(resizedBitmap);
        } else {
           drugImageView.setImageResource(m_drug.getImageId());
        }
        textDrugName.setText(m_drug.getDrugName());

        //setting up nicknames
        Log.i("testing", "m_drug.getDrugNickNames().size() == " + m_drug.getDrugNickNames().size());
        if(m_drug.getDrugNickNames().size() == 0 || m_drug.getDrugNickNames() == null){
            textDrugNicknames.setText("Nicknames: None");
        } else {
            String nickNames = "";
            for(int x = 0;x < m_drug.getDrugNickNames().size();x++){
                if(x == m_drug.getDrugNickNames().size() - 1){
                    nickNames += m_drug.getDrugNickNames().get(x);
                } else {
                    nickNames += m_drug.getDrugNickNames().get(x) + ", ";
                }
            }
            textDrugNicknames.setText("Nicknames: " + nickNames);
        }


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       // ((DrugView) activity).onSectionAttached(getArguments().getInt("SectionNumber"));
    }

   /* public void clickErowid(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(m_drug.getErowidLink()));
        startActivity(browserIntent);
    }
    public void clickWiki(){

    }
    public void clickReddit(){

    }*/

}
