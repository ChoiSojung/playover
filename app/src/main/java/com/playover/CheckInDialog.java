package com.playover;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.playover.broadcast_receivers.AlarmReceiver;
import com.playover.broadcast_receivers.MyNotificationPublisher;
import com.playover.models.Hotel;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.HotelViewModel;
import com.playover.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class CheckInDialog extends Dialog implements View.OnClickListener
{
    private Context mCtx;
    private String mPlaceText;
    private String mAddress;
    private String latitude;
    private String longitude;
    private int positionInArrayList;

    private TextView mPlace;
    private Spinner mDate;
    private TimePicker mTime;
    private Button mBtnConfirm;
    private Spinner mReminder;

    private String dateSelection;
    private int hourSelection;
    private int minuteSelection;
    private String remindSelection;

    //DB
    private AuthUserViewModel authVm = new AuthUserViewModel();
    private HotelViewModel hotelVm = new HotelViewModel();
    private UserViewModel userVm = new UserViewModel();
    private List<Hotel> hotelList = new ArrayList<>();
    private Hotel result;

    public static final String NOTIFICATION_CHANNEL_ID = "1001";
    private final static String default_notification_channel_id="default";


    public CheckInDialog(int pos, @NonNull Context context, Hotel hotel) {
        super(context);
        mCtx = context;
        mPlaceText = hotel.getName();
        mAddress = hotel.getAddress();
        positionInArrayList = pos;
        latitude = hotel.getLat();
        longitude = hotel.getLon();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_dialog);
        String inClass = "inClass";
        String inClassMsg = "inClass CheckIn Dialog";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create View";
        Log.i(onCreate, onCreateMsg);
        mPlace = findViewById(R.id.txtCheckInHotel);
        mPlace.setText(mPlaceText);
        mDate = findViewById(R.id.sprCheckout);
        mReminder = findViewById(R.id.sprReminder);

        mTime = findViewById(R.id.tprCheckOut);

        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                try
                {
                    String enterTry = "Enter try";
                    String enterTryMsg = "Enter Try Block";
                    Log.i(enterTry, enterTryMsg);
                    hourSelection = hourOfDay;
                    minuteSelection = minute;
                }
                catch (Exception ex)
                {
                }
            }
        });

        mBtnConfirm = findViewById(R.id.btnCheckInConfirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            try
            {
                dateSelection = mDate.getSelectedItem().toString();
                remindSelection = mReminder.getSelectedItem().toString();
                String enterTry = "Enter try";
                String enterTryMsg = "Enter Try Block";
                Log.i(enterTry, enterTryMsg);
                //@TODO make it UTC time
                String checkOutTime = getTime(dateSelection, hourSelection, minuteSelection);

                Hotel obj = ListHotels_Fragment.mPlacesList.get(positionInArrayList);

                //////////
                //write db
                //////////
                String userId = authVm.getUser().getUid();
                if(null != userId)
                {

                    result = null;

                    hotelVm.findHotel(obj.getName() + obj.getLat() + obj.getLon(),
                            (DataSnapshot hotel) -> {
                            try
                            {
                                DataSnapshot snapshot = hotel.getChildren().iterator().next();
                                result = snapshot.getValue(Hotel.class);
                            }
                            catch (Exception e)
                            {

                            }

                            if (null != result)
                            {
                                //do update
                                hotelVm.updateExistingHotelWithNewGuest(userId, checkOutTime, result, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                            else
                            {
                                //do insert
                                hotelVm.putGuest(userId, checkOutTime, obj);
                            }
                            //clear listeners if any
                            hotelVm.clear();

                    }, null);
                }
            }
            catch (Exception ex)
            {
               // System.out.println(ex.getMessage());
            }
                //get checkout time
                Calendar checkOut = getCal(dateSelection, hourSelection, minuteSelection);
                //set alarm
                setAlarm(checkOut);

                Calendar checkOutAlert = getCal(dateSelection, hourSelection, minuteSelection);
                int reminderSelectionInMinutes = getReminder(remindSelection);

                checkOutAlert.add(Calendar.MINUTE, -reminderSelectionInMinutes);

                //schedule notification for checkout
                scheduleNotification(getNotification("Your checkout time is " + getTime(dateSelection, hourSelection, minuteSelection)), checkOutAlert);

                doTransition();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }



    private void doTransition()
    {

        Toast.makeText(getContext(),"Checked-In to " + mPlaceText, Toast.LENGTH_LONG).show();
        FragmentTransaction transaction = CheckIn.mFragMgr.beginTransaction();
        Bundle args = new Bundle();
        args.putString("hotel",mPlaceText);
        args.putString("address",mAddress);
        args.putString("checkoutDay", dateSelection);
        args.putString("checkoutTime", hourSelection + ":" + minuteSelection);
        args.putShort("pos",(short)positionInArrayList);

        //go to selected hotel
        SelectedHotel_Fragment selectedHotel = new SelectedHotel_Fragment();
        selectedHotel.setArguments(args);
        transaction.replace(R.id.containerCheckIn, selectedHotel,"SelectedHotel");
        transaction.addToBackStack("SelectedHotel");
        transaction.commit();

        dismiss();
    }

    public static String getTime(String day, int hours, int minutes)
    {
        int days = 0;
        switch (day)
        {
            case "Today": days = 0;
                break;
            case "Tomorrow": days =  1;
                break;
            case "3 Days": days = 2;
                break;
            case "4 Days": days = 3;
                break;
            case "5 Days": days = 4;
                break;
            case "6 Days": days = 5;
                break;
            case "7 Days": days = 6;
                break;
            default:
                break;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + days);
        calendar.set(Calendar.HOUR_OF_DAY, hours);// for 6 hour
        calendar.set(Calendar.MINUTE, minutes);// for 0 min

        return calendar.getTime().toString();
    }

    //helper method to get value of spinner item selected for checkout reminder notifcation
    public static int getReminder(String reminder)
    {
        int minutes=0;

        switch (reminder)
        {
            case "At Time of Checkout": minutes = 0;
                break;
            case "5 Minutes Before": minutes = 5; ;
                break;
            case "10 Minutes Before": minutes = 10;
                break;
            case "15 Minutes Before": minutes = 15;
                break;
            case "1 Hour Before": minutes = 60;
                break;
            case "2 Hours Before": minutes = 120;
                break;
            case "1 Day Before": minutes = 1440;
                break;
            default:
                break;
        }
        return minutes;
    }

    public static Calendar getCal(String day, int hours, int minutes)
    {
        int days = 0;
        switch (day)
        {
            case "Today": days = 0;
                break;
            case "Tomorrow": days =  1;
                break;
            case "3 Days": days = 2;
                break;
            case "4 Days": days = 3;
                break;
            case "5 Days": days = 4;
                break;
            case "6 Days": days = 5;
                break;
            case "7 Days": days = 6;
                break;
            default:
                break;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + days);
        calendar.set(Calendar.HOUR_OF_DAY, hours);// for 6 hour
        calendar.set(Calendar.MINUTE, minutes);// for 0 min

        return calendar;
    }


    //helper method to set alarm for auto checkout task
    public void setAlarm(Calendar c){
        Intent checkOutIntent = new Intent(getContext(), AlarmReceiver.class);
        checkOutIntent.setAction(AlarmReceiver.ACTION_ALARM);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(),
                0, checkOutIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getContext().getApplicationContext().
                getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), alarmIntent);

    }

    //helper method to schedule notification for checkout time
    public void scheduleNotification(Notification notification, Calendar c){
        Intent notifyCheckoutIntent = new Intent(getContext(), MyNotificationPublisher.class);
        notifyCheckoutIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
        notifyCheckoutIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        PendingIntent notifyIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(),
                0, notifyCheckoutIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getContext().getApplicationContext().
                getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), notifyIntent);
    }

    private Notification getNotification(String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), default_notification_channel_id);
        builder.setContentTitle("Scheduled Checkout");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }
}
