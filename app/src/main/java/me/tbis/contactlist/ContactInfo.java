package me.tbis.contactlist;

import java.io.Serializable;
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

    ContactInfo(int id, String name, String phone, List<Map<String, String>> relationship, boolean chk){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.relationship = relationship;
        this.chk = false;
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

    void addRelationship(Map<String, String> aRs){
//        for(int i = 0; i <rList.size(); i++)
//        {
//            Map<String, String> map = rList.get(i);
//            relationship.add(map);
//        }
        relationship.add(aRs);
    }

    void delRelationship(Map<String, String> dRs){
        relationship.remove(dRs);
    }

    void setChk(boolean b){
        this.chk = b;
    }

}
