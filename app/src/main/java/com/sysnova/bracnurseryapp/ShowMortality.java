package com.sysnova.bracnurseryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ShowMortality extends RecyclerView.Adapter<ShowMortality.ViewHolder> {
    List<ViewMortalityListModel> orderListActivities;
    Context mContext;
    String Order_ID,dateText;
    View itemView1=null;
    DatabaseHelper mdatabashelper;

    public ShowMortality(ArrayList<ViewMortalityListModel> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
        mdatabashelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_mortality_list_temp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.pcodevw.setText(orderListActivities.get(position).getPro_code());
        holder.pnamevw.setText(orderListActivities.get(position).getPro_name());
        holder.qtyvw.setText(String.valueOf(orderListActivities.get(position).getMor_qty()));
        holder.product_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdatabashelper.deleteOneMortality(orderListActivities.get(position).getM_product_id());
                int newPosition = holder.getAbsoluteAdapterPosition();
                orderListActivities.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, orderListActivities.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView pcodevw,pnamevw,qtyvw;
        LinearLayout tableRow;
        ImageView product_delete_img;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.orderListRow);
            pcodevw = itemView.findViewById(R.id.pcode);
            pnamevw = itemView.findViewById(R.id.pname);
            qtyvw  = itemView.findViewById(R.id.qty);
            product_delete_img = itemView.findViewById(R.id.product_delete_img);
        }
    }
}
