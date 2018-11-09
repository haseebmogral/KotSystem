package kot.amits.com.kotsystem.purchase_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.purchase_adapter.purchase_model;

public class purchase_adapter extends RecyclerView.Adapter<purchase_adapter.MyViewHolder> {



    private Context mContext;
    private List<purchase_model> albumList1;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description;


        public MyViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
        }

    }


    public purchase_adapter(Context mContext, List<purchase_model> albumList1) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
        Toast.makeText(mContext, "samana eede ethi", Toast.LENGTH_SHORT).show();
//        this.itemClickListener=itemClickListener;

    }

    @Override
    public purchase_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_adapter_layout, parent, false);

        return new purchase_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final purchase_adapter.MyViewHolder holder, final int position) {
        final purchase_model album = albumList1.get(position);
        holder.description.setText(album.getP_description());
        Toast.makeText(mContext, "ash eedeyum ethi"+albumList1.get(position).getP_description(), Toast.LENGTH_SHORT).show();

//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int cat_id=album.getCat_id();
//
//                Toast.makeText(mContext,holder. title.getText().toString(), Toast.LENGTH_SHORT).show();
////                    claculate();
//                itemClickListener.onCustomItemClick(position,String.valueOf(cat_id));
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

}



