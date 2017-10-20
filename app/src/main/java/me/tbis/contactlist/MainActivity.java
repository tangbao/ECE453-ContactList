package me.tbis.contactlist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MyInterface.OnContactSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (findViewById(R.id.frame_left) != null) {
            FragMain fragMain = new FragMain();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_left, fragMain);
            transaction.commit();
        } else if(findViewById(R.id.frame_main) != null){
            FragMain fragMain = new FragMain();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_main, fragMain);
            transaction.commit();
        }
    }

    public void onContactSelected(ContactInfo contactInfo){
            FragProfile fragProfile = new FragProfile();
            Bundle bundle = new Bundle();
            bundle.putString("contact", contactInfo.getBase64());
            fragProfile.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // 无论 fragment_container 视图里是什么，用该 Fragment 替换它。并将
            // 该事务添加至回栈，以便用户可以往回导航（译注：回栈，即 Back Stack。
            // 在有多个 Activity 的 APP 中，将这些 Activity 按创建次序组织起来的
            // 栈，称为回栈）
            transaction.replace(R.id.frame_right, fragProfile);
            transaction.addToBackStack(null);
            transaction.commit();
    }

}
