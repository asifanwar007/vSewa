
package com.example.vsewa.NavigationButton.ui.needymap;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vsewa.R;

public class NeedyMapFragment extends Fragment {

    private NeedyMapViewModel mViewModel;

    public static NeedyMapFragment newInstance() {
        return new NeedyMapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_needy_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NeedyMapViewModel.class);
        // TODO: Use the ViewModel
    }

}
