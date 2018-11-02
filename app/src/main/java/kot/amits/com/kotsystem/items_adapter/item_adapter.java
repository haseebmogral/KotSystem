package kot.amits.com.kotsystem.items_adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class item_adapter extends RecyclerView.Adapter<item_adapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<item_album> albumList1;
    private List<cart_items> cart_items;
    int  qty=1;


    private CustomItemClickListener itemClickListener;

    @Override
    public Filter getFilter() {
        return null;
    }

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
        holder.price.setText(album.getPrice());

        Glide.with(mContext).load("https://st2.depositphotos.com/2808141/10024/i/950/depositphotos_100246242-stock-photo-green-spinach-kale-detox-smoothie.jpg").into(holder.itemimage);

       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, albumList1.get(position).getid(), Toast.LENGTH_SHORT).show();
                final kot.amits.com.kotsystem.items_adapter.cart_items[] items = new cart_items[1];
                final int cart_id= Integer.parseInt(dBmanager.CART_ID);
                final int item_id= Integer.parseInt(album.getid());
                final long price= Long.parseLong(album.getPrice());
                final long[] total = {0};
                final String name=album.getItem_name();


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.add_to_cart_dialog, null);
                dialogBuilder.setView(dialogView);



                final EditText edt = (EditText) dialogView.findViewById(R.id.quantity);
                final Button plus = (Button) dialogView.findViewById(R.id.plus);
                final Button minus = (Button) dialogView.findViewById(R.id.minus);



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
                        qty=qty-1;
                        edt.setText(String.valueOf(qty));
                    }
                });

//                dialogBuilder.setTitle("Custom dialog");
//                dialogBuilder.setMessage("Enter text below");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        total[0] =qty*price;

                        items[0] =new cart_items(cart_id,name,item_id,price,qty,total[0]);
                        cart_items.add(items[0]);
                        itemClickListener.onCustomItemClick(position);
                        //do something with edt.getText().toString();
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


}
