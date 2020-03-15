package com.mubiridziri.qrscnr.ui.stored_data_page;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoredViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StoredViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}