package me.tbis.contactlist;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContactProfile extends AppCompatActivity implements MyInterface.OnContactSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (findViewById(R.id.frameP_right) != null) {
            FragMain fragMain = new FragMain();
            FragProfile fragProfile = new FragProfile();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frameP_left, fragMain);
            transaction.replace(R.id.frameP_right, fragProfile);
            transaction.commit();
        } else if (findViewById(R.id.frameP_main) != null) {
            FragProfile fragProfile = new FragProfile();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frameP_main, fragProfile);
            transaction.commit();
        }
    }

    public void onContactSelected(ContactInfo contactInfo){
        FragProfile fragProfile = new FragProfile();
        Bundle bundle = new Bundle();
        bundle.putString("contact", contactInfo.getBase64());
        fragProfile.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameP_right, fragProfile);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
