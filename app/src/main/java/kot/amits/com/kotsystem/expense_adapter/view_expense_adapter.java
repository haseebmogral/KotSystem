package kot.amits.com.kotsystem.expense_adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.activity.Add_supplier;
import kot.amits.com.kotsystem.activity.Expense_activity;


public class view_expense_adapter extends RecyclerView.Adapter<view_expense_adapter.MyViewHolder> {

    private Context mContext;
    private List<view_expense_model> albumList1;

    public view_expense_adapter(Expense_activity mContext, List<view_expense_model> albumList1) {

        this.mContext = mContext;
        this.albumList1 = albumList1;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView exp_type,date,desc,amount,slno;
        public ImageView itemimage;
        public RelativeLayout relativeLayout;
        public CardView itemcard;



        public MyViewHolder(View view) {
            super(view);
            exp_type = (TextView) view.findViewById(R.id.exp_type);
            slno = (TextView) view.findViewById(R.id.slno);
            desc = (TextView) view.findViewById(R.id.desc);
            date = (TextView) view.findViewById(R.id.date);
            amount = (TextView) view.findViewById(R.id.amount);
//            supplier_address=(TextView)view.findViewById(R.id.supplier_address);
//            supplier_contact=(TextView)view.findViewById(R.id.supplier_contact);
//            itemimage=(ImageView)view.findViewById(R.id.item_image);
//            relativeLayout=(RelativeLayout) view.findViewById(R.id.relative_layout);
//            itemcard=(CardView)view.findViewById(R.id.item_card);



        }
    }
//
//
//
    @Override
    public view_expense_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_expense_layout, parent, false);
        return new view_expense_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final view_expense_adapter.MyViewHolder holder, final int position) {
        final view_expense_model album = albumList1.get(position);
        holder.exp_type.setText(album.getExp_type());
        holder.slno.setText(album.getSlno());
        holder.desc.setText(album.getExp_desc());
        holder.date.setText(album.getDate());
        holder.amount.setText(album.getExp_amount());
//        holder.supplier_contact.setText(album.getSupplier_contact());



//        holder.itemcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });




    }

    @Override
    public int getItemCount() {
        return albumList1 == null ? 0 : albumList1.size();
    }




}
