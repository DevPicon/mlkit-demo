package pe.devpicon.android.mlkitappclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private FirebaseAuth firebaseAuth;

    private LinearLayout containerSignUp;
    private LinearLayout containerStart;
    private Button buttonSubmit;
    private Button buttonRegister;
    private Button buttonLogin;
    private Button buttonSubmitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        containerSignUp = findViewById(R.id.container_sign_up);
        containerStart = findViewById(R.id.container_start);

        buttonSubmit = findViewById(R.id.button_submit);
        buttonRegister = findViewById(R.id.button_register);
        buttonLogin = findViewById(R.id.button_login);
        buttonSubmitLogin = findViewById(R.id.button_submit_login);

        buttonSubmit.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        buttonSubmitLogin.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            goToNextActivity();
        } else {
            containerStart.setVisibility(View.VISIBLE);
            containerSignUp.setVisibility(View.GONE);
        }
    }

    private void goToNextActivity() {
        startActivity(new Intent(this, NextActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit:
                registerNewUser();
                break;
            case R.id.button_register:
                showSignUpForm();
                break;
            case R.id.button_login:
                showLoginForm();
                break;
            case R.id.button_submit_login:
                loginUser();
                break;
        }
    }

    private void loginUser() {

        EditText editTextEmail = findViewById(R.id.edit_text_email);
        EditText editTextPassword = findViewById(R.id.edit_text_password);

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void showLoginForm() {
        containerSignUp.setVisibility(View.VISIBLE);
        containerStart.setVisibility(View.GONE);
        buttonSubmit.setVisibility(View.GONE);
        buttonSubmitLogin.setVisibility(View.VISIBLE);
    }

    private void showSignUpForm() {
        containerSignUp.setVisibility(View.VISIBLE);
        containerStart.setVisibility(View.GONE);
    }

    private void registerNewUser() {

        EditText editTextEmail = findViewById(R.id.edit_text_email);
        EditText editTextPassword = findViewById(R.id.edit_text_password);

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
