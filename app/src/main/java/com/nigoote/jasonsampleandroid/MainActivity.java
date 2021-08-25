package com.nigoote.jasonsampleandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edfname,edlname,edage;
    Button btnSave,btnView;
    TextView listStudent;
    RequestQueue requestQueue;
    String insertURL ="http://192.168.20.225:8080/jsonsample/insert.php";
    String showURL ="http://192.168.20.225:8080/jsonsample/showStudent.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edfname = (EditText) findViewById(R.id.fname);
        edlname = (EditText) findViewById(R.id.lname);
        edage = (EditText) findViewById(R.id.age);
        btnSave = (Button) findViewById(R.id.btnsave);
        btnView = (Button) findViewById(R.id.btnview);
        listStudent = (TextView) findViewById(R.id.list);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showURL,null,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray student = response.getJSONArray("student");
                            for (int i=0; i<student.length();i++){
                                JSONObject students = student.getJSONObject(i);
                                String firstname = students.getString("firstname");
                                String lastname = students.getString("lastname");
                                String age = students.getString("age");
                                Toast.makeText(MainActivity.this, firstname+"", Toast.LENGTH_SHORT).show();
                                listStudent.append(firstname+" "+lastname+" "+age+"\n");
                            }
                            listStudent.append("===\n");
                        } catch (JSONException e) {
                            Log.d("enoc2",e+"erro2");
                            Toast.makeText(MainActivity.this, e+" Not get Student", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("enoc3",error+"erro3");
                        Toast.makeText(MainActivity.this, error+"System Problem", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response+" registered!", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param = new HashMap<String, String>();
                        param.put("firstname",edfname.getText().toString());
                        param.put("lastname",edlname.getText().toString());
                        param.put("age",edage.getText().toString());
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

    }
}