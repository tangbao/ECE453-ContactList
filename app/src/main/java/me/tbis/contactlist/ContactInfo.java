package me.tbis.contactlist;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tzzma on 2017/10/11.
 *
 */

public class ContactInfo implements Serializable {
    private int id;
    private String name;
    private String phone;
    private List<Map<String, String>> relationship;
    private boolean chk;
//    private String pic;

    ContactInfo(int id, String name, String phone, List<Map<String, String>> relationship, boolean chk){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.relationship = relationship;
        this.chk = chk;
        //this.pic = "moren";
    }

    String getBase64(){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            String base64 =Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            baos.close();
            oos.close();
            return base64;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    int getId(){
        return id;
    }

    String getName(){
        return name;
    }

    String getPhone(){
        return phone;
    }

    List<Map<String, String>> getRelationship(){
        return relationship;
    }

    boolean getChk(){
        return chk;
    }

    void setID(int id){
        this.id = id;
    }

    void addRelationship(ContactInfo newR){
        Map<String, String> map = new HashMap<>();
        map.put("id", newR.getId()+"");
        map.put("name", newR.getName());
        this.relationship.add(map);
    }

    void delRelationship(ContactInfo delR){
        Map<String, String> map = new HashMap<>();
        map.put("id", delR.getId()+"");
        map.put("name", delR.getName());
        this.relationship.remove(map);
    }

    void setChk(boolean b){
        this.chk = b;
    }

}
