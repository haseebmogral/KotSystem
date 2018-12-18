package kot.amits.com.kotsystem.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.MySingleton;
import kot.amits.com.kotsystem.constants.constant;

import static kot.amits.com.kotsystem.DBhelper.DBHelper.BRANCH_ID_KEY;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.BUSINESS_ID_KEY;
import static kot.amits.com.kotsystem.DBhelper.DBmanager.*;

public class settings_activity extends AppCompatActivity {
    LinearLayout sync,column;
    DBmanager dBmanager;
    Cursor cursor;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);




        sync = findViewById(R.id.sync);
        column = findViewById(R.id.item_column);
        dBmanager = new DBmanager(this);
        dBmanager.open();

        sharedpreferences = getSharedPreferences(DBmanager.sharedpreference_name, Context.MODE_PRIVATE);



        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sync_expense();
//                sync_slaes();
                sync_slaes_items();
//                sync_supplier();
//                sync_customer();
//                sync_feedback();
//                sync_employee();

            }
        });

        column.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(settings_activity.this);
                View promptsView = li.inflate(R.layout.column_adapter, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        settings_activity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.columns);

                userInput.setText(sharedpreferences.getString(dBmanager.sharedpreference_column,""));

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        userInput.setText(userInput.getText());

                                        SharedPreferences.Editor editor = sharedpreferences.edit();

                                        editor.putString(dBmanager.sharedpreference_column,userInput.getText().toString());
                                        editor.commit();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }

        });
    }

    public void sync_slaes() {
        String output="";
        JsonArray array = new JsonArray();
        cursor = dBmanager.get_all_sales_report();
        if (cursor.getCount() > 0) {
            JsonObject object = null;
            while (cursor.moveToNext()) {
                object = new JsonObject();
                object.addProperty(BUSINESS_ID_KEY,sharedPreferences.getString(dBmanager.sharedpreference_business_id,""));
                object.addProperty(BRANCH_ID_KEY,sharedPreferences.getString(dBmanager.sharedpreference_branch_id,""));
                object.addProperty(DBHelper.cart_id, cursor.getString(cursor.getColumnIndex(DBHelper.cart_id)));
                object.addProperty(DBHelper.cart_customer_id, cursor.getString(cursor.getColumnIndex(DBHelper.cart_customer_id)));
                object.addProperty(DBHelper.date, cursor.getString(cursor.getColumnIndex(DBHelper.date)));
                object.addProperty(DBHelper.time, cursor.getString(cursor.getColumnIndex(DBHelper.time)));
                object.addProperty(DBHelper.cart_status, cursor.getString(cursor.getColumnIndex(DBHelper.cart_status)));
                object.addProperty(DBHelper.total, cursor.getString(cursor.getColumnIndex(DBHelper.total)));
                object.addProperty(DBHelper.cart_type, cursor.getString(cursor.getColumnIndex(DBHelper.cart_type)));
                array.add(object);
            }
            Log.d("json", array.toString());
        }
        output=array.toString();
        sync(output,"sales_details");
    }

    public void sync_slaes_items() {
        String output="";
        JsonArray array = new JsonArray();
        cursor = dBmanager.get_all_sales_items_upload();
        if (cursor.getCount() > 0) {
            JsonObject object = null;
            while (cursor.moveToNext()) {
                object = new JsonObject();
                object.addProperty("business_ids","1");
                object.addProperty("branch_id","1");
                object.addProperty(DBHelper.cart_details_id, cursor.getString(cursor.getColumnIndex(DBHelper.cart_details_id)));
                object.addProperty(DBHelper.c_item_id, cursor.getString(cursor.getColumnIndex(DBHelper.c_item_id)));
                object.addProperty(DBHelper.c_qty, cursor.getString(cursor.getColumnIndex(DBHelper.c_qty)));
                object.addProperty(DBHelper.c_qty, cursor.getString(cursor.getColumnIndex(DBHelper.c_qty)));
                object.addProperty(DBHelper.c_total, cursor.getString(cursor.getColumnIndex(DBHelper.c_total)));
                array.add(object);
            }
            Log.d("sales_items", array.toString());
        }
        output=array.toString();
        sync(output,"sales_items");
    }

    public void sync_purchase() {
        String output="";
        JsonArray array = new JsonArray();
        cursor = dBmanager.get_purchase();
        if (cursor.getCount() > 0) {
            JsonObject object = null;
            while (cursor.moveToNext()) {
                object = new JsonObject();
                object.addProperty(DBHelper.p_supplier_id, cursor.getString(cursor.getColumnIndex(DBHelper.p_supplier_id)));
                object.addProperty(DBHelper.p_date, cursor.getString(cursor.getColumnIndex(DBHelper.p_date)));
                object.addProperty(DBHelper.p_description, cursor.getString(cursor.getColumnIndex(DBHelper.p_description)));
                object.addProperty(DBHelper.p_amount, cursor.getString(cursor.getColumnIndex(DBHelper.p_amount)));
                object.addProperty(DBHelper.p_paid, cursor.getString(cursor.getColumnIndex(DBHelper.p_paid)));
                object.addProperty(DBHelper.p_bal, cursor.getString(cursor.getColumnIndex(DBHelper.p_bal)));
                array.add(object);


            }
            Log.d("json", array.toString());
        }
        output=array.toString();
        sync(output,"purchase");
    }

    public void sync_supplier() {
        String output="";
        JsonArray array = new JsonArray();
        cursor = dBmanager.get_supplier();
        if (cursor.getCount() > 0) {
            JsonObject object = null;
            while (cursor.moveToNext()) {
                object = new JsonObject();
                object.addProperty(DBHelper.supplier_id, cursor.getString(cursor.getColumnIndex(DBHelper.supplier_id)));
                object.addProperty(DBHelper.supplier_name, cursor.getString(cursor.getColumnIndex(DBHelper.supplier_name)));
                object.addProperty(DBHelper.supplier_address, cursor.getString(cursor.getColumnIndex(DBHelper.supplier_address)));
                object.addProperty(DBHelper.supplier_contact, cursor.getLong(cursor.getColumnIndex(DBHelper.supplier_contact)));
                array.add(object);


            }
            Log.d("json", array.toString());
        }
        output=array.toString();
        sync(output,"supplier");
    }

    public void sync_customer() {
        String output="";
        JsonArray array = new JsonArray();
        cursor = dBmanager.get_supplier();
        if (cursor.getCount() > 0) {
            JsonObject object = null;
            while (cursor.moveToNext()) {
                object = new JsonObject();
                object.addProperty(DBHelper.supplier_id, cursor.getString(cursor.getColumnIndex(DBHelper.supplier_id)));
                object.addProperty(DBHelper.supplier_name, cursor.getString(cursor.getColumnIndex(DBHelper.supplier_name)));
                object.addProperty(DBHelper.supplier_address, cursor.getString(cursor.getColumnIndex(DBHelper.supplier_address)));
                object.addProperty(DBHelper.supplier_contact, cursor.getString(cursor.getColumnIndex(DBHelper.supplier_contact)));
                array.add(object);


            }
            Log.d("json", array.toString());
        }
        output=array.toString();
        sync(output,"customer");
    }

    public void sync_feedback() {
        JsonArray array = new JsonArray();
        String output="";
        cursor = dBmanager.get_feedback();
        if (cursor.getCount() > 0) {
            JsonObject object = null;
            while (cursor.moveToNext()) {
                object = new JsonObject();
                object.addProperty(DBHelper.feedback_id, cursor.getString(cursor.getColumnIndex(DBHelper.feedback_id)));
                object.addProperty(DBHelper.feedback_order_id, cursor.getString(cursor.getColumnIndex(DBHelper.feedback_order_id)));
                object.addProperty(DBHelper.ambience_rating, cursor.getString(cursor.getColumnIndex(DBHelper.ambience_rating)));
                object.addProperty(DBHelper.staff_rating, cursor.getString(cursor.getColumnIndex(DBHelper.staff_rating)));
                object.addProperty(DBHelper.feedback_review, cursor.getString(cursor.getColumnIndex(DBHelper.feedback_review)));
                array.add(object);
            }
            Log.d("json", array.toString());
        }
        output=array.toString();
        sync(output,"customer");
    }

    public void sync_expense() {
        String output="";
        cursor = dBmanager.get_expense_for_sync();
        JsonArray array = new JsonArray();
        if (cursor.getCount() > 0) {
            JsonObject object = null;
            while (cursor.moveToNext()) {
                object = new JsonObject();
                object.addProperty("business_ids","1");
                object.addProperty("branch_id","1");
                object.addProperty(DBHelper.e_id, cursor.getString(cursor.getColumnIndex(DBHelper.e_id)));
                object.addProperty(DBHelper.e_type, cursor.getString(cursor.getColumnIndex(DBHelper.e_type)));
                object.addProperty(DBHelper.e_amount, cursor.getString(cursor.getColumnIndex(DBHelper.e_amount)));
                object.addProperty(DBHelper.e_desc, cursor.getString(cursor.getColumnIndex(DBHelper.e_desc)));
                object.addProperty(DBHelper.e_date, cursor.getString(cursor.getColumnIndex(DBHelper.e_date)));
                array.add(object);
            }
            Log.d("json", array.toString());
        }
        output=array.toString();
        sync(output,"expense");


    }

    public void sync_employee() {
        String output="";
        JsonArray array = new JsonArray();
        cursor = dBmanager.get_employee();
        if (cursor.getCount() > 0) {
            JsonObject object = null;
            while (cursor.moveToNext()) {
                object = new JsonObject();
                object.addProperty(DBHelper.emp_id, cursor.getString(cursor.getColumnIndex(DBHelper.emp_id)));
                object.addProperty(DBHelper.emp_name, cursor.getString(cursor.getColumnIndex(DBHelper.emp_name)));
                object.addProperty(DBHelper.emp_address, cursor.getString(cursor.getColumnIndex(DBHelper.emp_address)));
                object.addProperty(DBHelper.emp_contact, cursor.getLong(cursor.getColumnIndex(DBHelper.emp_contact)));
                object.addProperty(DBHelper.emp_designation, cursor.getString(cursor.getColumnIndex(DBHelper.emp_designation)));
                object.addProperty(DBHelper.emp_status, cursor.getString(cursor.getColumnIndex(DBHelper.emp_status)));
                object.addProperty(DBHelper.emp_salary, cursor.getString(cursor.getColumnIndex(DBHelper.emp_salary)));
                object.addProperty(DBHelper.emp_doj, cursor.getString(cursor.getColumnIndex(DBHelper.emp_doj)));
                array.add(object);


            }
            Log.d("json", array.toString());
        }
        output=array.toString();
        sync(output,"customer");

    }
    public void sync(final String json, final String table_name){
            String url= constant.BASE_URL+constant.SYNC_DATA;
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);

                            try {
                                JSONArray data = new JSONArray(response);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject c = data.getJSONObject(i);
                                    String value_type = c.getString("value_type");
                                    String ids = c.getString("id");
                                    if (value_type.equals("expense")){
                                        dBmanager.update_expense_upload_status(ids);
                                    }
                                    else if (value_type.equals("sales_details")){
                                        dBmanager.update_sales_details_upload_status(ids);
                                    } else if (value_type.equals("sales_items")){
//                                        dBmanager.update_sales_details_upload_status(ids);
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put(table_name,json);

                    return params;
                }
            };

            // Add StringRequest to the RequestQueue
            MySingleton.getInstance(this).addToRequestQueue(postRequest);

        }

    }


