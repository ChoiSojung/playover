package appdev.playover.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import appdev.playover.CheckIn;
import appdev.playover.viewmodels.AuthUserViewModel;
import appdev.playover.viewmodels.HotelViewModel;
import appdev.playover.viewmodels.UserViewModel;

public class AlarmReceiver extends BroadcastReceiver {
    private static HotelViewModel hotelVm = new HotelViewModel();
    private static AuthUserViewModel authVm = new AuthUserViewModel();
    private static UserViewModel userVm = new UserViewModel();
    private String hotelCheckedInto = null;
    private Boolean isCheckedIn;

    public static final String ACTION_ALARM = "playover.ACTION_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_ALARM.equals(intent.getAction())){
            String userId = authVm.getUser().getUid();
            hotelVm.findHotelCheckedInto(userId, (DataSnapshot hotel) ->
                    {
                        try
                        {
                            DataSnapshot snapshot = hotel.getChildren().iterator().next();
                            hotelCheckedInto = snapshot.getValue().toString();

                            //////////
                            //write db
                            //////////
                            hotelVm.removeGuest(userId, hotelCheckedInto);

                            CheckIn.setHotelCheckedInto(null);

                            hotelVm.clear();
                            authVm.clear();
                            userVm.clear();
                            //show the hotels recyclerview
                            isCheckedIn = false;

                            //Fragment cur = getActivity().getSupportFragmentManager().findFragmentByTag("SelectedHotel");
                            //if(null != cur && cur.isVisible())
                            //{
                            //    getActivity().getSupportFragmentManager().popBackStack();
                            //}

                            //getActivity().recreate();
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                        }
                    },null);
                }
            }


}

