package com.example.bus_app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogHelper {

    public static void ExitBack(Activity context){
        (new AlertDialog.Builder(context))
                .setTitle("Closing this App")
                .setMessage("Are you sure you want to close this app")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                }). setNegativeButton("No", null)
                .show();
    }


    public static void MsgBack(Activity activity, String title, String msg){
        (new AlertDialog.Builder(activity))
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Yes", null)
                .show();
    }

}
