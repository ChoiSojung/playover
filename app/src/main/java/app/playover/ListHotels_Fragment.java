package app.playover;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.playover.R;

import app.playover.models.Hotel;

import java.util.ArrayList;
import java.util.List;

public class ListHotels_Fragment extends Fragment{

    //here sdk data
    static List<Hotel> mPlacesList = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_hotels, container, false);
        String inClass = "inClass";
        String inClassMsg = "inClass ListHotels Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create View";
        Log.i(onCreate, onCreateMsg);
        recyclerView = v.findViewById(R.id.recycler_view_hotels);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.thread_divider));
        recyclerView.addItemDecoration(divider);
        return v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView description;
        public TextView distance;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            name =  itemView.findViewById(R.id.list_title);
            description =  itemView.findViewById(R.id.list_desc);
            distance = itemView.findViewById(R.id.list_dist);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CheckInDialog checkIn = new CheckInDialog(getAdapterPosition(), v.getContext(), mPlacesList.get(getAdapterPosition()/*mPlacesList.get(getAdapterPosition()).getName(), mPlacesList.get(getAdapterPosition()).getAddress()*/));
            checkIn.show();
        }
    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = mPlacesList.size();
        private final Context c;

        public ContentAdapter(Context context) {
            c = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater l = LayoutInflater.from(parent.getContext());
            View v = l.inflate(R.layout.item_list, parent, false);
            return new ViewHolder( l, parent );
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            try
            {
                holder.name.setText(mPlacesList.get(position).getName());
                holder.description.setText(mPlacesList.get(position).getAddress());
                holder.distance.setText(mPlacesList.get(position).getDistance());
            }
            catch (NullPointerException npe)
            {

            }
            catch (IndexOutOfBoundsException iobe)
            {

            }
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
