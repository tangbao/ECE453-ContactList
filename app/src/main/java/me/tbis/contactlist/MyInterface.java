package me.tbis.contactlist;

/**
 *
 * Created by tzzma on 2017/10/18.
 *
 */


public class MyInterface {

    //when select a contact in the listview
    interface OnContactSelectedListener{
        void onContactSelected(ContactInfo contactInfo);
    }

    interface OnSaveStatusListener{
        void onSaveStatus();
    }

}
