package com.example.vsewa.NavigationButton.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.vsewa.NavigationButton.ui.dashboard.CustomAdapter;
import com.example.vsewa.NavigationButton.ui.dashboard.DataModel;
import com.example.vsewa.R;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private ListView listView;
    private ArrayList<DataModel> myDataset;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        listView = root.findViewById(R.id.listViewVolun);
        myDataset = new ArrayList<>();
        notificationsViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ArrayList<DataModel>>() {
            @Override
            public void onChanged(ArrayList<DataModel> dataModels) {
                myDataset = dataModels;
                if(dataModels != null) {
//                    listView.removeAllViews();
                    ArrayAdapter<DataModel> arrayAdapter = new CustomAdapter(dataModels, getContext());
                    listView.setAdapter(arrayAdapter);
                }else {
                    ArrayList<String> ar = new ArrayList<>();
                    ar.add("No Value");
                    ar.add("No Value");
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ar);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });
        return root;
    }
}