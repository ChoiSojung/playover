package com.playover;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.playover.models.Comment;
import com.playover.models.Discount;
import com.playover.models.Person;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.DiscountsViewModel;
import com.playover.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddDiscount_Fragment extends Fragment {
    private Spinner category;
    private Spinner states;
    private EditText business_name;
    private EditText address;
    private EditText city;
    private EditText phone;
    private EditText website;
    private EditText discount_details;
    private EditText first_comment;
    private boolean edit_discount = false;
    private TextView lblcomment;
    private String uId = "";
    private String usersName;
    private String today;

    public AddDiscount_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_discount, container, false);
        setHasOptionsMenu(true);
        category = rootView.findViewById(R.id.category);
        states = rootView.findViewById(R.id.states);
        business_name = rootView.findViewById(R.id.business_name);
        address = rootView.findViewById(R.id.address);
        city = rootView.findViewById(R.id.city);
        phone = rootView.findViewById(R.id.phone);
        website = rootView.findViewById(R.id.website);
        first_comment = rootView.findViewById(R.id.first_comment);
        discount_details = rootView.findViewById(R.id.discount_details);
        lblcomment = rootView.findViewById(R.id.lblcomment);
        //sets sets discount categories
        ArrayAdapter<CharSequence> discountTypeAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.discountCategories, R.layout.spinner_item);
        discountTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        category.setAdapter(discountTypeAdapter);

        //sets sets discount categories
        ArrayAdapter<CharSequence> statesAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.usStates, R.layout.spinner_item);
        statesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        states.setAdapter(statesAdapter);
        Bundle fromDiscountDetail = this.getArguments();
        if (fromDiscountDetail != null) {
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_EDIT))) {
                edit_discount = true;
                first_comment.setVisibility(View.GONE);
                lblcomment.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_BUSINESSNAME))) {
                business_name.setText(fromDiscountDetail.getString(Constants.KEY_BUSINESSNAME));
            }
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_ADDRESS))) {
                address.setText(fromDiscountDetail.getString(Constants.KEY_ADDRESS));
            }
            if (!TextUtils.isEmpty((fromDiscountDetail.getString(Constants.KEY_CITY)))) {
                city.setText(fromDiscountDetail.getString(Constants.KEY_CITY));
            }
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_STATE))) {
                int spinnerPosition = statesAdapter.getPosition(fromDiscountDetail.getString(Constants.KEY_STATE));
                states.setSelection(spinnerPosition);
            }
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_PHONE))) {
                phone.setText(fromDiscountDetail.getString(Constants.KEY_PHONE));
            }
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_WEBSITE))) {
                website.setText(fromDiscountDetail.getString(Constants.KEY_WEBSITE));
            }
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_DETAILS))) {
                discount_details.setText(fromDiscountDetail.getString(Constants.KEY_DETAILS));
            }
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_UID))) {
                uId = fromDiscountDetail.getString(Constants.KEY_UID);
            }
            if (!TextUtils.isEmpty(fromDiscountDetail.getString(Constants.KEY_CATEGORY))) {
                int spinnerPosition = discountTypeAdapter.getPosition(fromDiscountDetail.getString(Constants.KEY_CATEGORY));
                category.setSelection(spinnerPosition);
            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        }
        inflater.inflate(R.menu.add_discount_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveDiscount:
                if (TextUtils.isEmpty(business_name.getText())) {
                    business_name.setError("Business Name Required!");
                }
                if (TextUtils.isEmpty((city.getText()))) {
                    city.setError("City is required!");

                }
                if (states.getSelectedItemId() == 0) {
                    TextView errorText = (TextView) states.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.required);
                }
                if (TextUtils.isEmpty(discount_details.getText())) {
                    discount_details.setError("Please enter discount details!");
                }
                if (category.getSelectedItemId() == 0) {
                    TextView errorText = (TextView) category.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.required);
                }
                if (TextUtils.isEmpty(address.getText())) {
                    address.setError("Address is a required field!");
                }
                if (!TextUtils.isEmpty(business_name.getText()) &&
                        states.getSelectedItemId() != 0 && !TextUtils.isEmpty(discount_details.getText()) &&
                        category.getSelectedItemId() != 0 && !TextUtils.isEmpty(address.getText())) {
                    //create discount and treat strings
                    //uid is assigned after we push to db
                    DiscountsViewModel discVm = new DiscountsViewModel();
                    ProgressDialog discountDialog = new ProgressDialog(getActivity());
                    AuthUserViewModel authVm = new AuthUserViewModel();
                    UserViewModel userVm = new UserViewModel();
                    userVm.getUser(authVm.getUser().getUid(),
                            (Person user) -> {
                                if (user != null) {
                                    usersName = user.getFirstName().toUpperCase() + " "
                                            + user.getLastName().toUpperCase().substring(0, 1) + ".";
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                                    Date date = new Date();
                                    Comment comment = null;
                                    today = dateFormat.format(date);
                                    if (!edit_discount) {
                                        discountDialog.setTitle("Adding Discount...");
                                        discountDialog.show();
                                        if (first_comment.getText().toString().trim().length() != 0) {
                                            comment = new Comment("", today, first_comment.getText().toString().toUpperCase().trim(), usersName, user.getuId());
                                        }

                                        Discount newDiscount = new Discount("", business_name.getText().toString().trim().toUpperCase(), address.getText().toString().trim().toUpperCase(),
                                                city.getText().toString().trim().toUpperCase(), states.getSelectedItem().toString(), phone.getText().toString().trim(),
                                                website.getText().toString().trim().toLowerCase(), discount_details.getText().toString().trim().toUpperCase(), category.getSelectedItem().toString(),
                                                comment, authVm.getUser().getUid(), usersName, today);
                                        discVm.createNewDiscount(newDiscount, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    discountDialog.dismiss();
                                                    if (getActivity() != null) {
                                                        Toast.makeText(getActivity(), "Discount Added!", Toast.LENGTH_SHORT).show();
                                                        getActivity().onBackPressed();
                                                    }

                                                } else {
                                                    if (task.getException() != null) {
                                                        discountDialog.dismiss();
                                                        if (getActivity() != null) {
                                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        discountDialog.setTitle("Updating Discount...");
                                        discountDialog.show();
                                        Discount newDiscount = new Discount(uId, business_name.getText().toString().trim().toUpperCase(), address.getText().toString().trim().toUpperCase(),
                                                city.getText().toString().trim().toUpperCase(), states.getSelectedItem().toString(), phone.getText().toString().trim(),
                                                website.getText().toString().trim().toLowerCase(), discount_details.getText().toString().trim().toUpperCase(), category.getSelectedItem().toString(),
                                                comment, authVm.getUser().getUid(), usersName, today);
                                        discVm.updateDiscount(newDiscount, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    discountDialog.dismiss();
                                                    if (getContext() != null) {
                                                        hideSoftKeyboard(getContext());
                                                    }
                                                    if(getActivity()!=null) {
                                                        Toast.makeText(getActivity(), "Discount Updated!", Toast.LENGTH_SHORT).show();
                                                    }
                                                    if (getActivity() != null) {
                                                        Bundle b = new Bundle();
                                                        b.putString(Constants.KEY_BUSINESSNAME, business_name.getText().toString().trim().toUpperCase());
                                                        b.putString(Constants.KEY_ADDRESS, address.getText().toString().trim().toUpperCase());
                                                        b.putString(Constants.KEY_DETAILS, discount_details.getText().toString().trim().toUpperCase());
                                                        b.putString(Constants.KEY_WEBSITE, website.getText().toString().trim().toLowerCase());
                                                        b.putString(Constants.KEY_PHONE, phone.getText().toString().trim());
                                                        b.putString(Constants.KEY_CITY, city.getText().toString().trim().toUpperCase());
                                                        b.putString(Constants.KEY_STATE, states.getSelectedItem().toString());
                                                        b.putString(Constants.KEY_CATEGORY, category.getSelectedItem().toString());
                                                        b.putString(Constants.KEY_UID, uId);
                                                        b.putString(Constants.KEY_DISPLAY_NAME, usersName);
                                                        b.putString(Constants.KEY_LAST_UPDATE, today);
                                                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                        if (fragmentManager != null) {
                                                            fragmentManager.popBackStack();
                                                            DiscountDetailFragment updated = (DiscountDetailFragment) fragmentManager.findFragmentByTag("DiscountDetail");
                                                            if (updated != null) {
                                                                updated.setArguments(b);
                                                            } else {
                                                                if (getActivity() != null) {
                                                                    Toast.makeText(getActivity(), "Unable to update discount!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        } else {
                                                            if (getActivity() != null) {
                                                                Toast.makeText(getActivity(), "Unable to update discount!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    if (task.getException() != null) {
                                                        discountDialog.dismiss();
                                                        if (getActivity() != null) {
                                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                } else {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Please fill in the required fields!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }


    //hide soft keyboard method
    public static void hideSoftKeyboard(Context context) {
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) context).getCurrentFocus();
        if (v == null) {
            return;
        }
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}