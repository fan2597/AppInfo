package com.x.appinfo;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fanhq on 2015/9/7.
 */
public class RomAdapter extends BaseAdapter {
    LayoutInflater mInflater=null;
    List<HashMap<String, String>> data=new ArrayList<HashMap<String, String>>();

    public RomAdapter(Context context, List<HashMap<String, String>> data){
        mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
           convertView=mInflater.inflate(R.layout.item_rom,null);
        }

        TextView app=(TextView)convertView.findViewById(R.id.rom_app_value);
        TextView version=(TextView)convertView.findViewById(R.id.rom_version_value);
        TextView fileSize=(TextView)convertView.findViewById(R.id.rom_filesizes_value);
        TextView pkg=(TextView)convertView.findViewById(R.id.rom_pkg_value);
        TextView process=(TextView)convertView.findViewById(R.id.rom_process_value);
        TextView activity=(TextView)convertView.findViewById(R.id.rom_activity_value);
        TextView path=(TextView)convertView.findViewById(R.id.rom_path_value);

        TextView appkey=(TextView)convertView.findViewById(R.id.rom_app);
        TextPaint tp = appkey.getPaint();
        tp.setFakeBoldText(true);

        TextPaint ap=app.getPaint();
        ap.setFakeBoldText(true);


        app.setText(data.get(position).get("app"));
        version.setText(data.get(position).get("version"));
        fileSize.setText(data.get(position).get("fileSize"));
        pkg.setText(data.get(position).get("pkg"));
        process.setText(data.get(position).get("process"));
        activity.setText(data.get(position).get("activity"));
        path.setText(data.get(position).get("path"));

        return convertView;
    }
}
