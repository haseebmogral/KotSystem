package kot.amits.com.kotsystem.select_item_adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import kot.amits.com.kotsystem.constants.constant;


import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.activity.select_item;
import kot.amits.com.kotsystem.items_adapter.cart_items;
import kot.amits.com.kotsystem.items_adapter.item_album;

public class select_item_adapter extends RecyclerView.Adapter<select_item_adapter.MyViewHolder>
{

    private Context mContext;
    private List<select_item_album> albumList1;


    int  qty=1;


    private CustomItemClickListener itemClickListener;

    public select_item_adapter(select_item mContext, List<select_item_album> albumList1) {

        this.mContext = mContext;
        this.albumList1 = albumList1;
//        this.cart_items = cart_items;

    }


    public interface CustomItemClickListener{
        void onCustomItemClick(int position);
        void onCustomItemLongClick(int position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemname,price;
        public ImageView itemimage;
        public RelativeLayout relativeLayout;
        public CardView itemcard;



        public MyViewHolder(View view) {
            super(view);
            itemname = (TextView) view.findViewById(R.id.item_name);
            price=(TextView)view.findViewById(R.id.price);
            itemimage=(ImageView)view.findViewById(R.id.item_image);
            relativeLayout=(RelativeLayout) view.findViewById(R.id.relative_layout);
            itemcard=(CardView)view.findViewById(R.id.item_card);



        }
    }



    @Override
    public select_item_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_adapter, parent, false);
        return new select_item_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final select_item_adapter.MyViewHolder holder, final int position) {
        final select_item_album album = albumList1.get(position);
        holder.itemname.setText(album.getItem_name());
        holder.price.setText(album.getPrice());
        String image=constant.BASE_URL+constant.ITEM_IMAGE+album.getImage();
        Log.d("image_tag",image);
        Log.d("image_tag",album.getImage());

        Glide.with(mContext).load(image).into(holder.itemimage);
        if (album.isSelected()){
            holder.relativeLayout.setBackgroundColor(album.isSelected() ? Color.CYAN : Color.WHITE);
        }

        holder.itemcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("itemselection", String.valueOf(album.isSelected()));

                album.setSelected(!album.isSelected());
                holder.relativeLayout.setBackgroundColor(album.isSelected() ? Color.CYAN : Color.WHITE);

            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked","relativelayout");

            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList1 == null ? 0 : albumList1.size();
    }


}
