package com.example.bus_app.view;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_app.R;

import java.util.List;

public class RegisterAdapter extends RecyclerView.Adapter<RegisterAdapter.RegisterViewHolder> {

    List<Register> lst;
    History history_Page;
    public RegisterAdapter(List<Register> lst, History history) {
        this.lst = lst;
        history_Page = history;
    }

    @Override
    public RegisterAdapter.RegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_elements, null, false);
        return new RegisterViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RegisterAdapter.RegisterViewHolder holder, int position) {
        holder.setData(lst.get(position));

        if(history_Page.position == position){
            holder.checkBox.setChecked(true);
            history_Page.position = -1;
        }

        if(history_Page.isActionMode){
            Anim anim = new Anim(100, holder.linearLayout);
            anim.setDuration(300);
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.linearLayout.setAnimation(anim);
        } else
        {
            Anim anim = new Anim(0, holder.linearLayout);
            holder.linearLayout.setVisibility(View.GONE);
            anim.setDuration(300);
            holder.linearLayout.setAnimation(anim);
            holder.checkBox.setChecked(false);
        }
        holder.itemView.setOnLongClickListener(v->{
            history_Page.startSelection(position);
            return true;
        });

        holder.checkBox.setOnClickListener(v->{
            history_Page.check(v, position);
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public class RegisterViewHolder extends RecyclerView.ViewHolder {
        TextView txtTransaction, txtAmount, txtDate;
        CheckBox checkBox;
        ConstraintLayout linearLayout;
        History history_Page;
        public RegisterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTransaction = (TextView) itemView.findViewById(R.id.transaction);
            txtAmount = (TextView) itemView.findViewById(R.id.amount);
            txtDate = (TextView) itemView.findViewById(R.id.date);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            linearLayout.setVisibility(View.GONE);
            checkBox = itemView.findViewById(R.id.checkbox);
            this.history_Page = history_Page;
        }

        public void setData(Register register) {
            txtTransaction.setText(register.rtransaction);
            txtAmount.setText("$"+register.ramount);
            txtDate.setText(register.rdate);
        }
    }

    class Anim extends Animation{
        private  int width, startWith;
        private View view;

        public Anim(int width, View view){
            this.width = width;
            this.view = view;
            this.startWith = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            int newWidth = startWith + (int) ((width - startWith) * interpolatedTime);
            view.getLayoutParams().width = newWidth;
            view.requestLayout();
            super.applyTransformation(interpolatedTime, t);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
