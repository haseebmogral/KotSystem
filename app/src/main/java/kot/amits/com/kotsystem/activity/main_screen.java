package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class main_screen extends AppCompatActivity {
    DBmanager dBmanager;
    Button start;


    ImageView myButton;
    View myView;
    boolean isUp;
    LinearLayout order;
    CarouselView carouselView;


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
//         slider = findViewById(R.id.slider);

//         myView = findViewById(R.id.my_view);
//        myButton = findViewById(R.id.my_button);

        // initialize as invisible (could also do in xml)
//        myView.setVisibility(View.INVISIBLE);
//        isUp = false;





//        FabOptions fabOptions = (FabOptions) findViewById(R.id.fab_options);
//        fabOptions.setButtonsMenu(R.menu.your_fab_buttons);

//        carouselView = (CarouselView) findViewById(R.id.carouselView);
//        carouselView.setPageCount(sampleImages.length);
//
//        carouselView.setImageListener(imageListener);


        dBmanager = new DBmanager(this);
        dBmanager.open();

        order=(LinearLayout) findViewById(R.id.order);


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dBmanager.CART_ID = String.valueOf(dBmanager.add_to_cart_details(dBmanager.get_date(), dBmanager.get_time()));
                Intent intent = new Intent(main_screen.this, Order_screen.class);
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
