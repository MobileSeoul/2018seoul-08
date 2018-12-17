package com.example.mj975.woder_woman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.mj975.woder_woman.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        Button loginButton = findViewById(R.id.button_sign_in);
        Button signUpButton = findViewById(R.id.button_sign_up);

        EditText emailEdit = findViewById(R.id.email_edit);
        EditText passwordEdit = findViewById(R.id.password_edit);


        loginButton.setOnClickListener(view -> {
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            auth.signInWithEmailAndPassword(email, password).
                    addOnSuccessListener(authResult -> finish()).
                    addOnFailureListener(e -> Snackbar.make(view, "아이디 혹은 비밀번호를 다시 확인해 주세요.", Snackbar.LENGTH_SHORT).show());
        });

        signUpButton.setOnClickListener(v -> {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
        });

        setResult(RESULT_OK);
    }
}
