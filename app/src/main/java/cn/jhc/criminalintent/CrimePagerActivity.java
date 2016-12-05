package cn.jhc.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

import cn.jhc.criminalintent.bean.Crime;
import cn.jhc.criminalintent.db.CrimeLab;
import cn.jhc.criminalintent.util.LogUtils;

/**
 * Created by Administrator on 2016-12-02.
 */
public class CrimePagerActivity extends FragmentActivity{
    private static final String EXTRA_CRIME_ID = "cn.jhc.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newIntent(crime.getmId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for(int i=0; i < mCrimes.size();i++) {
            if(mCrimes.get(i).getmId().equals(crimeId)) {
                LogUtils.e(LogUtils.LOG_TAG,i+"");
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}