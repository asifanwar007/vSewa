package com.example.vsewa.NavigationButton.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsewa.Dialogs.volunteerRequiredDialogs;
import com.example.vsewa.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements volunteerRequiredDialogs.volunteerRequiredDialogListener {

    private DashboardViewModel dashboardViewModel;
    private String volunteerGender, reasonRequired;
    private int noOfVolunteerRequired;
    private TextView tvGender, tvReason, tvNumberVolunteer;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataModel> myDataset;
    private ListView listView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        tvGender = root.findViewById(R.id.tvGender);
//        tvReason = root.findViewById(R.id.tvShowReason);
//        tvNumberVolunteer = root.findViewById(R.id.tvNumberOfVolunteer);

//        tvGender.setText(volunteerGender);
//        tvReason.setText(reasonRequired);
//        tvNumberVolunteer.setText(noOfVolunteerRequired + "");
        listView = root.findViewById(R.id.listViewVolun);
        myDataset = new ArrayList<>();
       dashboardViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ArrayList<DataModel>>() {
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
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Toast.makeText(getContext(), "helo", Toast.LENGTH_SHORT).show();
           }
       });
//        dashboardViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
//            @Override
//            public void onChanged(ArrayList<String> strings) {
//                if(strings != null) {
//                    myDataset = strings;
//                    Log.d("TAG1--", myDataset.get(0));
//                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, myDataset);
//                    listView.setAdapter(arrayAdapter);
//                }else{
//                    myDataset.add("No Value");
//                    myDataset.add("Asif");
//                    myDataset.add("Dummy Values 1");
//                    Log.d("TAG2--", myDataset.get(0));
//                }
//            }
//        });
//        myDataset = dashboardViewModel.getData1();

//        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//
//        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(getContext(), myDataset);
//        recyclerView.setAdapter(mAdapter);


//        Log.d("Listview", myDataset.get(0));


        return root;
    }

    @Override
    public void applyAllValues(String gender, String reason, int numberofperson) {
        volunteerGender = gender;
        reasonRequired = reason;
        noOfVolunteerRequired = numberofperson;
    }
}