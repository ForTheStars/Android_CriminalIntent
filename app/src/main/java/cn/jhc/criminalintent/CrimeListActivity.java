package cn.jhc.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

import cn.jhc.criminalintent.util.LogUtils;

/**
 * Created by Administrator on 2016-11-27.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        LogUtils.d(LogUtils.LOG_TAG,"create");
        return new CrimeListFragment();
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext,CrimeListActivity.class);
        return intent;
    }
}
