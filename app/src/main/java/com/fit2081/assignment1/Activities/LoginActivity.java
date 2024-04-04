package com.fit2081.assignment1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fit2081.assignment1.Keys;
import com.fit2081.assignment1.R;

public class LoginActivity extends AppCompatActivity {
    EditText usernameText;
    EditText passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.usernameText2);
        passwordText = findViewById(R.id.passwordText2);

        //get the latest saved account and display
        SharedPreferences latestSavedAccSP = getSharedPreferences(Keys.ACC_SP, MODE_PRIVATE);
        String username = latestSavedAccSP.getString(Keys.USERNAME, "");
        usernameText.setText(username);
    }

    public void onLoginClick(View view) {
        //save account information into the sharedPreferences, so that we can retrieve it when logging in
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences(Keys.ACC_SP, MODE_PRIVATE);
        String passRet = sharedPreferences.getString(Keys.PASSWORD, null);
        String userRet = sharedPreferences.getString(Keys.USERNAME, null);
        // we want to check if the password entered is the same with the password in the account information
        if(passRet != null && passRet.equals(password) && userRet != null && userRet.equals(username)) {
            onDashboardNavigate(view);
        } else {
            Toast.makeText(this, "Authentication failure: Username or Password incorrect", Toast.LENGTH_LONG).show();
        }
    }

    public void onSignUpNavClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onDashboardNavigate(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }


}