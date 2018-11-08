package kot.amits.com.kotsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class main_screen extends AppCompatActivity {
    DBmanager dBmanager;
    Button start;

    ImageView myButton;
    View myView;
    boolean isUp;
    LinearLayout order,active_orders;
    CarouselView carouselView;

    Cursor orders;


    int[] sampleImages = {R.drawable.burg, R.drawable.burg};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_screen);


        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);
        dBmanager = new DBmanager(this);
        dBmanager.open();

        order=(LinearLayout) findViewById(R.id.order);
        active_orders=(LinearLayout) findViewById(R.id.active);


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dBmanager.CART_ID = String.valueOf(dBmanager.add_to_cart_details());
                Intent intent = new Intent(main_screen.this, Order_screen.class);
                intent.putExtra("mode","new_order");
                startActivity(intent);
            }
        });

        active_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders=dBmanager.get_active_orders();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(main_screen.this);
                builderSingle.setIcon(R.drawable.category_back);
                builderSingle.setTitle("Select Bill");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(main_screen.this, android.R.layout.simple_list_item_1);

                if (orders.getCount()<=0){

                }
                else{
                    while (orders.moveToNext()){
                        arrayAdapter.add(String.valueOf(orders.getInt(orders.getColumnIndex(DBHelper.cart_id))));

                    }
                }

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cart_id = arrayAdapter.getItem(which);
                        dBmanager.CART_ID=cart_id;
                        Intent intent=new Intent(main_screen.this,Order_screen.class);
                        intent.putExtra("mode","active_order");
                        startActivity(intent);

                    }
                });
                builderSingle.show();

            }
        });


    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };


//    public void slideUp(View view){
//        view.setVisibility(View.VISIBLE);
//        TranslateAnimation animate = new TranslateAnimation(
//                0,                 // fromXDelta
//                0,                 // toXDelta
//                view.getHeight(),  // fromYDelta
//                0);                // toYDelta
//        animate.setDuration(500);
//        animate.setFillAfter(true);
//        view.startAnimation(animate);
//    }
//
//    public void slideDown(View view){
//        myView.setVisibility(View.GONE);
//        TranslateAnimation animate = new TranslateAnimation(
//                0,                 // fromXDelta
//                0,                 // toXDelta
//                0,                 // fromYDelta
//                view.getHeight()); // toYDelta
//        animate.setDuration(500);
//        animate.setFillAfter(true);
//        view.startAnimation(animate);
//    }
//
//    public void onSlideViewButtonClick(View view) {
//        if (isUp) {
//            slideDown(myView);
//        } else {
//            slideUp(myView);
//            myView.setVisibility(View.GONE);
//
//        }
//        isUp = !isUp;
//    }

//    ImageListener imageListener = new ImageListener() {
//        @Override
//        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
//        }
//    };
}
