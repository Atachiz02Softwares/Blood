package morpheus.softwares.blood.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.R;

public class SignUpActivity extends AppCompatActivity {
    // Declare variables
    private static final int REQUEST_CODE = 10;
    Uri profilePicture;
    CircleImageView profilePic;
    TextView email, password, confirmPassword, login;
    ProgressBar progressBar;
    Button signUp;
    CircleImageView google;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Returns an instance of this class corresponding to the default FirebaseApp instance.
        mAuth = FirebaseAuth.getInstance();

        // Initialize variables
        profilePic = findViewById(R.id.signupProfilePic);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.signupConfirmPassword);
        progressBar = findViewById(R.id.signupProgressBar);
        signUp = findViewById(R.id.signupSignUp);
        login = findViewById(R.id.signupLogIn);
        google = findViewById(R.id.signupGoogle);

        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });

        signUp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            String mail = String.valueOf(email.getText()).trim();
            String pass = String.valueOf(password.getText()).trim();
            String cPass = String.valueOf(confirmPassword.getText()).trim();

            if ((!mail.isEmpty()) && (!pass.isEmpty()) && (!cPass.isEmpty())) {
                if (pass.equals(cPass)) {
                    mAuth.createUserWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(task -> {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Account created!",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                    Toast.makeText(SignUpActivity.this, "Sign Up successful!", Toast.LENGTH_LONG).show();
                } else {
                    password.setError("Password and Confirm password did not match!");
                    confirmPassword.setError("Confirm password and Password did not match!");
                }
            } else {
                Snackbar.make(findViewById(R.id.signup), "No field should be empty...",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        login.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            finish();
        });

        google.setOnClickListener(v -> {

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                profilePicture = data.getData();
                profilePic.setImageURI(profilePicture);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // If user is already signed in, jump to the MainActivity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finishAffinity();
        }
    }
}