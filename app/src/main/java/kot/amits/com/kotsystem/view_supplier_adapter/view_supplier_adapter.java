package kot.amits.com.kotsystem.view_supplier_adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.activity.Add_supplier;
import kot.amits.com.kotsystem.activity.select_item;
import kot.amits.com.kotsystem.items_adapter.cart_items;
import kot.amits.com.kotsystem.items_adapter.item_album;
import kot.amits.com.kotsystem.view_supplier_adapter.view_supplier_model;

public class view_supplier_adapter extends RecyclerView.Adapter<view_supplier_adapter.MyViewHolder>
//        implements Filterable
{

    private Context mContext;
    private List<view_supplier_model> albumList1;



    public view_supplier_adapter(Add_supplier mContext, List<view_supplier_model> albumList1) {

        this.mContext = mContext;
        this.albumList1 = albumList1;
//        this.cart_items = cart_items;

    }




    public interface CustomItemClickListener{
        void onCustomItemClick(int position);
        void onCustomItemLongClick(int position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView supplier_name,supplier_address,supplier_contact;
        public ImageView itemimage;
        public RelativeLayout relativeLayout;
        public CardView itemcard;



        public MyViewHolder(View view) {
            super(view);
            supplier_name = (TextView) view.findViewById(R.id.supplier_name);
            supplier_address=(TextView)view.findViewById(R.id.supplier_address);
            supplier_contact=(TextView)view.findViewById(R.id.supplier_contact);
            itemimage=(ImageView)view.findViewById(R.id.item_image);
            relativeLayout=(RelativeLayout) view.findViewById(R.id.relative_layout);
            itemcard=(CardView)view.findViewById(R.id.item_card);



        }
    }



    @Override
    public view_supplier_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_supplier_layout_adapter, parent, false);
        return new view_supplier_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final view_supplier_adapter.MyViewHolder holder, final int position) {
        final view_supplier_model album = albumList1.get(position);
        holder.supplier_name.setText(album.getSupplier_name());
        holder.supplier_address.setText(album.getSupplier_address());
        holder.supplier_contact.setText(album.getSupplier_contact());
        Toast.makeText(mContext, "coming value is "+
                album.getSupplier_contact(), Toast.LENGTH_SHORT).show();



//        holder.itemcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

//


    }

    @Override
    public int getItemCount() {
        return albumList1 == null ? 0 : albumList1.size();
    }


}
