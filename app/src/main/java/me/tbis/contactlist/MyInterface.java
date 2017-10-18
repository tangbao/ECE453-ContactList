package me.tbis.contactlist;

/**
 * Created by tzzma on 2017/10/18.
 */

public class MyInterface {

    interface OnContactSelectedListener{
        void onContactSelected(ContactInfo contactInfo);
    }

    interface OnAddReturnListener{
        void onAddReturn(ContactInfo contactInfo);
    }


    interface OnAddClickPListener{
        void onAddClickP();
    }
}
