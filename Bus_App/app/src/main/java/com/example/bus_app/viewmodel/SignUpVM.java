package com.example.bus_app.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.bus_app.ui.AlertDialogHelper;
import com.example.bus_app.util.services.LoginSQL;
import com.example.bus_app.util.services.UserSQL;
import com.example.bus_app.util.table.LoginTable;
import com.example.bus_app.util.table.UserTable;

public class SignUpVM extends LoginVM {

    private long id;
    private Activity activity;
    private Context context;

    public MutableLiveData<String> confirm_password;
    public MutableLiveData<String> conf_pass_msg;
    public MutableLiveData<String> agree_msg;
    public MutableLiveData<Boolean> agree;
    public MutableLiveData<Boolean> keep_login;

    public SignUpVM() {
        super();
        confirm_password = new MutableLiveData<>();
        agree = new MutableLiveData<>();
        agree.setValue(false);
        conf_pass_msg = new MutableLiveData<>();
        agree_msg = new MutableLiveData<>();
        id = 0;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void CheckUser() {
        user_msg.setValue("");
        pass_msg.setValue("");
        conf_pass_msg.setValue("");
        agree_msg.setValue("");
        if (validate()) {
            try {
                SQLiteDatabase db = (new UserSQL(context)).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(UserTable.COLUMN_NAME_USERNAME, this.username.getValue().toString());
                values.put(UserTable.COLUMN_NAME_PASSWORD, this.password.getValue().toString());
                values.put(UserTable.COLUMN_NAME_HAND, UserTable.RIGHT_HAND);
                values.put(UserTable.COLUMN_NAME_MONEY, 0f);
                long newRowId = db.insert(UserTable.TABLE_NAME, null, values);
                setId(newRowId);

                try {
                    SQLiteDatabase db_log = (new LoginSQL(context)).getWritableDatabase();
                    ContentValues values_log = new ContentValues();
                    values_log.put(LoginTable.COLUMN_NAME_ID, (int)this.id);
                    values_log.put(LoginTable.COLUMN_NAME_USERNAME, this.username.getValue());
                    values_log.put(LoginTable.COLUMN_NAME_PASSWORD, this.password.getValue());
                    db_log.insert(LoginTable.TABLE_NAME, null, values_log);
                    db_log.close();
                }catch (Exception e){
                    AlertDialogHelper.MsgBack(activity, "UPS", "Sorry, something went wrong");
                    return;
                }
                db.close();
                this.start_activity.setValue(true);
            }catch (Exception e){
                AlertDialogHelper.MsgBack(activity, "UPS", "Sorry, something went wrong");
            }
        } else {
            if (TextUtils.isEmpty(this.username.getValue())) {
                this.user_msg.setValue("Invalid User");
            }
            if (TextUtils.isEmpty(this.password.getValue())) {
                this.pass_msg.setValue("Invalid Password");
            }
            if (!TextUtils.isEmpty(this.confirm_password.getValue())) {
                if(!this.password.getValue().equals(this.confirm_password.getValue())){
                    this.conf_pass_msg.setValue("Password don't match");
                }
            } else {
                this.conf_pass_msg.setValue("Password don't match");
            }
            if (!this.agree.getValue().booleanValue()) {
                agree_msg.setValue("You doesn't check");
            }
        }
    }

    @Override
    protected boolean validate() {
        String[] projection = {UserTable.COLUMN_NAME_ID};
        String selection = UserTable.COLUMN_NAME_USERNAME + " =?";
        String[] selectionArgs = {this.username.getValue()};

         if(!TextUtils.isEmpty(this.username.getValue()) &&
                 !TextUtils.isEmpty(this.password.getValue()) &&
                 this.password.getValue().equals(this.confirm_password.getValue()) &&
                 this.agree.getValue()){

             try {
                 SQLiteDatabase db = (new UserSQL(context)).getReadableDatabase();
                 Cursor cursor = db.query(UserTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                 if(cursor.getCount() == 0){
                     db.close();
                     cursor.close();
                     return true;
                 }
                 AlertDialogHelper.MsgBack(activity, "Error", "The user already exists");
                 this.user_msg.setValue("The user already exists");
                 db.close();
                 cursor.close();
             }catch (Exception e){
                 AlertDialogHelper.MsgBack(activity, "UPS", "Sorry, something went wrong");
             }
         }
        return false;
    }
}