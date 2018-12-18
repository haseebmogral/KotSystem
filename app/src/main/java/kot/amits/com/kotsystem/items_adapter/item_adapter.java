package kot.amits.com.kotsystem.items_adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class item_adapter extends RecyclerView.Adapter<item_adapter.MyViewHolder>
//        implements Filterable
{

    private Context mContext;
    private List<item_album> albumList1;
    private List<cart_items> cart_items;

    int  qty;


    private CustomItemClickListener itemClickListener;

    public interface CustomItemClickListener{
        void onCustomItemClick(int position);
        void onCustomItemLongClick(int position);
    }


    DBmanager dBmanager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemname,price;
        public ImageView itemimage;
        public RelativeLayout relativeLayout;


        public MyViewHolder(View view) {
            super(view);
            itemname = (TextView) view.findViewById(R.id.item_name);
            price=(TextView)view.findViewById(R.id.price);
            itemimage=(ImageView)view.findViewById(R.id.item_image);
            relativeLayout=(RelativeLayout) view.findViewById(R.id.relative_layout);

            dBmanager=new DBmanager(mContext);
        }
    }
    public item_adapter(Context mContext, List<item_album> albumList1,List<cart_items> cart_items,CustomItemClickListener customItemClickListener) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
        this.cart_items = cart_items;
        this.itemClickListener=customItemClickListener;

        Log.d("size_in_adapter",String.valueOf(albumList1.size()));

    }

    @Override
    public item_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_adapter, parent, false);
        return new item_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final item_adapter.MyViewHolder holder, final int position) {
        final item_album album = albumList1.get(position);

        holder.itemname.setText(album.getItem_name());
        holder.price.setText("₹ "+album.getPrice());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.title);
//        requestOptions.error();

        Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.mims_logo).error(R.drawable.mims_logo)).load(album.getImage()).into(holder.itemimage);

       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty=1;
//                Toast.makeText(mContext, albumList1.get(position).getid(), Toast.LENGTH_SHORT).show();
                final kot.amits.com.kotsystem.items_adapter.cart_items[] items = new cart_items[1];
                final int cart_id= Integer.parseInt(dBmanager.CART_ID);
                final int item_id= Integer.parseInt(album.getid());
                final long price= Long.parseLong(album.getPrice());
                final long[] total = {0};
                final String name=album.getItem_name();

                Log.d("value_in_adapter",String.valueOf(albumList1.size()));


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.add_to_cart_dialog, null);
                dialogBuilder.setView(dialogView);



                final TextView edt = (TextView) dialogView.findViewById(R.id.quantity);
                final FloatingActionButton plus = (FloatingActionButton) dialogView.findViewById(R.id.plus);
                final FloatingActionButton minus = (FloatingActionButton) dialogView.findViewById(R.id.minus);
                final TextView title = (TextView) dialogView.findViewById(R.id.title);

                title.setText("add "+album.getItem_name()+" to cart");




                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty= Integer.parseInt(edt.getText().toString());
                        qty=qty+1;
                        edt.setText(String.valueOf(qty));

                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty= Integer.parseInt(edt.getText().toString());
                        if (qty<=0){
                            qty=1;
                            edt.setText(String.valueOf(qty));
                        }
                        else{
                            qty=qty-1;
                            edt.setText(String.valueOf(qty));
                            qty= Integer.parseInt(edt.getText().toString());
                            if (qty<=0){
                                qty=1;
                                edt.setText(String.valueOf(qty));
                            }


                        }

                    }
                });

//                dialogBuilder.setTitle("select the quantity");
//                dialogBuilder.setMessage("select the quantity");

                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        qty= Integer.parseInt(edt.getText().toString());
                        if (has_value(item_id,qty)){
                            edt.setText(String.valueOf(qty));
                            Toast.makeText(mContext, "item already exists on cart", Toast.LENGTH_SHORT).show();
                            itemClickListener.onCustomItemClick(position);
                        }
                        else{
                            Toast.makeText(mContext, "item added to cart", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(mContext, String.valueOf(item_id)+"for else", Toast.LENGTH_SHORT).show();
                            qty= Integer.parseInt(edt.getText().toString());
                            total[0] =qty*price;
                            items[0] =new cart_items(cart_id,name,item_id,price,qty,total[0],"cart");
                            cart_items.add(items[0]);
                            itemClickListener.onCustomItemClick(position);
                        }
                        }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

    public boolean has_value(final int name,int qty) {
        for (cart_items transactionLine : cart_items) {
            if (transactionLine.getItem_id()==name) {
                    transactionLine.set_qty(qty);
                    transactionLine.set_total(transactionLine.get_price()*qty);
                Toast.makeText(mContext,String.valueOf(cart_items.indexOf(transactionLine)) , Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }








}
