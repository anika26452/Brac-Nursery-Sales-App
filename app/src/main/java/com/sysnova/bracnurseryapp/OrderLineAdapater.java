package com.sysnova.bracnurseryapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sysnova.bracnurseryapp.OrderLineModel;

import java.util.Date;
import java.util.List;

public class OrderLineAdapater extends RecyclerView.Adapter<OrderLineAdapater.MyViewHolder> {

    private List<OrderLineModel> medium;
    private Context context;
    DatabaseHelper databaseHelper;

    public OrderLineAdapater(List<OrderLineModel> medium, Context context) {
        this.medium = medium;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_order_line_adapater, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        OrderLineModel model = medium.get(position);
        //holder.code.setText(model.getCode());
        holder.name.setText(" "+model.getName());
        holder.qty.setText(String.valueOf(model.getQty()));
        holder.total.setText(String.valueOf(model.getTotal()));
        holder.serial.setText(String.valueOf(position+1));
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteOneProductOrderLine(model.getId());
                int newPosition = holder.getAbsoluteAdapterPosition();
                medium.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, medium.size());
                double grandTotal=getTotal(medium);
                int totalQty=getTotaQty(medium);


                Intent intent = new Intent("custom-message");
                intent.putExtra("TOTAL",grandTotal);
                intent.putExtra("TOTALQTY",totalQty);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int gottenQTY =Integer.parseInt(holder.qty.getText().toString()) ;
                double gottenTotal =Double.parseDouble(holder.total.getText().toString()) ;
                gottenQTY++;
                gottenTotal=(Double)(model.getPrice()*gottenQTY);

                try{
                    if(databaseHelper.updateProductOrderLine(model.getId(),gottenQTY,gottenTotal)) {
              //          notifyDataSetChanged();
                        holder.qty.setText(String.valueOf(gottenQTY));
                        holder.total.setText(String.valueOf(gottenTotal));
                        Intent intent = new Intent("custom-message");
                        loadArrayList();
                        intent.putExtra("TOTAL",getTotal(medium));
                        intent.putExtra("TOTALQTY",getTotaQty(medium));

                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                     //   toast("Clicked"+getTotal(medium)+" "+getTotaQty(medium));
                    }
                    else {
                        toast("Could not update ");
                    }
                }catch (Exception e) {
                    toast(e.getMessage());
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gottenQTY =Integer.parseInt(holder.qty.getText().toString()) ;
                double gottenTotal =Double.parseDouble(holder.total.getText().toString()) ;

                if(gottenQTY==1) {
                    toast("Minimum 1 unit must be ordered");
                }
                else {
                    gottenQTY--;
                    gottenTotal=(Double)(model.getPrice()*gottenQTY);
                    if(databaseHelper.updateProductOrderLine(model.getId(),gottenQTY,gottenTotal)) {
                        holder.qty.setText(String.valueOf(gottenQTY));
                        holder.total.setText(String.valueOf(gottenTotal));
                        Intent intent = new Intent("custom-message");
                        loadArrayList();
                        intent.putExtra("TOTAL",getTotal(medium));
                        intent.putExtra("TOTALQTY",getTotaQty(medium));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                    else{
                        toast("Could not update !");
                    }
                }
            }
        });
        holder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int qtyval;
                if(holder.qty.getText().toString().equals("") || holder.qty.getText().toString().equals(null)){
                    System.out.println("please insert a value");
                }else if(Integer.parseInt(holder.qty.getText().toString()) <= 0 || holder.qty.getText().toString().equals(".")) {
                    Toast.makeText(context, "quantity cannot be zero", Toast.LENGTH_LONG).show();
                } else {
                    qtyval = Integer.parseInt(holder.qty.getText().toString());
                    double gottenTotal =Double.parseDouble(holder.total.getText().toString()) ;
                    gottenTotal=(Double)(model.getPrice()*qtyval);

                    try{
                        if(databaseHelper.updateProductOrderLine(model.getId(),qtyval,gottenTotal)) {
           //                 notifyDataSetChanged();
////                            holder.qty.setText(String.valueOf(qtyval));
                            holder.total.setText(String.valueOf(gottenTotal));
                            Intent intent = new Intent("custom-message");
                            loadArrayList();
                            intent.putExtra("TOTAL",getTotal(medium));
                            intent.putExtra("TOTALQTY",getTotaQty(medium));

                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        } else {
                            toast("Could not update ");
                        }
                    }catch (Exception e) {
                        toast(e.getMessage());
                    }
                }
            }
        });
    }
    public double getTotal(List<OrderLineModel> list){
        double total=0.0;
        for(int i=0;i<list.size();i++){
            total=total+list.get(i).getTotal();
        }
        return total;
    }

    public void loadArrayList(){
        medium.clear();
        Cursor data = databaseHelper.getProductOrderLine();
        if(data.moveToFirst()){
            do{
                int id= data.getInt(0);
                String code=data.getString(1);
                String name=data.getString(2);
                int qty=data.getInt(3);
                double price=data.getDouble(4);
                double total=data.getDouble(5);
                OrderLineModel orderListModal = new OrderLineModel(id,code,name,qty,price,total);
                medium.add(orderListModal);
            }while(data.moveToNext());
        }
    }
    public int getTotaQty(List<OrderLineModel> list){
        int total=0;
        for(int i=0;i<list.size();i++){
            total=total+list.get(i).getQty();
        }
        return total;
    }
    void toast(String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return medium.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView code,name,total,serial;
        EditText qty;
        ImageView cross,add,minus;

        public MyViewHolder(View itemView) {

            super(itemView);
           // code = itemView.findViewById(R.id.codeTV);
            name = itemView.findViewById(R.id.nameTV);
            qty = itemView.findViewById(R.id.qtyTV);

            total = itemView.findViewById(R.id.totalTV);
            serial = itemView.findViewById(R.id.serial);
            cross=(ImageView)itemView.findViewById(R.id.crossTV);
            add=(ImageView)itemView.findViewById(R.id.addTV);
            minus=(ImageView)itemView.findViewById(R.id.minusTV);
        }
    }
}