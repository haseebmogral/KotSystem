package kot.amits.com.kotsystem.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.idescout.sql.SqlScoutServer;
import com.phi.phiprintlib.PrintService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.backgroundTask;
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
import kot.amits.com.kotsystem.printer_sdk.BTPrinter;

import static kot.amits.com.kotsystem.DBhelper.DBmanager.*;

public class Order_screen extends AppCompatActivity implements View.OnClickListener, item_adapter.CustomItemClickListener, cat_adapter.CustomItemClickListener {
    DBmanager mydb;
    long id;
    Cursor category_cursor, item_cursor, ordered_items_cursor;

    private ProgressDialog progress;
    int position;
    private Paint p = new Paint();

    private Button snacks, juice;
    SearchView searchView;
    private int flag = 0;
    Button order, finish_Order;
    String mode;
    static  boolean confirmation ;
    ProgressBar progressBar;




    private AlbumsAdapter1 adapter;
    private List<Album1> albumList;

    private cat_adapter cat_adapt;
    private List<cat_album> catlist;

    private item_adapter item_adapter;

    private List<item_album> itemlist;
    private ArrayList<cart_items> cart_list;


    RecyclerView bill_items, categoy, itemrecycler;
    TextView total_textview;

    int column_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_screen);


        mydb = new DBmanager(this);
        mydb.open();
        sharedpreferences = getSharedPreferences(DBmanager.sharedpreference_name, Context.MODE_PRIVATE);
        if (sharedpreferences.getString(DBmanager.sharedpreference_column,"")==null ||sharedpreferences.getString(DBmanager.sharedpreference_column,"").equals("") ){
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString(DBmanager.sharedpreference_column,"4");
            editor.commit();
            column_count=4;
        }
        else{
            column_count= Integer.parseInt(sharedpreferences.getString(DBmanager.sharedpreference_column,""));

        }



//        add_cat_item();

        bill_items = (RecyclerView) findViewById(R.id.billrecycler);
        categoy = (RecyclerView) findViewById(R.id.subcat_recycler);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        itemrecycler = (RecyclerView) findViewById(R.id.item_recycler);
        initSwipe();

        cart_list = new ArrayList<>();
        itemlist=new ArrayList<item_album>();
        total_textview = (TextView) findViewById(R.id.total_amount);


        mode = getIntent().getStringExtra("mode");
        if (mode.equals("new_order")) {

        } else if (mode.equals("active_order")) {
            ordered_items_cursor = mydb.get_active_order_by_bill(mydb.CART_ID);
            if (ordered_items_cursor.getCount() <= 0) {

            } else {
                cart_items c;
                while (ordered_items_cursor.moveToNext()) {
                    String i_name = ordered_items_cursor.getString(ordered_items_cursor.getColumnIndex(DBHelper.item_name));
                    int i_id = ordered_items_cursor.getInt(ordered_items_cursor.getColumnIndex(DBHelper.item_id));
                    long i_price = ordered_items_cursor.getLong(ordered_items_cursor.getColumnIndex(DBHelper.item_price));
                    int i_qty = ordered_items_cursor.getInt(ordered_items_cursor.getColumnIndex(DBHelper.c_qty));
                    String status = ordered_items_cursor.getString(ordered_items_cursor.getColumnIndex(DBHelper.c_item_order_status));
                    long total = i_price * i_qty;
                    c = new cart_items(Integer.parseInt(mydb.CART_ID), i_name, i_id, i_price, i_qty, total, status);
                    cart_list.add(c);
                }
                load_cart_items();
            }

        }

        order = (Button) findViewById(R.id.order);
        finish_Order = (Button) findViewById(R.id.finish_Order);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);

        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(Order_screen.this, R.anim .layout_animation_fall_down);

        itemrecycler.setLayoutAnimation(controller);

        itemrecycler.addItemDecoration(new GridSpacingItemDecoration(column_count, dpToPx(1), true));
        linearLayoutManager = new GridLayoutManager(Order_screen.this, column_count);
        itemrecycler.setLayoutManager(linearLayoutManager);







        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //space to order place in database

                if (check_bt()) {

                    TextPaint header;
                    header = new TextPaint();
                    header.setTextSize(30);
                    header.setColor(Color.BLACK);
                    header.setTypeface(Typeface.create(String.valueOf(Typeface.NORMAL), Typeface.BOLD));
                    BTPrinter.printUnicodeText("Mims Cafe,Kasaragod", Layout.Alignment.ALIGN_CENTER, header);
                    header.setTextSize(20);
                    BTPrinter.printUnicodeText("Kitchen Order", Layout.Alignment.ALIGN_CENTER, header);
                    BTPrinter.printText(mydb.add_space(32, "Bill No:" + mydb.CART_ID) + "Date:" + mydb.get_date());
                    BTPrinter.printText("------------------------------------------------");
                    BTPrinter.printText(mydb.get_header_title_for_kitchen());
                    BTPrinter.printText("------------------------------------------------");
                    BTPrinter.printText("                                           ");

                    int sl = 1;
                    for (cart_items a : cart_list) {
                        if (a.get_status().equals("cart")) {
                            BTPrinter.printText(mydb.add_space(10, String.valueOf(sl)) + mydb.add_space(30, a.get_name()) + mydb.add_space(5, String.valueOf(a.get_qty())));

                        }
                        sl++;
                    }
                    if (cart_list.size() < 5) {
                        for (int c = 0; c <= 5 - cart_list.size(); c++) {
                            BTPrinter.printText("");
                        }
                    }
                    BTPrinter.printText(mydb.get_footer());
                    BTPrinter.printText(mydb.get_website());

                    for (cart_items a : cart_list) {
                        a.set_status("sent");
                    }
                    mydb.place_order(cart_list);


                } else {
                    Intent intent = new Intent(Order_screen.this, printer_connect_activity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        finish_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String status= mydb.get_order_status(mydb.CART_ID);
               if (status.equals("1")){
                   Toast.makeText(Order_screen.this, "send order to kitchen first then finish", Toast.LENGTH_SHORT).show();
               }
               else{
                   confirm_finish_order();
               }
            }
        });

        SqlScoutServer.create(this, getPackageName());

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setCanceledOnTouchOutside(false);
        snacks = (Button) findViewById(R.id.snacks);
        juice = (Button) findViewById(R.id.juice);
        snacks.setOnClickListener(this);
        juice.setOnClickListener(this);
