package com.example.bus_app.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bus_app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChargeStep2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChargeStep2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    public ChargeStep2() {
        // Required empty public constructor
    }
    public static ChargeStep2 newInstance() { return new ChargeStep2(); }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static ChargeStep2 newInstance(String param1, String param2) {
        ChargeStep2 fragment = new ChargeStep2();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charge_step2, container, false);
        EditText cardNumber = (EditText) view.findViewById(R.id.editTextNumber3);
        EditText expirationDate = (EditText) view.findViewById(R.id.editTextNumber) ;
        EditText code = (EditText) view.findViewById(R.id.editTextNumberPassword2);
        //EditText name = (EditText) view.findViewById(R.id.name);
        CheckBox debit = (CheckBox) view.findViewById(R.id.checkBox);
        CheckBox credit = (CheckBox) view.findViewById(R.id.checkBox2);
        Button btn = (Button) view.findViewById(R.id.button_next_2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateCheckBox(debit,credit) && validateInsert(cardNumber) && validateInsert(expirationDate) && validateInsert(code) )
                {
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.charge_container, new ChargeStep3());
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }

            }
        });
        Button back = (Button) view.findViewById(R.id.charge_btn_back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.charge_container, new ChargeStep1());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        return view;
    }

    private boolean validateInsert(EditText camp)
    {
        String c1 = camp.getText().toString();
        if(c1.isEmpty())
        {
            camp.setError("Campo obligatorio");
            return false;
        }
        return true;
    }

    private boolean validateCheckBox(CheckBox c1, CheckBox c2)
    {
        if((c1.isChecked() && c2.isChecked()) == true)
        {
            c1.setError("Seleccione solo una opción");
            c2.setError("");
            return false;
        }
        if((c1.isChecked() && c2.isChecked()) == false)
        {
            c1.setError("Seleccione una opción");
            return false;
        }
        return true;
    }
}