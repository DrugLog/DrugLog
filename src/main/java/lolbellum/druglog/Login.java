package lolbellum.druglog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private DrugManager drugManager = Calendar.drugManager;
    private static final String PREF_USER_PASSCODE = "userPasscode";
    private static final String PREF_PASSCODE_ENABLED = "passcodeEnabled";
    private static final String PREF_TRY_LOGIN = "tryLogin";
    private boolean tryLogin = true;
    private boolean passcodeEnabled = true;
    private String passcode;
    private boolean passcodeExists = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        Button buttonLogin = (Button) findViewById(R.id.button_click_login);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        passcodeEnabled = sp.getBoolean(PREF_PASSCODE_ENABLED, true);
        passcode = sp.getString(PREF_USER_PASSCODE,"");

        if(passcodeEnabled == false){
            Intent i = new Intent(this,Calendar.class);
            startActivity(i);
            finish();
        }

        if(passcode.equalsIgnoreCase("")){
            textViewTitle.setText("Choose a passcode. Don't lose your passcode!");
            buttonLogin.setText("Set passcode");
        } else {
            passcodeExists = true;
            textViewTitle.setText("Enter your passcode");
        }
    }

    public void clickLogin(View view){

        EditText editTextPasscode = (EditText) findViewById(R.id.edit_text_passcode);
        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        Button buttonLogin = (Button) findViewById(R.id.button_click_login);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(passcodeExists){
            String enteredPasscode = editTextPasscode.getText().toString();

            if(enteredPasscode.equalsIgnoreCase("")){
                Toast.makeText(this,"You must enter a passcode!",Toast.LENGTH_LONG).show();
                return;
            }
            if(enteredPasscode.equals(passcode)){
                sp.edit().putBoolean(PREF_TRY_LOGIN,false).commit();
                Toast.makeText(this,"Login successful",Toast.LENGTH_LONG).show();
                Intent i = new Intent(this,Calendar.class);
                startActivity(i);
                finish();
            } else {
                Log.i("testing","Incorrect password " + enteredPasscode + " entered. The real passcode is " + passcode);
                Toast.makeText(this,"Incorrect passcode",Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            passcode = editTextPasscode.getText().toString();
            if(passcode.equalsIgnoreCase("")){
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("No passcode entered");
                alertDialog.setMessage("Do you want to disable the passcode?");
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("testing", "No");
                        alertDialog.dismiss();
                        return;
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Log.i("testing", "Yes");
                        sp.edit().putBoolean(PREF_PASSCODE_ENABLED, false).commit();
                        Toast.makeText(getApplicationContext(), "Passcode disabled. You can enable the passcode again in the Calendar items menu", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),Calendar.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog.show();
            } else {
                Log.i("testing","Passcode set. The passcode is " + passcode);
                Toast.makeText(this,"Passcode set",Toast.LENGTH_LONG).show();
                sp.edit().putString(PREF_USER_PASSCODE, passcode).commit();
                editTextPasscode.setText("");
                passcodeExists = true;
                buttonLogin.setText("Login");
                textViewTitle.setText("Enter your passcode");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
