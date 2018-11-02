package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class main_screen extends AppCompatActivity {
    DBmanager dBmanager;
    CardView start;
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.ic_burger, R.drawable.circular_button_s};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);


        dBmanager = new DBmanager(this);
        dBmanager.open();

        start   = (CardView) findViewById(R.id.start_order);
        start.setOnClickListener(new View.OnClickListener() {
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
}
