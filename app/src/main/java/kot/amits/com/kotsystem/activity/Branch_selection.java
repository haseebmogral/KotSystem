package kot.amits.com.kotsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.poovam.pinedittextfield.SquarePinField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.constants.constant;

import static kot.amits.com.kotsystem.DBhelper.DBmanager.*;


public class Branch_selection extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner,business_spinner;
    Button nextstep;
    SquarePinField pin_number;
    RequestQueue requestQueue;
    String[] branch_ids, business_ids,spinnerArray,business_id_list,business_names,branch_addresses;
    String loc,bsns_id,branch_location,business_id,branch_address;
    DBmanager dBmanager;
    RelativeLayout business_layout,branch_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue=Volley.newRequestQueue(this);
        setContentView(R.layout.activity_branch_selection);
        if (check_app_setup_status()==true){
            Intent intent=new Intent(Branch_selection.this,main_screen.class);
            startActivity(intent);
            finish();
        }
        else{

        }
        spinner = (Spinner) findViewById(R.id.spinner);
        business_spinner = (Spinner) findViewById(R.id.business);
        nextstep=(Button)findViewById(R.id.next);
        pin_number=(SquarePinField)findViewById(R.id.pin_number);

        business_layout=findViewById(R.id.business_layout);
        branch_layout=findViewById(R.id.branch_layout);

        dBmanager=new DBmanager(this);
        dBmanager.open();
        load_business();

        pin_number.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               loc= branch_ids[position];
               bsns_id= business_ids[position];
               branch_location= spinnerArray[position];
               branch_address= branch_addresses[position];
                Toast.makeText(Branch_selection.this, loc, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        business_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                business_id=business_id_list[position];
                branch_layout.setVisibility(View.VISIBLE);
                Log.d("business_id",business_id);
                load_branches();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nextstep.setOnClickListener(this);

    }

    private void load_branches() {

        StringRequest postRequest = new StringRequest(Request.Method.GET, constant.BASE_URL +constant.BRANCH_LIST+business_id ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Branch_selection.this, response, Toast.LENGTH_SHORT).show();
                        Log.d("response",response);
                        try {
                            JSONArray data = new JSONArray(response);
                            spinnerArray = new String[data.length()];
                            branch_ids =new String[data.length()];
                            business_ids =new String[data.length()];
                            branch_addresses =new String[data.length()];

//                            JSONObject a=data.getJSONObject(1);
//                            Log.d("allbranch",a.getString("branch_id"));
//                            Log.d("allbranchlength",String.valueOf(data.length()));

//                            Log.d("allbranch","started");

                            for (int i = 0; i < data.length(); i++) {
//                                Log.d("allbranch","started");
//                                Log.d("allbranch",data.getString(1));


                                JSONObject c = data.getJSONObject(i);
//                                Log.d("allbranchid",c.getString("branch_id"));
//                                Log.d("allbranchloc",c.getString("location"));
//                                Log.d("allbranchbusiness",c.getString("business_id"));

                                String  branch_id = c.getString("branch_id");
                                String location=c.getString("location");
                                String bsns_id=c.getString("business_id");
                                String branch_address=c.getString("address");
                                Log.d("allbranch",branch_id+"\n"+location);

                                branch_ids[i]=branch_id;
                                business_ids[i]=bsns_id;
                                spinnerArray[i] = location;
                                branch_addresses[i] = branch_address;

//                                Log.d("allbranch",branch_id);
//                                Log.d("allbranch",location);

                            }

                            ArrayAdapter<String> adapter =new ArrayAdapter<String>(Branch_selection.this,android.R.layout.simple_spinner_item, spinnerArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Branch_selection.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    private void load_business() {

        StringRequest postRequest = new StringRequest(Request.Method.GET, constant.BASE_URL +constant.BUSINESS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        try {
                            JSONArray data = new JSONArray(response);
                            business_id_list = new String[data.length()];
                            business_names = new String[data.length()];

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                String  business_id = c.getString("business_id");
                                String  business_name = c.getString("b_name");
                                business_id_list[i]=business_id;
                                business_names[i]=business_name;
                            }

                            ArrayAdapter<String> adapter =new ArrayAdapter<String>(Branch_selection.this,android.R.layout.simple_spinner_item, business_names);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            business_spinner.setAdapter(adapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Branch_selection.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };

    @Override
    public void onClick(View v) {

        if (v==nextstep){

            if (pin_number.getText().toString().equals("")){

                Toast.makeText(this, "Please enter pin number", Toast.LENGTH_SHORT).show();
            }
            else {
                String n=pin_number.getText().toString();
                String url=constant.BASE_URL+constant.MANAGER_LOGIN+loc+"/"+n;


                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response",response.toString());

//                        Toast.makeText(Branch_selection.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                          String a=  response.getString("status");
                          String b=  response.getString("msg");
//                            Toast.makeText(Branch_selection.this, a, Toast.LENGTH_SHORT).show();
                            if (a.equals("success")){
                                sharedpreferences = getSharedPreferences(sharedpreference_name, Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(DBmanager.sharedpreference_password,pin_number.getText().toString());
                                editor.putString(DBmanager.sharedpreference_branch_id,loc);
                                editor.putString(DBmanager.sharedpreference_business_id,bsns_id);
                                editor.putString(DBmanager.sharedpreference_branch_name,branch_location);
                                editor.putString(DBmanager.sharedpreference_branch_address,branch_address);
                                editor.commit();

                                Log.d("bid",sharedpreferences.getString(DBmanager.sharedpreference_branch_id,""));

                                Intent intent=new Intent(Branch_selection.this,category_selection.class);
                                intent.putExtra("mode","start");
                                startActivity(intent);
                                Toast.makeText(Branch_selection.this, b, Toast.LENGTH_SHORT).show();

                            }
                            else{

                                Toast.makeText(Branch_selection.this,"Login Failed", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                        if (response !=null)
//                        {
//
//                            JSONArray b;
//
//
//                            try {
//                                b=response.getJSONArray("status");
//
//                                Toast.makeText(Branch_selection.this, b.toString(), Toast.LENGTH_SHORT).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                        else {
//
//                            Toast.makeText(Branch_selection.this, "nothing", Toast.LENGTH_SHORT).show();
//
//                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(jsonObjectRequest);

            }








            }
        }

        public boolean check_app_setup_status(){
            sharedpreferences = getSharedPreferences(sharedpreference_name, Context.MODE_PRIVATE);

            String setup= sharedpreferences.getString(dBmanager.sharedpreference_app_setup,"");
        if (setup.equals("1")){
            return true;
        }
        else{
            return false;
        }
        }

    }
