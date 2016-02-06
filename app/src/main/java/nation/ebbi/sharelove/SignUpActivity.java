package nation.ebbi.sharelove;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {
    private EditText mUsername, mPassword, mEmail;


    ErrorBox error;
    StartActivity mStartActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.hide();
        mUsername= (EditText) findViewById(R.id.UsernameLayout1);
        mPassword= (EditText) findViewById(R.id.PasswordLayout1);
        mEmail= (EditText) findViewById(R.id.EmailLayout);
        Button signUpButton = (Button) findViewById(R.id.SignUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String username = mUsername.getText().toString();
                username = username.trim();
                password = password.trim();
                email = email.trim();
                user.setEmail(email);
                user.setPassword(password);
                user.setUsername(username);
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    error.CreateErrorMessage(SignUpActivity.this, "Please Enter a Email, Address and Password!", "Oops!", "OK");
                }
                setSupportProgressBarIndeterminateVisibility(true);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        setSupportProgressBarIndeterminateVisibility(false);
                        if (e == null) {
                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setMessage(e.getMessage())
                                    .setTitle("Oops!")
                                    .setPositiveButton("OK!", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
