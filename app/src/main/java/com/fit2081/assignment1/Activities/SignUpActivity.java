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

public class SignUpActivity extends AppCompatActivity {
    EditText usernameText;
    EditText passwordText;
    EditText confirmPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        confirmPasswordText = findViewById(R.id.confirmPassword);
    }

    public void onSignUpClick(View view) {
        //save account information into the sharedPreferences, so that we can retrieve it when logging in
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences(Keys.ACC_SP, MODE_PRIVATE);
        SharedPreferences latestSavedUserSP = getSharedPreferences(Keys.LATEST_SAVED_USER, MODE_PRIVATE);
        // we don't want the same username in the sharedPreferences
        if (sharedPreferences.contains(username)) {
            Toast.makeText(this, "The username has been used, please change another one", Toast.LENGTH_LONG).show();
        } else {
            // check if the password has a value
            if(password.length() < 1) {
                Toast.makeText(this, "Password can not be empty, please try again", Toast.LENGTH_LONG).show();
                return;
            }
            if(!confirmPassword.equals(password)){
                Toast.makeText(this, "The passwords you entered do not match.", Toast.LENGTH_LONG).show();
                return;
            }
            // Key does not exist, safe to insert the account info into the shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(username, password);
            editor.apply();

            //Update the latest saved acc
            SharedPreferences.Editor editorLatest = latestSavedUserSP.edit();
            editorLatest.putString("username", username);
            editorLatest.apply();

            Toast.makeText(this, "Your account has been created successfully", Toast.LENGTH_LONG).show();
            onLoginNavClick(view);
        }
    }

    public void onLoginNavClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}