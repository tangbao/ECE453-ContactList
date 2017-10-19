package me.tbis.contactlist;


/*
 *
 * Created by tzzma on 2017/10/15.
 *
 * MyAdapter(Context c, List<ContactInfo> list, int mode)
 *
 * list: contents all the contacts
 *
 * mode 1: for MainActivity/FragMain, normal style, a textView with a checkBox
 *
 * mode 2: for ContactDetails/FragDetails, the selected Item will jump to the top
 *
 * mode 3: for ContactProfile/FragProfile, the checkboxes are invisible
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

import java.util.List;

class MyAdapter extends BaseAdapter {
    private Context mContext;
    private List<ContactInfo> mList;
    private LayoutInflater mInflater;
    private int mode;
    public MyAdapter(Context c, List<ContactInfo> list, int mode){
        this.mContext = c;
        this.mList = list;
        mInflater = LayoutInflater.from(mContext);
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public ContactInfo getItem(int position) {
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

            if(mode == 3){
                holder.mCheckBox.setVisibility(View.INVISIBLE);
            }

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mList.get(position).setChk(isChecked);
                if(mode == 2){
                    if(isChecked){
                        for(int i = 0;i<position;i++){
                            if(!mList.get(i).getChk()){
                                ContactInfo temp = mList.get(i);
                                mList.set(i, mList.get(position));
                                mList.set(position, temp);
                                notifyDataSetChanged();
                                break;
                            }
                        }
                    }else{
                        int p_now = position;
                        for(int i = position;i<mList.size();i++){
                            if(mList.get(i).getChk()){
                                ContactInfo temp = mList.get(i);
                                mList.set(i, mList.get(p_now));
                                mList.set(p_now, temp);
                                p_now = i;
                                notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });

        holder.mCheckBox.setChecked(mList.get(position).getChk());
        holder.mTextView.setText(mList.get(position).getName());

        return convertView;
    }
    private final class ViewHolder{
        private CheckBox mCheckBox;
        private TextView mTextView;
    }
}