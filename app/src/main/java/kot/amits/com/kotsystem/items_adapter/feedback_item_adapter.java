package kot.amits.com.kotsystem.items_adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class feedback_item_adapter extends RecyclerView.Adapter<feedback_item_adapter.MyViewHolder>
{

    private Context mContext;
    private List<cart_items> cart_items;
    SmileRating smileRating;
    int rate;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemname,price;
        public ImageView itemimage;
        public RelativeLayout relativeLayout;


        public MyViewHolder(View view) {
            super(view);
            smileRating=view.findViewById(R.id.smile_rating);
            itemname = (TextView) view.findViewById(R.id.item_name);


        }
    }



    public feedback_item_adapter(Context mContext,List<cart_items> cart_items) {
        this.mContext = mContext;
        this.cart_items = cart_items;
    }

    @Override
    public feedback_item_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_items_adapter, parent, false);
        return new feedback_item_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final feedback_item_adapter.MyViewHolder holder, final int position) {
        final cart_items album = cart_items.get(position);
        holder.itemname.setText(album.get_name());

        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        rate=SmileRating.BAD+1;
//                            Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        rate=SmileRating.GOOD+1;
//                            Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        rate=SmileRating.GREAT+1;
//                            Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        rate=SmileRating.OKAY+1;
//                            Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        rate=SmileRating.TERRIBLE+1;
//                            Log.i(TAG, "Terrible");
                        break;
                }
                album.setRate(rate);
                Log.d("rate_from_model", String.valueOf(album.getrate()));

            }
        });






    }

    @Override
    public int getItemCount() {
        return cart_items.size();
    }









}
