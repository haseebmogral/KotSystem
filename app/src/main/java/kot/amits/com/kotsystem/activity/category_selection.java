package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.category_model.DataModel;
import kot.amits.com.kotsystem.category_model.category_adapter;
import kot.amits.com.kotsystem.constants.AppController;
import kot.amits.com.kotsystem.constants.constant;

public class category_selection extends AppCompatActivity implements View.OnClickListener {

    ArrayList dataModels;
    ListView listView;
    public category_adapter adapter;
    RequestQueue requestQueue;
    Button gotonext;
    ArrayList<String> cat_List;
    Bundle extras;
    String output = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
        setTitle("Select Category");
        gotonext = findViewById(R.id.next);
        gotonext.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);
        cat_List = new ArrayList<String>();


        load_categories();

        listView = (ListView) findViewById(R.id.listView);

        dataModels = new ArrayList();



//        dataModels.add(new DataModel(1, "Apple Pie", false));
//        dataModels.add(new DataModel(2, "Apple ", false));
//        dataModels.add(new DataModel(3, " Pie", false));


        adapter = new category_adapter(dataModels, getApplicationContext(), cat_List);


    }

    private void load_categories() {
//        AppController.getInstance().cancelPendingRequests();


        StringRequest postRequest = new StringRequest(Request.Method.GET, constant.BASE_URL + constant.CATEGORY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(category_selection.this, response, Toast.LENGTH_SHORT).show();


                        try {
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                String[] cat_id = c.getString("f_cat_id").split(",");
                                String[] cat_name = c.getString("food_cat").split(",");


                                int catid = Integer.parseInt(cat_id[0]);
                                String catname = cat_name[0];


                                dataModels.add(new DataModel(catid, catname, false));

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

    @Override
    public void onClick(View v) {


        for (int i = 0; i < cat_List.size(); i++) {
//            Toast.makeText(this,String.valueOf(cat_List.get(i)) , Toast.LENGTH_SHORT).show();

            String myConcatedString = cat_List.get(i).concat(",");

            output = output + myConcatedString;

            Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
        }

        if (output.equals("")) {

            Toast.makeText(this, "please select atleast one cateory", Toast.LENGTH_SHORT).show();
        } else {



            StringRequest postcat_id = new StringRequest(Request.Method.GET, constant.BASE_URL + constant.INSERT_CATEGORY_TO_DB+output,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(category_selection.this, response, Toast.LENGTH_SHORT).show();

//                            if (response!=null)
//                            {
//                                startActivity(new Intent(category_selection.this, select_item.class));
//
//                            }


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

//                    params.put("cat_id",output);
//
                    return params;
                }
            };

            postcat_id.setRetryPolicy(new DefaultRetryPolicy(
                    6 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            requestQueue.add(postcat_id);


        }


    }
}




