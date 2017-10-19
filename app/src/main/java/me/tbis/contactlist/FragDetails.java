package me.tbis.contactlist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.ContentFrameLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tbis.contactlist.MyInterface.*;

import static android.app.Activity.RESULT_OK;

public class FragDetails extends Fragment {
    private boolean if_land;
    private ContactInfo contactInfo;
    private List<ContactInfo> allContact;
    OnContactSelectedListener mCallback;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnContactSelectedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if_land = (getActivity().findViewById(R.id.frame_right) != null);

        Button btn_ap = getActivity().findViewById(R.id.btn_ap);
        ListView lv_relation = getActivity().findViewById(R.id.lv_relation);
        final ContactManager contactManager = new ContactManager();
        allContact =  contactManager.findAll(getContext());
        MyAdapter adapter = new MyAdapter(getContext(), allContact,2);
        lv_relation.setAdapter(adapter);
        lv_relation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(if_land){
                mCallback.onContactSelected(allContact.get(i));
            }else{
                Intent intent = new Intent(getActivity(), ContactProfile.class);
                String contactS = allContact.get(i).getBase64();
                intent.putExtra("contact", contactS);
                startActivity(intent);
            }
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
                    List<Map<String, String>> relationship = new ArrayList<>();

                    for(int i = 0; i<allContact.size();i++){
                        if(allContact.get(i).getChk()){
                            Map<String, String> map = new HashMap<>();
                            map.put("id", allContact.get(i).getId()+"");
                            map.put("name", allContact.get(i).getName());
                            relationship.add(map);
                        }
                    }

                    contactInfo = new ContactInfo(0,
                            etName.getText().toString(), etPhone.getText().toString(),
                            relationship, false);
                    contactManager.add(contactInfo, getContext());

                    if(if_land){
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        allContact = contactManager.listAddUpdate(allContact, contactInfo, getContext());
                        FragMain fragMain = new FragMain();
                        fragmentTransaction.replace(R.id.frame_left,fragMain);
                        ContentFrameLayout cfl =  getActivity().findViewById(R.id.frame_right);
                        cfl.removeAllViews();
                        fragmentTransaction.commit();

                    }else{
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        String contactBase64 = contactInfo.getBase64();
                        intent.putExtra("new_contact", contactBase64);
                        getActivity().setResult(RESULT_OK, intent);
                        getActivity().finish();
                    }
                }
            }
        });
    }

}
