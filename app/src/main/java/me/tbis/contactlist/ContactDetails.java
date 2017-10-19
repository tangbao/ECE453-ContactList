package me.tbis.contactlist;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactDetails extends AppCompatActivity implements MyInterface.OnContactSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
    }

    public void onContactSelected(ContactInfo contactInfo){
        FragProfile fragProfile = new FragProfile();
        Bundle bundle = new Bundle();
        bundle.putString("contact", contactInfo.getBase64());
        fragProfile.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_right, fragProfile);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }
}
