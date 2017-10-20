package me.tbis.contactlist;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactDetails extends AppCompatActivity implements MyInterface.OnContactSelectedListener,
    MyInterface.OnSaveStatusListener{
    private Bundle contactRecoveringBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        //if it's land, add two fragment
        if (findViewById(R.id.frameD_right) != null) {
            FragMain fragMain = new FragMain();
            FragDetails fragDetails = new FragDetails();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frameD_left, fragMain);
            transaction.replace(R.id.frameD_right, fragDetails);
            transaction.commit();
        } else if (findViewById(R.id.frameD_main) != null) { //if it's portrait
            FragDetails fragDetails = new FragDetails();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frameD_main, fragDetails);
            transaction.commit();
        }
    }

    //pass a contact to a profile fragment
    public void onContactSelected(ContactInfo contactInfo){
        FragProfile fragProfile = new FragProfile();
        Bundle bundle = new Bundle();
        bundle.putString("contact", contactInfo.getBase64());
        fragProfile.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameD_right, fragProfile);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //save status bundle in this activity
    public void onSaveStatus(ContactInfo contactInfo){
        contactRecoveringBundle = new Bundle();
        contactRecoveringBundle.putString("contactRecovering", contactInfo.getBase64());
    }

    //save the status bundle saved above
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBundle("contactRecoveringBundle",contactRecoveringBundle);
    }

    //recover the bundle, pass it to a new details fragment
    @Override
    public void onRestoreInstanceState(Bundle restoreBundle){
        super.onRestoreInstanceState(restoreBundle);

        Bundle bundle = restoreBundle.getBundle("contactRecoveringBundle");
        FragDetails fragDetails = new FragDetails();
        fragDetails.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(this.findViewById(R.id.frameD_right)!=null){
            transaction.replace(R.id.frameD_right, fragDetails);
        }else if(this.findViewById(R.id.frameD_main)!=null){
            transaction.replace(R.id.frameD_main,fragDetails);
        }

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }
}
