package kot.amits.com.kotsystem.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
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
    LinearLayout order,active_orders,settings;
    CarouselView carouselView;

    Cursor orders;
    int[] sampleImages = {R.drawable.burg, R.drawable.burg};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_screen);

        isStoragePermissionGranted();


        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);
        dBmanager = new DBmanager(this);
        dBmanager.open();

        order=(LinearLayout) findViewById(R.id.order);
        active_orders=(LinearLayout) findViewById(R.id.active);
        settings=(LinearLayout) findViewById(R.id.settings);


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] type = new String[1];
                new FancyGifDialog.Builder(main_screen.this)
                        .setTitle("Select the type of order")
                        .setNegativeBtnText("Take away")
                        .setPositiveBtnBackground("#28e031")
                        .setPositiveBtnText("Dine in")
                        .setNegativeBtnBackground("#e02828")
                        .setGifResource(R.drawable.cooking)
                        .isCancellable(true)
                        //Pass your Gif here
                        .OnPositiveClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                type[0] ="Dine in";
                                dBmanager.ORDER_TYPE =type[0];
                                dBmanager.CART_ID = String.valueOf(dBmanager.add_to_cart_details());
                                Intent intent = new Intent(main_screen.this, Order_screen.class);
                                intent.putExtra("mode","new_order");
                                startActivity(intent);
                            }
                        })
                        .OnNegativeClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                type[0] ="Take away";
                                dBmanager.ORDER_TYPE =type[0];
                                dBmanager.CART_ID = String.valueOf(dBmanager.add_to_cart_details());
                                Intent intent = new Intent(main_screen.this, Order_screen.class);
                                intent.putExtra("mode","new_order");
                                startActivity(intent);
                                }
                        })
                        .build();



            }
        });

        active_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders=dBmanager.get_active_orders();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(main_screen.this);
                builderSingle.setIcon(R.drawable.category_back);
                builderSingle.setTitle("Select Bill");
                final ArrayList<String> items =new ArrayList<String>();
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(main_screen.this, android.R.layout.simple_list_item_1);

                if (orders.getCount()<=0){

                }
                else{
                    while (orders.moveToNext()){
                        String order_type=orders.getString(orders.getColumnIndex(DBHelper.cart_type));
                        String order_id=String.valueOf(orders.getInt(orders.getColumnIndex(DBHelper.cart_id)));
                        items.add(order_id);
                        arrayAdapter.add(order_id+"  -  "+order_type);

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
                        String cart_id = items.get(which);
                        dBmanager.CART_ID=cart_id;
                        Log.d("cart_id",cart_id);
                        Intent intent=new Intent(main_screen.this,Order_screen.class);
                        intent.putExtra("mode","active_order");
                        startActivity(intent);

                    }
                });
                builderSingle.show();

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(main_screen.this,login_activity.class);
                startActivity(intent);
            }
        });


    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
//            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

}
