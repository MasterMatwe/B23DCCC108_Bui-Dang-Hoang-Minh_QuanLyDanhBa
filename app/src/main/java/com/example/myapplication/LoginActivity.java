package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public final class LoginActivity extends BaseActivity {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLayout(R.layout.activity_login);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        TextView error = findViewById(R.id.login_error);
        findViewById(R.id.login_button).setOnClickListener(view -> {
            error.setVisibility(View.GONE);
            boolean missing = false;
            if (username.getText().toString().trim().isEmpty()) {
                username.setError(getString(R.string.username_required));
                missing = true;
            }
            if (password.getText().toString().isEmpty()) {
                password.setError(getString(R.string.password_required));
                missing = true;
            }
            if (missing) return;
            if (USERNAME.equals(username.getText().toString().trim())
                    && PASSWORD.equals(password.getText().toString())) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                error.setVisibility(View.VISIBLE);
                error.announceForAccessibility(getString(R.string.login_failed));
            }
        });
        password.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                findViewById(R.id.login_button).performClick();
                return true;
            }
            return false;
        });
    }
}
