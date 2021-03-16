package com.example.bus_app.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.bus_app.R;
import com.example.bus_app.model.User;
import com.example.bus_app.ui.AlertDialogHelper;
import com.example.bus_app.ui.ErrorMsg;
import com.example.bus_app.util.services.HistorySQL;
import com.example.bus_app.util.services.UserSQL;
import com.example.bus_app.util.table.HistoryTable;
import com.example.bus_app.util.table.UserTable;
import com.example.bus_app.viewmodel.SharedVM;

import java.util.ArrayList;
import java.util.List;
public class History extends Fragment {
    public static History newInstance() { return new History(); }
    private SharedVM share;
    RecyclerView recyclerViewRegistro;
    Toolbar toolbar;
    TextView txtToolbar;
    ImageButton btnBack;
    ImageButton btnDelete;
    ImageButton btnSelectAll;
    ArrayList<Register> lst = new ArrayList<>();
    TextView txtHistory;
    ArrayList<Register> selectionList = new ArrayList<>();
    RegisterAdapter adapter;
    int counter = 0;
    boolean isActionMode = false;
    int position = -1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public History() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private List<Register> GetData(User user) {
        lst = new ArrayList<>();

        String[] projection = {HistoryTable.COLUMN_NAME_ID, HistoryTable.COLUMN_NAME_DESCRIPTION, HistoryTable.COLUMN_NAME_AMOUNT, HistoryTable.COLUMN_NAME_DATE};
        String selection = HistoryTable.COLUMN_NAME_USER + " =?";
        String[] selectionArgs = {user.getId()};

        try {
            SQLiteDatabase db = (new HistorySQL(getContext())).getReadableDatabase();
            Cursor cursor = db.query(HistoryTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            cursor.moveToFirst();
            System.out.println(cursor.getCount());
            if(cursor.getCount() != 0){
                do {
                    lst.add(new Register(cursor.getInt(cursor.getColumnIndex(HistoryTable.COLUMN_NAME_ID)),
                            cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_NAME_DESCRIPTION)),
                            Double.parseDouble(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_NAME_AMOUNT))),
                            cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_NAME_DATE))));
                }while(cursor.moveToNext());
            }
            db.close();
            cursor.close();
        }catch (Exception e){
            AlertDialogHelper.MsgBack(getActivity(), ErrorMsg.SORRY_TITLE, ErrorMsg.SORRY_MSG);
        }
        return lst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        share = ViewModelProviders.of(requireActivity()).get(SharedVM.class);
        adapter = new RegisterAdapter(GetData(share.getUser().getValue()), this);
        View vista = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerViewRegistro = (RecyclerView) vista.findViewById(R.id.lista);
        txtHistory = (TextView) vista.findViewById(R.id.txt_History);
        txtHistory.setVisibility(View.VISIBLE);

        toolbar = (Toolbar) vista.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtToolbar = (TextView) vista.findViewById(R.id.text_toolbar);
        txtToolbar.setVisibility(View.GONE);

        btnBack = (ImageButton) vista.findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);

        btnDelete = (ImageButton) vista.findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.GONE);

        btnSelectAll = (ImageButton) vista.findViewById(R.id.btn_select_All);
        btnSelectAll.setVisibility(View.GONE);

        recyclerViewRegistro.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewRegistro.getContext(),
                DividerItemDecoration.VERTICAL);

        recyclerViewRegistro.addItemDecoration(dividerItemDecoration);
        recyclerViewRegistro.setAdapter(adapter);
        btnBack.setOnClickListener(v->{
            clearActionMode();
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectionList.size() > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    if(selectionList.size() == 1){
                        builder.setMessage("¿Está seguro que desea eliminar "+ selectionList.size() + " elemento?");
                    } else{
                        builder.setMessage("¿Está seguro que desea eliminar "+ selectionList.size() + " elementos?");
                    }
                    builder.setTitle("Confirmar");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (Register register : selectionList){
                                delete_entry(register);
                                lst.remove(register);
                            }
                            updateToolBarText(0);
                            clearActionMode();
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionList.clear();
                selectionList.addAll(lst);
                counter = selectionList.size();
                updateToolBarText(counter);

            }
        });

        return vista;
    }

    private void clearActionMode() {
        isActionMode = false;
        txtToolbar.setVisibility(View.GONE);
        txtToolbar.setText("0 elementos seleccionados");
        btnBack.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        btnSelectAll.setVisibility(View.GONE);
        txtHistory.setVisibility(View.VISIBLE);
        counter = 0;
        selectionList.clear();
        toolbar.getMenu().clear();
        adapter.notifyDataSetChanged();
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    public void startSelection(int index){
        if(!isActionMode){
            isActionMode = true;
            selectionList.add(lst.get(index));
            counter++;
            updateToolBarText(counter);
            txtToolbar.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnSelectAll.setVisibility(View.VISIBLE);
            txtHistory.setVisibility(View.GONE);
            position = index;
            adapter.notifyDataSetChanged();
        }
    }

    public void check(View v, int index){
        if(((CheckBox)v).isChecked()){
            selectionList.add(lst.get(index));
            counter++;
            updateToolBarText(counter);
        } else{
            selectionList.remove(lst.get(index));
            counter--;
            updateToolBarText(counter);
        }
    }

    private void updateToolBarText(int counter) {
        if(counter==0){
            txtToolbar.setText("0 elementos seleccionados");
        }
        if(counter==1){
            txtToolbar.setText("1 elemento seleccionado");
        }
        else {
            txtToolbar.setText(counter+" elementos seleccionados");
        }
    }

    private void delete_entry(Register user){
        String selection = HistoryTable.COLUMN_NAME_ID + " =?";
        String[] selectionArgs = {"" + user.getId()};

        try {
            SQLiteDatabase db = (new HistorySQL(getContext())).getReadableDatabase();
            db.delete(HistoryTable.TABLE_NAME, selection, selectionArgs);
            db.close();
        }catch (Exception e){
            AlertDialogHelper.MsgBack(getActivity(), ErrorMsg.SORRY_TITLE, ErrorMsg.SORRY_MSG);
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_delete && selectionList.size() > 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Está seguro que desea eliminar "+ selectionList.size() + " elementos?");
            builder.setTitle("Confirmar");
            builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (Register register : selectionList){
                        lst.remove(register);
                    }
                    updateToolBarText(0);
                    clearActionMode();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }*/
}