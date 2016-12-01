package cn.jhc.criminalintent;

import android.support.v4.app.Fragment;
import android.util.Log;

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
}
