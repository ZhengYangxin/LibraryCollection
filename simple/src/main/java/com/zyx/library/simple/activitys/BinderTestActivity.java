package com.zyx.library.simple.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.zyx.library.binderlibrary.ProcessManager;
import com.zyx.library.simple.R;
import com.zyx.library.simple.bean.Person;
import com.zyx.library.simple.binder.IUserManager;

/**
 * @author pielan
 * @date 10/3/19 下午10:16
 * @dec
 */
public class BinderTestActivity extends AppCompatActivity {

    IUserManager userManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binder_test_activity);
        ProcessManager.getInstance().connect(this, null);
    }

    public void getUser(View view) {
        userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
        Person person = userManager.getPerson();
        Toast.makeText(this, person.getName() + "说：" + person.getSay(), Toast.LENGTH_SHORT).show();
    }

    public void getName(View view) {
        userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
        Toast.makeText(this, userManager.getName(), Toast.LENGTH_SHORT).show();

    }
}
