package kot.amits.com.kotsystem.manage_employee_adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.activity.Add_supplier;
import kot.amits.com.kotsystem.activity.Manage_employee;
import kot.amits.com.kotsystem.view_supplier_adapter.view_supplier_model;

import static com.android.volley.VolleyLog.TAG;

public class manage_employee_adapter extends RecyclerView.Adapter<manage_employee_adapter.MyViewHolder>
//        implements Filterable
{

    private Context mContext;
    private List<manage_employee_model> albumList1;


    public manage_employee_adapter(Manage_employee mContext, List<manage_employee_model> albumList1) {

        this.mContext = mContext;
        this.albumList1 = albumList1;
//        this.cart_items = cart_items;

    }


    public interface CustomItemClickListener {
        void onCustomItemClick(int position);

        void onCustomItemLongClick(int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView emp_id, emp_name, emp_address, emp_desig, emp_contact,emp_salary,emp_doj,emp_status;
        public ImageView itemimage;
        public RelativeLayout relativeLayout;
        public CardView emp_card;


        public MyViewHolder(final View view) {
            super(view);

            emp_id = (TextView) view.findViewById(R.id.emp_id);
            emp_name = (TextView) view.findViewById(R.id.emp_name);
            emp_address = (TextView) view.findViewById(R.id.emp_address);
            emp_desig = (TextView) view.findViewById(R.id.emp_desig);
            emp_contact = (TextView) view.findViewById(R.id.emp_contact);

            emp_salary=(TextView)view.findViewById(R.id.emp_salary);
            emp_doj=(TextView)view.findViewById(R.id.employ_doj);
            emp_status=(TextView)view.findViewById(R.id.emp_status);
            emp_card=(CardView) view.findViewById(R.id.emp_card);
            
            emp_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    TextView id=v.findViewById(R.id.emp_id);
                    String employee_id=id.getText().toString();
                    Toast.makeText(mContext, employee_id, Toast.LENGTH_SHORT).show();

                }
            });

//            itemimage=(ImageView)view.findViewById(R.id.item_image);
//            relativeLayout=(RelativeLayout) view.findViewById(R.id.relative_layout);
//            itemcard=(CardView)view.findViewById(R.id.item_card);


        }
    }


    @Override
    public manage_employee_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_employee_layout, parent, false);
        return new manage_employee_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final manage_employee_adapter.MyViewHolder holder, final int position) {
        final manage_employee_model album = albumList1.get(position);
        holder.emp_name.setText(album.getEmp_name());
        holder.emp_address.setText(album.getEmp_address());
        holder.emp_desig.setText(album.getEmp_desig());
        holder.emp_contact.setText(album.getEmp_contact());
        holder.emp_salary.setText(album.getEmp_salray());
        holder.emp_doj.setText(album.getEmp_doj());
        holder.emp_id.setText(album.getEmp_id());

        Log.i("contact",album.getEmp_status());

        if (album.getEmp_status().equals("0"))




        {
            holder.emp_status.setText("Inactive");
            Drawable img = mContext.getResources().getDrawable( R.drawable.ic_offline );
            img.setBounds( 0, 0, 25, 25 );
            holder.emp_status.setCompoundDrawables( img, null, null, null );

        }
        else {

            holder.emp_status.setText("Active");
            Drawable img = mContext.getResources().getDrawable( R.drawable.online_icon );
            img.setBounds( 0, 0, 25, 25 );
            holder.emp_status.setCompoundDrawables( img, null, null, null );

        }

//        Toast.makeText(mContext, "coming value is "+
//                album.getSupplier_contact(), Toast.LENGTH_SHORT).show();


//        holder.itemcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

//


    }

    @Override
    public int getItemCount() {
        return albumList1 == null ? 0 : albumList1.size();
    }


}
