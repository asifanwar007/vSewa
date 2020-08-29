package com.example.vsewa.NavigationButton.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vsewa.NavigationButton.ui.settings.SettingsFragment;
import com.example.vsewa.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtAgeGender;
        TextView txtAddress;
        ImageView info;
        public ViewHolder(View convertView){
            txtName = (TextView) convertView.findViewById(R.id.name);
            txtAgeGender = (TextView) convertView.findViewById(R.id.itemCheckAge);
            txtAddress = (TextView) convertView.findViewById(R.id.itemCheckAddr);
            info = (ImageView) convertView.findViewById(R.id.itemCheckImage);
        }
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.item_history, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = (View) inflater.inflate(R.layout.item_history, parent, false);

            viewHolder = new ViewHolder(convertView);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.itemCheckName);
            viewHolder.txtAgeGender = (TextView) convertView.findViewById(R.id.itemCheckAge);
            viewHolder.txtAddress = (TextView) convertView.findViewById(R.id.itemCheckAddr);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.itemCheckImage);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        Log.d("NameTag", dataModel.getName() + " " + dataModel.getGender());
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtAgeGender.setText(dataModel.getAge() + ", " + dataModel.getGender());
        viewHolder.txtAddress.setText(dataModel.getAddress());
        Glide.with(mContext)
//                        .using(new FirebaseImageLoader())
                .load(dataModel.getImageLink())
                .into(viewHolder.info);
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}