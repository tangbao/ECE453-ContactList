package me.tbis.contactlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragMain extends Fragment {
    private boolean if_land;
    private ListView lv_contact;
    private MyAdapter adapter;
    private ContactManager contactManager;
    private List<ContactInfo> listContacts;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main, container, false);

        lv_contact = view.findViewById(R.id.lv_contact);
        contactManager = new ContactManager();
        listContacts = contactManager.findAll(getContext());
        adapter = new MyAdapter(getContext(), listContacts,1);

        lv_contact.setAdapter(adapter);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(if_land) {
                    Fragment fragment = new FragProfile();
                    getFragmentManager().beginTransaction().replace(R.id.frame_right, fragment).commit();
                }
                else {
                    Intent intent = new Intent(getActivity(), ContactProfile.class);
                    String details_id = listContacts.get(position).getId()+"";
                    intent.putExtra("id", details_id);
                    getActivity().finish();
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    public void onResume(Bundle bundle){
        super.onResume();
        Intent intent = getActivity().getIntent();
        String new_contact = intent.getStringExtra("new_contact");
        ContactInfo contactInfo = contactManager.getFromBase64(new_contact);
        listContacts.add(contactInfo);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (getActivity().findViewById(R.id.frame_right) != null) {
            if_land = true;
        } else {
            if_land = false;
        }

        Button btn_add = getActivity().findViewById(R.id.btn_add);
        Button btn_del = getActivity().findViewById(R.id.btn_del);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(if_land) {
                    Fragment fragment = new FragDetails();
                    getFragmentManager().beginTransaction().replace(R.id.frame_right, fragment).commit();
                }
                else {
                    Intent intent = new Intent(getActivity(), ContactDetails.class);
                    startActivity(intent);
                }
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<listContacts.size();i++){
                    if(listContacts.get(i).getChk()){
                        contactManager.delete(listContacts.get(i), getContext());
                        listContacts.remove(i);
                        adapter.notifyDataSetChanged();
                        i--;
                    }
                }
            }

        });
    }

}
