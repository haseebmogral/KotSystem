package kot.amits.com.kotsystem.category_adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kot.amits.com.kotsystem.R;

public class cat_adapter extends RecyclerView.Adapter<cat_adapter.MyViewHolder> {


    int selected_position = 0;
    private Context mContext;
    private List<cat_album> albumList1;

    private CustomItemClickListener itemClickListener;


    public interface CustomItemClickListener{
        void onCustomItemClick(int position,String type);
        void onCustomItemLongClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.cat_name);
        }

    }






    public cat_adapter(Context mContext, List<cat_album> albumList1, CustomItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
        this.itemClickListener=itemClickListener;

    }

    @Override
    public cat_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_adapter, parent, false);


        return new cat_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final cat_adapter.MyViewHolder holder, final int position) {
        final cat_album album = albumList1.get(position);
//        if (selected_position==position){
//            holder.title.setTextColor(Color.parseColor("#4682B4"));
//            holder.title.setTypeface(holder.title.getTypeface(), Typeface.BOLD);
//        }
//        else {
//            holder.title.setTextColor(Color.parseColor("#000000"));
//            holder.title.setTypeface(holder.title.getTypeface(), Typeface.NORMAL);
//        }
        itemClickListener.onCustomItemClick(position,String.valueOf(albumList1.get(0).getCat_id()));



        holder.title.setText(album.getName());

                holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                unselect();
//                album.setSelected(true);
//
//                if (album.isSelected()==true){
//                    Log.d("aaa","this album is selected");
//                    holder.title.setTextColor(Color.parseColor("#4682B4"));
//                    holder.title.setTypeface(holder.title.getTypeface(), Typeface.BOLD);
//                }
//                else if(album.isSelected()==false){
//                    Log.d("aaa","this album is not selected");
//                    holder.title.setTextColor(Color.parseColor("#000000"));
//                    holder.title.setTypeface(holder.title.getTypeface(), Typeface.NORMAL);
//                }

//                if (position == RecyclerView.NO_POSITION) return;
//                holder.itemView.setBackgroundColor(selected_position == position ? Color.GREEN : Color.TRANSPARENT);


//                // Updating old as well as new positions
//                notifyItemChanged(selected_position);
//                selected_position = position;

                int cat_id=album.getCat_id();
                itemClickListener.onCustomItemClick(position,String.valueOf(cat_id));
            }
        });


    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

    public void unselect(){
        for (cat_album a:albumList1 ){
            int s;
            if (a.isSelected()){
                s=1;
            }
            else{
                s=0;
            }
            Log.d("aaa",String.valueOf(s));
            a.setSelected(false);
        }
    }

}



