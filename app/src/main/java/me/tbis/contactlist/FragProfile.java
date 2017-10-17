package me.tbis.contactlist;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;


public class FragProfile extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        String show_id = intent.getStringExtra("id");

        TextView showName = getActivity().findViewById(R.id.showName);
        TextView showPhone = getActivity().findViewById(R.id.showPhone);
        final ListView lv_allrelation = getActivity().findViewById(R.id.lv_allrelation);
        ListAdapter adapter;

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        final String contactString = sharedPref.getString(show_id,"");
        ContactManager contactManager = new ContactManager();
        ContactInfo contactInfo = contactManager.getFromBase64(contactString);
        final List<ContactInfo> relationship = contactInfo.getRelationship();
        showName.setText(contactInfo.getName());
        showPhone.setText(contactInfo.getPhone());
        adapter = new MyAdapter(getContext(), relationship, 3);
        lv_allrelation.setAdapter(adapter);
        Toast.makeText(getContext(),relationship.toString(),Toast.LENGTH_LONG).show();
        lv_allrelation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ContactProfile.class);
                String details_id = relationship.get(i).getId()+"";
                intent.putExtra("id", details_id);
                startActivity(intent);
            }
        });

    }
}
