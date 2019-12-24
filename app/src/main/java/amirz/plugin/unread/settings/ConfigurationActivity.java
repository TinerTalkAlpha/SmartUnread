package amirz.plugin.unread.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.android.launcher3.Utilities;

import amirz.plugin.unread.widget.AbstractWidgetProvider;
import amirz.smartunread.R;

public class ConfigurationActivity extends Activity {
    private static final String USE_GOOGLE_SANS = "pref_google_sans";
    private static final String UCFIRST = "pref_ucfirst";
    private static final String DOUBLE_TITLE = "pref_double_title";
    private static final String CURRENT_DATE = "pref_current_date";
    private static final String CHARGING_PERC = "pref_charging_perc";
    private static final String SILENT_NOTIFS = "pref_silent_notifs";
    private static final String GREETING = "pref_greeting";

    private static final String[] PREFS = {
            USE_GOOGLE_SANS,
            UCFIRST,
            DOUBLE_TITLE,
            CURRENT_DATE,
            CHARGING_PERC,
            SILENT_NOTIFS,
            GREETING
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_title);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new ConfigurationFragment())
                .commit();
    }

    public static class ConfigurationFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {
        private Activity mContext;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mContext = getActivity();

            getPreferenceManager().setSharedPreferencesName(mContext.getPackageName());
            addPreferencesFromResource(R.xml.preferences);

            if (!Utilities.ATLEAST_OREO) {
                SwitchPreference googleSans = (SwitchPreference) findPreference(USE_GOOGLE_SANS);
                googleSans.setChecked(false);
                googleSans.setEnabled(false);
            }

            for (String pref : PREFS) {
                findPreference(pref).setOnPreferenceChangeListener(this);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            for (String pref : PREFS) {
                findPreference(pref).setOnPreferenceChangeListener(null);
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            AbstractWidgetProvider.updateAll(mContext);
            return true;
        }
    }

    public static boolean useGoogleSans(Context context) {
        return getPrefs(context).getBoolean(USE_GOOGLE_SANS, true)
                && Utilities.ATLEAST_OREO;
    }

    public static boolean ucFirst(Context context) {
        return getPrefs(context).getBoolean(UCFIRST, true);
    }

    public static boolean doubleTitle(Context context) {
        return getPrefs(context).getBoolean(DOUBLE_TITLE, false);
    }

    public static boolean currentDate(Context context) {
        return getPrefs(context).getBoolean(CURRENT_DATE, true);
    }

    public static boolean chargingPerc(Context context) {
        return getPrefs(context).getBoolean(CHARGING_PERC, true);
    }

    public static boolean silentNotifs(Context context) {
        return getPrefs(context).getBoolean(SILENT_NOTIFS, true);
    }

    public static String greeting(Context context) {
        return getPrefs(context).getString(GREETING, "");
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
    }
}