package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.ArrayList;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.items_adapter.cart_items;
import kot.amits.com.kotsystem.items_adapter.feedback_item_adapter;
import kot.amits.com.kotsystem.R;


public class feedback_activity extends AppCompatActivity {

    RecyclerView recyclerView;
    private feedback_item_adapter item_adapter;
    DBmanager dBmanager;
    ArrayList<cart_items> cart_lis;
    EditText custom_feedback,customer_name,contact;

    SmileRating ambience_rater,satff_rater;
    int amibience_rate,staff_rate;
    Button button;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_activity);
        recyclerView=findViewById(R.id.recylerview);
        button=findViewById(R.id.button);
        custom_feedback=findViewById(R.id.custom_review);
        customer_name=findViewById(R.id.customer_name);
        contact=findViewById(R.id.contact);
        dBmanager=new DBmanager(this);
        dBmanager.open();
//        Toast.makeText(this,String.valueOf(dBmanager.cart_list.size()) , Toast.LENGTH_SHORT).show();

        ambience_rater=findViewById(R.id.ambience_rater);
        satff_rater=findViewById(R.id.staff_rater);
        ambience_rater.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        amibience_rate=SmileRating.BAD+1;
//                            Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        amibience_rate=SmileRating.GOOD+1;
//                            Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        amibience_rate=SmileRating.GREAT+1;
//                            Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        amibience_rate=SmileRating.OKAY+1;
//                            Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        amibience_rate=SmileRating.TERRIBLE+1;
//                            Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

        satff_rater.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        staff_rate=SmileRating.BAD+1;
//                            Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        staff_rate=SmileRating.GOOD+1;
//                            Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        staff_rate=SmileRating.GREAT+1;
//                            Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        staff_rate=SmileRating.OKAY+1;
//                            Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        staff_rate=SmileRating.TERRIBLE+1;
//                            Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dBmanager.feedback(customer_name.getText().toString(),contact.getText().toString(),dBmanager.CART_ID,cart_lis,amibience_rate,staff_rate,custom_feedback.getText().toString());
                new FancyGifDialog.Builder(feedback_activity.this)
                        .setTitle("Thanks for feedback & visit again")
                        .setMessage("")
                        .setPositiveBtnBackground("#FF4081")
                        .setPositiveBtnText("Ok")
                        .setGifResource(R.drawable.thanks)   //Pass your Gif here
                        .isCancellable(false)
                        .OnPositiveClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                finish();
                                Intent i = new Intent(feedback_activity.this, main_screen.class);
// set the new task and clear flags
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        })
                        .build();
            }
        });


        cart_lis=dBmanager.cart_list;
        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);

        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(feedback_activity.this, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        linearLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.d("size", String.valueOf(cart_lis.size()));

        item_adapter=new feedback_item_adapter(this,cart_lis);
        recyclerView.setAdapter(item_adapter);

        item_adapter.notifyDataSetChanged();



    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
