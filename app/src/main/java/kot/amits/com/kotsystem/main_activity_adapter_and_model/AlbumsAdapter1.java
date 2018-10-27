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

/**
 * Created by Ravi Tamada on 18/05/ha.
 */
public class AlbumsAdapter1 extends RecyclerView.Adapter<AlbumsAdapter1.MyViewHolder> {
    private Context mContext;
    private List<Album1> albumList1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, title.getText().toString(), Toast.LENGTH_SHORT).show();
                    claculate();
                }
            });
        }

    }
    public void claculate(){
        for (int i=0;i<albumList1.size();i++){
            Toast.makeText(mContext, albumList1.get(i).getName(), Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public int getItemCount() {
        return albumList1.size();
    }

    }
