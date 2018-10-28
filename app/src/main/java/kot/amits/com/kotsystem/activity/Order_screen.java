package kot.amits.com.kotsystem.activity;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.idescout.sql.SqlScoutServer;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.main_activity_adapter_and_model.Album1;
import kot.amits.com.kotsystem.main_activity_adapter_and_model.AlbumsAdapter1;

public class Order_screen extends AppCompatActivity {
    DBHelper mydb;
    long id;

    ElegantNumberButton elegantNumberButton;

//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;

    private AlbumsAdapter1 adapter;
    private List<Album1> albumList;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_screen);

        SqlScoutServer.create(this, getPackageName());


        mydb = new DBHelper(this);
        id= mydb.insertContact("avil milk","drinks");
//        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

        recyclerView=(RecyclerView)findViewById(R.id.billrecycler);
        load_cat();
    }


    public void load_cat() {

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter1(this, albumList);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        Album1 a;

        for (int i = 0; i < 15; i++) {
            a = new Album1(String.valueOf(i));
            albumList.add(a);

        }

        adapter.notifyDataSetChanged();
    }

//    public void load(){
//        recyclerView.setHasFixedSize(true);
//        // use a linear layout manager
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        List<String> input = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            input.add("Test" + i);
//        }// define an adapter
//        mAdapter = new MyAdapter(input);
//        recyclerView.setAdapter(mAdapter);
//    }

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
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}
