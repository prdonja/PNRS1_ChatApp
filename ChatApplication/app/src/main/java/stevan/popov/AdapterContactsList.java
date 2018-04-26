package stevan.popov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import stevan.popov.R;


/**
 * Created by Steva on 4/1/2018.
 */

public class AdapterContactsList extends BaseAdapter {
    private Context mContext;
    private ArrayList<ModelForContactsList> mCharacters;

    public AdapterContactsList(Context context) {
        mContext = context;
        mCharacters = new ArrayList<ModelForContactsList>();
    }

    public void AddCharacter(ModelForContactsList model) {
        mCharacters.add(model);
        notifyDataSetChanged();
    }

    public void removeContact(int position) {
        mCharacters.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    public void update(ModelForContactsList[] contacts) {
        mCharacters.clear();
        if (contacts != null) {
            for (ModelForContactsList contact : contacts) {
                mCharacters.add(contact);
            }
        }

        notifyDataSetChanged();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_itame, null);
            final ViewHolder holder = new ViewHolder();
            final ImageView SendPic = (ImageView) convertView.findViewById(R.id.SendPictureImageViewRowItame);
            final View bundleConvertView = convertView;
            holder.image = (ImageView) convertView.findViewById(R.id.SendPictureImageViewRowItame);
            holder.name = (TextView) convertView.findViewById(R.id.NameTextViewRowItame);
            holder.firstChar = (TextView) convertView.findViewById(R.id.FirstCharacterOfNameTextViewRowItame);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE).edit();
                    editor.putInt("receiverId", Integer.parseInt(view.getTag().toString()));
                    editor.apply();

                    Bundle bundle = new Bundle();
                    TextView text = bundleConvertView.findViewById(R.id.NameTextViewRowItame);
                    bundle.putString("NameFromContactsList", text.getText().toString());
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            });
            convertView.setTag(holder);
        }

        ModelForContactsList model = (ModelForContactsList) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_send_button));
        holder.image.setTag(position);
        String Name = model.getmFirstName() + " " + model.getmLastName();
        holder.name.setText(Name);
        String FirstCh = model.getmFirstName().substring(0, 1).toUpperCase();
        holder.firstChar.setText(FirstCh);

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.firstChar.setBackgroundColor(color);

        holder.image.setTag(model.getmContactId());

        return convertView;
    }

    private class ViewHolder {
        public ImageView image = null;
        public TextView name = null;
        public TextView firstChar = null;

    }
}
