package me.tbis.contactlist;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tbis.contactlist.MyAdapter;


public class FragDetails extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btn_ap = getActivity().findViewById(R.id.btn_ap);
        ListView lv_relation = getActivity().findViewById(R.id.lv_relation);
        final ContactManager contactManager = new ContactManager();
        final List<ContactInfo> allContact =  contactManager.findAll(getContext());
        MyAdapter adapter = new MyAdapter(getContext(), allContact,2);
        lv_relation.setAdapter(adapter);
        lv_relation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ContactProfile.class);
                String details_id = allContact.get(i).getId()+"";
                intent.putExtra("id", details_id);
                startActivity(intent);
            }
        });

        btn_ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView etName = getActivity().findViewById(R.id.etName);
                TextView etPhone = getActivity().findViewById(R.id.etPhone);

                if(etName.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"You must fill in all the info!",Toast.LENGTH_LONG).show();
                }else{
                    List<ContactInfo> relationship = new ArrayList<ContactInfo>();

                    for(int i = 0; i<allContact.size();i++){
                        if(allContact.get(i).getChk()){
                            relationship.add(allContact.get(i));
                        }
                    }

                    ContactInfo contactInfo = new ContactInfo(0,
                            etName.getText().toString(), etPhone.getText().toString(),
                            relationship, false);
                    contactManager.add(contactInfo, getContext());

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    String contactBase64 = contactInfo.getBase64();
                    intent.putExtra("new_contact", contactBase64);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });
    }



}
