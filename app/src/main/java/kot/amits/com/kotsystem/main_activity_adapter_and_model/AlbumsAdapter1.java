package kot.amits.com.kotsystem.main_activity_adapter_and_model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.items_adapter.item_adapter;

/**
 * Created by Ravi Tamada on 18/05/ha.
 */
public class AlbumsAdapter1 extends RecyclerView.Adapter<AlbumsAdapter1.MyViewHolder> {
    private Context mContext;
    private List<Album1> albumList1;
    TextView textView;





    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sl_no;
        public TextView title;
        public TextView qty;
        public TextView price;
        public TextView total;


        public MyViewHolder(View view) {
            super(view);
            sl_no = (TextView) view.findViewById(R.id.slno);
            title = (TextView) view.findViewById(R.id.item_name);
            qty = (TextView) view.findViewById(R.id.qty);
            price = (TextView) view.findViewById(R.id.price);
            total = (TextView) view.findViewById(R.id.total);
        }

    }
    public void claculate(){
        long tot=0;
        for (int i=0;i<albumList1.size();i++){
//            Toast.makeText(mContext, String.valueOf(albumList1.get(i).getTotal()), Toast.LENGTH_SHORT).show();
           tot=albumList1.get(i).getTotal()+tot;
           textView.setText(String.valueOf(tot));
        }
    }


    public AlbumsAdapter1(Context mContext, List<Album1> albumList1,TextView total) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
        this.textView=total;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_items_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album1 album = albumList1.get(position);
        holder.title.setText(album.getName());
        holder.qty.setText(String.valueOf(album.getQty()));
        holder.price.setText(String.valueOf(album.getprice()));
        holder.total.setText(String.valueOf(album.getTotal()));
        holder.sl_no.setText(String.valueOf(position+1));

        claculate();

    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

    }
