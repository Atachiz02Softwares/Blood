package morpheus.softwares.blood.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import morpheus.softwares.blood.R;

public class LoginActivity extends AppCompatActivity {
    TextView email, password;
    Button login;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.loginProgressBar);
        login = findViewById(R.id.loginLogin);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            String mail = String.valueOf(email.getText()).trim();
            String pass = String.valueOf(password.getText()).trim();

            if (!(mail.isEmpty() && pass.isEmpty())) {
                mAuth.signInWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login successful!",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(findViewById(R.id.login), "No field should be empty...",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
}