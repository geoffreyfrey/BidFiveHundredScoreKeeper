package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Map;


/**
 * Created by geoff on 6/21/16.
 */
public class SettingsActivity extends PreferenceActivity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar_no_options));

//        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        getFragmentManager().beginTransaction().replace(R.id.linear, new MyPreferenceFragment()).commit();

    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Map<String,?> allEntries = getPreferenceManager().getSharedPreferences().getAll();

            for (Map.Entry<String, ?> entry : allEntries.entrySet()){
                Preference pref = getPreferenceManager().findPreference(entry.getKey());

                if (pref instanceof ListPreference) {
                    ListPreference listPref = (ListPreference) pref;
                    pref.setSummary(listPref.getEntry());
                }
            }

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference pref = findPreference(key);

            if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                pref.setSummary(listPref.getEntry());
            }
        }
        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    private void setSupportActionBar(@Nullable Toolbar toolbar) {
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text_no_options);
        toolbarText.setText("GAME SETTINGS");
        getDelegate().setSupportActionBar(toolbar);
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

}




//public class SettingsActivity extends PreferenceActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//    }
//
//    public static class MyPreferenceFragment extends PreferenceFragment
//    {
//        @Override
//        public void onCreate(final Bundle savedInstanceState)
//        {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.preferences);
//        }
//    }


    //Older Code


//    //Setting up the views
//    TextView nonbiddingPoints;
//
//    //Setting up the variables
//    String nonbiddingRule;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        //Creating the layout and action bar
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//
//        //Call function to setup the textviews
//        setupViews();
//    }
//
//    private void setupViews(){
//        //initializing the variables
//        nonbiddingPoints = (TextView) findViewById(R.id.nonbidding_points);
//
//        //onClickListeners
//        nonbiddingPoints.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                setNonbiddingPoints();
//            }
//        });
//    }
//
//    public void setNonbiddingPoints(){
//
//        AlertDialog levelDialog;
//
//        // Strings to Show In Dialog with Radio Buttons
////        final CharSequence[] items = {" Easy "," Medium "," Hard "," Very Hard "};
//
//        // Creating and Building the Dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select The Difficulty Level");
//        builder.setSingleChoiceItems(R.array.nonbidding_points_options, -1, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                String choice;
//
//                switch(item)
//                {
//                    case 0:
//                        choice = "always";
//                        break;
//                    case 1:
//                        choice = "onLoss";
//                        break;
//                    case 2:
//                        choice = "never";
//                        break;
//                    default:
//                        choice = nonbiddingRule;
//                        break;
//
//                }
//                setNonbiddingRule(choice);
//                dialog.dismiss();
//            }
//        });
//        levelDialog = builder.create();
//        levelDialog.show();
//    }
//
//    public void setNonbiddingRule(String choice){
//        nonbiddingRule = choice;
//        Toast.makeText(this, nonbiddingRule, Toast.LENGTH_SHORT).show();
//    }
//}
