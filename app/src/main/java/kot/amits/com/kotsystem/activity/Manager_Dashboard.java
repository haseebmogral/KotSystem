package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import kot.amits.com.kotsystem.adapter.CustomAdapter;
import kot.amits.com.kotsystem.R;

public class Manager_Dashboard extends AppCompatActivity {

    public GridView gridview;
    private static String [] app_name = {"Category", "Items", "Supplier", "Purchase", "Expense", "Attendance", "Sales", "Salary",
            "Daily stock manage", "Feedback"};
    private static int [] app_icon = {R.drawable.circle_shape, R.drawable.items, R.drawable.circle_shape, R.drawable.circle_shape,
            R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape,
            R.drawable.circle_shape};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager__dashboard);


        gridview=(GridView)findViewById(R.id.gridView);
        // setting up Adapter tp GridView
        gridview.setAdapter(new CustomAdapter(this,app_name,app_icon));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:


                        Intent add_supplier=new Intent(Manager_Dashboard.this,Add_supplier.class);
                        startActivity(add_supplier);

                        break;
                    case 3:
                        Intent intent=new Intent(Manager_Dashboard.this,Purchase_Activity.class);
                        startActivity(intent);
                        break;


                }
            }
        });


    }
}
