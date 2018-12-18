package kot.amits.com.kotsystem.feedback_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.activity.Expense_activity;
import kot.amits.com.kotsystem.activity.manage_feedback;
import kot.amits.com.kotsystem.activity.manage_feedback_items;
import kot.amits.com.kotsystem.expense_adapter.view_expense_model;


public class view_feedback_adapter extends RecyclerView.Adapter<view_feedback_adapter.MyViewHolder> {

    private Context mContext;
    private List<view_feedback_model> albumList1;

    public view_feedback_adapter(manage_feedback mContext, List<view_feedback_model> albumList1) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bill_no,bill_date,cust_name,ambience_rating_text,staff_rating_text,review,click;
        public RatingBar ambience_rating,staff_rating;
        public MyViewHolder(View view) {
            super(view);
            bill_no = (TextView) view.findViewById(R.id.bill_number);
            bill_date = (TextView) view.findViewById(R.id.f_date);
            cust_name = (TextView) view.findViewById(R.id.customer_name);
            ambience_rating = (RatingBar) view.findViewById(R.id.ambience_rating);
            staff_rating = (RatingBar) view.findViewById(R.id.staff_rating);

            ambience_rating_text=view.findViewById(R.id.ambience_rating_text);
            staff_rating_text=view.findViewById(R.id.staff_rating_text);
            review=view.findViewById(R.id.review);
            click=view.findViewById(R.id.click);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final view_feedback_model album = albumList1.get(position);
        holder.bill_no.setText(album.getBillno());
        holder.bill_date.setText(album.getDate());
        holder.cust_name.setText(album.getCustomer_name());
        holder.ambience_rating.setRating(Float.parseFloat(String.valueOf(album.getAmbience_rating())));
        holder.staff_rating.setRating(Float.parseFloat(String.valueOf(album.getStaff_rating())));

        holder.ambience_rating_text.setText(String.valueOf(album.getAmbience_rating()));
        holder.staff_rating_text.setText(String.valueOf(album.getStaff_rating()));

        holder.review.setText("\"" + album.getReview() + "\"");

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,manage_feedback_items.class);
                intent.putExtra("f_id",album.getF_id());
                mContext.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return albumList1 == null ? 0 : albumList1.size();
    }




}
