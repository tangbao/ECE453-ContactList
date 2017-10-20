
package me.tbis.contactlist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
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
    private Button btn_ap;
    private MyAdapter adapter;
    private ListView lv_relation;
    private TextView etName, etPhone;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

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
        View view = inflater.inflate(R.layout.frag_details, container, false);
        btn_ap = view.findViewById(R.id.btn_ap);
        lv_relation = view.findViewById(R.id.lv_relation);
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        allContact =  contactManager.findAll(getContext());
        adapter = new MyAdapter(getContext(), allContact,2);
        lv_relation.setAdapter(adapter);

        sharedPref = getActivity().getSharedPreferences(
                getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String conString = sharedPref.getString("savedStatus",null);

        if(conString != null){
            recoverStatus(conString);
            Log.d("conString","no null");
        }

        if_land = (getActivity().findViewById(R.id.frame_right) != null);

        final boolean if_m = getActivity().findViewById(R.id.frame_right) != null;
        final boolean if_d = getActivity().findViewById(R.id.frameD_right) != null;
        final boolean if_p = getActivity().findViewById(R.id.frameP_right) != null;


//        if_land = (getActivity().findViewById(R.id.frame_right) != null) ||
//                (getActivity().findViewById(R.id.frameD_right) != null) ||
//                (getActivity().findViewById(R.id.frameP_right) != null);

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

                editor.putString("savedStatus", null);
                editor.apply();

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

                        if(if_m){
                            fragmentTransaction.replace(R.id.frame_left,fragMain);
                            ContentFrameLayout cfl =  getActivity().findViewById(R.id.frame_right);
                            cfl.removeAllViews();
                        }else if (if_d){
                            fragmentTransaction.replace(R.id.frameD_left,fragMain);
                            ContentFrameLayout cfl =  getActivity().findViewById(R.id.frameD_right);
                            cfl.removeAllViews();
                        }else if(if_p){
                            fragmentTransaction.replace(R.id.frameP_left,fragMain);
                            ContentFrameLayout cfl =  getActivity().findViewById(R.id.frameP_right);
                            cfl.removeAllViews();
                        }


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

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        List<Map<String, String>> relationship = new ArrayList<>();

        for(int i = 0; i<allContact.size();i++){
            if(allContact.get(i).getChk()){
                Map<String, String> map = new HashMap<>();
                map.put("id", allContact.get(i).getId()+"");
                map.put("name", allContact.get(i).getName());
                relationship.add(map);
            }
        }

        ContactInfo contactSaving = new ContactInfo(0,
                etName.getText().toString(), etPhone.getText().toString(),
                relationship, false);

        if(!contactSaving.getBase64().equals(sharedPref.getString("savedStatus",null))){
            Toast.makeText(getContext(), "The draft has been saved.", Toast.LENGTH_LONG).show();
            editor.putString("savedStatus", contactSaving.getBase64());
            editor.apply();
        }


    }

    private void delStatus(){
        sharedPref = getActivity().getSharedPreferences(
                getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("savedStatus",null);
        editor.apply();
    }

    private void recoverStatus(String conString){
        ContactInfo contactRecovering = contactManager.getFromBase64(conString);
        etName.setText(contactRecovering.getName());
        etPhone.setText(contactRecovering.getPhone());
        List<Map<String, String>> relationship = contactRecovering.getRelationship();
        for(int i = 0; i < relationship.size();i++) {
            for (int j = 0; j < allContact.size(); j++) {
                if (allContact.get(j).getId() == Integer.valueOf(relationship.get(i).get("id"))) {
                    allContact.get(j).setChk(true);//find the contact in the list, update it
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }

    }


}
