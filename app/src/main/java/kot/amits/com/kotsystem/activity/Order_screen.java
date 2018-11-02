package kot.amits.com.kotsystem.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.idescout.sql.SqlScoutServer;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.category_adapter.cat_adapter;
import kot.amits.com.kotsystem.category_adapter.cat_album;
import kot.amits.com.kotsystem.items_adapter.cart_items;
import kot.amits.com.kotsystem.items_adapter.item_album;
import kot.amits.com.kotsystem.items_adapter.item_adapter;
import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.main_activity_adapter_and_model.Album1;
import kot.amits.com.kotsystem.main_activity_adapter_and_model.AlbumsAdapter1;

public class Order_screen extends AppCompatActivity implements View.OnClickListener,item_adapter.CustomItemClickListener,cat_adapter.CustomItemClickListener {
    DBmanager mydb;
    long id;
    Cursor category_cursor,item_cursor;

    private ProgressDialog progress;
    int position;
    private Paint p = new Paint();

    private Button snacks, juice,get;
    SearchView searchView;
    private int flag = 0;


//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;

    private AlbumsAdapter1 adapter;
    private List<Album1> albumList;

    private cat_adapter cat_adapt;
    private List<cat_album> catlist;

    private item_adapter item_adapter;
    private List<item_album> itemlist;

    private List<cart_items> cart_list;



    RecyclerView bill_items, categoy,itemrecycler;
    TextView total_textview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_screen);

        total_textview =(TextView)findViewById(R.id.total_amount);
        searchView =(SearchView) findViewById(R.id.searchview);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                item_adapter.getFilter().filter(newText);
                return true;
            }
        });

        SqlScoutServer.create(this, getPackageName());

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setCanceledOnTouchOutside(false);
        snacks = (Button) findViewById(R.id.snacks);
        juice = (Button) findViewById(R.id.juice);
        get = (Button) findViewById(R.id.click);
        snacks.setOnClickListener(this);
        juice.setOnClickListener(this);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Order_screen.this,String.valueOf(cart_list.size()) , Toast.LENGTH_SHORT).show();

                for (int i=0;i<cart_list.size();i++){
                    String id= String.valueOf(cart_list.get(i).getItem_id());
                    Toast.makeText(Order_screen.this,id, Toast.LENGTH_SHORT).show();
                }
            }
        });
//        progress.show();


        mydb = new DBmanager(this);
        mydb.open();
        id = mydb.insertcategory("egg puffs", "snacks");
        id = mydb.insertcategory("apple juice", "drinks");

        mydb.insertitems("avil milk", "2", "10", "https://cdn1.medicalnewstoday.com/content/images/articles/320/320834/bottles-of-fruit-juice.jpg");

//        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

        bill_items = (RecyclerView) findViewById(R.id.billrecycler);
        categoy = (RecyclerView) findViewById(R.id.subcat_recycler);

        itemrecycler=(RecyclerView)findViewById(R.id.item_recycler);
        initSwipe();

        cart_list=new ArrayList<>();
    }

    @Override
    public void onCustomItemClick(int position,String cat_id) {
        load_items(cat_id);
//        Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
//
//        Toast.makeText(this, "clciked"+String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCustomItemClick(int position) {
        load_cart_items();
    }

    @Override
    public void onCustomItemLongClick(int position) {
    }


    public void load_cart_items() {

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter1(this, albumList, total_textview);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);

