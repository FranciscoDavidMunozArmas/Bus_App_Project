package com.example.bus_app.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ReplacementSpan;
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
public class ChargeStep2 extends Fragment implements TextWatcher {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int maxLength = 5;
    private boolean internalStopFormatFlag;

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
        CheckBox debit = (CheckBox) view.findViewById(R.id.checkBox);
        CheckBox credit = (CheckBox) view.findViewById(R.id.checkBox2);
        Button btn = (Button) view.findViewById(R.id.button_next_2);
        expirationDate.addTextChangedListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateCheckBox(debit,credit) && validateInsert(cardNumber) && validateInsert(expirationDate) && validateInsert(code) )
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("¿Desea continuar?").setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                                    trans.replace(R.id.charge_container, new ChargeStep3());
                                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    trans.addToBackStack(null);
                                    trans.commit();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog title = alert.create();
                    title.setTitle("Confirmacion");
                    title.show();
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
        if((c1.isChecked() || c2.isChecked()) == false)
        {
            c1.setError("Seleccione una opción");
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (internalStopFormatFlag) {
            return;
        }
        internalStopFormatFlag = true;
        formatExpiryDate(s, maxLength);
        internalStopFormatFlag = false;
    }

    public static void formatExpiryDate(@NonNull Editable expiryDate, int maxLength) {
        int textLength = expiryDate.length();
        // first remove any previous span
        SlashSpan[] spans = expiryDate.getSpans(0, expiryDate.length(), SlashSpan.class);
        for (int i = 0; i < spans.length; ++i) {
            expiryDate.removeSpan(spans[i]);
        }
        // then truncate to max length
        if (maxLength > 0 && textLength > maxLength - 1) {
            expiryDate.replace(maxLength, textLength, "");
            --textLength;
        }
        // finally add margin spans
        for (int i = 1; i <= ((textLength - 1) / 2); ++i) {
            int end = i * 2 + 1;
            int start = end - 1;
            SlashSpan marginSPan = new SlashSpan();
            expiryDate.setSpan(marginSPan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static class SlashSpan extends ReplacementSpan {

        public SlashSpan() {}

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            float[] widths = new float[end - start];
            float[] slashWidth = new float[1];
            paint.getTextWidths(text, start, end, widths);
            paint.getTextWidths("/", slashWidth);
            int sum = (int) slashWidth[0];
            for (int i = 0; i < widths.length; ++i) {
                sum += widths[i];
            }
            return sum;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            String xtext = "/" + text.subSequence(start, end);
            canvas.drawText(xtext, 0, xtext.length(), x, y, paint);
        }

    }

}


