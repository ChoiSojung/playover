package appdev.playover.viewmodels;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import appdev.playover.datamodels.DiscountsDataModel;
import appdev.playover.models.Comment;
import appdev.playover.models.Discount;

import java.util.ArrayList;
import java.util.function.Consumer;

public class DiscountsViewModel {

    private DiscountsDataModel discountsDataModel;

    //constructor.  view model instantiates a data model
    public DiscountsViewModel() {
        discountsDataModel = new DiscountsDataModel();
    }

    //view model create a new discount entry
    public void createNewDiscount(Discount discount, OnCompleteListener<Void> activityCallBack) {
        discountsDataModel.createNewDiscount(discount, activityCallBack);

    }

    //view model update discount entry
    public void updateDiscount(Discount discount, OnCompleteListener<Void> activityCallBack) {
        discountsDataModel.updateDiscount(discount, activityCallBack);

    }

    //returning all discounts in db for now.  will revise with query once I have more data
    public void getDiscounts(String State, String City, Consumer<ArrayList<Discount>> responseCallback) {
        discountsDataModel.getDiscounts(State, City,
                (DataSnapshot dataSnapshot) -> {
                    ArrayList<Discount> discounts = new ArrayList<>();
                    for (DataSnapshot discountSnapshot : dataSnapshot.getChildren()) {
                        //we don't want comments in this call
                        String businessName = discountSnapshot.child("businessName").getValue(String.class);
                        String address = discountSnapshot.child("address").getValue(String.class);
                        String city = discountSnapshot.child("city").getValue(String.class);
                        String state = discountSnapshot.child("state").getValue(String.class);
                        String phone = discountSnapshot.child("phone").getValue(String.class);
                        String website = discountSnapshot.child("website").getValue(String.class);
                        String details = discountSnapshot.child("details").getValue(String.class);
                        String category = discountSnapshot.child("category").getValue(String.class);
                        String uId = discountSnapshot.child("uid").getValue(String.class);
                        String posterName = discountSnapshot.child("posterName").getValue(String.class);
                        String lastUpdated = discountSnapshot.child("lastUpdate").getValue(String.class);
                        //we dont care about comments or author uids here
                        Discount discount = new Discount(uId, businessName, address, city, state, phone, website, details, category,
                                null, null, posterName,lastUpdated);
                        discounts.add(discount);
                    }
                    responseCallback.accept(discounts);
                },
                (databaseError -> System.out.println("Error reading Matches: " + databaseError))
        );
    }

    //viewmodel get all comments for a particular discount.
    public void getDiscountComments(String state, String city, String uId, Consumer<ArrayList<Comment>> responseCallback) {
        discountsDataModel.getDiscountComments(state, city, uId,
                (DataSnapshot dataSnapshot) -> {
                    ArrayList<Comment> comments = new ArrayList<>();
                    for (DataSnapshot discountSnapshot : dataSnapshot.getChildren()) {
                        Comment comment = new Comment(discountSnapshot.getKey(),
                                discountSnapshot.child("commentDate").getValue(String.class),
                                discountSnapshot.child("commentDetail").getValue(String.class),
                                discountSnapshot.child("commentPoster").getValue(String.class),
                                discountSnapshot.child("posterUId").getValue(String.class));
                        comments.add(comment);
                    }
                    responseCallback.accept(comments);
                },
                (databaseError -> System.out.println("Error reading Comments: " + databaseError))
        );

    }

    //delete discount viewmodel
    public void deleteDiscount(String state, String city, String uId, OnCompleteListener<Void> activityCallBack) {
        discountsDataModel.deleteDiscount(state, city, uId, activityCallBack);
    }

    //add comment viewmodel
    public void addComment(String state, String city, String uId, Comment comment, OnCompleteListener<Void> activityCallBack) {
        discountsDataModel.addComment(state, city, uId, comment, activityCallBack);
    }

    //clear listeners viewmodel
    public void clear() {
        discountsDataModel.clear();
    }
}
