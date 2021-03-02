package com.example.bus_app.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.bus_app.R;

import java.util.ArrayList;
import java.util.List;
public class History extends Fragment {
    public static History newInstance() { return new History(); }
    RecyclerView recyclerViewRegistro;
    List<Register> lst;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private List<Register> GetData() {
        lst = new ArrayList<>();
        lst.add(new Register(1,"Recarga Banco", 5.00, "05/01/2021"));
        lst.add(new Register(2,"Recarga Tienda", 2.00, "06/01/2021"));
        lst.add(new Register(3,"Recarga Tienda", 3.00, "07/01/2021"));
        lst.add(new Register(4,"Pago", 0.25, "08/01/2021"));
        lst.add(new Register(5,"Recarga Tienda", 1.00, "09/01/2021"));
        lst.add(new Register(6,"Pago", 0.25, "10/01/2021"));
        lst.add(new Register(7,"Recarga Banco", 5.00, "11/01/2021"));
        lst.add(new Register(1,"Recarga Banco", 10.25, "12/01/2021"));
        lst.add(new Register(2,"Recarga Tienda", 5.15, "13/01/2021"));
        lst.add(new Register(3,"Recarga Tienda", 3.10, "14/01/2021"));
        lst.add(new Register(4,"Pago", 0.25, "15/01/2021"));
        lst.add(new Register(5,"Recarga Tienda", 0.50, "16/01/2021"));
        lst.add(new Register(6,"Pago", 0.25, "17/01/2021"));
        lst.add(new Register(7,"Recarga Banco", 5.00, "18/01/2021"));
        return lst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerViewRegistro = (RecyclerView) vista.findViewById(R.id.lista);
        recyclerViewRegistro.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewRegistro.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerViewRegistro.addItemDecoration(dividerItemDecoration);
        RegisterAdapter adapter = new RegisterAdapter(GetData());
        recyclerViewRegistro.setAdapter(adapter);
        return vista;
    }
    /*RecyclerView recyclerViewRegistro;
    Toolbar toolbar;
    TextView txtToolbar;
    ImageButton btnBack;
    List<Register> lst;
    ArrayList<Register> selectionList = new ArrayList<>();
    RegisterAdapter adapter = new RegisterAdapter(GetData());
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

    private List<Register> GetData() {
        lst = new ArrayList<>();
        lst.add(new Register(1,"Recarga Banco", 5.00, "05/01/2021"));
        lst.add(new Register(2,"Recarga Tienda", 2.00, "06/01/2021"));
        lst.add(new Register(3,"Recarga Tienda", 3.00, "07/01/2021"));
        lst.add(new Register(4,"Pago", 0.25, "08/01/2021"));
        lst.add(new Register(5,"Recarga Tienda", 1.00, "09/01/2021"));
        lst.add(new Register(6,"Pago", 0.25, "10/01/2021"));
        lst.add(new Register(7,"Recarga Banco", 5.00, "11/01/2021"));
        lst.add(new Register(1,"Recarga Banco", 10.25, "12/01/2021"));
        lst.add(new Register(2,"Recarga Tienda", 5.15, "13/01/2021"));
        lst.add(new Register(3,"Recarga Tienda", 3.10, "14/01/2021"));
        lst.add(new Register(4,"Pago", 0.25, "15/01/2021"));
        lst.add(new Register(5,"Recarga Tienda", 0.50, "16/01/2021"));
        lst.add(new Register(6,"Pago", 0.25, "17/01/2021"));
        lst.add(new Register(7,"Recarga Banco", 5.00, "18/01/2021"));
        return lst;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerViewRegistro = (RecyclerView) vista.findViewById(R.id.lista);

        toolbar = vista.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtToolbar = vista.findViewById(R.id.text_toolbar);
        txtToolbar.setVisibility(View.GONE);

        btnBack = vista.findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);

        recyclerViewRegistro.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewRegistro.getContext(),
                DividerItemDecoration.VERTICAL);

        recyclerViewRegistro.addItemDecoration(dividerItemDecoration);
        recyclerViewRegistro.setAdapter(adapter);
        btnBack.setOnClickListener(v->{
            clearActionMode();
        });
        return vista;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void clearActionMode() {
        isActionMode = false;
        txtToolbar.setVisibility(View.GONE);
        txtToolbar.setText("Selección: 0 elementos seleccionados");
        btnBack.setVisibility(View.GONE);
        counter = 0;
        selectionList.clear();
        toolbar.getMenu().clear();
        adapter.notifyDataSetChanged();
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startSelection(int index){
        if(!isActionMode){
            isActionMode = true;
            selectionList.add(lst.get(index));
            counter++;
            updateToolBarText(counter);
            txtToolbar.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            toolbar.inflateMenu(R.menu.delete);
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
            txtToolbar.setText("Seleccion: 0 elementos seleccionados");
        }
        if(counter==1){
            txtToolbar.setText("Seleccion: 1 elemento seleccionado");
        }
        else {
            txtToolbar.setText("Seleccion: "+counter+" elementos seleccionados");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_delete && selectionList.size() > 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Eliminar "+ selectionList.size() + " elemento?");
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