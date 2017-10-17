package me.tbis.contactlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
            try{
                String contactString = i.getValue().toString();
                byte[] base64Product = Base64.decode(contactString, Base64.DEFAULT);
                ByteArrayInputStream bais = new ByteArrayInputStream(base64Product);
                ObjectInputStream ois = new ObjectInputStream(bais);
                ContactInfo contactInfo = (ContactInfo) ois.readObject();
                if (idmax < contactInfo.getId()){
                    idmax = contactInfo.getId();
                }
                bais.close();
                ois.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        idmax ++;

    }

    int add(ContactInfo contactInfo, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        try{
            getMaxID(context);
            contactInfo.setID(idmax);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(contactInfo);
            String base64Contact = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            editor.putString(Integer.toString(contactInfo.getId()), base64Contact);
            editor.apply();
            baos.close();
            oos.close();
            return idmax;
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return 0;
    }

    void delete(String id, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove(id);
        editor.apply();
    }

    List <Map <String, Object>> findAll(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Map<String, ?> allContacts;
        List <Map <String, Object>> results = new ArrayList <>();
        allContacts = sharedPref.getAll();

        for(Map.Entry<String, ?>  i : allContacts.entrySet()){
            Map<String, Object> map = new HashMap<>();

            try{
                String contactString = i.getValue().toString();
                byte[] base64Product = Base64.decode(contactString, Base64.DEFAULT);
                ByteArrayInputStream bais = new ByteArrayInputStream(base64Product);
                ObjectInputStream ois = new ObjectInputStream(bais);
                ContactInfo contactInfo = (ContactInfo) ois.readObject();
                map.put("id", Integer.toString(contactInfo.getId()));
                map.put("name", contactInfo.getName());
                map.put("checked", false);
                results.add(map);
                bais.close();
                ois.close();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        return results;
    }


}
