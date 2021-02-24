package com.example.bus_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bus_app.model.User;

public class SharedVM extends ViewModel {

    private MutableLiveData<User> user;
    private MutableLiveData<String> str;

    public void setUser(User user) {
        if(this.user == null){
            this.user = new MutableLiveData<>();
        }
        this.user.setValue(user);
    }

    public void setStr(String str) {
        if(this.str == null){
            this.str = new MutableLiveData<String>();
        }
        this.str.setValue(str);
    }

    public LiveData<User> getUser(){
        if(user == null){
            user = new MutableLiveData<>();
        }
        return user;
    }

    public LiveData<String> getStr(){
        if(str == null){
            str = new MutableLiveData<>();
        }
        return str;
    }
}
