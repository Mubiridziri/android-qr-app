package com.mubiridziri.qrscnr.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mubiridziri.qrscnr.R;
import com.mubiridziri.qrscnr.ScannerActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button btnTakePicture, btnScanBarcode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initEvents(root);

        return root;
    }

    public void initEvents(View view) {

        btnScanBarcode = view.findViewById(R.id.btnScanBarcode);

        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScannerActivity.class));
            }
        });
    }
}
