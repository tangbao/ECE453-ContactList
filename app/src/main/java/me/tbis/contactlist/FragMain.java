package me.tbis.contactlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.util.List;

import me.tbis.contactlist.MyInterface.OnContactSelectedListener;

import static android.app.Activity.RESULT_OK;


public class FragMain extends Fragment{
    private boolean if_land;
    private ListView lv_contact;
    private MyAdapter adapter;
    private ContactManager contactManager;
    private List<ContactInfo> listContacts;
    OnContactSelectedListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnContactSelectedListener) context;
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

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if_land = (getActivity().findViewById(R.id.frame_right) != null);

        Button btn_add = getActivity().findViewById(R.id.btn_add);
        Button btn_del = getActivity().findViewById(R.id.btn_del);

        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(if_land) {
                    mCallback.onContactSelected(listContacts.get(position));
                }
                else {
                    Intent intent = new Intent(getActivity(), ContactProfile.class);
                    ContactInfo contactInfo = listContacts.get(position);
                    intent.putExtra("contact", contactInfo.getBase64());
                    startActivity(intent);
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(if_land) {
                    FragDetails fragment = new FragDetails();
                    getFragmentManager().beginTransaction().replace(R.id.frame_right, fragment).commit();
                        if(getArguments() != null)
                        {
                            Toast.makeText(getContext(),"nmb",Toast.LENGTH_LONG).show();
                            String contactString = getArguments().getString("contact");
                            ContactManager contactManager = new ContactManager();
                            ContactInfo contactInfo = contactManager.getFromBase64(contactString);
                            listContacts.add(contactInfo);
                            adapter.notifyDataSetChanged();
                        }
                    }
                else {
                    Intent intent = new Intent(getActivity(), ContactDetails.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<listContacts.size();i++){
                    if(listContacts.get(i).getChk()){
                        listContacts = contactManager.listDelUpdate(listContacts, listContacts.get(i), getContext());
                        contactManager.delete(listContacts.get(i), getContext());
                        listContacts.remove(i);
                        adapter.notifyDataSetChanged();
                        i--;
                    }
                }
                Toast.makeText(getContext(), "Delete Successfully", Toast.LENGTH_LONG).show();
                if(if_land){
                    ContentFrameLayout cfl =  getActivity().findViewById(R.id.frame_right);
                    cfl.removeAllViews();
                }
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (resultCode == RESULT_OK){
            String new_contact = intent.getStringExtra("new_contact");
            ContactManager contactManager = new ContactManager();
            ContactInfo contactInfo = contactManager.getFromBase64(new_contact);
            listContacts = contactManager.listAddUpdate(listContacts, contactInfo, getContext());
            adapter.notifyDataSetChanged();
        }
    }

}
