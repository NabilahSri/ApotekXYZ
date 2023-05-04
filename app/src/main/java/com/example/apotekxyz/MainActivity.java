package com.example.apotekxyz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sesion=getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sesion.edit();
        EditText user=findViewById(R.id.user);
        Koneksi koneksi=new Koneksi();
        EditText pass=findViewById(R.id.pass);
        AppCompatButton daftar=findViewById(R.id.daftar);
        AppCompatButton masuk=findViewById(R.id.masuk);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().length()==0){
                    user.setError("Masukan Username anda");
                }else if (pass.getText().length()==0){
                    pass.setError("Masukan Password anda");
                }else {
                    RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, koneksi.getUrl() + "login", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if (jsonObject.getString("status").equals("success")){
                                    try {
                                        JSONObject jsonObject1=new JSONObject(jsonObject.getString("users"));
                                        editor.putString("iduser",jsonObject1.getString("iduser"));
                                        editor.putString("nama",jsonObject1.getString("nama"));
                                        editor.commit();
                                        startActivity(new Intent(getApplicationContext(),Transaksi.class));
                                        Toast.makeText(MainActivity.this,"Berhasil login",Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (jsonObject.getString("status").equals("failed")) {
                                    Toast.makeText(MainActivity.this,"Gagal login",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this,"gagal menampilkan halaman login",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded;charset=UTF-8";
                        }

                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param=new HashMap<>();
                            param.put("username",user.getText().toString());
                            param.put("pass",pass.getText().toString());
                            return param;
                        }
                    };
                    requestQueue.getCache().clear();
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}