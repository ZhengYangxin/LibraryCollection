package com.zyx.library.simple.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.zyx.library.binderlibrary.ProcessManager;
import com.zyx.library.simple.R;
import com.zyx.library.simple.bean.Person;
import com.zyx.library.simple.binder.UserManager;

/**
 * @author pielan
 * @date 10/3/19 下午10:16
 * @dec
 */
public class BinderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binder_activity);
        ProcessManager.getInstance().register(UserManager.class, UserManager.getInstance());
        UserManager.getInstance().setPerson(new Person("郑阳鑫", "哈喽"));
        UserManager.getInstance().setName("pielan");
    }

    public void jump(View view) {
        Intent intent = new Intent(this, BinderTestActivity.class);
        startActivity(intent);
    }
}
