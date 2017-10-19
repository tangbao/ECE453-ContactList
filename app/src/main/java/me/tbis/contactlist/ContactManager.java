package me.tbis.contactlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by tzzma on 2017/10/11.
 *
 */

public class ContactManager {
    private int idmax = 0;

    ContactManager(){
    }

    private void getMaxID(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Map<String, ?> allContacts = sharedPref.getAll();
        for(Map.Entry<String, ?>  i : allContacts.entrySet()) {
            String contactString = i.getValue().toString();
            ContactInfo contactInfo = getFromBase64(contactString);
            if (idmax < contactInfo.getId()){
                idmax = contactInfo.getId();
            }
        }
        idmax ++;
    }

    void add(ContactInfo contactInfo, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        getMaxID(context);
        contactInfo.setID(idmax);
        //setRelationship(contactInfo, context);
        String base64Contact = contactInfo.getBase64();
        editor.putString(Integer.toString(contactInfo.getId()), base64Contact);
        editor.apply();
        Toast.makeText(context,"Add successfully",Toast.LENGTH_LONG).show();
    }

    void delete(ContactInfo contactInfo, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //removeRelationship(contactInfo, context);
        editor.remove(Integer.toString(contactInfo.getId()));
        editor.apply();
    }

    List <ContactInfo> findAll(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Map<String, ?> allContacts;
        List <ContactInfo> results = new ArrayList <>();
        allContacts = sharedPref.getAll();

        for(Map.Entry<String, ?>  i : allContacts.entrySet()){
            String contactString = i.getValue().toString();
            ContactInfo contactInfo = getFromBase64(contactString);
            results.add(contactInfo);
        }
        return results;
    }

    List<ContactInfo> listAddUpdate(List<ContactInfo> list, ContactInfo contactInfo, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        ContactManager contactManager = new ContactManager();
        List<Map<String, String>> contactR = contactInfo.getRelationship();

        for(int i = 0; i < contactR.size();i++){
            String base64Contact = sharedPref.getString(contactR.get(i).get("id"), "");
            ContactInfo contactUpdating = contactManager.getFromBase64(base64Contact);
            contactUpdating.addRelationship(contactInfo);
            editor.putString(contactR.get(i).get("id") ,contactUpdating.getBase64());
            for(int j = 0; j<list.size();j++) {
                if(list.get(j).getId() == Integer.valueOf(contactR.get(i).get("id")) ){
                    list.get(j).addRelationship(contactInfo);
                    break;
                }
            }
        }
        list.add(contactInfo);
        editor.apply();
        return list;
    }

    List<ContactInfo> listDelUpdate(List<ContactInfo> list, ContactInfo contactInfo, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        ContactManager contactManager = new ContactManager();
        List<Map<String, String>> contactR = contactInfo.getRelationship();

        for(int i = 0; i < contactR.size();i++){
            String base64Contact = sharedPref.getString(contactR.get(i).get("id"), "");
            ContactInfo contactUpdating = contactManager.getFromBase64(base64Contact);
            contactUpdating.delRelationship(contactInfo);
            editor.putString(contactR.get(i).get("id") ,contactUpdating.getBase64());
            for(int j = 0; j<list.size();j++) {
                if(list.get(j).getId() == Integer.valueOf(contactR.get(i).get("id")) ){
                    list.get(j).delRelationship(contactInfo);
                    break;
                }
            }
        }
        editor.apply();
        return list;
    }

    ContactInfo getFromBase64(String base64){
        try{
            byte[] base64Contact = Base64.decode(base64, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Contact);
            ObjectInputStream ois = new ObjectInputStream(bais);
            ContactInfo contactInfo = (ContactInfo) ois.readObject();
            bais.close();
            ois.close();
            return contactInfo;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
