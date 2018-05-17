package stevan.popov;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AdapterMessageList extends BaseAdapter {
    private Context mContext;
    private ArrayList<ModelForMessageList> mCharacters;

    public AdapterMessageList(Context context) {
        mContext = context;
        mCharacters = new ArrayList<ModelForMessageList>();
    }

    public void AddCharacter(ModelForMessageList model) {
        mCharacters.add(model);
        notifyDataSetChanged();
    }

    public void update(ModelForMessageList[] messages) {
        mCharacters.clear();
        if (messages != null) {
            for (ModelForMessageList message : messages) {
                mCharacters.add(message);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = mCharacters.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void RemoveMessage(ModelForMessageList model) {
        mCharacters.remove(model);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_message_list, null);
            ViewHolder holder = new ViewHolder();
            holder.messageText = (TextView) convertView.findViewById(R.id.MessageTextRowItemMessageList);
            convertView.setTag(holder);

        }

        ModelForMessageList model = (ModelForMessageList) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.messageText.setText(model.getmMessage());

        SharedPreferences prefs = mContext.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String ActiveUserId = prefs.getString("ActiveUser", null);
        String SenderId = prefs.getString("receiverId", null);


        if (!SenderId.equals(model.getmSender_id())) {
            holder.messageText.setBackgroundColor(Color.parseColor("#cbd3ce"));
            holder.messageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            final LayoutParams layoutparams = (LayoutParams) holder.messageText.getLayoutParams();
            layoutparams.setMargins(200, 5, 20, 5);
            holder.messageText.setLayoutParams(layoutparams);
        } else {
            holder.messageText.setBackgroundColor(Color.parseColor("#42f47d"));
            holder.messageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

            final LayoutParams layoutparams = (LayoutParams) holder.messageText.getLayoutParams();
            layoutparams.setMargins(20, 5, 200, 5);
            holder.messageText.setLayoutParams(layoutparams);
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView messageText = null;
    }

}
