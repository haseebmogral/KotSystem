package kot.amits.com.kotsystem.sales_items_adapter_model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.activity.view_bill_items;
import kot.amits.com.kotsystem.sales_adapter_model.sales_modal;

public class sales_items_adapter extends RecyclerView.Adapter<sales_items_adapter.MyViewHolder> {



    private Context mContext;
    private List<sales_items_modal> albumList1;
    private TextView total;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sl,name,qty,rate,total;


        public MyViewHolder(View view) {
            super(view);
            sl = (TextView) view.findViewById(R.id.slno);
            qty = (TextView) view.findViewById(R.id.qty);
            name = (TextView) view.findViewById(R.id.item_name);
            rate = (TextView) view.findViewById(R.id.rate);
            total = (TextView) view.findViewById(R.id.total);


        }

    }


    public sales_items_adapter(Context mContext, List<sales_items_modal> albumList1, TextView total) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
        this.total = total;

    }

    @Override
    public sales_items_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_items_adapter, parent, false);

        return new sales_items_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final sales_items_adapter.MyViewHolder holder, final int position) {
        final sales_items_modal album = albumList1.get(position);
        holder.sl.setText(String.valueOf(position+1));
        holder.name.setText(album.getItem_name());
        holder.qty.setText(album.getQty());
        holder.rate.setText(album.getRate());
        holder.total.setText(String.valueOf(album.getTotal()));
        get_total(albumList1);
    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

    public void get_total(List<sales_items_modal>  sales_modal){
        float amount=0;
        for (sales_items_modal a:sales_modal){
            amount=a.getTotal()+amount;
        }

       total.setText(String.valueOf(amount));

    }

}



