package com.example.j053.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LaunchActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button bLogin;
    private TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            setContentView(R.layout.activity_launch);

            etUsername = findViewById(R.id.etUsername);
            etPassword = findViewById(R.id.etPassword);
            bLogin = findViewById(R.id.bLogin);
            tvSignup = findViewById(R.id.tvSignup);

            bLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = etUsername.getText().toString();
                    final String password = etPassword.getText().toString();
                    login(username, password);
                }
            });

            tvSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LaunchActivity.this, SingupActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.i("LaunchActivity", "Login successful");
                    final Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LaunchActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
