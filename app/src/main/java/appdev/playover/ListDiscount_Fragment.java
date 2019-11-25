package appdev.playover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import appdev.playover.R;

import appdev.playover.models.Discount;
import appdev.playover.viewmodels.DiscountsViewModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListDiscount_Fragment extends Fragment {

    private ListDiscount_Fragment.ContentAdapter adapter;
    static String cityName;
    static String stateName;
    private TextView deviceLocation;
    private TextView noDiscounts;
    private BottomNavigationView bottomNavigation;
    static String filter = "All";

    public ListDiscount_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateData(ArrayList<Discount> updatedData, String city, String state) {
        cityName = city;
        stateName = state;
        if (cityName != null && stateName != null) {
            String cityAndState = "Discounts for " + cityName + ", " + stateName.toUpperCase();
            if (deviceLocation != null)
                deviceLocation.setText(cityAndState);
        } else {
            String noLocation = "Unable to find device location!";
            if (deviceLocation != null)
                deviceLocation.setText(noLocation);
        }
        if (adapter != null) {
            adapter.updateAdapter(updatedData);
            setNoDiscounts();
        }

    }

    public void setNoDiscounts() {
            if (adapter.getItemCount() == 0) {
                noDiscounts.setVisibility(View.VISIBLE);
            } else {
                noDiscounts.setVisibility(View.GONE);
            }

    }

    //allows for more than three items in bottom navigation
    //without enabling shift mode
    @SuppressLint("RestrictedApi")
    public void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (Exception ex) {
            Log.e("error: ", ex.getMessage());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_discount, container, false);
        setHasOptionsMenu(true);
        deviceLocation = rootView.findViewById(R.id.device_location);
        RecyclerView recyclerViewDiscounts = rootView.findViewById(R.id.recycler_view_discounts);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerViewDiscounts.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.thread_divider));
        recyclerViewDiscounts.addItemDecoration(divider);
        noDiscounts = rootView.findViewById(R.id.noDiscounts);
        bottomNavigation = rootView.findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.all_discounts:
                        if (adapter != null) {
                            adapter.updateFilter("All");
                            setNoDiscounts();
                        }
                        break;
                    case R.id.food_discounts:
                        if (adapter != null) {
                            adapter.updateFilter("Food");
                            setNoDiscounts();
                        }
                        break;
                    case R.id.fun_discounts:
                        if (adapter != null) {
                            adapter.updateFilter("Fun");
                            setNoDiscounts();
                        }
                        break;
                    case R.id.hotel_car_discounts:
                        if (adapter != null) {
                            adapter.updateFilter("Hotel/Car");
                            setNoDiscounts();
                        }
                        break;
                }
                return true;
            }
        });
        removeShiftMode(bottomNavigation);
        FragmentManager fragmentManager = getFragmentManager();
        adapter = new ListDiscount_Fragment.ContentAdapter(recyclerViewDiscounts.getContext(), fragmentManager);
        recyclerViewDiscounts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDiscounts.setAdapter(adapter);
        recyclerViewDiscounts.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        }
        inflater.inflate(R.menu.list_discount_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addDiscount:
                AddDiscount_Fragment newDiscount = new AddDiscount_Fragment();
                if (getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.containerDiscounts, newDiscount, "AddDiscount");
                    transaction.addToBackStack("AddDiscount");
                    transaction.commit();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private int LENGTH;
        private final Context c;
        private ArrayList<Discount> uDiscounts;
        private ArrayList<Discount> fDiscounts;
        private FragmentManager fragManager;

        private ContentAdapter(Context context, FragmentManager fragmentManager) {
            fragManager = fragmentManager;
            uDiscounts = new ArrayList<Discount>();
            fDiscounts = new ArrayList<Discount>();
            LENGTH = 0;
            c = context;
        }

        //update adapter with new information
        private void updateAdapter(ArrayList<Discount> updatedDiscounts) {
            if (updatedDiscounts != null && updatedDiscounts.size() > 0) {
                uDiscounts = updatedDiscounts;
                fDiscounts.clear();
                for (Discount d : updatedDiscounts) {
                    if (filter.compareTo("All") == 0) {
                        fDiscounts.add(d);
                    } else if (d.getCategory() != null && d.getCategory().trim().compareTo(filter) == 0) {
                        fDiscounts.add(d);
                    }
                }
                LENGTH = fDiscounts.size();
                notifyDataSetChanged();
            }
        }

        //update adapter with filter change
        private void updateFilter(String Filter) {
            fDiscounts.clear();
            filter = Filter;
            int count = 0;
            for (Discount d : uDiscounts) {
                if (filter.compareTo("All") == 0) {
                    fDiscounts.add(d);
                    count++;

                } else if (d.getCategory().trim().compareTo(Filter) == 0) {
                    fDiscounts.add(d);
                    count++;
                }
            }
            LENGTH = fDiscounts.size();
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layout = LayoutInflater.from(parent.getContext());
            return new ViewHolder(layout, parent, fragManager);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            try {
                holder.name.setText(fDiscounts.get(position).getBusinessName());
                holder.details.setText(fDiscounts.get(position).getDetails());
                holder.address.setText(fDiscounts.get(position).getAddress());
                holder.city.setText(fDiscounts.get(position).getCity());
                holder.state.setText(fDiscounts.get(position).getState());
                holder.website.setText(fDiscounts.get(position).getWebsite());
                holder.phone.setText(fDiscounts.get(position).getPhone());
                holder.category.setText(fDiscounts.get(position).getCategory());
                holder.uId.setText(fDiscounts.get(position).getUId());
                holder.nameList.setText(fDiscounts.get(position).getPosterName());
                holder.lastUpdatedList.setText(fDiscounts.get(position).getLastUpdate());
            } catch (Exception ex) {
                Log.e("onBindViewHolder Error:", ex.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView details;
        private TextView address;
        private TextView website;
        private TextView phone;
        private TextView city;
        private TextView state;
        private TextView category;
        private TextView uId;
        private TextView nameList;
        private TextView lastUpdatedList;
        private FragmentManager fragmentManager;


        private ViewHolder(LayoutInflater inflater, ViewGroup parent, FragmentManager fragManager) {
            super(inflater.inflate(R.layout.item_discount_list, parent, false));
            name = itemView.findViewById(R.id.list_title);
            details = itemView.findViewById(R.id.list_desc);
            address = itemView.findViewById(R.id.list_address);
            website = itemView.findViewById(R.id.websiteList);
            phone = itemView.findViewById(R.id.phoneList);
            city = itemView.findViewById(R.id.cityList);
            state = itemView.findViewById(R.id.stateList);
            category = itemView.findViewById(R.id.categoryList);
            uId = itemView.findViewById(R.id.uIdList);
            nameList = itemView.findViewById(R.id.nameList);
            lastUpdatedList = itemView.findViewById(R.id.lastUpdatedList);
            fragmentManager = fragManager;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle b = new Bundle();
            b.putString(Constants.KEY_BUSINESSNAME, name.getText().toString());
            b.putString(Constants.KEY_ADDRESS, address.getText().toString());
            b.putString(Constants.KEY_DETAILS, details.getText().toString());
            b.putString(Constants.KEY_WEBSITE, website.getText().toString());
            b.putString(Constants.KEY_PHONE, phone.getText().toString());
            b.putString(Constants.KEY_CITY, city.getText().toString());
            b.putString(Constants.KEY_STATE, state.getText().toString());
            b.putString(Constants.KEY_CATEGORY, category.getText().toString());
            b.putString(Constants.KEY_UID, uId.getText().toString());
            b.putString(Constants.KEY_DISPLAY_NAME, nameList.getText().toString());
            b.putString(Constants.KEY_LAST_UPDATE, lastUpdatedList.getText().toString());
            DiscountDetailFragment discountDetails = new DiscountDetailFragment();
            discountDetails.setArguments(b);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.containerDiscounts, discountDetails, "DiscountDetail");
            transaction.addToBackStack("DiscountDetail");
            transaction.commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        DiscountsViewModel discVm = new DiscountsViewModel();
        discVm.clear();
    }

}
