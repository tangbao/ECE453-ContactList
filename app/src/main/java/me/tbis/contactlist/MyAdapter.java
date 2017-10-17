package me.tbis.contactlist;


/*
 *
 * Created by tzzma on 2017/10/15.
 *
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MyAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String,Object>> mList;
    private LayoutInflater mInflater;
    public Map<Integer,Boolean> mCBFlag = null;
    //public Map<String, Integer> mCName = null;
    public MyAdapter(Context c, List<Map<String,Object>> list){
        this.mContext = c;
        this.mList = list;
        mInflater = LayoutInflater.from(mContext);
        mCBFlag = new HashMap<>();
        init();
    }

    public void init(){
        for (int i = 0; i < mList.size(); i++) {
            mCBFlag.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Map<String,Object> getItem(int position) {
        return mList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.contact_listview, null);
            holder.mCheckBox = convertView.findViewById(R.id.checkBox);
            holder.mTextView = convertView.findViewById(R.id.textName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //状态保存
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mList.get(position).put("checked", true);
                    mCBFlag.put(position, true);
                }else{
                    mList.get(position).put("checked", false);
                    mCBFlag.put(position, false);
                }
            }
        });

        //holder.mCheckBox.setChecked(mCBFlag.get(position));
        boolean status = mList.get(position).getChk();
        holder.mCheckBox.setChecked();
        holder.mTextView.setText(mList.get(position).get("name").toString());

        return convertView;
    }
    private final class ViewHolder{
        private CheckBox mCheckBox;
        private TextView mTextView;
    }
}