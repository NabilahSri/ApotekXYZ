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
import android.widget.ImageView;
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

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText user=findViewById(R.id.user);
        Koneksi koneksi=new Koneksi();
        EditText pass=findViewById(R.id.pass);
        EditText nama=findViewById(R.id.nama);
        EditText konfirm=findViewById(R.id.konfirm);
        ImageView back=findViewById(R.id.back);
        EditText alamat=findViewById(R.id.alamat);
        EditText telp=findViewById(R.id.telp);
        AppCompatButton daftar=findViewById(R.id.daftar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nama.getText().length()==0){
                    nama.setError("Masukan nama lengkap anda");
                }else if (user.getText().length()==0){
                    user.setError("Masukan nama lengkap anda");
                }else if (telp.getText().length()==0){
                    telp.setError("Masukan nama lengkap anda");
                }else if (alamat.getText().length()==0){
                    alamat.setError("Masukan nama lengkap anda");
                }else if (pass.getText().length()==0){
                    pass.setError("Masukan nama lengkap anda");
                }else if (konfirm.getText().length()==0){
                    konfirm.setError("Masukan nama lengkap anda");
                }else {
                    if (pass.getText().toString().equals(konfirm.getText().toString())){
                        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, koneksi.getUrl() + "api/tblusers", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    if (jsonObject.getString("status").equals("success")){
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        Toast.makeText(Register.this,"Berhasil register",Toast.LENGTH_SHORT).show();
                                    } else if (jsonObject.getString("status").equals("failed")) {
                                        Toast.makeText(Register.this,"Gagal register",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Register.this,"gagal menampilkan halaman register",Toast.LENGTH_SHORT).show();
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
                                param.put("nama",nama.getText().toString());
                                param.put("alamat",alamat.getText().toString());
                                param.put("telp",telp.getText().toString());
                                param.put("tipe","Member");
                                param.put("pass",pass.getText().toString());
                                return param;
                            }
                        };
                        requestQueue.getCache().clear();
                        requestQueue.add(stringRequest);
                    }else {
                        Toast.makeText(Register.this,"Passsword anda tidak sesuai",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}