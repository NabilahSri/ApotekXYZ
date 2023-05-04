package com.example.apotekxyz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class JenisAdapter extends ArrayAdapter<JenisModel> implements Filterable {
    private Context context;
    private int resource;
    private BottomSheetDialog bsd;
    private List<JenisModel> jenisModelList,jenisList;
    private String nmjenis;
    filterFilter ff;
    EditText jo;

    public JenisAdapter(@NonNull Context context, int resource, BottomSheetDialog bsd, List<JenisModel> jenisModelList, String nmjenis,EditText jo) {
        super(context, resource,jenisModelList);
        this.context = context;
        this.resource = resource;
        this.bsd = bsd;
        this.jenisModelList = jenisModelList;
        this.jenisList = jenisModelList;
        this.nmjenis=nmjenis;
        this.jo=jo;
    }

    class filterFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                constraint=constraint.toString().toUpperCase();
                ArrayList<JenisModel> arrayList=new ArrayList<>();
                for (int i = 0; i < jenisList.size(); i++) {
                    if (jenisList.get(i).getNama().toUpperCase().contains(constraint)){
                        arrayList.add(new JenisModel(jenisList.get(i).getNama()));
                    }
                }
                filterResults.values=arrayList;
            }else{
                filterResults.values=jenisModelList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            jenisList=(ArrayList<JenisModel>) results.values;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (ff==null){
            ff=new filterFilter();
        }
        return ff;
    }

    public String getNmjenis() {
        return nmjenis;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(resource,null,false);
        }
        TextView jenis=convertView.findViewById(R.id.jenis);
        JenisModel jenisModel=jenisList.get(position);
        jenis.setText(jenisModel.getNama());
        jenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nmjenis=jenisModel.getNama();
                jo.setText(jenisModel.getNama());
                bsd.dismiss();
            }
        });
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public int getCount() {
        return  jenisList.size();
    }
}
