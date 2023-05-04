package com.example.apotekxyz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profil extends AppCompatActivity {

    SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        Koneksi koneksi=new Koneksi();
        sesion=getSharedPreferences("sesion", Context.MODE_PRIVATE);
        TextView nama=findViewById(R.id.nama);
        TextView user=findViewById(R.id.user);
        TextView alamat =findViewById(R.id.alamat);
        ImageView transaksi=findViewById(R.id.transaksi);
        ImageView profil=findViewById(R.id.profil);
        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Transaksi.class));
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Profil.class));
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, koneksi.getUrl() + "api/tblusers/"+sesion.getString("iduser",""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject  =new JSONObject(response);
                    nama.setText(jsonObject.getString("nama"));
                    user.setText(jsonObject.getString("username"));
                    alamat.setText(jsonObject.getString("alamat"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}