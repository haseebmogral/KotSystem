package kot.amits.com.kotsystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.category_model.DataModel;
import kot.amits.com.kotsystem.category_model.category_adapter;

public class category_selection extends AppCompatActivity {

    ArrayList dataModels;
    ListView listView;
    public category_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
        setTitle("Select Category");

        listView = (ListView) findViewById(R.id.listView);

        dataModels = new ArrayList();

        dataModels.add(new DataModel(1,"Apple Pie", false));
        dataModels.add(new DataModel(2,"Apple ", false));
        dataModels.add(new DataModel(3," Pie", false));
        dataModels.add(new DataModel(4," Pie", false));


        adapter = new category_adapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);

    }





    public void next(View view) {

    }
}




