package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class main_screen extends AppCompatActivity {
    DBmanager dBmanager;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        dBmanager=new DBmanager(this);
        dBmanager.open();

        start=(Button)findViewById(R.id.start_order);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               dBmanager.CART_ID= String.valueOf(dBmanager.add_to_cart_details(dBmanager.get_date(),dBmanager.get_time()));
                Intent intent=new Intent(main_screen.this, Order_screen.class);
                startActivity(intent);
            }
        });



    }
}
