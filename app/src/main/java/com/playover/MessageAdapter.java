package com.playover;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.playover.models.MessageBubble;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageBubble> {

    private Activity activity;
    private List<MessageBubble> messages;

    public MessageAdapter(Activity context, int resource, List<MessageBubble> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0;
        MessageBubble messageBubble = getItem(position);
        int viewType = getItemViewType(position);

        if (messageBubble.myMessage()) {
            layoutResource = R.layout.right_message_bubble;
        } else {
            layoutResource = R.layout.left_message_bubble;
        }

        convertView = inflater.inflate(layoutResource, parent, false);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);

        holder.userName.setText(messageBubble.getUserName());
        holder.timeStamp.setText(messageBubble.getTimeStamp());
        holder.msg.setText(messageBubble.getContent());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public void clear() {
        super.clear();
        messages.clear();
    }

    private class ViewHolder {
        private TextView userName;
        private TextView timeStamp;
        private TextView msg;

        public ViewHolder(View v) {
            userName = v.findViewById(R.id.user_name);
            timeStamp = v.findViewById(R.id.time_stamp);
            msg = v.findViewById(R.id.txt_msg);
        }
    }
}