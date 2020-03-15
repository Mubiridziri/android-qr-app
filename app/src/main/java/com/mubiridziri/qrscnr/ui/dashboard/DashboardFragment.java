package com.mubiridziri.qrscnr.ui.dashboard;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mubiridziri.qrscnr.R;
import com.mubiridziri.qrscnr.adapter.LinkAdapter;
import com.mubiridziri.qrscnr.appdatabase.AppDatabase;
import com.mubiridziri.qrscnr.entity.Link;
import com.mubiridziri.qrscnr.repository.LinkRepository;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DashboardFragment extends Fragment {

    public RecyclerView storedView;
    public RecyclerView.Adapter adapter;
    List<Link> listLink;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stored, container, false);
        storedView = root.findViewById(R.id.storedData);

        storedView.setLayoutManager(new LinearLayoutManager(getContext()));

        Thread databaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getContext(),
                        AppDatabase.class, "qrscnr").build();
                LinkRepository linkRepository = db.getLinkRepository();
                 listLink = linkRepository.getAll();
                db.close();

            }
        });
        databaseThread.start();
        //This is of course very bad, but I don't know how to do otherwise
        while(true) {
            if (listLink == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else break;
        }

        adapter = new LinkAdapter(listLink);
        storedView.setAdapter(adapter);

        return root;
    }
}
