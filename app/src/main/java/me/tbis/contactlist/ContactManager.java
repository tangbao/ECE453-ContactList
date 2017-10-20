package me.tbis.contactlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by tzzma on 2017/10/11.
 *
 * the functions to manage contacts
 *
 */

public class ContactManager {
    private int idmax = 0;

    ContactManager(){
    }

    //to get MAX id in all the contacts
    private void getMaxID(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Map<String, ?> allContacts = sharedPref.getAll();//get all the contacts from sharedPref
        for(Map.Entry<String, ?>  i : allContacts.entrySet()) {
            String contactString = i.getValue().toString();//get a contact base64 string
            ContactInfo contactInfo = getFromBase64(contactString);//recover from it
            if (idmax < contactInfo.getId()){
                idmax = contactInfo.getId();
            }
        }
        idmax ++;
    }

    //only add a contact into sharedPref
    void add(ContactInfo contactInfo, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        getMaxID(context);
        contactInfo.setID(idmax);
        String base64Contact = contactInfo.getBase64();//encode it by base64, and convert it into a string
        editor.putString(Integer.toString(contactInfo.getId()), base64Contact);//store it
        editor.apply();
        Toast.makeText(context,"Add successfully",Toast.LENGTH_LONG).show();
    }

    //only delete a contact from sharedPref
    void delete(ContactInfo contactInfo, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove(Integer.toString(contactInfo.getId()));
        editor.apply();
    }

    //get all contact strings from sharedPref, and convert all the strings to contact objects
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

    //update a list which is adding a contact
    //update all the contacts in the list, especially who build relationships with the new contact.
    //store the updated contacts into sharedPref
    List<ContactInfo> listAddUpdate(List<ContactInfo> list, ContactInfo contactInfo, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        ContactManager contactManager = new ContactManager();
        List<Map<String, String>> contactR = contactInfo.getRelationship();

        for(int i = 0; i < contactR.size();i++){
            //update existing contacts
            String base64Contact = sharedPref.getString(contactR.get(i).get("id"), "");//get it's string from sharedPref
            ContactInfo contactUpdating = contactManager.getFromBase64(base64Contact);//recover it from the string
            contactUpdating.addRelationship(contactInfo);// add relationship to it
            editor.putString(contactR.get(i).get("id") ,contactUpdating.getBase64());//update
            for(int j = 0; j<list.size();j++) {
                if(list.get(j).getId() == Integer.valueOf(contactR.get(i).get("id")) ){
                    list.get(j).addRelationship(contactInfo);//find the contact in the list, update it
                    break;
                }
            }
        }
        list.add(contactInfo);
        editor.apply();
        return list;
    }

    //update a list which is deleting a contact
    //update all the contacts in the list, especially who remove relationships with the certain contact.
    //store the updated contacts into sharedPref
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

    //convert a contact base64 string to a contact object
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
