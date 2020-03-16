package com.mubiridziri.qrscnr.ui.stored_data_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mubiridziri.qrscnr.R;
import com.mubiridziri.qrscnr.adapter.StoredDataAdapter;
import com.mubiridziri.qrscnr.appdatabase.AppDatabase;
import com.mubiridziri.qrscnr.entity.StoredData;
import com.mubiridziri.qrscnr.repository.StoredDataRepository;

import java.util.List;

public class StoredFragment extends Fragment {

    private List<StoredData> listStoredData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stored, container, false);
        RecyclerView storedView = root.findViewById(R.id.storedData);

        storedView.setLayoutManager(new LinearLayoutManager(getContext()));

        Thread databaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getContext(),
                        AppDatabase.class, "qrscnr").build();
                StoredDataRepository storedDataRepository = db.getLinkRepository();
                 listStoredData = storedDataRepository.getAll();
                db.close();

            }
        });
        databaseThread.start();
        //This is of course very bad, but I don't know how to do otherwise
        while(true) {
            if (listStoredData == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else break;
        }

        RecyclerView.Adapter adapter = new StoredDataAdapter(listStoredData);
        storedView.setAdapter(adapter);

        return root;
    }
}
