package com.sysnova.bracnurseryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class ViewProductsListRecyclerViewAdapter extends RecyclerView.Adapter<ViewProductsListRecyclerViewAdapter.ViewHolder> {
    List<ViewProductDetailsModal> approvalProductDetailsModals;
    Context mContext;

    public ViewProductsListRecyclerViewAdapter(List<ViewProductDetailsModal> approvalProductDetailsModals, Context mContext) {
        this.approvalProductDetailsModals = approvalProductDetailsModals;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_vieworder_product_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.slTextView.setText(String.valueOf(position + 1));
        holder.productTextView.setText(approvalProductDetailsModals.get(position).getProduct());
        holder.uomsymbolTextView.setText(approvalProductDetailsModals.get(position).getUom());
        holder.bagTextView.setText(approvalProductDetailsModals.get(position).getQty());
        holder.rateTextView.setText(approvalProductDetailsModals.get(position).getPrice());
        holder.takaTextView.setText(approvalProductDetailsModals.get(position).getTotal());
    }

    @Override
    public int getItemCount() {
        return approvalProductDetailsModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView slTextView,productTextView,bagTextView,rateTextView,takaTextView,uomsymbolTextView;

        public ViewHolder(View itemView){
            super(itemView);
            slTextView  = itemView.findViewById(R.id.sl_id);
            productTextView = itemView.findViewById(R.id.product_name_id);
            uomsymbolTextView = itemView.findViewById(R.id.uom_id);
            bagTextView = itemView.findViewById(R.id.qty_bag_id);
            rateTextView = itemView.findViewById(R.id.rate_id);
            takaTextView = itemView.findViewById(R.id.total_taka_id);
        }
    }
}
