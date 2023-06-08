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

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.R;

public class SignUpActivity extends AppCompatActivity {
    // Declare variables
    private static final int REQUEST_CODE = 10;
    private static final int REQ_ONE_TAP = 30;
    Uri profilePicture;
    CircleImageView profilePic;
    TextView email, password, confirmPassword, login;
    ProgressBar progressBar;
    Button signUp;
    CircleImageView google;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    SignInClient oneTapClient;
    BeginSignInRequest signInRequest;
    private boolean showOneTapUI = true;

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
            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            finish();
        });

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();


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

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    // Got an ID token from Google. Use it to authenticate
                    // with Firebase.
                    Snackbar.make(findViewById(R.id.signup), "Got ID token.",
                            Snackbar.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(findViewById(R.id.signup), Objects.requireNonNull(e.getMessage()),
                        Snackbar.LENGTH_LONG).show();
                finish();
            }
        } else {
            throw new IllegalStateException("Unexpected value: " + requestCode);
        }

        SignInCredential googleCredential;
        try {
            googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        String idToken = googleCredential.getGoogleIdToken();
        if (idToken != null) {
            // Got an ID token from Google. Use it to authenticate
            // with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, "Signed up successfully!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Sign up failed!",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            finish();
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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