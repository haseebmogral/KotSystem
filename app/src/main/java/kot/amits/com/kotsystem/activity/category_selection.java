package kot.amits.com.kotsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Map;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.category_model.DataModel;
import kot.amits.com.kotsystem.category_model.category_adapter;
import kot.amits.com.kotsystem.constants.constant;

import static kot.amits.com.kotsystem.DBhelper.DBmanager.*;

public class category_selection extends AppCompatActivity implements View.OnClickListener {

    ArrayList dataModels;
    ListView listView;
    public category_adapter adapter;
    RequestQueue requestQueue;
    Button gotonext;
    ArrayList<String> cat_List;
    String output = "";
    DBmanager dBmanager;
    String mode;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
        setTitle("Select Category");
        mode=getIntent().getStringExtra("mode");
        dBmanager=new DBmanager(this);
        dBmanager.open();
        sharedpreferences = getSharedPreferences(sharedpreference_name, Context.MODE_PRIVATE);

        gotonext = findViewById(R.id.next);
        gotonext.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);
        cat_List = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listView);
        dataModels = new ArrayList();
        adapter = new category_adapter(dataModels, getApplicationContext(), cat_List);

//        if (mode.equals("start")){
//            load_categories();
//        }
//        else{
//            cursor=dBmanager.getData();
//            load_cat(cursor);
//        }


    }

    private void load_categories() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, constant.BASE_URL + constant.CATEGORY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(category_selection.this, response, Toast.LENGTH_SHORT).show();


                        try {
                            JSONArray data = new JSONArray(response);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                String[] cat_id = c.getString("f_cat_id").split(",");
                                String[] cat_name = c.getString("food_cat").split(",");
                                String[] cat_type = c.getString("food_type").split(",");
                                Log.d("cat_type",cat_type[0]);
                                int catid = Integer.parseInt(cat_id[0]);
                                String catname = cat_name[0];
                                String category_type = cat_type[0];

                                dBmanager.insertcategory(catid, catname,category_type,"0");
                                dBmanager.update_category(catname,"0");

                                dataModels.add(new DataModel(catid, catname, false,category_type));

                                Log.d(String.valueOf(dataModels), "get_category");


                            }

                            listView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(category_selection.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        //AppController.getInstance().addToRequestQueue(postRequest,
//                "get_order_details_request");

    }

    public void load_cat(Cursor cursor){
        Log.d("correct","correct");
        while (cursor.moveToNext()){
            DataModel a;

            int catid = cursor.getInt(cursor.getColumnIndex(DBHelper.cat_id));
            String catname =cursor.getString(cursor.getColumnIndex(DBHelper.cat_name));
            String category_type = cursor.getString(cursor.getColumnIndex(DBHelper.cat_type));
            String status = cursor.getString(cursor.getColumnIndex(DBHelper.cat_status));
            boolean isactive;
            if (status.equals("1")){
                isactive=true;
            }
            else{
                isactive=false;
            }
            Log.d("selection", String.valueOf(isactive));
             dBmanager.update_category(catname,"0");
            a=new DataModel(catid, catname, isactive,category_type);

            dataModels.add(a);
            Log.d(String.valueOf(dataModels), "get_category");


        }

        listView.setAdapter(adapter);



    }

    @Override
    public void onBackPressed() {
        if (mode.equals("start")){
            Toast.makeText(this, R.string.select, Toast.LENGTH_SHORT).show();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostResume() {
        if (mode.equals("start")){
            load_categories();
        }
        else{
            cursor=dBmanager.getData();
            load_cat(cursor);
        }
        super.onPostResume();
    }

    @Override
    public void onClick(View v) {
//        output="";
//        gotonext.setVisibility(View.INVISIBLE);
//        for (int i = 0; i < cat_List.size(); i++) {
//            DataModel a;
//            a= (DataModel) dataModels.get(i);
//            boolean f=a.isChecked();
//            Log.d("checked", String.valueOf(f));
//            String[] seperated=cat_List.get(i).split("/");
//            String myConcatedString =seperated[1].concat(",");
//            output = output + myConcatedString;
//            Log.d("cat_ids",myConcatedString);
////            Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
//        }
                boolean se=isselectedone(dataModels);
                Log.d("selection",String.valueOf(se));
          if (se==false){
              Toast.makeText(this, "atleast select one category to continue", Toast.LENGTH_SHORT).show();
          }
          else{
              for (int i=0;i<dataModels.size();i++){
                  DataModel a;
                  a= (DataModel) dataModels.get(i);
                  boolean f=a.isChecked();
                  if (f==true){
                      dBmanager.update_category(a.getName(),"1");
                  }

              }
              Intent intent=new Intent(category_selection.this,select_item.class);
              intent.putExtra("mode",mode);
              startActivity(intent);
          }

//            StringRequest postcat_id = new StringRequest(Request.Method.POST, constant.BASE_URL + constant.INSERT_CATEGORY_BY_BRANCH,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            Toast.makeText(category_selection.this, response, Toast.LENGTH_SHORT).show();
//                            try {
//                                JSONObject jsonObject=new JSONObject(response);
//                                if(jsonObject.getString("status").equals("success")){
//                                    Intent intent=new Intent(category_selection.this,select_item.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                                else{
//                                    gotonext.setVisibility(View.VISIBLE);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            Toast.makeText(category_selection.this, error.toString(), Toast.LENGTH_SHORT).show();
//
//                            // error
//                            Log.d("Error.Response", error.toString());
//                        }
//                    }
//            ) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("data",output);
//                    params.put("bid",sharedpreferences.getString(sharedpreference_branch_id,""));
//                    params.put("bussiness_id",sharedpreferences.getString(sharedpreference_business_id,""));
//
//                    return params;
//                }
//            };
//
//            postcat_id.setRetryPolicy(new DefaultRetryPolicy(
//                    6 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(postcat_id);





    }
    public boolean isselectedone(ArrayList<DataModel>  a){
        for (DataModel b:a){
            if (b.isChecked()==true){
                return true;
            }
        }
        return false;
    }
}