//        progress.show();


    }


    @Override
    public void onCustomItemClick(int position, String cat_id) {
//        cat_adapt.notifyItemChanged(position);

        load_items(cat_id);
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
        albumList.clear();
        adapter = new AlbumsAdapter1(this, albumList, total_textview);

        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);

//        bill_items.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        bill_items.setLayoutManager(linearLayoutManager);
        bill_items.setAdapter(adapter);

        Album1 a;

        for (int i = 0; i < cart_list.size(); i++) {

            int id = cart_list.get(i).getItem_id();
            String name = String.valueOf(cart_list.get(i).get_name());
            int qty = cart_list.get(i).get_qty();
            long price = cart_list.get(i).get_price();
            long total = cart_list.get(i).get_total();
//            Toast.makeText(this, "total of each items:"+String.valueOf(total), Toast.LENGTH_SHORT).show();

            a = new Album1(id, name, qty, price, total);
            albumList.add(a);

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

                item_adapter = null;
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

                item_adapter = null;
                itemrecycler.setAdapter(item_adapter);

                load_categories();


            } else {
                flag = 0; // 0 => Button OFF
                juice.setBackgroundResource(R.drawable.circular_button_s);
            }

        }
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
                    alertDialogBuilder.setMessage("Are you sure, You want to delete?");
                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            albumList.remove(position);
                            cart_list.remove(position);

                            if (cart_list.size() <= 0) {
                                total_textview.setText("00");
                            }

                            Toast.makeText(Order_screen.this, "item removed", Toast.LENGTH_SHORT).show();

                            adapter.notifyDataSetChanged();
                        }


                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int size = albumList.size();

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
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
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
        cat_adapt = new cat_adapter(Order_screen.this, catlist, this);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        linearLayoutManager.setReverseLayout(false);

