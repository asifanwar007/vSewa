package com.example.vsewa.NavigationButton.ui.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vsewa.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView addr;
        public MyViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.itemCheckName);
//            addr = (TextView) v.findViewById(R.id.itemCheckAddr);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    Context context;
    public MyAdapter(Context context, ArrayList<String> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        Log.d("Viwod holder", "Inside on create upper");
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        Log.i("ITAG1", "View v created");
        Toast.makeText(context, "created", Toast.LENGTH_LONG).show();
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset.get(position));
        Log.d("TAG3--", mDataset.get(position));
//        holder.view.setText(mDataset.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "helo", Toast.LENGTH_LONG).show();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}