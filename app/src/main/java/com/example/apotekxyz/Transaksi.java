package com.example.apotekxyz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Transaksi extends AppCompatActivity {
ObatAdapter obatAdapter;
JenisAdapter jenisAdapter;
ArrayList<String> listid,listnm,listhr;
String idtrans,nmjenis;
Integer tb;
    SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        sesion=getSharedPreferences("sesion", Context.MODE_PRIVATE);
        Koneksi koneksi=new Koneksi();
        listnm=new ArrayList<String>();
        listhr=new ArrayList<String>();
        listid=new ArrayList<String>();
        EditText pasien=findViewById(R.id.pasien);
        EditText obat=findViewById(R.id.obat);
        EditText jo=findViewById(R.id.jo);
        EditText dokter=findViewById(R.id.dokter);
        TextView sb=findViewById(R.id.sb);
        AppCompatButton tambah=findViewById(R.id.tambah);
        BottomSheetDialog bsd=new BottomSheetDialog(Transaksi.this);
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
        obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater=getLayoutInflater();
                View dialogView=layoutInflater.inflate(R.layout.d_obat,null);
                List<ObatModel> obatModelList=new ArrayList<>();
                ListView listView=dialogView.findViewById(R.id.list_item);

                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest=new StringRequest(Request.Method.GET, koneksi.getUrl() + "api/tblobats", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray=null;
                        try {
                            jsonArray=new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                obatModelList.add(new ObatModel(jsonObject.getString("idobat"), jsonObject.getString("nama"), jsonObject.getString("harga") ));
                            }
                            obatAdapter=new ObatAdapter(getApplicationContext(),R.layout.obat,bsd,obatModelList,listid,listnm,listhr);
                            listView.setAdapter(obatAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bsd.setContentView(dialogView);
                        bsd.show();
                        SearchView cari=dialogView.findViewById(R.id.cari);
                        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                obatAdapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                        Button submit=dialogView.findViewById(R.id.submit);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listnm=obatAdapter.getListnm();
                                listhr=obatAdapter.getListhr();
                                obat.setText(listnm.toString());
                                String harga;
                                tb=0;
                                for (int i = 0; i < listhr.size(); i++) {
                                    harga=listhr.get(i);
                                    tb=tb+Integer.parseInt(harga);
                                }
                                sb.setText(formatRupiah(Double.parseDouble(String.valueOf(tb))));
                                bsd.dismiss();
                            }
                        });
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
        });
        jo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater=getLayoutInflater();
                View dialogView=layoutInflater.inflate(R.layout.d_jenis,null);
                List<JenisModel> jenisModelList=new ArrayList<>();
                ListView listView=dialogView.findViewById(R.id.list_item);

                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest=new StringRequest(Request.Method.GET, koneksi.getUrl() + "api/tbljenis", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray=null;
                        try {
                            jsonArray=new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                jenisModelList.add(new JenisModel(jsonObject.getString("nama")));
                            }
                            jenisAdapter=new JenisAdapter(getApplicationContext(),R.layout.jenis,bsd,jenisModelList,nmjenis,jo);
                            listView.setAdapter(jenisAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bsd.setContentView(dialogView);
                        bsd.show();
                        SearchView cari=dialogView.findViewById(R.id.cari);
                        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                jenisAdapter.getFilter().filter(newText);
                                return false;
                            }
                        });
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
        });
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pasien.getText().length()==0){
                    pasien.setError("Masukan nama pasien");
                }else if (obat.getText().length()==0){
                    obat.setError("pilih nama obat");
                }else if (jo.getText().length()==0){
                    jo.setError("Pilih jenis obat");
                }else if (dokter.getText().length()==0){
                    dokter.setError("Masukan nama dokter");
                }else {
                    RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest=new StringRequest(Request.Method.GET, koneksi.getUrl() + "noresep", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String notrans=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            String tgltrans=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                            String tglresep=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                            try {
                                String noresep;
                                JSONArray jsonArray=new JSONArray(response);
                                if (jsonArray.length()!=0){
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String nores=jsonObject.getString("nores");
                                    String kanan=nores.substring(nores.length()-3);
                                    int urut=Integer.parseInt(kanan)+1;
                                    noresep="R"+String.format("%03d",urut);
                                }else {
                                    noresep="R001";
                                }
                                RequestQueue requestQueue1= Volley.newRequestQueue(getApplicationContext());
                                StringRequest stringRequest1=new StringRequest(Request.Method.POST, koneksi.getUrl() + "api/tbltrans", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject=new JSONObject(response);
                                            idtrans=jsonObject.getString("idtrans");

                                            listid=obatAdapter.getListid();
                                            for (int i = 0; i < listid.size(); i++) {
                                                String idobt=listid.get(i);
                                                String nmobt=listnm.get(i);

                                                RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
                                                StringRequest stringRequest2=new StringRequest(Request.Method.POST, koneksi.getUrl() + "api/tblreseps", new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Intent intent=new Intent(getApplicationContext(),Invoice.class);
                                                        intent.putExtra("total",sb.getText().toString());
                                                        intent.putExtra("pasien",pasien.getText().toString());
                                                        intent.putExtra("namaobat",listnm);
                                                        intent.putExtra("jo",jo.getText().toString());
                                                        intent.putExtra("notrans",notrans);
                                                        startActivity(intent);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Transaksi.this,"gagal transaksi",Toast.LENGTH_SHORT).show();
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
                                                        param.put("namaobat",nmobt);
                                                        param.put("jumlahobat","1");
                                                        param.put("idobat",idobt);
                                                        param.put("idtrans",idtrans);
                                                        return param;
                                                    }
                                                };
                                                requestQueue2.getCache().clear();
                                                requestQueue2.add(stringRequest2);
                                            }
                                            Toast.makeText(Transaksi.this,"Transaksi Berhasil",Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Transaksi.this,"gagal transaksi",Toast.LENGTH_SHORT).show();
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
                                        param.put("notrans",notrans);
                                        param.put("tgltrans",tgltrans);
                                        param.put("total",String.valueOf(tb));
                                        param.put("iduser",sesion.getString("iduser",""));
                                        param.put("namakasir", sesion.getString("nama",""));
                                        param.put("status","1");
                                        param.put("dokter",dokter.getText().toString());
                                        param.put("pasien",pasien.getText().toString());
                                        param.put("nores",noresep);
                                        param.put("tglres",tglresep);
                                        return param;
                                    }
                                };
                                requestQueue1.getCache().clear();
                                requestQueue1.add(stringRequest1);
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
        });
    }
    private String formatRupiah(Double number){
        Locale locale=new Locale("in","ID");
        NumberFormat format=NumberFormat.getCurrencyInstance(locale);
        return format.format(number);
    }
}