//        categoy.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Order_screen.this, 2);
        categoy.setLayoutManager(linearLayoutManager);
        categoy.setAdapter(cat_adapt);

        cat_album cat;

        cat=new cat_album("Top items",999,false);
        catlist.add(cat);
        while (category_cursor.moveToNext()) {

            String catname = category_cursor.getString(category_cursor.getColumnIndex(DBHelper.cat_name));
            int cat_id = category_cursor.getInt(category_cursor.getColumnIndex(DBHelper.cat_id));


//            Toast.makeText(this, catname, Toast.LENGTH_SHORT).show();

            cat = new cat_album(catname, cat_id,false);
            catlist.add(cat);
        }

        cat_adapt.notifyDataSetChanged();


    }


    private void load_items(String category) {
        itemlist = new ArrayList<>();


        new backgroundTask(itemrecycler,progressBar,this,itemlist, cart_list, this,category).execute();
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

    @Override
    public void onBackPressed() {

        if (cart_list.size() > 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mydb.cancel_order(mydb.CART_ID);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            finish();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to cancel the order?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

        } else {
            mydb.cancel_order(mydb.CART_ID);
            finish();
        }

    }

    public boolean check_bt() {
        if (PrintService.printer == null) {
            return false;
        } else if (PrintService.printer.isConnected()==true){
            return true;
        }
        else{
            return false;
        }

    }

    public boolean confirm_finish_order(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        finish_order();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to finish this order?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        return confirmation;
    }

    public void finish_order(){
                    if (check_bt()) {
                        Cursor last_Cursor = mydb.get_active_order_by_bill(mydb.CART_ID);
                        last_Cursor.moveToFirst();

                        TextPaint header;
                        header = new TextPaint();
                        header.setTextSize(30);
                        header.setColor(Color.BLACK);
                        header.setTypeface(Typeface.create(String.valueOf(Typeface.NORMAL), Typeface.BOLD));
                        BTPrinter.printUnicodeText("Mims Cafe,Kasaragod", Layout.Alignment.ALIGN_CENTER, header);
                        header.setTextSize(20);
                        BTPrinter.printUnicodeText("Invoice Bill", Layout.Alignment.ALIGN_CENTER, header);
                        BTPrinter.printUnicodeText(last_Cursor.getString(last_Cursor.getColumnIndex(DBHelper.cart_type)), Layout                               .Alignment.ALIGN_CENTER, header);
                        last_Cursor.moveToPrevious();
                        BTPrinter.printText(mydb.add_space(32, "Bill No:" + mydb.CART_ID) + "Date:" + mydb.get_date());
                        BTPrinter.printText("------------------------------------------------");
                        BTPrinter.printText(mydb.get_header_title_for_bill());
                        BTPrinter.printText("------------------------------------------------");
                        BTPrinter.printText("                                           ");



                        int sl = 1;
                        long total_price = 0;

                        if (last_Cursor.getCount() <= 0) {
                            Toast.makeText(Order_screen.this, "Add some items to cart and order!", Toast.LENGTH_SHORT).show();
                        } else {
                            while (last_Cursor.moveToNext()) {
                                String s = mydb.add_space(5, String.valueOf(sl));
                                String item_Name = mydb.add_space(27, last_Cursor.getString(last_Cursor.getColumnIndex("item_name")));
                                String qty = mydb.add_space(5, last_Cursor.getString(last_Cursor.getColumnIndex("c_qty")));
                                String price = mydb.add_space(5, last_Cursor.getString(last_Cursor.getColumnIndex("item_price")));
                                String total = mydb.total_format(5, last_Cursor.getString(last_Cursor.getColumnIndex("c_total")));
                                BTPrinter.printText(s + item_Name.substring(0, Math.min(item_Name.length(), 27)) + qty + price + total);
                                long item_total = Long.parseLong(total.trim());
                                total_price = total_price + item_total;
                                sl++;

                            }

                        }

                        if (last_Cursor.getCount() < 5) {
                            for (int c = 0; c <= 5 - last_Cursor.getCount(); c++) {
                                BTPrinter.printText("");
                            }
                        }
                        Double tot_double = Double.valueOf(total_price);

                        BTPrinter.printText(mydb.get_line());
                        BTPrinter.printText("Total : " + mydb.total_format(39, "Rs " + String.format("%.02f", tot_double)));

                        BTPrinter.printText(mydb.get_footer());
                        BTPrinter.printText(mydb.get_website());
                        mydb.finish_order(mydb.CART_ID,String.format("%.02f", tot_double));
                        mydb.cart_list=cart_list;

                        last_Cursor.moveToFirst();

                        if (last_Cursor.getString(last_Cursor.getColumnIndex(DBHelper.cart_type)).equals("Take away")){
                            finish();
                        }
                        else {
                            Intent intent=new Intent(Order_screen.this,feedback_activity.class);
                            startActivity(intent);
                        }


                    } else {
                        Intent intent = new Intent(Order_screen.this, printer_connect_activity.class);
                        startActivityForResult(intent, 1);
                    }

    }

    private void add_cat_item() {
        {
            id = mydb.insertcategory("Burgers", "snacks");
            id = mydb.insertcategory("Smoothies", "drinks");

            mydb.insertitems("Strawberry Shortcake Smoothie", "2", "50", "https://reciperunner.com/wp-content/uploads/2016/04/Triple-Berry-Layered-Smoothie2.jpg");
            mydb.insertitems("Pineapple-Mango Smoothie", "2", "50", "data:image/jpeg;base64,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");
            mydb.insertitems("Bacon Burgers", "1", "70", "https://jesspryles.com/wp-content/uploads/2015/06/bacon-36-1440x900.jpg");
            mydb.insertitems("Green-Chile Burgers", "1", "70", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUSEhIVFhUVFRgVFRUVFRcXFRUVFRUWFhUVFRYYHSggGBolGxUVITEhJSkrLi4uGB8zODMsNygtLisBCgoKDg0OGhAQGi0dHyYtLS4vLS0tLS0tLy8tLS0uLS4tLS0tLS0rLSs3LS0tKy0tKy0tKy8tLS0tLS0tLSstLf/AABEIAL4BCQMBIgACEQEDEQH/xAAcAAACAwEBAQEAAAAAAAAAAAAEBQIDBgcBAAj/xAA+EAABAwIEAwYEAwUIAwEAAAABAAIDBBEFEiExQVFhBhMicZGhFDJSgUKxwQdi0eHwFRYjM0NygvFTorKS/8QAGQEAAwEBAQAAAAAAAAAAAAAAAAECAwQF/8QALBEAAgICAQMDAwMFAQAAAAAAAAECEQMhEgQxQRMiUWGhsXHR8CMyQoGRFP/aAAwDAQACEQMRAD8Ayk7lbTlDTHVXQleWu56XgKGpRVJ8wQTXq5lQAbrqi9HPM2NDUWATJtSsrQVYO6YfF9USM0aiKW4RUIWdpK1PqScEK4Ckz6ogus1i9Ba5WslkFklrjmuFqYs5+aQ3JUn05stE+mAvohJ4xYqGBmJmHmps2V841UWxiySEQLkDWTEBGSu5JfIy+6YwEtJ1uV6y4RfcqMkdkMZ7HUEDdQlqOqHeUPK8qREZ5LndVx6lUuKKowgYXE0qdldDHdeytskMqhmKPjqSRukpk1KmyoSopMch/VCVEvVDOqdEHJMUyWyU7+qCe88yrHSKtNIk9b5qTSpBq+yqgLWFFd51QOZX94gBzKFbFsq5VZFsvP8AJ6ng9kKqMtlORDSrpXYwkgymqHcE1hkchMJpbrRQ0mmyLM3E9oSU9oaiyBpqe3BFMjC0gyZRGTprhBSAlEU4RBg6LUxaFU0eiV1EW91qPhkqxOn0SaEZKrhQTimNcLJFPPqeikRc+yDlOuig+rGwKnAOKB0SbdUzq990LPzQwA5EPK5SkkVV0gRUW3V9MNV41iMpGWRZSQwhFgh6t2iuDkPUlIuhRM+yi16qq3XK8jCdEWF30UHlfNK+cgGVEL1rVElTa5MRa0r5y8aVaxiAKmtuie6RENIjPhFSAtlVsOyqlVkC8/yen4PJlTELuCumQ7D4guhdjJmswqHQLQQM0SHCZhYJ0yfRNozbC3uAaoQzApXXVtggKev10Tj3IbNjA9Mon3WSpa88U0hxEBbpmLHEjksxBwsrIpXSfI0ny/ir48HLrGQ2uflG/wByVM5pdxqLfYwWLne2pO1lnZ8Aq3khlPIbb6Wt6rszIKdgu1rRd5a11sxuBqotcc98zmtPAgW+6459TT0jePTNrbORxdiK3KHGNouL2LxcDryTODsVVZQTkDjbwZtddrrodUXMO3hF/EXAjbQ9dNEorqmVpa2E3Y5wvK4AG5G1+AWf/ql8Gi6ePyYKpwmeNxa+J1xvYXFudwlVSbaLoNaDFcGbKddLmx6JJU1LahxaYQ42sHNFrOtxIWkeqX+Sozn09bTMRJEm/wDc6qABcGMJF8r32eB+8ADY9DquhdnOyMNPeplu6SNpcInj/LeBma634nWDiOAsq6qtYNSc7n6tOujDex89tOhU5+p4pcTkbp0c8/u/Ug5e6J6tLSD7r1+Hyx3zxvFtzbQeZC6thbIo4jK+xeSABxbmdYGyQ02IgzOHC5FzxsTclc8utnCHKSX6IpTOfumQVXU8E77a0Pdyd7GLRSONgNmvABc3pfcDz5LLEElehimskFJeSmyTY1YGBTjjVndFakg79FU+RXztQjmpgetdqpqpsZRtPBzQB7TsR1PEVCNiPptkwDqSMWR3dBLY5bIr4hFgBSKUCjIpQrzvJ6fg8mVDDqr5kG9y6Y9jKQ8wuYk2WjYdFmcDcN+Ke9/YK0jGYJiNybBDxC2iPa2+qLwvC3Su8Db235DzKpa2ZfQFjvoADdbDBey5BbJO4Ftr5BxJ4EptheBMhAdYOkGhJ214BMJyQ0i1yBcWsB5gnlzWWTL8GkMXyRlZkBsQBrYfKLAJc2pDmPYHZg9oLX2yty3uQL66DihauuZJJkJ8LQbuAuXcS0eeyHrpf8SIsDmgsGZx2BIsWctL+64JZL2v58nbHHWi3EixsLZInNyD6ibkXIIHAajdewTh4u8tAbYvNwNDrpfihsUqWRwBjS6zXNaTlve+p1tprdAmK7HFp5OynQW3NjbQfxWbq7+haToExnEWzF4Phs28YBuCQRa5HMJRVTS275waO88LWtykR2v4S0jQ+G4PJCVuLAvaZnWaD4gLaNuL2t5IyauilqGksBipmNMh4Puf8FhPE2AufP7kVptkZZKC+gWzCyYL1UpDQx0rGAWe4nYkkbX0VtVVNgipXCNpj0kLRcePI24J4k769Uvx7tg+oY6OKnzG5e4Ri7je4F7nbbTos/SYtUmLK6lmdY6HITY2ttwIBSknL9P+fZnmyWXJutG8rceD6eQscS6S4c7YgZcuR32v6rIVM2Xuw35mxtc4cy65t52ISqmrgXiMPyEuGcOBBA43HFNpsThLh3jRYOaC5u5Yw2t/+dLqXGf+X+jBxa7mnirjJG2IXLbF2g1aRYh2boQNFl62B7Zi3dxJvbjroR0O6cYHXRCd0rp2iMtdoCG2As4AtPkBxQ39stkkfI8BxJOV30g8NNNOCzle2w2DQhsrXQusbixB115jkQsdW4Y+KQxvGo9xwIWskkY+bPAMp1dYXtcC9vuR7q7FaR1e6KSFozBpjeCbWI8QueO7h6Ls6Oag6b0XFmSiiVndptimDy09hIAM17WN9jYoWGG69RNPaKFc8CpbSJ/LSryGluqoYkjp9UUyCyYSUll93eiKEBsjUXvyq86IKoddAEhU3RPfpFNJZXd+kIevlCnTOBNkoeH32R2G4dO83a1T6KOn12aqjw2Nw1CudgER4IelpKpotkHuiAyq+ge61WNIzeVsLosEjbsEf/ZcaTZqofg/NTaKtxAyjXTiq4olzscUuFCV/dR6HcnkFtMMw9kEYa1v+48XHn6qjAsKbBHmJvIQMzuvRM433dbSzR+fNcWWdukbQhWz5xsQL63F7C5uQdyllVI52cNYRu0X/ENOA6o6SQ5jpp0NtAkNVVudqPCGZiADuDYkk+QXJkmqOnHHYvo+7jc+M2L+8Y4DXXUZ2h3l+atnrw5krngG9+7aNm6aeeqQYjj8UVrFpLX58rRcnSxGm1+Z5LHY92gf45A7IJD/AJYPA7kLKMJUkjd1ds1PaLtEyEU+eTPlu90QtpI1wIJbxuDxusXjXa+Z8jyxxDXOLrXuRcW12G3Cyy8+IZ3HQm3v5lQp2Sym0YAtr0+67Y9Okrkck8/iAzpYTPd80mVo3Nxc3PVN8NxRnio6SO/eWGY2NrfMS4fb0WcfSSEZDlFr7XOvTgjezlS6jeZO7DydNTaw420P9BLKrg3F2/CMYrnNep2O7dm+zbIqQBoBe82LtLk8Sfv7IjF6MRZWgi+XxW5rJ4L+1aMRtD6aRuXTNmBH2FgSjY+1dPVuLhMMw3aQ4WHDcLw+s6dLDfFuerf3f02etgyXOk1x8L8HtRTsdq5oJHEgX9Vm8XwWF1/Dbq029tlp5Zm20IPUG/5JLW8V5/TynF6bR2ThCa9ysylbSzDRkgLRbwOOUac7AgpdPiFU06xQgfuDf73/ACsntWUoqnL3cOVte5Jnl5+jxeNDDsnWSPlcHsGVsbnONrWOgYL8bk7efJPcEYYpnRn8QDhbmNfyJP2We7I1OWqYHaseSHN4OIa4svbX5racVu6Wge901VkJLWOLG23sNBb7KcqrJpdzy8uPhKgWev8AjGyQOsJIswklcBYCN7gfW3DisrV0/dutFmkbtcNO/kOCXUtU5pd4j47h+vzAm5v91sOy1c066BzMrQBpdoFhfmdF2QyyjISVGXfiI2X0eKAJNjtO/wCIlsfxk6dSgfhpOq9NbVlGqGIhxRkb2kcFjI6aQbEp7hVHM/imhDV8bOiFmp28gjx2fn5+y+/u5Nz9k+IjPVVI3kFT8KOQWkPZibn7K3+60nP2RxDRQynYXAcLro/Z+iiDRsubUXZ+pf47gcdU9p4qtvhD2gfdUnXcfc6SIoui9MUXRYWF0o+eX3RTMQtxup9SI+DNaYYuisjp2b22WTZiriQANytWHWZY72XP1HUcY6NsOG3sKmlHdue7QAbdEvw+qDo22u64zOI0Aa52oJ5oavrgfA7QHgktXijYY8gdvpYGx1Xkyzrlf0o71hbQ5xLGO7te1vETcjcOuB6LB41jMrzIGWZHJo61r2O4HIEi6FrTLKAS4taOF90JKNFPKUi+MYdhLUvELCd3cufRZLEKgk3cbuPsOQWox5hyE8vySXCsNjeb1BcxrvkLdyfIjUL0OncYxc2cWZyk6F1My9rGwv8Acp5TReGzLgHew58bq52GiK7w1zoxo0lu5vx9l0Ps3SxxwudI5p71jfAbbb6hZ9R1Kq/BnGBmqDsY7K15BdmbmtnH5A3U6XscXvfd7Y8pGVr766XJB9uK0cLhTSSG12PaBGcxuzUkjy29FXh9bHUuykOEm27sshJsLEbHa91w+vkctbNOCRm8Q7JyRuDpZG90dPBcG+thY76Am6ddgqamL3xO0zfLbQvJNtSd9LLQ43Qxd2I3NBLdQbkEG1rjol/ZrDWsayUMyyMcTmJNjwvY6EdQh5+/J9iktWjztVhUcM+VpcxuUEEEnM4HxC+tuG/NA0uHyStJjkBsR8z9cvMi11oq/EI82aYtJ1IL9QCdeO2wWZx2sAc58YYC7W4aL32JzBYxywyf2x0VynHyAR05c97ZXBuRxabauNjbQeu6toOzbZy5zHuexmj7MyAG18mdx8TrEHwg+Y0WTfjDopSXNzX1NuOuupXQOz9dKIHs7sMEgMjW65mktG522AK7vTeON0S8s35MHWsMMpa1x8JBa4aHgQQRxH6LovZLte6WGSGpe35Mudzw0vaQdBtrzI6LKsgbZzpSAAba87D7lTqaAd2JYiHsOmZpDrHk4btPQqZTbVU19SJ+5UwRtMTZ3S4NttNQQrG05JsBr0PBaXsFRiWZwfqI2h1iNDc21UP2gwCGoHdsyNewO0Fml1yDlt9rrbHjlw9RnI7Toy8tM5p8Ubh5gqrvWBdP7JYiKxpbJCLxtAdIDo52w0toTYndMqvsrTv3YPRepCSkrQrOQCpYnXZ3E42vsVp679m8Dvlu3yKz1d+zSZusUnqr2Fm7pq6IgbK74mLouZMwavjOU3tzGqZRYTVEfOfROwN2aqLovvjIuixAwKp/8hVn9g1H/kKdiDJMScdALBVGdx3JQ7WlFRQri2zqPGtVrI0RFToyKmVKIrBaeA5h5haerns0XOwCWxQJB2r7Rd1mhaLu3J5cly9XCTpI6ulp2S7R42M4aNTbhz4JTFSZz3kmp3AOwss3SVnjzEkuJvc8PJPW1jj/ADXmyhwls7ruOguVw1120SurkAUaiY8SllXXMYddzqtIK9Ixki4RZzsD0Krr8PdlbltmYdWgHSx0A4WTzsfhUrmmolFmvF4o7eLLwc7kNrD7prW92wEhvj4eaWTqIwnxW6/JzuLl2Mm34iUZLAN5cAORJWupMCe2NwnOdxFvDswWsA02vz9lLs/g80h0aXAm9tgN9yNgtTXYTI2FoZJGXbEX200GbiRay58jnOH9Jdh8OL9zMJjVKXMEbM2bgxtybfoER2cwplFlllzGRzTxuIib6C3G1wT520VM8MtPI54zl1tTxtuQEvGKSytN2lrQBl625q8bksdJkvb+hrhM2ZzbnwEi9ja6DldI0uDXtLQ5wZvo0E5Re/AJLgjHxzkWJieNWgjwkgXcL7a7+acVdMxo2cSToCMuvK54b63sonDlrkiuLXgT4jd3zlvmf5pRUStylkV3X47NFtNBZHVWGlxJfpa/hGwtvc8UlqKoR6AXPAHQfdaYI+Iu/shuFbkeDBnEZzY8N+X/AGn1DWz5mlxvpkt/uGVYibFqgk2OUAgWGtyeqKw3HnfNI4nKdABqTz1NrLtn0+ardMjnj7DfFG+NwLgTe7iAQ0C1rNH68V5SV/gyx3aDo7LxG+oJ12S52L964hzSD+nPYImCwcC0W/L0S4taktkKFqxnDjr6R7ZYyMxNnMOz2WNwR521W+rYmYnRtkYMr7F0ebg78TCR+E29gVxmsJMxzm2o1JsGtO3kLFd4wKmZFCyNhzNAFjzvxHRehgh7eL7HLlovwHCmU0QjYBfQvcL+J9gC7U6bbJmCqGvUw9dSSSpGJcFY1Uhym1yYFhjB3AUXUbTwUg5SDkABy0RG1lV3DvpTMOUroGc5ipkfBTK+KBGxRLFRNnIoip0VHTq9kava1VRNlTIFzvtvgMrZDK1pexxu7S5HTyXTWr1zQRYi6meNTVMvHkcHaOGQyNZr3dj1BU31cr75GOPkP1XY6ukjyn/DafsufYxFKHOyWaOgXFLo4p3J2d0eqclSVCLsvhstXUiJ/gaA554k5Ncv3Ngtq/sbC7KXNte1wRuN7D3Q37OaNommdMHFwiJaRwBdZ9v3jcAeZW2oYgb6W203aDl2F1yZ8fujx0aRyypisQvyvc1nym2xs0HbyG3qk8NAAbv1dzWygika4lrzZwILct26aa8bJfVULzd2TQndljp0YPtovK6jpsvFcU3/ADv+9muCeNPdCqmq3wk9261xY9UyoWCVpLQObmknNqDfzbc3SeocASOI5gg+h1VlHOWEOadf60PRcmLI4PjPa/H6fzZ05MSkrj3K8VpC59/HYEAhtgbX8jbS+6qgwenvdpyD6XHjfTWwtx0PJMzK83cTe5zaje+p1PBRNW3XPHmBFtHEW31AN/6C7cfVYmuL+6/Y5/Rktpf8IxU8TbEFvzegG9/5chssp267RuAa1mUE6C7RwGpsNB5fmm1bO0fLfnrz46rmmJ1vfVMlz4YxlA5/VwOv8F0dIvVnVVFbf1+CcvsjfkuL5XMu+R5LiLAC3hO1gN0Fi7mCwi18NjpbxD5uZI8+FlbR1lmgXsWki9/FY87eV0DXPA1vqTw346r2YwinSRxNtq7AY+p1/ojX7r4xi1xz9VVLJl0/714ql1TpYbdV0pGDDKeSzhrtxv0tZOYqnlZZ6kppZD4WEj0HqdE5jw14F3aDaw1P8Fz51G9s0xXWgTET3kzyDpcActABoPsut/s/xTNTNicfFFZuu+X8J/T7LmjKdrNSP5cFoMFqHROL27EW97qoZdk5Ie06s2oCsFQsNBjx4oyLGuq6VM5eJsW1CmKhZVuLDSx80SzEhzTUgo0rZ1YJ1nGV45q5tcOaqxUaBs6t74JAysHNX/FjmiwK4mopjVTGiGpUVZMKxoUGq1oTEfKbWr0BWZUAVll0pr8JDuCdtCmGKXstOjLYTQ9zNcjwOBY//aba+oC0sTMpOUaAjfW1x4bjj/JW/Cgokw3Fj09tiuTNgvce5tHL4YoneQWgbDxeYsGuB+4uiIpWCO5d819AdTw24Kytp9jx205f0UI6maHG2o4HmuNqcZM25RaKpWskAEjA4Da+p9d0E/CorlzC+Mg6ZHabC+hB48kyfHY7bHXoFER+E23vt/EqHi5d1Y1lcezoA+CIBDX5uWZgO+33QcmDuP8AqN9Cm4J0IFsv5jifuvi2xzHUOJP3K55dDje6/JqupmvP4MnW9nJHbSsHXKVkz+y85y8VeriSRk3JNzxXUJmk39uiGy5bHkb/AHurxxnh1DSFPJ6n92zAwfszhLcpqJM+a5fYAZbfKG6634rxv7O6bN4ppXW00sPf+C6DK3LIX2BzszBt9jp6oanpQ4EuP247bhauefspEezu0YSo7FUTBfI5x6vcR99VTLhFOwgQwAaakgXvzutdOzM6zQAL26b7qmWBmS2Xxk69LHh6+yn+rK7k2XcF2RmYqJoaZL3cx7fAdnMN7+eoA8igZKYknTc3sOGuy0tY1txoNABpyCBkcBtx1Wij4JcxDPSdNNFYNBZTqKgONgoArqhGtnPOd6JAqQkI4qtfBamQQ2qIVrcQKGZESiYqMlUrEXR4iUTHXuUYcPTCGgHFaJMln1PWOR3xRVUVMAisgVokdxPRcb1e7BXjYhw58UMYS02II8wqAvBV7ShGuKtY5ABLSrXIeJ1yrHFA0WtKtBQzXKxrkmMKaURHsgWORgekIjKLXP8AXNAFwvYkXOqYlyBraBkm415jQrGeO+xcZUVyDS19CqCAEHU4dO3/AC5XeTrO/PX3Sir+NbsWH/if4rCUJLwaJp+R+0i9gdFE2GnC6y/x9U3eJh6hxHsQUHUYtV8IP/fT/wCVFS+CqXya4kaqipl0Db6dBt5rLMxqp0DqcjmQ+/6ISqxiqv4afT95/wCgCXGXwGvk1jpGgm17W04G6GkqLdFlzi9Wf9Bo/wCRP6IWSeucdmD/AIk/qp9OT8DtfJopphqgZ61rRdzgB1KRyUFa/eQjoAB+irb2Ve43eS483Ek+6uOGQnNE63Hotm3ef3dvXZKp6uSTfRvIfqU+h7L23RkXZ9o3W0cNGbmZaOKyIZATwWqZhDAiGULBwWixk8jKw0DjwR8GE81oRCOAUxEeStQRLkKY8OARLYAEb3JUe5PAKqFZQGr4uRkeHSO4IlmFMbrI4D7piFbA52gCL+Ak+k+ynVY3TQDQj7fxS3++8PL3TtBTNkypeN/AehJGm/AL6TGXt+Zuccxv6ImpiWd7QzdxC+UbgWb1cdG+5v6rakyB1FjVO/fQolgid8r/AHXC29opWaus/rxN+P5+iPpO2beN2qCjtTaXk4FQNO4LmtH2sv8ALL7pzTdqn/WClQWbGx4he5lnYu1R4gIpnaRp3CKHY9pjqi8yz8WPRolmMMPFS4hY2zrzMlwxNvNSGIM5ooLDyVW9gKG+PbzX3xzOaKCyUlK08AhJMNaeCJ+NZzXhrmc0cQsAfhQVDsJTQ1zOYUHYhHzCXFBYqdhhVTsOcmzsTi5hVOxeHmEcUOxW6gcvPgXI9+OQ8wh39o4RxCKQrB/gHL74By8k7UxDYKh/atvAIoLCRhpVjMMKUS9q3cAg5u0sh4p0BqhQi24Cg5kbd3gLEVGOPO7z6pVV44Bu/wB0wOhzYhTs3N0tqu1UbflaFzSr7RDgSUqnxZ7tkgOhYj24frYgDoshivbB7r+MnyWSrpH5vETrqELdKh2M6vFZJONlXndzPuggUV/XFMR+kJ+2tHr43D/g7+t9Vju3faenmYyKNziAc7vCRsLNGu+pJWfdDrbTif0Wfqrkla2Ki+orIdiCdLnTnwHl+qVTVMZ4He/5KMkaHfCpA9FU3gSERDijm2tIfdL3wnoqzEeiQzRU/aeRv4r/AGTOn7ZkfMFie7K+DD0RYUdLpO2cTtCbeeibU3aeB20g9QuQxxm/BWGA9E7FR2uLGWH8YRLcTb9Q9Vw5gcNnEeRIVzauYbSu9UWKjt4r2/V7r41g+pcVbitQP9T1/wClIY3Uj8YRYUdldWD6lRJWj6lyL+36j6h7qJx2o+oe6LCjqNTiGvzIGXFD9S5u/GZz+Ie6odiMx/EPdKx0dDkxU/Uhn4t+97rAOq5T+JVmWT6khm8dio+r3VLsXb9Swxzn8XuVExnmgDaux6MfiCqd2njHErHd0ei+7k9EAamTtYOAKDm7UPO2iR9wei+7k9EAGzYxI7cn1QzqslViE9F6ID0QBY2sP0hWjEf3ff8Akhu4PReiBAHtTU5/wgWVIciRT+SqfAQUAVhyKzKjuT0RXclAH//Z");
            mydb.insertitems("Umami Burgers", "1", "80", "https://upload.wikimedia.org/wikipedia/commons/1/11/Umami_Burger_hamburger.jpg");
            mydb.insertitems("Pug Burger", "1", "80", "https://imagesvc.timeincapp.com/v3/mm/image?url=https%3A%2F%2Fcdn-image.foodandwine.com%2Fsites%2Fdefault%2Ffiles%2Fstyles%2Fmedium_2x%2Fpublic%2Ffw2007_r_xl_pubburger.jpg%3Fitok%3D3zRqH0C8&w=1000&c=sc&poi=face&q=70");
            mydb.insertitems("Spa Cucumber Smoothie", "2", "80", "http://thetravelbite.com/wp-content/uploads/2017/06/Cucumber-Melon-Smoothie-19-small-1000x750.jpeg");
            mydb.insertitems("Creamsicle Smoothie", "2", "80", "data:image/jpeg;base64,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");
        }
    }



}
