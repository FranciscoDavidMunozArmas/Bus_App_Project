package com.example.bus_app.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bus_app.R;
import com.example.bus_app.viewmodel.SharedVM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChargeStep1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChargeStep1 extends Fragment {

    private SharedVM share;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChargeStep1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Charge_step1.
     */
    // TODO: Rename and change types and number of parameters
    public static ChargeStep1 newInstance(String param1, String param2) {
        ChargeStep1 fragment = new ChargeStep1();
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
        share = ViewModelProviders.of(requireActivity()).get(SharedVM.class);

        View view = inflater.inflate(R.layout.fragment_charge_step1, container, false);
        EditText textNumberDecimal = (EditText) view.findViewById(R.id.editTextNumberDecimal);
        Button btn = (Button) view.findViewById(R.id.button_next_1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInsert(textNumberDecimal))
                {
                    share.setAmount(Float.parseFloat(((TextView)view.findViewById(R.id.editTextNumberDecimal)).getText().toString()));
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.charge_container, new ChargeStep2());
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            }
        });
        Button back = (Button) view.findViewById(R.id.charge_btn_back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.charge_container, new Home());
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
            camp.setError("Llene este campo");
            return false;
        }
        return true;
    }

}