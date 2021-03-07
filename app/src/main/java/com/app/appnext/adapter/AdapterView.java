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
import com.app.appnext.modelclasses.ModelView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AdapterView extends RecyclerView.Adapter<AdapterView.ViewHolder> {

    ArrayList<ModelView> modelViewArrayList;
    hereItemClicked activity;

    public interface hereItemClicked
    {
        void onHereItemClicked(int index);
    }

    public AdapterView(Context context, ArrayList<ModelView> modelViewArrayList){
        this.modelViewArrayList = modelViewArrayList;
        activity = (hereItemClicked) context;
    }

    @NonNull
    @Override
    public AdapterView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterView.ViewHolder holder, int position) {

        holder.itemView.setTag(modelViewArrayList.get(position));
        holder.tvOne.setText(modelViewArrayList.get(position).getNameA());
        holder.tvTwo.setText(modelViewArrayList.get(position).getNameB());
        holder.tvDate.setText(modelViewArrayList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return modelViewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvOne, tvTwo,tvDate;
        MaterialCardView cvInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOne = itemView.findViewById(R.id.tvOne);
            tvTwo = itemView.findViewById(R.id.tvTwo);
            tvDate = itemView.findViewById(R.id.tvDate);
            cvInfo = itemView.findViewById(R.id.cvInfo);

           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.onHereItemClicked(modelViewArrayList.indexOf((ModelView) view.getTag()));

                }
            });

        }
    }
}
