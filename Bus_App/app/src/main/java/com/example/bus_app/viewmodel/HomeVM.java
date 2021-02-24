package com.example.bus_app.viewmodel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bus_app.model.User;
import com.example.bus_app.ui.AlertDialogHelper;
import com.example.bus_app.util.services.LoginSQL;
import com.example.bus_app.util.services.UserSQL;
import com.example.bus_app.util.table.LoginTable;
import com.example.bus_app.util.table.UserTable;

public class HomeVM extends ViewModel {

    private Activity activity;
    private Context context;

    public MutableLiveData<String> cash_amount;
    public MutableLiveData<String> user_name;
    public MutableLiveData<String> user_id;
    public MutableLiveData<Boolean> nfc;
    public MutableLiveData<Boolean> hand;
    private MutableLiveData<Boolean> floating_btn;
    private MutableLiveData<Boolean> logout;

    public HomeVM() {
        cash_amount = new MutableLiveData<>();
        user_name = new MutableLiveData<>();
        user_id = new MutableLiveData<>();
        nfc = new MutableLiveData<>();
        hand = new MutableLiveData<>();
        hand.setValue(true);;
        cash_amount.setValue("$0.00");
        user_name.setValue("User");
        user_id.setValue("12345");
        nfc.setValue(false);
    }

    public void setId(String id) {
        String[] projection = {UserTable.COLUMN_NAME_ID,
                UserTable.COLUMN_NAME_USERNAME,
                UserTable.COLUMN_NAME_MONEY,
                UserTable.COLUMN_NAME_HAND};
        String selection = UserTable.COLUMN_NAME_ID + " =?";
        String[] selectionArgs = {id};

        SQLiteDatabase db = (new UserSQL(context)).getReadableDatabase();
        Cursor cursor = db.query(UserTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        try{
            if(cursor.getCount() != 0){
                cursor.moveToFirst();
                this.user_id.setValue("" + cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_NAME_ID)));
                this.user_name.setValue(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_USERNAME)));
                this.cash_amount.setValue(String.format("$%.2f", cursor.getFloat(cursor.getColumnIndex(UserTable.COLUMN_NAME_MONEY))));
                int flag = cursor.getInt(3);
                if(flag == 1){
                    this.hand.setValue(true);
                } else {
                    this.hand.setValue(false);
                }
            }

            db.close();
            cursor.close();
        } catch (Exception e){
            System.out.println(e.toString());
            AlertDialogHelper.MsgBack(activity, "Sorry","Something when wrong");
        }

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

    public LiveData<Boolean> get_floating_btn(){
        if(this.floating_btn == null){
            this.floating_btn = new MutableLiveData<>();
            this.floating_btn.setValue(false);
        }
        return  this.floating_btn;
    }

    public LiveData<Boolean> get_logout(){
        if(this.logout == null){
            this.logout = new MutableLiveData<>();
            this.logout.setValue(false);
        }
        return  this.logout;
    }

    public LiveData<Boolean> get_hand(){
        return  this.hand;
    }

    public void openFloatingBox(){
        this.floating_btn.setValue(!this.floating_btn.getValue().booleanValue());
    }

    public void changeHand(){
        try {
            SQLiteDatabase db = (new UserSQL(context)).getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_NAME_HAND, (this.hand.getValue())? UserTable.RIGHT_HAND : UserTable.LEFT_HAND);
            String selection = UserTable.COLUMN_NAME_ID + " =?";
            String[] selectionArgs = {this.user_id.getValue()};

            int count = db.update(UserTable.TABLE_NAME, values, selection, selectionArgs);
            db.close();
            if(count != 0){
                return;
            }
        }catch (Exception e){
            AlertDialogHelper.MsgBack(activity, "UPS", "Sorry, something went wrong");
        }
    }

    public void setLogout(){

        try{
            SQLiteDatabase db = (new LoginSQL(context)).getWritableDatabase();
            db.delete(LoginTable.TABLE_NAME, null, null);
            db.close();
            System.out.println("Logged");
        }catch (Exception e){
            AlertDialogHelper.MsgBack(activity, "Sorry","Something went wrong");
            return;
        }

        this.logout.setValue(!this.logout.getValue().booleanValue());
    }
}
