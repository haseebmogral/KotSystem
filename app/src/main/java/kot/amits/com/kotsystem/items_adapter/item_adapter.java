package kot.amits.com.kotsystem.items_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class item_adapter extends RecyclerView.Adapter<item_adapter.MyViewHolder> {

    private Context mContext;
    private List<item_album> albumList1;
    private List<cart_items> cart_items;

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

        Glide.with(mContext).load("https://www.pallasfoods.com/wp-content/uploads/2018/01/600-by-400-px.jpg").into(holder.itemimage);

       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onCustomItemClick(position);
                Toast.makeText(mContext, albumList1.get(position).getid(), Toast.LENGTH_SHORT).show();
                cart_items items;
                int cart_id= Integer.parseInt(dBmanager.CART_ID);
                int item_id= Integer.parseInt(album.getid());
                long price= Long.parseLong(album.getPrice());
                String name=album.getItem_name();

                items=new cart_items(cart_id,name,item_id,price,1,price);
                cart_items.add(items);

            }
        });


    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

}
