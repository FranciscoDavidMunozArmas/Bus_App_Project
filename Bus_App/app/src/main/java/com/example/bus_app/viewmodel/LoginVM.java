package com.example.bus_app.viewmodel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bus_app.model.User;
import com.example.bus_app.ui.AlertDialogHelper;
import com.example.bus_app.util.services.LoginSQL;
import com.example.bus_app.util.services.UserSQL;
import com.example.bus_app.util.table.LoginTable;
import com.example.bus_app.util.table.UserTable;

public class LoginVM extends ViewModel {

    private long id;
    private Activity activity;
    private Context context;

    public MutableLiveData<String> user_msg;
    public MutableLiveData<String> pass_msg;
    public MutableLiveData<String> username;
    public MutableLiveData<String> password;
    public MutableLiveData<Boolean> keep_login;
    public MutableLiveData<Boolean> start_activity;
    public LoginVM(){
        username = new MutableLiveData<>();
        password = new MutableLiveData<>();
        user_msg = new MutableLiveData<>();
        pass_msg = new MutableLiveData<>();
        start_activity = new MutableLiveData<>();
        keep_login = new MutableLiveData<>();
        keep_login.setValue(false);
    }

    public LiveData<Boolean> getStart(){
        if(start_activity == null){
            start_activity = new MutableLiveData<>();
        }
        return start_activity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean CheckLogin(){
        String login_username;
        String login_password;

        String[] login_projection = {LoginTable.COLUMN_NAME_USERNAME, LoginTable.COLUMN_NAME_PASSWORD};

        try {
            SQLiteDatabase db = (new LoginSQL(context)).getReadableDatabase();
            Cursor cursor = db.query(LoginTable.TABLE_NAME, login_projection, null, null, null, null, null);
            System.out.println("Login items" + cursor.getCount());
            if(cursor.getCount() == 0){
                db.close();
                cursor.close();
                return false;
            }
            cursor.moveToFirst();
            login_username = cursor.getString(cursor.getColumnIndex(LoginTable.COLUMN_NAME_USERNAME));
            login_password = cursor.getString(cursor.getColumnIndex(LoginTable.COLUMN_NAME_PASSWORD));
            db.close();
            cursor.close();
        }catch (Exception e){
            System.out.println(e.toString());
            AlertDialogHelper.MsgBack(activity, "UPS", "Sorry, something went wrong");
            return false;
        }


        String[] user_projection = new String[]{UserTable.COLUMN_NAME_ID, UserTable.COLUMN_NAME_USERNAME, UserTable.COLUMN_NAME_PASSWORD};
        String selection = UserTable.COLUMN_NAME_USERNAME + " =? AND " + UserTable.COLUMN_NAME_PASSWORD + " =?";
        String[] selectionArgs = {login_username, login_password};

        try {
            SQLiteDatabase db = (new UserSQL(context)).getReadableDatabase();
            Cursor cursor = db.query(UserTable.TABLE_NAME, user_projection, selection, selectionArgs, null, null, null);
            if(cursor.getCount() == 0){
                db.close();
                cursor.close();
                return false;
            }
            cursor.moveToFirst();
            this.setId(cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_NAME_ID)));
            db.close();
            cursor.close();
        }catch (Exception e){
            AlertDialogHelper.MsgBack(activity, "UPS", "Sorry, something went wrong");
            return false;
        }
        if(this.getId() == 0){
            return false;
        }

        return true;

    }

    public void CheckUser(){
        user_msg.setValue("");
        pass_msg.setValue("");
        if(validate()){
            if(keep_login.getValue()){
                try {
                    SQLiteDatabase db = (new LoginSQL(context)).getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(LoginTable.COLUMN_NAME_ID, (int)this.id);
                    values.put(LoginTable.COLUMN_NAME_USERNAME, this.username.getValue());
                    values.put(LoginTable.COLUMN_NAME_PASSWORD, this.password.getValue());
                    db.insert(LoginTable.TABLE_NAME, null, values);
                    db.close();
                }catch (Exception e){
                    AlertDialogHelper.MsgBack(activity, "UPS", "Sorry, something went wrong");
                    return;
                }
            }
            this.start_activity.setValue(true);
        } else {
            if(TextUtils.isEmpty(this.username.getValue())){
                this.user_msg.setValue("Invalid User");
            }
            if(TextUtils.isEmpty(this.password.getValue())){
                this.pass_msg.setValue("Invalid Password");
            }
        }
    }

    protected boolean validate(){
        String[] projection = {UserTable.COLUMN_NAME_ID};
        String selection = UserTable.COLUMN_NAME_USERNAME + " =? AND " + UserTable.COLUMN_NAME_PASSWORD + " =?";
        String[] selectionArgs = {this.username.getValue(), this.password.getValue()};

         if (!TextUtils.isEmpty(this.username.getValue()) && !TextUtils.isEmpty(this.password.getValue())){
             try {
                 SQLiteDatabase db = (new UserSQL(context)).getReadableDatabase();
                 Cursor cursor = db.query(UserTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                 if(cursor.getCount() != 0){
                     cursor.moveToFirst();
                     this.setId(cursor.getInt(0));
                     db.close();
                     cursor.close();
                     return true;
                 }
                 this.user_msg.setValue("The user doesn't exists");
                 db.close();
                 cursor.close();
             }catch (Exception e){
                 AlertDialogHelper.MsgBack(activity, "UPS", "Sorry, something went wrong");
             }
         }
         return false;
    }

    public void no_available(){
        AlertDialogHelper.MsgBack(getActivity(), "Lo sentimos", "Servicio no disponible por el momento");
    }
}