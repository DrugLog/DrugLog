package lolbellum.druglog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Eliot on 8/25/2015.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String PREF_USER_PASSCODE = "userPasscode";
    private static final String PREF_PASSCODE_ENABLED = "passcodeEnabled";
    private boolean passCodeEnabled;
    private String passcode;
    private boolean passcodeExists;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference checkBoxPasscodeEnabled = (CheckBoxPreference)findPreference("pref_passcode_enabled");
        Preference prefChangePasscode = (Preference) findPreference("pref_change_passcode");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        passCodeEnabled = sp.getBoolean(PREF_PASSCODE_ENABLED,true);
        if(passCodeEnabled){
            checkBoxPasscodeEnabled.setChecked(true);
        } else {
            checkBoxPasscodeEnabled.setChecked(false);
        }
        passcode = sp.getString(PREF_USER_PASSCODE, "");
        if(passcode.equalsIgnoreCase("")){
            passcodeExists = false;
        } else {
            passcodeExists = true;
        }



        prefChangePasscode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i("testing", "Preference, 'Change passcode' clicked.");
                if (!passCodeEnabled) {
                    Toast.makeText(getApplicationContext(), "Passcode is disabled!", Toast.LENGTH_LONG).show();
                    return false;
                }
                showNewDialog("Confirm passcode", true, true);
                return false;
            }
        });

        checkBoxPasscodeEnabled.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(passCodeEnabled){
                    showDisablePasscode("Confirm password",true);
                } else {
                    Toast.makeText(getApplicationContext(), "Passcode enabled!", Toast.LENGTH_LONG).show();
                    sp.edit().putBoolean(PREF_PASSCODE_ENABLED,true).apply();
                }
                return false;
            }
        });

    }

    private void showDisablePasscode(String title, boolean canCancel){
        final AlertDialog loginDialog = new AlertDialog.Builder(this).create();
        final LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.login_dialog, null);
        loginDialog.setView(layout);
        final EditText editTextPasscode = (EditText) layout.findViewById(R.id.password);
        loginDialog.setTitle(title);
        loginDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        if (canCancel) {
            loginDialog.setCancelable(true);
            loginDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }


        loginDialog.show();


        final Button buttonConfirmPasscode = (Button) loginDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button cancelConfirmPasscode = (Button) loginDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        buttonConfirmPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String enteredPasscode = editTextPasscode.getText().toString();
                if(enteredPasscode.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"You must enter a passcode!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(enteredPasscode.equals(passcode)){
                    Toast.makeText(getApplicationContext(),"Passcode disabled",Toast.LENGTH_LONG).show();
                    sp.edit().putBoolean(PREF_PASSCODE_ENABLED, false).apply();
                    sp.edit().putString(PREF_USER_PASSCODE, "").apply();
                    loginDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(),"Incorrect passcode!",Toast.LENGTH_LONG).show();
                    editTextPasscode.setText("");
                    return;
                }
            }
        });

        cancelConfirmPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });
    }

    private void showNewDialog(String title, boolean canCancel, boolean test) {

        final AlertDialog loginDialog = new AlertDialog.Builder(this).create();
        final LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.login_dialog, null);
        loginDialog.setView(layout);
        final EditText editTextPasscode = (EditText) layout.findViewById(R.id.password);
        loginDialog.setTitle(title);
        loginDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        if (canCancel) {
            loginDialog.setCancelable(true);
            loginDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }


        loginDialog.show();


        final Button buttonConfirmPasscode = (Button) loginDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button cancelConfirmPasscode = (Button) loginDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        // final Button enterPasscode = (Button) enterPasscodeDialog.getButton(AlertDialog.BUTTON_POSITIVE);

        if (!test) {
            buttonConfirmPasscode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String enteredPasscode = editTextPasscode.getText().toString();
                    if (enteredPasscode.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "You must enter a passcode!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Passcode changed!", Toast.LENGTH_LONG).show();
                    sp.edit().putString(PREF_USER_PASSCODE, enteredPasscode).apply();
                    loginDialog.dismiss();
                }
            });
        } else {

            buttonConfirmPasscode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String enteredPasscode = editTextPasscode.getText().toString();
                    if (enteredPasscode.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "You must enter a passcode!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (enteredPasscode.equals(passcode)) {
                        loginDialog.dismiss();
                        // enterPasscodeDialog.show();
                        showNewDialog("Enter new passcode", false, false);

                    /*enterPasscode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            String enteredPasscode = editTextPasscode.getText().toString();
                            if (enteredPasscode.equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(), "You must enter a passcode!", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Toast.makeText(getApplicationContext(), "Passcode changed!", Toast.LENGTH_LONG).show();
                            sp.edit().putString(PREF_USER_PASSCODE, enteredPasscode).apply();
                            enterPasscodeDialog.dismiss();
                        }
                    });*/
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect passcode!", Toast.LENGTH_LONG).show();
                        editTextPasscode.setText("");
                        return;
                    }
                }
            });

    }
        cancelConfirmPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("pref_passcode_enabled")){
            if(passCodeEnabled){

            }
        }
    }
}
