package com.zyx.library.simple.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class BinderActivity extends Activity {

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

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        };

        asyncTask.execute();
    }
}
