package cn.jhc.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016-11-27.
 */
public class CrimeListActivity extends SingleFramentActivity{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
