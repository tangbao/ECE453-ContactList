package me.tbis.contactlist;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.tbis.contactlist.MyInterface.OnContactSelectedListener;

public class FragProfile extends Fragment {
    private boolean if_land;
    private TextView showName, showPhone;
    private ListView lv_allrelation;
    private MyAdapter adapter;
    private ContactInfo contactInfo;
    private List<Map<String, String>> relationshipL;
    private List<ContactInfo> relationship;
    private SharedPreferences sharedPref;
    private ContactManager contactManager = new ContactManager();

    OnContactSelectedListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnContactSelectedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_profile, container, false);
        showName = view.findViewById(R.id.showName);
        showPhone = view.findViewById(R.id.showPhone);
        lv_allrelation = view.findViewById(R.id.lv_allrelation);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if_land = (getActivity().findViewById(R.id.frame_right) != null);


        final boolean if_m = getActivity().findViewById(R.id.frame_right) != null;
        final boolean if_d = getActivity().findViewById(R.id.frameD_right) != null;
        final boolean if_p = getActivity().findViewById(R.id.frameP_right) != null;



        String contactString ;

         if(if_land){
             contactString = getArguments().getString("contact");
         }else {
             Intent intent = getActivity().getIntent();
             contactString = intent.getStringExtra("contact");
         }

        if(contactString != null){
            contactInfo = contactManager.getFromBase64(contactString);
            relationshipL = contactInfo.getRelationship();
            relationship = new ArrayList<>();
            sharedPref = getActivity().getSharedPreferences(
                    getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            for(int i = 0; i<relationshipL.size();i++){
                String contactS = sharedPref.getString(relationshipL.get(i).get("id"),"");
                ContactInfo contactC = contactManager.getFromBase64(contactS);
                relationship.add(contactC);
            }

            showName.setText(contactInfo.getName());
            showPhone.setText(contactInfo.getPhone());
            adapter = new MyAdapter(getContext(), relationship, 3);
            lv_allrelation.setAdapter(adapter);
            lv_allrelation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(if_land){
                        mCallback.onContactSelected(relationship.get(i));
                    }else{
                        Intent intent = new Intent(getActivity(), ContactProfile.class);
                        String contactS = sharedPref.getString(relationshipL.get(i).get("id"),"");
                        intent.putExtra("contact", contactS);
                        startActivity(intent);
                    }
                }
            });
        }
        else{
            if(if_m){
                ContentFrameLayout cfl =  getActivity().findViewById(R.id.frame_right);
                cfl.removeAllViews();
            }else if (if_d){
                ContentFrameLayout cfl =  getActivity().findViewById(R.id.frameD_right);
                cfl.removeAllViews();
            }else if(if_p){
                ContentFrameLayout cfl =  getActivity().findViewById(R.id.frameP_right);
                cfl.removeAllViews();
            }
        }

    }
}
