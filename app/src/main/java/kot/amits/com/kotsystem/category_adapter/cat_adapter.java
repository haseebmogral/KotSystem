package kot.amits.com.kotsystem.category_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kot.amits.com.kotsystem.R;

public class cat_adapter extends RecyclerView.Adapter<cat_adapter.MyViewHolder> {



    private Context mContext;
    private List<cat_album> albumList1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.cat_name);
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



    public cat_adapter(Context mContext, List<cat_album> albumList1) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
    }

    @Override
    public cat_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_adapter, parent, false);

        return new cat_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final cat_adapter.MyViewHolder holder, int position) {
        cat_album album = albumList1.get(position);
        holder.title.setText(album.getName());

    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

}