//        bill_items.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        bill_items.setLayoutManager(linearLayoutManager);
        bill_items.setAdapter(adapter);


        Album1 a;
        long tot=0;

        for (int i=0;i<cart_list.size();i++){
            int id= cart_list.get(i).getItem_id();
            String name= String.valueOf(cart_list.get(i).get_name());
            int qty= cart_list.get(i).get_qty();
            long price= cart_list.get(i).get_price();
            long total= cart_list.get(i).get_total();
            Toast.makeText(this, "total of each items:"+String.valueOf(total), Toast.LENGTH_SHORT).show();

            a = new Album1(id,name,qty,price,total);
            albumList.add(a);
//             tot=tot+total;
//            total_textview.setText(String.valueOf(tot));
        }



        adapter.notifyDataSetChanged();
    }




    @Override
    public void onClick(View v) {
        Button a = (Button) findViewById(v.getId());
        if (v == snacks) {

            if (flag == 0) {
                flag = 1; // 1 => Button ON
                snacks.setBackgroundResource(R.drawable.circle_button_click);
                juice.setBackgroundResource(R.drawable.circular_button_s);
                cat_adapt = null;
                category_cursor = null;
                category_cursor = mydb.getData(a.getText().toString().toLowerCase());

                item_adapter=null;
                itemrecycler.setAdapter(item_adapter);


                load_categories();




            } else {
                flag = 0; // 0 => Button OFF
                snacks.setBackgroundResource(R.drawable.circular_button);

            }


        } else if (v == juice) {

            if (flag == 1) {
                flag = 0; // 1 => Button ON
                juice.setBackgroundResource(R.drawable.second_circle_click);
                snacks.setBackgroundResource(R.drawable.circular_button);
                cat_adapt = null;
                category_cursor = null;
                category_cursor = mydb.getData(a.getText().toString().toLowerCase());

                item_adapter=null;
                itemrecycler.setAdapter(item_adapter);

                load_categories();



            } else {
                flag = 0; // 0 => Button OFF
                juice.setBackgroundResource(R.drawable.circular_button_s);
            }

        }
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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                position = viewHolder.getAdapterPosition();
//                final TextView id=(TextView)viewHolder.itemView.findViewById(R.id.id);


                if (direction == ItemTouchHelper.LEFT) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Order_screen.this);
                    alertDialogBuilder.setMessage("Are you sure, You wanted to delete?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    albumList.remove(position);

                                    Toast.makeText(Order_screen.this, "item removed", Toast.LENGTH_SHORT).show();

                                    adapter.notifyDataSetChanged();
                                }


                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int size = albumList.size();
//                            if (size > 0) {
//                                for (int i = 0; i < size; i++) {
//                                    albumList.remove(0);
//                                }
//
//                                kot.amits.com.kotsystem.adapter.notifyDataSetChanged();
//                            }
//                            load_cart_items();
                            adapter.notifyDataSetChanged();

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    int size = albumList.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            albumList.remove(0);
                        }

                        adapter.notifyDataSetChanged();
                    }
                    load_cart_items();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
//                        p.setColor(Color.parseColor("#388E3C"));
//                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
//                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeReso+urce(getResources(), R.drawable.ic_edit_white);
//                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(bill_items);
    }

    public void load_categories() {
        catlist = new ArrayList<>();
        cat_adapt = new cat_adapter(Order_screen.this, catlist,this);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        linearLayoutManager.setReverseLayout(false);

//        categoy.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Order_screen.this, 2);
        categoy.setLayoutManager(linearLayoutManager);
        categoy.setAdapter(cat_adapt);

        cat_album cat;


        while (category_cursor.moveToNext()) {

            String catname = category_cursor.getString(category_cursor.getColumnIndex(DBHelper.cat_name));
            int cat_id = category_cursor.getInt(category_cursor.getColumnIndex(DBHelper.cat_id));


//            Toast.makeText(this, catname, Toast.LENGTH_SHORT).show();

            cat = new cat_album(catname,cat_id);
            catlist.add(cat);
        }


        cat_adapt.notifyDataSetChanged();


    }

    private void load_items(String category) {
        item_cursor=mydb.getitems(category);


        itemlist = new ArrayList<>();
        item_adapter = new item_adapter(Order_screen.this, itemlist,cart_list,this);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(Order_screen.this, R.anim.layout_animation_fall_down);

        itemrecycler.setLayoutAnimation(controller);

//        itemrecycler.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));
        linearLayoutManager = new GridLayoutManager(Order_screen.this, 3);
        itemrecycler.setLayoutManager(linearLayoutManager);
        itemrecycler.setAdapter(item_adapter);

        item_album items;


        while (item_cursor.moveToNext()) {

            String item_id = String.valueOf(item_cursor.getInt(item_cursor.getColumnIndex(DBHelper.item_id)));
            String itemname = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.item_name));
            String itemprice = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.item_price));
            String image = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.image));

//            Toast.makeText(this, itemname+itemprice+image, Toast.LENGTH_SHORT).show();



//            Toast.makeText(this, catname, Toast.LENGTH_SHORT).show();

            items = new item_album(item_id,itemname,itemprice,image);
            itemlist.add(items);
        }


        cat_adapt.notifyDataSetChanged();




    }



}
