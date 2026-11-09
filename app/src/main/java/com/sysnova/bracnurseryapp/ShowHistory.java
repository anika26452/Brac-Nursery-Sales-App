package com.sysnova.bracnurseryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ShowHistory extends RecyclerView.Adapter<ShowHistory.ViewHolder> {
    List<ViewHistoryListModel> orderListActivities;
    Context mContext;

    public ShowHistory(ArrayList<ViewHistoryListModel> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_history_list_temp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.doc_numvw.setText(orderListActivities.get(position).getSerial());
        holder.cdatevw.setText(orderListActivities.get(position).getReporting_date());
        holder.qtyvw.setText(String.valueOf(orderListActivities.get(position).getMortality_qty()));
    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView doc_numvw,cdatevw,qtyvw;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.orderListRow);
            doc_numvw = itemView.findViewById(R.id.doc_num);
            cdatevw = itemView.findViewById(R.id.cdate);
            qtyvw  = itemView.findViewById(R.id.qty);
        }
    }
}
