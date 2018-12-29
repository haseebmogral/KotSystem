package kot.amits.com.kotsystem.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.feedback_adapter.view_feedback_adapter;
import kot.amits.com.kotsystem.feedback_adapter.view_feedback_model;

public class manage_feedback extends AppCompatActivity {


    private view_feedback_adapter view_feedback_adapter;
    private List<view_feedback_model> view_feedback_model;
    private TextView emptyview;
    DBmanager mydb;
    Cursor get_feedbacks_from_DB,branch_feedbacks,food_feedback=null;



    TextView ambience_pie_chart,staff_pie_chart,food_pie_chart;

    RecyclerView feedback_recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_feedback);

        feedback_recycler=findViewById(R.id.feedback_recycler);
        feedback_recycler.setNestedScrollingEnabled(false);

        emptyview = findViewById(R.id.empty);
        emptyview.setVisibility(View.INVISIBLE);

        mydb = new DBmanager(this);
        mydb.open();

        branch_feedbacks=mydb.get_branch_feedbacks();
        food_feedback=mydb.get_branch_food_feedbacks();


        ambience_pie_chart = (TextView) findViewById(R.id.sales);
        staff_pie_chart = (TextView) findViewById(R.id.purchase);
        food_pie_chart = (TextView) findViewById(R.id.expense);

//        while (food_feedback.moveToNext()){
//            Float food= Float.valueOf(food_feedback.getString(food_feedback.getColumnIndex("food_rate")));
//            food_pie_chart.setText(String.format("%.01f", food)+"/5");
//
//        }
//        while (branch_feedbacks.moveToNext()){
//            Float cafe= Float.valueOf(branch_feedbacks.getString(branch_feedbacks.getColumnIndex("ambience")));
//            Float staff= Float.valueOf(branch_feedbacks.getString(branch_feedbacks.getColumnIndex("staff")));
//
//            ambience_pie_chart.setText(String.format("%.01f", cafe)+"/5");
//            staff_pie_chart.setText(String.format("%.01f", staff)+"/5");
//        }



        if (food_feedback==null || branch_feedbacks==null){
            Log.d("check","if");
        }
        else{
            Log.d("check","else");

            branch_feedbacks.moveToFirst();
            food_feedback.moveToFirst();
            Float food= Float.valueOf(food_feedback.getString(food_feedback.getColumnIndex("food_rate")));
            Float cafe= Float.valueOf(branch_feedbacks.getString(branch_feedbacks.getColumnIndex("ambience")));
            Float staff= Float.valueOf(branch_feedbacks.getString(branch_feedbacks.getColumnIndex("staff")));
            Log.d("cafe", String.valueOf(cafe)+" "+staff+" "+food);

            ambience_pie_chart.setText(String.format("%.01f", cafe)+"/5");
            staff_pie_chart.setText(String.format("%.01f", staff)+"/5");
            food_pie_chart.setText(String.format("%.01f", food)+"/5");
        }







        load_feedback();

    }

    private void load_feedback() {


        get_feedbacks_from_DB = mydb.get_feedback();


        if (mydb.get_feedback().getCount() <= 0) {

            emptyview.setVisibility(View.VISIBLE);


        } else {

            emptyview.setVisibility(View.INVISIBLE);
            view_feedback_model = new ArrayList<>();
            view_feedback_adapter = new view_feedback_adapter(manage_feedback.this, view_feedback_model);


            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            linearLayoutManager.setReverseLayout(false);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(manage_feedback.this, 1);
            //feedback_recycler.addItemDecoration(new SimpleDividerItemDecoration(manage_feedback));

            feedback_recycler.setLayoutManager(mLayoutManager);
            feedback_recycler.setAdapter(view_feedback_adapter);


            view_feedback_model cat;

            int i = 1;

            float total = 0;


            while (get_feedbacks_from_DB.moveToNext()) {

                String order_id = get_feedbacks_from_DB.getString(get_feedbacks_from_DB.getColumnIndex(DBHelper.feedback_order_id));
                String f_id = get_feedbacks_from_DB.getString(get_feedbacks_from_DB.getColumnIndex(DBHelper.feedback_id));
                Double ambience = Double.valueOf(get_feedbacks_from_DB.getString(get_feedbacks_from_DB.getColumnIndex(DBHelper.ambience_rating)));
                Double staff_raing = Double.valueOf(get_feedbacks_from_DB.getString(get_feedbacks_from_DB.getColumnIndex(DBHelper.staff_rating)));
                String review = get_feedbacks_from_DB.getString(get_feedbacks_from_DB.getColumnIndex(DBHelper.feedback_review));
                String customer = get_feedbacks_from_DB.getString(get_feedbacks_from_DB.getColumnIndex(DBHelper.customer_name));
                String date = get_feedbacks_from_DB.getString(get_feedbacks_from_DB.getColumnIndex(DBHelper.date));


                cat = new view_feedback_model(f_id, order_id,date, customer, ambience, staff_raing,review);
                view_feedback_model.add(cat);

                i++;
            }


            String final_amount= NumberFormat.getNumberInstance(Locale.US).format(total);

//
//            DecimalFormat formatter = new DecimalFormat("####,##,#");
//            String yourFormattedString = formatter.format(total);



//            amount.setText(String.valueOf(final_amount));

            view_feedback_adapter.notifyDataSetChanged();


        }

    }
}
