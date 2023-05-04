package com.example.apotekxyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Invoice extends AppCompatActivity {
    File file,f;
    SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        requestPermission();
        LinearLayout linear=findViewById(R.id.linear);
        sesion=getSharedPreferences("sesion", Context.MODE_PRIVATE);
        LinearLayout content=findViewById(R.id.content);
        TextView total=findViewById(R.id.total);
        TextView pasien=findViewById(R.id.pasien);
        TextView nama=findViewById(R.id.nama);
        TextView jo=findViewById(R.id.jo);
        Button save=findViewById(R.id.save);
        Button share=findViewById(R.id.share);
        AppCompatButton selesai=findViewById(R.id.selesai);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Transaksi.class));
            }
        });

        Intent intent=getIntent();
        pasien.setText(intent.getExtras().getString("pasien"));
        nama.setText(sesion.getString("nama",""));
        total.setText(intent.getExtras().getString("total"));
        jo.setText(intent.getExtras().getString("jo"));
        ArrayList<String> arrayList=intent.getExtras().getStringArrayList("namaobat");
        for (int i = 0; i < arrayList.size(); i++) {
            String str=arrayList.get(i);
            LinearLayout linearLayout=new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (i==0){
                TextView textView=new TextView(this);
                textView.setText("Nama Obat");
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                textView.setTextColor(Color.BLACK);
                linearLayout.addView(textView);


                TextView textView1=new TextView(this);
                textView1.setText(str);
                textView1.setPadding(0,15,0,15);
                textView1.setGravity(Gravity.END);
                textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                textView1.setTextColor(Color.BLACK);
                linearLayout.addView(textView1);
                linear.addView(linearLayout);
            }else {
                TextView textView1=new TextView(this);
                textView1.setText(str);
                textView1.setPadding(0,15,0,15);
                textView1.setGravity(Gravity.END);
                textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                textView1.setTextColor(Color.BLACK);
                linearLayout.addView(textView1);
                linear.addView(linearLayout);
            }
            LinearLayout garis=new LinearLayout(this);
            garis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,2));
            garis.setBackgroundColor(Color.BLACK);
            linear.addView(garis);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setDrawingCacheEnabled(true);
                Bitmap bitmap=content.getDrawingCache();
                try {
                    if (android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Apotekku");
                        if (!file.exists()){
                            file.mkdir();
                        }
                        f=new File(file.getAbsolutePath()+"/"+intent.getExtras().getString("notrans")+".png");
                        FileOutputStream outputStream=new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.PNG,10,outputStream);
                        Toast.makeText(Invoice.this,"gambar berhasil di simpan",Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent();
                intent1.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(f)));
                intent1.setAction(Intent.ACTION_SEND);
                intent1.setType("image/png");
                startActivity(Intent.createChooser(intent1,"share image via......"));
            }
        });

    }
    private boolean requestPermission(){
        String[] permission;
        boolean request=true;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            permission=new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            };
        }else {
            permission=new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            };
        }
        if(permission.length!=0){
            ActivityCompat.requestPermissions(this,permission,102);
            request=true;
        }else{
            request=false;
        }
        return request;
    }
}