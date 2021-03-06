package cn.jhc.criminalintent;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.jhc.criminalintent.util.LogUtils;

/**
 * Created by Administrator on 2016-11-27.
 * 模板方法模式
 */
public abstract class SingleFragmentActivity extends AppCompatActivity{
    protected  abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            LogUtils.d(LogUtils.LOG_TAG,"SingleFragmentActivity");
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }
}
