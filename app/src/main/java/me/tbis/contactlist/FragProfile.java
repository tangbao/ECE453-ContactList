package me.tbis.contactlist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class FragProfile extends Fragment {
    private boolean if_land;
    MyInterface.OnContactSelectedListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (MyInterface.OnContactSelectedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if_land = (getActivity().findViewById(R.id.frame_right) != null);

        String contactString ;
        Toast.makeText(getContext(),"fuck",Toast.LENGTH_LONG).show();

         if(if_land){
             contactString = getArguments().getString("contact");
         }else{
             Intent intent = getActivity().getIntent();
             contactString = intent.getStringExtra("contact");
         }

        TextView showName = getActivity().findViewById(R.id.showName);
        TextView showPhone = getActivity().findViewById(R.id.showPhone);
        final ListView lv_allrelation = getActivity().findViewById(R.id.lv_allrelation);
        ListAdapter adapter;

        ContactManager contactManager = new ContactManager();
        final ContactInfo contactInfo = contactManager.getFromBase64(contactString);
        final List<ContactInfo> relationship = contactInfo.getRelationship();
        showName.setText(contactInfo.getName());
        showPhone.setText(contactInfo.getPhone());
        adapter = new MyAdapter(getContext(), relationship, 3);
        lv_allrelation.setAdapter(adapter);
        Toast.makeText(getContext(),relationship.toString(),Toast.LENGTH_LONG).show();
        lv_allrelation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(if_land){
                    mCallback.onContactSelected(relationship.get(i));
                }else{
                    Intent intent = new Intent(getActivity(), ContactProfile.class);
                    String contactS = relationship.get(i).getBase64();
                    intent.putExtra("contact", contactS);
                    startActivity(intent);
                }
            }
        });

    }
}
