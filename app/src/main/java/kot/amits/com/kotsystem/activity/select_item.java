package kot.amits.com.kotsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import kot.amits.com.kotsystem.constants.constant;
import kot.amits.com.kotsystem.select_item_adapter.select_item_adapter;
import kot.amits.com.kotsystem.select_item_adapter.select_item_album;

import static kot.amits.com.kotsystem.DBhelper.DBmanager.sharedpreference_business_id;
import static kot.amits.com.kotsystem.DBhelper.DBmanager.sharedpreference_name;
import static kot.amits.com.kotsystem.DBhelper.DBmanager.sharedpreferences;

public class select_item extends AppCompatActivity {

    private select_item_adapter select_item_adapter;
    private List<select_item_album> itemlist;
    Cursor item_list_cursour;
    RecyclerView items_recycler;
    DBmanager mydb;
    RequestQueue requestQueue;
    private RecyclerView.Adapter adapter;
    String business_id;
    Button button;
    String mode;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item);
        setTitle("Select Items");
        items_recycler=(RecyclerView)findViewById(R.id.loaditems_recycler);
        button=findViewById(R.id.button);
        mode=getIntent().getStringExtra("mode");

        requestQueue = Volley.newRequestQueue(this);
       sharedpreferences = getSharedPreferences(sharedpreference_name, Context.MODE_PRIVATE);
       business_id=sharedpreferences.getString(sharedpreference_business_id,"");


        mydb=new DBmanager(this);
        mydb.open();


        itemlist = new ArrayList<>();
        select_item_adapter = new select_item_adapter(select_item.this, itemlist);

        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(select_item.this, 6);
        items_recycler.setHasFixedSize(true);
        items_recycler.setLayoutManager(mLayoutManager);
        items_recycler.setAdapter(select_item_adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (select_item_album a :itemlist){
                    if (a.isSelected()==true){
                        mydb.update_items(String.valueOf(a.getid()),"1");
                        Log.d("selected",a.getItem_name()+"-"+a.getid());
                    }
                    else{
                        mydb.update_items(String.valueOf(a.getid()),"0");
                    }
                }
                sharedpreferences = getSharedPreferences(sharedpreference_name, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(DBmanager.sharedpreference_app_setup,"1");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), main_screen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if (mode.equals("start")){
            load_items();
        }
        else{
            cursor=mydb.get_items();
            load_items(cursor);
        }



    }

    private void load_items(Cursor cursor) {
        while (cursor.moveToNext()){
            String  item_id = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBHelper.item_id)));
            String  cat_id = cursor.getString(cursor.getColumnIndex(DBHelper.cat_cat_id));
            String  item_name = cursor.getString(cursor.getColumnIndex(DBHelper.item_name));
            String item_price=cursor.getString(cursor.getColumnIndex(DBHelper.item_price));
            String item_image=cursor.getString(cursor.getColumnIndex(DBHelper.image));
            String isactive_item=cursor.getString(cursor.getColumnIndex(DBHelper.item_active_status));
            int iid= Integer.parseInt(item_id);
            select_item_album cat;
            boolean active_status;
            if (isactive_item.equals("0")){
                active_status=false;
            }
            else {
                active_status=true;
            }

            if (mydb.isactive_category(cat_id)){
                cat = new select_item_album(iid,cat_id,item_name,item_image,item_price,active_status);
                itemlist.add(cat);
            }
        }
        select_item_adapter.notifyDataSetChanged();

    }

    private void load_items() {


        StringRequest postRequest = new StringRequest(Request.Method.GET, constant.BASE_URL +constant.ITEM_LIST+business_id ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(select_item.this, response, Toast.LENGTH_SHORT).show();
                        Log.d("response",response);
                        try {

                            JSONArray data = new JSONArray(response);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                String  item_id = c.getString("fid");
                                String  cat_id = c.getString("f_cat_id");
                                String  item_name = c.getString("name");
                                String item_price=c.getString("price");
                                String item_image=c.getString("image");
                                int iid= Integer.parseInt(item_id);
                                select_item_album cat;

                                mydb.insertitems(iid,item_name,cat_id,item_price,item_image,"0");

                                if (mydb.isactive_category(cat_id)){
                                    cat = new select_item_album(iid,cat_id,item_name,item_image,item_price,true);
                                    itemlist.add(cat);
                                }
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


}
