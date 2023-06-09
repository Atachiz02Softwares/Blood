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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.R;

public class SignUpActivity extends AppCompatActivity {
    // Declare variables
    private static final int REQUEST_CODE = 10;
    private static final int GOOGLE_REQUEST_CODE = 30;
    Uri profilePicture;
    CircleImageView profilePic;
    TextView email, password, confirmPassword, login;
    ProgressBar progressBar;
    Button signUp;
    CircleImageView google;
    FirebaseAuth mAuth;
    GoogleSignInOptions signInOptions;
    GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Returns an instance of this class corresponding to the default FirebaseApp instance.
        mAuth = FirebaseAuth.getInstance();
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        signInClient = GoogleSignIn.getClient(SignUpActivity.this, signInOptions);

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
                    if (pass.length() > 5)
                        mAuth.createUserWithEmailAndPassword(mail, pass)
                                .addOnCompleteListener(task -> {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sign Up successful!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    else
                        Snackbar.make(findViewById(R.id.signup), "Password must be more than 6 " +
                                "characters...", Snackbar.LENGTH_LONG).show();
                } else {
                    password.setError("Passwords don't match!");
                    confirmPassword.setError("Passwords don't match!");
                }
            } else {
                Snackbar.make(findViewById(R.id.signup), "No field should be empty...",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        login.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
        });


        google.setOnClickListener(v -> {
            Intent intent = signInClient.getSignInIntent();
            startActivityForResult(intent, GOOGLE_REQUEST_CODE);
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

        if (requestCode == GOOGLE_REQUEST_CODE) {
            progressBar.setVisibility(View.VISIBLE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                progressBar.setVisibility(View.GONE);
                finish();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            } catch (ApiException e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
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

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(SignUpActivity.this);
        if (signInAccount != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finishAffinity();
        }
    }
}