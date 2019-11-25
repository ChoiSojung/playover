package appdev.playover.viewmodels;
import com.google.android.gms.tasks.OnCompleteListener;
import appdev.playover.datamodels.BuddiesDataModel;
import appdev.playover.models.Person;
import java.util.ArrayList;
import java.util.function.Consumer;

public class BuddiesViewModel {

    private BuddiesDataModel buddiesDataModel;
    private UserViewModel userViewModel;

    public BuddiesViewModel() {
        buddiesDataModel = new BuddiesDataModel();
        userViewModel = new UserViewModel();
    }

    // checks if user has buddies from firebase
    public void getBuddies(String uId, Consumer<ArrayList<Person>> responseCallback) {
        buddiesDataModel.getBuddies(uId,
                (ArrayList<String> buddies) -> {
                    ArrayList<Person> buddyPersons = new ArrayList<Person>();
                    for (int i = 0; i < buddies.size(); i++) {
                        String buddyUid = buddies.get(i);
                        final int I = i;
                        userViewModel.getUser(buddyUid, (Person buddy) -> {
                            buddyPersons.add(buddy);
                            if (I == buddies.size() - 1) {
                                responseCallback.accept(buddyPersons);
                            }
                        });
                    }
                },
                (databaseError -> System.out.println("Error reading Buddies: " + databaseError))
        );
    }

    //updates users buddies
    public void putBuddy(String authUId, String buddyUId, Boolean blocked, OnCompleteListener<Void> activityCallBack) {
        buddiesDataModel.putBuddy(authUId, buddyUId, blocked, activityCallBack);

    }

    //deletes users buddies
    public void deleteBuddy(String authUId, String buddyUId, OnCompleteListener<Void> activityCallBack) {
        buddiesDataModel.deleteBuddy(authUId, buddyUId, activityCallBack);
    }

    //left commented out for the next guy.  only searches by first name.  case sensitive
   /* public void firebaseSearch(String searchText, Consumer<ArrayList<Person>> dataCallback) {
        buddiesDataModel.firebaseSearch(searchText,
                //parse array of collected results into user
                (ArrayList<String> users) -> {
                    //callnack data
                    ArrayList<Person> userResults = new ArrayList<>();
                    for (int i = 0; i < users.size(); i++) {
                        String userSearch = users.get(i);
                        int done = i;
                        userViewModel.getUser(userSearch, (Person user) -> {
                            userResults.add(user);
                            //check if there are any more results
                            if (done == users.size() - 1) {

                                dataCallback.accept(userResults);
                            }
                        });
                    }

                },

                (databaseError -> System.out.println("Error reading search results" + databaseError))
        );
    }*/

}
