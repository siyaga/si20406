package com.example.simplecrud;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DataDiriList extends ArrayAdapter<DataDiri> {

    private Activity context;
    private List<DataDiri> list;
    public DataDiriList(Activity context, List<DataDiri> list){
        super(context,R.layout.list_layout,list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);
        TextView tv1 = (TextView) listViewItem.findViewById(R.id.n);
        TextView tv2 = (TextView) listViewItem.findViewById(R.id.no);

        DataDiri dataDiri = list.get(position);
        tv1.setText(dataDiri.getName());
        tv2.setText(dataDiri.getNomor());
        return listViewItem;
    }
}
