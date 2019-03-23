package com.zyx.library.librarycollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.zyx.library.binderlibrary.CacheCenter;
import com.zyx.library.binderlibrary.ProcessManager;
import com.zyx.library.librarycollection.bean.Person;

public class MainActivity extends AppCompatActivity {

    IUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessManager.getInstance().connect(this, "com.zyx.library.simple");

        ProcessManager.getInstance().register(IUserManager.class);
        CacheCenter.getInstance().registerInstance(IUserManager.class, UserManager.getInstance());
        UserManager.getInstance().setName("zyxhahahahh");
    }

    public void getUser(View view) {
        userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
        Person person = userManager.getPerson();
        Toast.makeText(this, person.getName() + "说：" + person.getSay(), Toast.LENGTH_SHORT).show();
    }

    public void getLocalUser(View view) {
        userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
        Toast.makeText(this, userManager.getName(), Toast.LENGTH_SHORT).show();
    }
}
