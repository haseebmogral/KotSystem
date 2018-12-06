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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.poovam.pinedittextfield.SquarePinField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.constants.constant;

import static kot.amits.com.kotsystem.DBhelper.DBmanager.*;


public class Branch_selection extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner;
    Button nextstep;
    SquarePinField pin_number;
    RequestQueue requestQueue;
    String[] loc_id;
    String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue=Volley.newRequestQueue(this);
        setContentView(R.layout.activity_branch_selection);
        spinner = (Spinner) findViewById(R.id.spinner);
        nextstep=(Button)findViewById(R.id.next);
        pin_number=(SquarePinField)findViewById(R.id.pin_number);

        pin_number.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               loc=loc_id[position];
                Toast.makeText(Branch_selection.this, loc, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nextstep.setOnClickListener(this);

//        categories.add("MIMS Cafe,Uppala");
//        categories.add("MIMS Cafe,Kanhagngad");
//        categories.add("MIMS Cafe,Kochi");


        // Creating adapter for spinner

        // Drop down layout style - list view with radio button

        // attaching data adapter to spinner


        load_branches();

    }

    private void load_branches() {

        StringRequest postRequest = new StringRequest(Request.Method.GET, constant.BASE_URL +constant.BRANCH_LIST ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(select_item.this, response, Toast.LENGTH_SHORT).show();


                        try {



                            JSONArray data = new JSONArray(response.toString());
                            String[] spinnerArray = new String[data.length()];
                            loc_id=new String[data.length()];


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                String  branch_id = c.getString("branch_id");
                                String location=c.getString("location");

                                loc_id[i]=branch_id;
                                spinnerArray[i] = location;




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
                                editor.commit();

                                Log.d("bid",sharedpreferences.getString(DBmanager.sharedpreference_branch_id,""));

                                Intent intent=new Intent(Branch_selection.this,category_selection.class);
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

    }
