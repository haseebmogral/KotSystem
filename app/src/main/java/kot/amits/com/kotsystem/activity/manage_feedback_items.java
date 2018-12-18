package kot.amits.com.kotsystem.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class manage_feedback_items extends AppCompatActivity {
String f_id;
DBmanager dBmanager;
Cursor cursor;
LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_feedback_items);
        f_id =getIntent().getStringExtra("f_id");
        Log.d("f_id", f_id);
        dBmanager=new DBmanager(this);
        dBmanager.open();
        layout=findViewById(R.id.layout);
        cursor=dBmanager.get_feedback_items(f_id);
        Log.d("cursror_size", String.valueOf(cursor.getCount()));
        while (cursor.moveToNext()){
            String item_Name=cursor.getString(cursor.getColumnIndex(DBHelper.item_name));
            String rating=cursor.getString(cursor.getColumnIndex(DBHelper.rating));
            TextView textView=new TextView(this);
            TextView rate=new TextView(this);
            textView.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            rate.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            LinearLayout layout2 = new LinearLayout(this);

            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            layout2.setWeightSum(2);
            textView.setText(item_Name);
            rate.setText(rating+"/5");
            textView.setTextSize(20);
            rate.setTextSize(20);
            layout2.addView(textView);
            layout2.addView(rate);
            layout.addView(layout2);
            Log.d("rate",item_Name+"\n"+
            rating);
        }




    }
}
