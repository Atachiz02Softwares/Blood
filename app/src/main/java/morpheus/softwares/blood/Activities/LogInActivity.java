package morpheus.softwares.blood.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.R;

public class LogInActivity extends AppCompatActivity {
    TextView email, password;
    Button logIn;
    CircleImageView google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        logIn = findViewById(R.id.loginLogin);
        google = findViewById(R.id.loginGoogle);

        logIn.setOnClickListener(v -> {
            String mail = email.getText().toString();
            String pass = password.getText().toString();

            if ((!mail.isEmpty()) && (!pass.isEmpty())) {
                Toast.makeText(LogInActivity.this, "Sign Up successful!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                finish();
            } else {
                Snackbar.make(findViewById(R.id.signup), "No field should be empty...",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        google.setOnClickListener(v -> {

        });
    }
}