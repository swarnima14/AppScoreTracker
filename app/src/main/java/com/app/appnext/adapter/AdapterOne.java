package com.app.appnext.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appnext.R;
import com.app.appnext.modelclasses.ModelOne;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdapterOne extends RecyclerView.Adapter<AdapterOne.ViewHolder> {

    private Context context;
    ArrayList<ModelOne> modelOneList;
    ItemClicked activity;

    //interface to know which item in the list was clicked and send this to main activity
    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public AdapterOne(Context context, ArrayList<ModelOne> modelOneList){
        this.modelOneList = modelOneList;
        activity=(ItemClicked) context;
    }

    @NonNull
    @Override
    public AdapterOne.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setTag(modelOneList.get(position)); //setTag is used to know which item is clicked (in setonclicklistener method)
        holder.tvName.setText(modelOneList.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return modelOneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);

            //when any item in the list gets clicked this function gets activated

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.onItemClicked(modelOneList.indexOf((ModelOne) view.getTag()));

                }
            });
        }
    }


}
