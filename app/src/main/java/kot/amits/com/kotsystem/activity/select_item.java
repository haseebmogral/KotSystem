package kot.amits.com.kotsystem.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.category_adapter.cat_adapter;
import kot.amits.com.kotsystem.category_adapter.cat_album;
import kot.amits.com.kotsystem.category_model.DataModel;
import kot.amits.com.kotsystem.constants.constant;
import kot.amits.com.kotsystem.items_adapter.cart_items;
import kot.amits.com.kotsystem.items_adapter.item_adapter;
import kot.amits.com.kotsystem.items_adapter.item_album;
import kot.amits.com.kotsystem.select_item_adapter.select_item_adapter;
import kot.amits.com.kotsystem.select_item_adapter.select_item_album;

import static kot.amits.com.kotsystem.DBhelper.DBHelper.cat_id;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.category;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.image;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.item_price;

public class select_item extends AppCompatActivity {

    private select_item_adapter select_item_adapter;
    private List<select_item_album> itemlist;
    Cursor item_list_cursour;
    RecyclerView items_recycler;
    DBmanager mydb;
    RequestQueue requestQueue;
    private RecyclerView.Adapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item);
        setTitle("Select Items");
        items_recycler=(RecyclerView)findViewById(R.id.loaditems_recycler);

        requestQueue = Volley.newRequestQueue(this);

        mydb=new DBmanager(this);
        mydb.open();


        itemlist = new ArrayList<>();
            select_item_adapter = new select_item_adapter(select_item.this, itemlist);

//        item_list_cursour = mydb.getitemlist();
//        Toast.makeText(this, "size"+String.valueOf(item_list_cursour.getCount()), Toast.LENGTH_SHORT).show();


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(select_item.this, 3);
        items_recycler.setLayoutManager(mLayoutManager);
        items_recycler.setAdapter(select_item_adapter);





//        load_item_list();

        load_items();



    }

    private void load_items() {


        StringRequest postRequest = new StringRequest(Request.Method.GET, constant.BASE_URL +constant.ITEM_LIST ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(select_item.this, response, Toast.LENGTH_SHORT).show();





                        try {

                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                String  item_name = c.getString("name");
                                String item_price=c.getString("price");
                                String item_image=c.getString("image");




                                select_item_album cat;
                                cat = new select_item_album(item_name,constant.ITEM_IMAGE+item_image,item_price);
                                itemlist.add(cat);




                            }

                            select_item_adapter.notifyDataSetChanged();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(select_item.this, error.toString(), Toast.LENGTH_SHORT).show();

                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                6 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        requestQueue.add(postRequest);




    }

//    private void load_item_list() {
//
//
//            itemlist = new ArrayList<>();
//            select_item_adapter = new select_item_adapter(select_item.this, itemlist);
//
//
//            LinearLayoutManager linearLayoutManager;
//            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
//            linearLayoutManager.setReverseLayout(false);
//            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(select_item.this, 5);
//            items_recycler.setLayoutManager(mLayoutManager);
//        items_recycler.setAdapter(select_item_adapter);
//
//            select_item_album cat;
//
//
//            while (item_list_cursour.moveToNext()) {
//
//                String itemname = item_list_cursour.getString(item_list_cursour.getColumnIndex(DBHelper.item_name));
//                String image = item_list_cursour.getString(item_list_cursour.getColumnIndex(DBHelper.image));
//                String price = item_list_cursour.getString(item_list_cursour.getColumnIndex(DBHelper.item_price));
//
//                Toast.makeText(this,"item name"+ itemname, Toast.LENGTH_SHORT).show();
//
//                cat = new select_item_album(itemname,image,item_price);
//                itemlist.add(cat);
//            }
//
//
//            select_item_adapter.notifyDataSetChanged();
//
//
//
//    }

}
