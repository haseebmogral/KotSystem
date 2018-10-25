package kot.amits.com.kotsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/ha.
 */
public class AlbumsAdapter1 extends RecyclerView.Adapter<AlbumsAdapter1.MyViewHolder> {
    private Context mContext;
    private List<Album1> albumList1;
    boolean check=false;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title,id;
        public LinearLayout relativeLayout;
        ImageView imageView;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
//            id = (TextView) view.findViewById(R.id.id);
//            relativeLayout = (LinearLayout) view.findViewById(R.id.relative);
//            imageView=(ImageView)view.findViewById(R.id.image);
//
//            relativeLayout.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

//            TextView id=(TextView)v.findViewById(R.id.id);
//            TextView title=(TextView)v.findViewById(R.id.title);
//            if (id.getText().toString().equals("0")){
//                Intent intent=new Intent(mContext,detail_list.class);
//                intent.putExtra("id",id.getText().toString());
//                mContext.startActivity(intent);
//            }
//            else{
//                Intent intent=new Intent(mContext,detail_activity.class);
//                intent.putExtra("id",id.getText().toString());
//                mContext.startActivity(intent);
//            }
//            Toast.makeText(mContext, title.getText().toString(), Toast.LENGTH_SHORT).show();
//            if (utility.isNetworkConnected(mContext)==true){
//                if (utility.cat_id.equals(title.getText().toString())){
//
//                }
//                else{
//                    if(mContext instanceof MainActivity){
//                        ((MainActivity) mContext).clear_album();
//                        utility.cat_id=title.getText().toString();
//                        ((MainActivity)mContext).load(title.getText().toString(),"0");
//                    }
//                }
//            }
//            else{
//                Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
//
//            }



        }

    }


    public AlbumsAdapter1(Context mContext, List<Album1> albumList1) {

        this.mContext = mContext;
        this.albumList1 = albumList1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album1 album = albumList1.get(position);
        holder.title.setText(album.getName());
//        Toast.makeText(mContext, album.getName(), Toast.LENGTH_SHORT).show();
//        holder.id.setText(album.getId());










    }



    @Override
    public int getItemCount() {
        return albumList1.size();
    }

    public void removeItem(int position) {
        albumList1.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, albumList1.size());
    }
}
