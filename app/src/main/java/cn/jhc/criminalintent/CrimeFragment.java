package cn.jhc.criminalintent;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import cn.jhc.criminalintent.bean.Crime;
import cn.jhc.criminalintent.db.CrimeLab;
import cn.jhc.criminalintent.util.LogUtils;
import cn.jhc.criminalintent.util.TimeUtil;

/**
 * Created by Administrator on 2016-11-25.
 */
public class CrimeFragment extends Fragment {
    private static final String AGR_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_CALL = 2;
    private Crime mCrime;
    private EditText mTitleField;
    private File mPhotoFile;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallPhone;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;


    public static CrimeFragment newIntent(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(AGR_CRIME_ID,crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(AGR_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        returnResult();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setmTitle(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
//        mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
               // DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setmSolved(b);
            }
        });

        mReportButton = (Button) view.findViewById(R.id.crime_report);
        /*mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                i = Intent.createChooser(i,getString(R.string.send_reort));
                startActivity(i);
            }
        });*/
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(getActivity());
                intentBuilder.setType("text/plain");
                intentBuilder.setText(getCrimeReport());
                intentBuilder.setSubject(getString(R.string.crime_report_subject));
                Intent i = intentBuilder.createChooserIntent();
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //pickContact.addCategory(Intent.CATEGORY_HOME);  验证不带联系人的设备
        mSuspectButton = (Button) view.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });

        if(mCrime.getmSuspect() != null) {
            mSuspectButton.setText(mCrime.getmSuspect());
        }

        mCallPhone = (Button) view.findViewById(R.id.crime_phone);
        mCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact,REQUEST_CALL);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }
        mPhotoButton = (ImageButton) view.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView) view.findViewById(R.id.crime_photo);
        return view;
    }

    public void returnResult() {
        Intent intent = new Intent();
        intent.putExtra("mcrime_id",mCrime.getmTitle());
        getActivity().setResult(Activity.RESULT_OK,intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate();
        } else if(requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor cursor = getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);

            if(cursor.getCount() == 0) {
                return;
            }
            cursor.moveToFirst();
            String suspect = cursor.getString(0);
            mCrime.setmSuspect(suspect);
            mSuspectButton.setText(suspect);
        } else if(requestCode == REQUEST_CALL && data != null) {
            LogUtils.i(LogUtils.LOG_TAG,"call");
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor cursor = getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);
            if(cursor.getCount() == 0) {
                return;
            }
            cursor.moveToFirst();
            int telId = cursor.getInt(0);
            String telP = cursor.getString(1);
            LogUtils.i(LogUtils.LOG_TAG,"id+"+telId+"~~"+telP);
            getPhoneNumber(telId);
        }
    }
    private void getPhoneNumber(int telId) {
        LogUtils.i(LogUtils.LOG_TAG,telId+"");
        String column = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor phone=getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,queryFields,column+"="+telId,null,null);
        phone.moveToFirst();
        String phoneNumber=phone.getString(0);
        callPhone(phoneNumber);
    }
    private void callPhone(String phoneNumber) {
        Uri numUri = Uri.parse("tel:"+phoneNumber);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(numUri);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    private void updateDate() {
        mDateButton.setText(TimeUtil.DateFormat(mCrime.getmDate()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_crime_delete:
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.delCrime(mCrime);
                Intent intent = CrimeListActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if(mCrime.ismSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MM dd";
        String dateString = DateFormat.format(dateFormat,mCrime.getmDate()).toString();

        String suspect = mCrime.getmSuspect();
        if(suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report,mCrime.getmTitle(),dateString,solvedString,suspect);
        return  report;
    }
}
