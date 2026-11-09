package com.sysnova.bracnurseryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ShoworderDateWise extends RecyclerView.Adapter<ShoworderDateWise.ViewHolder> {

    List<ViewOrderListModel> orderListActivities;
    Context mContext;
    String Order_ID,dateText;
    View itemView1=null;

    public ShoworderDateWise(ArrayList<ViewOrderListModel> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_order_list_temp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Order_ID=orderListActivities.get(position).getOrderno();

        holder.slTextView.setText(String.valueOf(position + 1));
        holder.serialTextView.setText(orderListActivities.get(position).getSerial());
        holder.ordernoTextView.setText(orderListActivities.get(position).getOrderno());
        holder.TotalPriceTextView.setText(orderListActivities.get(position).getT_total());
        holder.statusView.setText(orderListActivities.get(position).getDocstatus());

        holder.detailvw.setClickable(true);
        holder.detailvw.setEnabled(true);

        holder.detailvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FullViewOrderActivity.class);
                intent.putExtra("sales_order_id", orderListActivities.get(position).getT_sales_order_parent_ID());
                intent.putExtra("grandtotal", orderListActivities.get(position).getGrandtotal());
                intent.putExtra("t_qty", orderListActivities.get(position).getT_qty());
                intent.putExtra("t_total", orderListActivities.get(position).getT_total());
                intent.putExtra("dateOrdered", orderListActivities.get(position).getDateordered());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ordernoTextView,TotalPriceTextView,statusView,serialTextView, detailvw, slTextView;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            slTextView    = itemView.findViewById(R.id.sl_id);
            tableRow         = itemView.findViewById(R.id.orderListRow);
            detailvw = itemView.findViewById(R.id.date);
            ordernoTextView = itemView.findViewById(R.id.order_No);
            TotalPriceTextView = itemView.findViewById(R.id.total_price);
            statusView = itemView.findViewById(R.id.docstatus);
            serialTextView= itemView.findViewById(R.id.serial);
        }
    }
}
