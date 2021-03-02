package com.example.bus_app.viewmodel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.bus_app.ui.AlertDialogHelper;
import com.example.bus_app.ui.ErrorMsg;
import com.example.bus_app.util.services.UserSQL;
import com.example.bus_app.util.table.UserTable;

import static com.example.bus_app.ui.ErrorMsg.PSW_ERROR_MSG;

public class ForgetPasswordVM extends LoginVM{

    private Activity activity;
    private Context context;

    public MutableLiveData<String> confirm_password;
    public MutableLiveData<String> conf_pass_msg;
    public ForgetPasswordVM(){
        super();
        confirm_password = new MutableLiveData<>();
        conf_pass_msg = new MutableLiveData<>();
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void CheckUser(){
        user_msg.setValue("");
        pass_msg.setValue("");
        conf_pass_msg.setValue("");
        if(validate()){
            this.start_activity.setValue(true);
        } else {
            if(TextUtils.isEmpty(this.username.getValue())){
                this.user_msg.setValue(ErrorMsg.USER_ERROR_MSG);
            }
            if(TextUtils.isEmpty(this.password.getValue())){
                this.pass_msg.setValue(ErrorMsg.PSW_ERROR_MSG);
            }
            if (!TextUtils.isEmpty(this.confirm_password.getValue())) {
                if(!this.password.getValue().equals(this.confirm_password.getValue())){
                    this.conf_pass_msg.setValue(ErrorMsg.PSW_DONT_MATCH);
                }
            } else {
                this.conf_pass_msg.setValue(ErrorMsg.PSW_DONT_MATCH);
            }
        }
    }

    @Override
    protected boolean validate(){
        if(!TextUtils.isEmpty(this.username.getValue()) &&
                !TextUtils.isEmpty(this.password.getValue()) &&
                this.password.getValue().equals(this.confirm_password.getValue()))
        {
            try {
                SQLiteDatabase db = (new UserSQL(context)).getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put(UserTable.COLUMN_NAME_PASSWORD, this.password.getValue().toString());
                String selection = UserTable.COLUMN_NAME_USERNAME + " =?";
                String[] selectionArgs = {this.username.getValue().toString()};

                int count = db.update(UserTable.TABLE_NAME, values, selection, selectionArgs);
                db.close();
                if(count != 0){
                    return true;
                }
                AlertDialogHelper.MsgBack(activity, ErrorMsg.ERROR_TITLE, ErrorMsg.USER_DOESNT_EXIST);
                this.user_msg.setValue(ErrorMsg.USER_DOESNT_EXIST);
            }catch (Exception e){
                AlertDialogHelper.MsgBack(activity, ErrorMsg.SORRY_MSG, ErrorMsg.SORRY_MSG);
            }
        }
        return false;
    }

}
