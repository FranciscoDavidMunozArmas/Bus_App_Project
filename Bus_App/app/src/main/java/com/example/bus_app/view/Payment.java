package com.example.bus_app.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.renderscript.Float3;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import android.widget.Button;
import android.widget.TextView;
import com.example.bus_app.R;
import com.example.bus_app.model.User;
import com.example.bus_app.ui.AlertDialogHelper;
import com.example.bus_app.ui.ErrorMsg;
import com.example.bus_app.util.services.HistorySQL;
import com.example.bus_app.util.services.UserSQL;
import com.example.bus_app.util.table.HistoryTable;
import com.example.bus_app.util.table.UserTable;
import com.example.bus_app.viewmodel.SharedVM;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.intellij.lang.annotations.JdkConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Payment extends Fragment {

    private SharedVM share;

    public static Payment newInstance() {
        return new Payment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        share = ViewModelProviders.of(requireActivity()).get(SharedVM.class);

        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        //IntentIntegrator integrator = new IntentIntegrator(getActivity());
        IntentIntegrator.forSupportFragment(this).
                setPrompt("Enfoca el codigo QR dentro del rectangulo")
                .setOrientationLocked(false)
                .setCaptureActivity(CaptureActivityPortrait.class)
                .initiateScan();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String data_result = result.getContents();
        System.out.println(data_result);
        try{
            float amount = Float.parseFloat(data_result);
            if(share.getUser().getValue().getMoney() > amount){
                if(update(share.getUser().getValue(), amount)){
                    create(share.getUser().getValue(), amount, HistoryTable.METHOD_3);
                }
            } else {
                AlertDialogHelper.MsgBack(getActivity(), ErrorMsg.ERROR_TITLE, "Saldo Insuficiente");
            }
        }catch(Exception ex){
            AlertDialogHelper.MsgBack(getActivity(), ErrorMsg.ERROR_TITLE, "QR no valido");
        }
        //System.out.println("The data is: " + data_result);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, Home.newInstance());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean update(User user, float amount){
        try {
            SQLiteDatabase db = (new UserSQL(getContext())).getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_NAME_MONEY, user.getMoney() - amount);
            String selection = UserTable.COLUMN_NAME_ID + " =?";
            String[] selectionArgs = {user.getId()};
            System.out.println(user.getId());
            int count = db.update(UserTable.TABLE_NAME, values, selection, selectionArgs);
            db.close();
            if(count != 0){
                return true;
            }
            AlertDialogHelper.MsgBack(getActivity(), ErrorMsg.ERROR_TITLE, ErrorMsg.USER_DOESNT_EXIST);
        }catch (Exception e){
            AlertDialogHelper.MsgBack(getActivity(), ErrorMsg.SORRY_MSG, ErrorMsg.SORRY_MSG);
        }
        return false;
    }

    private void create(User user, float amount, String type){
        try {
            SQLiteDatabase db = (new HistorySQL(getContext())).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(HistoryTable.COLUMN_NAME_USER, user.getId());
            values.put(HistoryTable.COLUMN_NAME_DESCRIPTION, type);
            values.put(HistoryTable.COLUMN_NAME_AMOUNT, amount);
            values.put(HistoryTable.COLUMN_NAME_DATE, ((new SimpleDateFormat("dd/mm/yyyy")).format((new Date()).getTime())).toString());
            long newRowId = db.insert(HistoryTable.TABLE_NAME, null, values);
            System.out.println(newRowId);
            db.close();
        }catch (Exception e){
            AlertDialogHelper.MsgBack(getActivity(), ErrorMsg.SORRY_TITLE, ErrorMsg.SORRY_MSG);
        }
    }
}
