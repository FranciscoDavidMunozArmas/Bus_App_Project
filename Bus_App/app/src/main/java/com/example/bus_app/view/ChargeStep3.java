package com.example.bus_app.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bus_app.R;
import com.example.bus_app.model.User;
import com.example.bus_app.ui.AlertDialogHelper;
import com.example.bus_app.ui.ErrorMsg;
import com.example.bus_app.util.services.HistorySQL;
import com.example.bus_app.util.services.LoginSQL;
import com.example.bus_app.util.services.UserSQL;
import com.example.bus_app.util.table.HistoryTable;
import com.example.bus_app.util.table.LoginTable;
import com.example.bus_app.util.table.UserTable;
import com.example.bus_app.viewmodel.SharedVM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChargeStep3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChargeStep3 extends Fragment {
    private SharedVM share;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChargeStep3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChargeStep.
     */
    // TODO: Rename and change types and number of parameters
    public static ChargeStep3 newInstance(String param1, String param2) {
        ChargeStep3 fragment = new ChargeStep3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        share = ViewModelProviders.of(requireActivity()).get(SharedVM.class);
        View view = inflater.inflate(R.layout.fragment_charge_step3, container, false);
        Button back = (Button) view.findViewById(R.id.button_finish);
        //Transaction notification
        Context context = view.getContext();
        initChannels(context);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default");
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("New notification");
        notification.setPriority(Notification.PRIORITY_HIGH);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Bus App");
        notification.setContentText("Se ha acreditado $0.00 a su pasaje el dd/mm/aa por XXXXX");
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1,notification.build());
        ///
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(update(share.getUser().getValue(), share.getAmount().getValue())){
                    create(share.getUser().getValue(), share.getAmount().getValue(), HistoryTable.METHOD_1);
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.charge_container, new Home());
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            }
        });
        return view;
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
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
    private boolean update(User user, float amount){
        try {
            SQLiteDatabase db = (new UserSQL(getContext())).getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_NAME_MONEY, user.getMoney() + amount);
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

}

