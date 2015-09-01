package lolbellum.druglog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private DrugManager drugManager = Calendar.drugManager;
    private ArrayList<Notification> m_notifications  = new ArrayList<Notification>();

    public NotificationListAdapter(Context context, ArrayList<Notification> notifications){
        super(context, R.layout.add_drug_listview, notifications);
        m_notifications = notifications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.notification_list_row, parent, false);

        TextView textViewNotificationInfo = (TextView) customView.findViewById(R.id.text_view_notification_info);
        final CheckBox checkBoxNotificationIsActive = (CheckBox) customView.findViewById(R.id.check_box_notification_is_active);

        final Notification notification = getItem(position);
        if(notification.isActive()){
            checkBoxNotificationIsActive.setChecked(true);
        } else {
            checkBoxNotificationIsActive.setChecked(false);
        }

       String timeFrame = notification.getTimeFrame();
        long endTime = notification.getEndTime();
        long timeQuantity = -1;
        if(timeFrame.equalsIgnoreCase("Hour(s)")){
            timeQuantity = TimeUnit.MILLISECONDS.toHours(endTime);
        } else if(timeFrame.equalsIgnoreCase("Day(s)")){
            timeQuantity = TimeUnit.MILLISECONDS.toDays(endTime);
        } else if(timeFrame.equalsIgnoreCase("Week(s)")){
            timeQuantity = TimeUnit.MILLISECONDS.toDays(endTime) / 7;
        } else if(timeFrame.equalsIgnoreCase("Month(s)")){
            timeQuantity = TimeUnit.MILLISECONDS.toDays(endTime) / 30;
        } else if(timeFrame.equalsIgnoreCase("Year(s)")){
            timeQuantity = TimeUnit.MILLISECONDS.toDays(endTime) / 365;
        } else if(timeFrame.equalsIgnoreCase("All Time")){
            timeQuantity = -1;
        }
        if(timeQuantity == -1){
            textViewNotificationInfo.setText("Notify when " + notification.getDoseAmount() + " " + notification.getMetric().getMetricName() + " of " + notification.getDrug().getDrugName() + " is used in.");
        } else {
            textViewNotificationInfo.setText("Notify when " + notification.getDoseAmount() + " " + notification.getMetric().getMetricName() + " of " + notification.getDrug().getDrugName() + " is used in " + timeQuantity + " " + notification.getTimeFrame());
        }
        checkBoxNotificationIsActive.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBoxNotificationIsActive.isChecked()) {
                            notification.setActive(true);
                        } else {
                            notification.setActive(false);
                        }
                        drugManager.addNotification(notification);
                    }
                }
        );

        return customView;
    }


}
