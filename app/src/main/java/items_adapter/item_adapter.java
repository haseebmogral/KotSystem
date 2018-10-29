package items_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import category_adapter.cat_adapter;
import category_adapter.cat_album;
import kot.amits.com.kotsystem.R;

public class item_adapter extends RecyclerView.Adapter<item_adapter.MyViewHolder> {

    private Context mContext;
    private List<item_album> albumList1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemname,price;
        public ImageView itemimage;


        public MyViewHolder(View view) {
            super(view);
            itemname = (TextView) view.findViewById(R.id.item_name);
            price=(TextView)view.findViewById(R.id.price);
            itemimage=(ImageView)view.findViewById(R.id.item_image);
//            title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, title.getText().toString(), Toast.LENGTH_SHORT).show();
//                    claculate();
//                }
//            });
//        }

//    }

        }
    }



    public item_adapter(Context mContext, List<item_album> albumList1) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
    }

    @Override
    public item_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_adapter, parent, false);

        return new item_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final item_adapter.MyViewHolder holder, int position) {
        item_album album = albumList1.get(position);
        holder.itemname.setText(album.getItem_name());
        holder.price.setText(album.getPrice());

        Glide.with(mContext).load("https://www.pallasfoods.com/wp-content/uploads/2018/01/600-by-400-px.jpg").into(holder.itemimage);




    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

}
