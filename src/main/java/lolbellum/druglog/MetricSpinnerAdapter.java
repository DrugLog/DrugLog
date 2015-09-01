package lolbellum.druglog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MetricSpinnerAdapter extends ArrayAdapter<Metric> {

    private ArrayList<Metric> m_metricList = new ArrayList<Metric>();

    public MetricSpinnerAdapter(Context context, ArrayList<Metric> metricList) {
        super(context,R.layout.support_simple_spinner_dropdown_item, metricList);
        m_metricList = metricList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.drug_class_list_row, parent, false);

        Metric metric = getItem(position);


        return customView;
    }
}
