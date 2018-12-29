package kot.amits.com.kotsystem.salary_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.sales_adapter_model.sales_modal;

public class salary_adapter extends RecyclerView.Adapter<salary_adapter.MyViewHolder> implements Filterable {



    private Context mContext;
    private List<salary_model> albumList1;
    private List<salary_model> filteredlist;
    TextView total;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String q=constraint.toString();
                Log.d("contraint",q);
                List<salary_model> filtered=new ArrayList<>();
                if (q.isEmpty()){
                    filtered.clear();
                    filtered=albumList1;
                }
                else {
                    String[] seperated=q.split("/");
                    if (seperated[1].equals("date")){
                        for (salary_model a:albumList1){
                            if (a.getSalary_date().contains(seperated[0])){
                                filtered.add(a);
                            }
                        }

                    }
                    else if (seperated[1].equals("employee")){
                        for (salary_model a:albumList1){
                            if (a.getName().contains(seperated[0])){
                                filtered.add(a);
                            }
                        }

                    }

                }
                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;          }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredlist = (ArrayList<salary_model>) results.values;
                notifyDataSetChanged();

            }
        };

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sl,name,salary_date,paid_date,amount;


        public MyViewHolder(View view) {
            super(view);
            sl = (TextView) view.findViewById(R.id.sl);
            name = (TextView) view.findViewById(R.id.name);
            salary_date = (TextView) view.findViewById(R.id.salary_date);
            paid_date = (TextView) view.findViewById(R.id.paid_date);
            amount = (TextView) view.findViewById(R.id.amount);
        }

    }




    public salary_adapter(Context mContext, List<salary_model> albumList1, TextView total) {
        this.mContext = mContext;
        this.albumList1 = albumList1;
        this.filteredlist = albumList1;
        this.total = total;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.salary_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final salary_model album = filteredlist.get(position);
        holder.sl.setText(String.valueOf(position+1));
        holder.name.setText(album.getName());
        holder.salary_date.setText(album.getSalary_date());
        holder.paid_date.setText(album.getPaid_date());
        holder.amount.setText(String.valueOf(album.getAmount()));
        get_total(filteredlist);

    }

    @Override
    public int getItemCount() {
        return filteredlist.size();
    }
    public void get_total(List<salary_model>  salary_model){
        float amount=0;
        for (salary_model a:salary_model){
            amount=a.getAmount()+amount;
        }
        total.setText(String.valueOf(amount));
    }


}



