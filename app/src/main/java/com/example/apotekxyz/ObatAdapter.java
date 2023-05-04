package com.example.apotekxyz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ObatAdapter extends ArrayAdapter<ObatModel> implements Filterable {
    private Context context;
    private int resource;
    private BottomSheetDialog bsd;
    private List<ObatModel> obatModelList,obatList;
    private ArrayList<String> listid,listnm,listhr;
    filterFilter ff;

    public ObatAdapter(@NonNull Context context, int resource, BottomSheetDialog bsd, List<ObatModel> obatModelList, ArrayList<String> listid, ArrayList<String> listnm, ArrayList<String> listhr) {
        super(context, resource,obatModelList);
        this.context = context;
        this.resource = resource;
        this.bsd = bsd;
        this.obatModelList = obatModelList;
        this.obatList = obatModelList;
        this.listid = listid;
        this.listnm = listnm;
        this.listhr = listhr;
    }

    class filterFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                constraint=constraint.toString().toUpperCase();
                ArrayList<ObatModel> arrayList=new ArrayList<>();
                for (int i = 0; i < obatList.size(); i++) {
                    if (obatList.get(i).getNama().toUpperCase().contains(constraint)){
                        arrayList.add(new ObatModel(obatList.get(i).getId(),obatList.get(i).getNama(),obatList.get(i).getHarga()));
                    }
                }
                filterResults.values=arrayList;
        }else{
                filterResults.values=obatModelList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            obatList=(ArrayList<ObatModel>) results.values;
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

    public ArrayList<String> getListid() {
        return listid;
    }

    public ArrayList<String> getListnm() {
        return listnm;
    }

    public ArrayList<String> getListhr() {
        return listhr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(resource,null,false);
        }
        TextView harga=convertView.findViewById(R.id.harga);
        CheckBox cb=convertView.findViewById(R.id.cb);
        ObatModel obatModel=obatList.get(position);
        cb.setText(obatModel.getNama());
        harga.setText(obatModel.getHarga());
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    listnm.add((cb.getText().toString()));
                    listid.add(obatModel.getId());
                    listhr.add(obatModel.getHarga());
                }else {
                    listnm.remove((cb.getText().toString()));
                    listid.remove(obatModel.getId());
                    listhr.remove(obatModel.getHarga());
                }
            }
        });
        if (listnm.contains(obatModel.getNama())){
            cb.setChecked(true);
        }else {
            cb.setChecked(false);
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public int getCount() {
        return  obatList.size();
    }
}
