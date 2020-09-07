package com.example.vsewa.NavigationButton.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vsewa.Messenger.MessengerActivity;
import com.example.vsewa.NavigationButton.BottomNavigatioActivity;
import com.example.vsewa.NavigationButton.ui.settings.SettingsFragment;
import com.example.vsewa.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

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
        Button acceptButton;
        public ViewHolder(View convertView){
            txtName = (TextView) convertView.findViewById(R.id.name);
            txtAgeGender = (TextView) convertView.findViewById(R.id.itemCheckAge);
            txtAddress = (TextView) convertView.findViewById(R.id.itemCheckAddr);
            info = (ImageView) convertView.findViewById(R.id.itemCheckImage);
            acceptButton = (Button) convertView.findViewById(R.id.btItemCheckAccept);
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

        Log.d("Onclik", "s " +  v.getId());

        switch (v.getId())
        {
            case R.id.btItemCheckAccept:
                Snackbar.make(v, "Gender " +dataModel.getGender(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final DataModel dataModel = getItem(position);
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

        Log.d("NameTag",  dataModel.getImageLink());
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtAgeGender.setText(dataModel.getAge() + ", " + dataModel.getGender());
        viewHolder.txtAddress.setText(dataModel.getAddress());
        try {
//            Glide.with(mContext)
////                        .using(new FirebaseImageLoader())
//                    .load(dataModel.getImageLink())
//                    .into(viewHolder.info);

            Picasso.get().load(dataModel.getImageLink()).resize(500,300).into(viewHolder.info);
        }catch (Exception e){
            Log.d("Image Glide", e.getStackTrace().toString());
        }




        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        viewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, dataModel.getAge() + dataModel.getGender(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MessengerActivity.class);
                mContext.startActivity(intent);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }
}