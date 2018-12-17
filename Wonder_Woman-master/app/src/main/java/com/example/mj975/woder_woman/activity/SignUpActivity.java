package com.example.mj975.woder_woman.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseAuth auth;

        EditText emailEditText = findViewById(R.id.enroll_email_edit);
        EditText nameEditText = findViewById(R.id.enroll_name_edit);
        EditText ageEditText = findViewById(R.id.enroll_age_edit);
        EditText passwordEditText = findViewById(R.id.enroll_password_edit);
        EditText passwordConfirmEditText = findViewById(R.id.pass_word_confirm_edit);

        Button signUp = findViewById(R.id.button_sign_up);

        passwordConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!passwordEditText.getText().toString()
                        .equals(passwordConfirmEditText.getText().toString())) {
                    signUp.setEnabled(false);
                    signUp.setText(R.string.wrong_password);
                    signUp.setBackgroundColor(Color.rgb(220, 10, 10));
                } else {
                    signUp.setEnabled(true);
                    signUp.setText("회원 가입");
                    signUp.setBackgroundColor(Color.rgb(10, 220, 10));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        auth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(view -> {
            if (emailEditText.getText().length() > 0 &&
                    passwordConfirmEditText.getText().length() > 0 &&
                    nameEditText.getText().length() > 0 &&
                    ageEditText.getText().length() > 0) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                int age = Integer.parseInt(ageEditText.getText().toString());

                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("회원 가입 진행 중입니다.");
                dialog.show();

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                User user = new User(email, name, age, "");

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("User").document(email)
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            Snackbar.make(view, "회원가입에 성공하였습니다.", Snackbar.LENGTH_SHORT).show();
                                        }).addOnFailureListener(e -> {
                                    dialog.dismiss();
                                    Snackbar.make(view, "회원가입에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
                                });
                                finish();
                            } else {
                                Snackbar.make(view, "회원가입에 실패하였습니다.",
                                        Snackbar.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            } else {
                Snackbar.make(view, "빈칸을 입력해주세요.",
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
