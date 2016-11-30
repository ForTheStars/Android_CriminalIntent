package cn.jhc.criminalintent;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Administrator on 2016-11-27.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Log.d("TAP","1");
        return new CrimeListFragment();
    }
}
