package kot.amits.com.kotsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import adapter.CustomAdapter;

public class Manager_Dashboard extends AppCompatActivity {

    public GridView gridview;
    private static String [] app_name = {"Category", "Items", "Clock", "Contacts", "Docs", "Earth", "Fit", "Home",
            "Keep", "Maps", "Phone", "Settings", "Sheet", "Slides", "Translate", "Youtube"};
    private static int [] app_icon = {R.drawable.circle_shape, R.drawable.items, R.drawable.circle_shape, R.drawable.circle_shape,
            R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape,
            R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape,
            R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__dashboard);


        gridview=(GridView)findViewById(R.id.gridView);
        // setting up Adapter tp GridView
        gridview.setAdapter(new CustomAdapter(this,app_name,app_icon));


    }
}
