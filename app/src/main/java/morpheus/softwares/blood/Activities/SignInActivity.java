package morpheus.softwares.blood.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.R;

public class SignInActivity extends AppCompatActivity {
    // Declare variables
//    private static final int GOOGLE_REQUEST_CODE = 10;
    TextView email, password, confirmPassword, login;
    ProgressBar progressBar;
    Button signUp;
//    CircleImageView google;
    FirebaseAuth mAuth;
//    GoogleSignInOptions signInOptions;
//    GoogleSignInClient signInClient;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setUpPermission();

        // Returns an instance of this class corresponding to the default FirebaseApp instance.
        mAuth = FirebaseAuth.getInstance();
//        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        signInClient = GoogleSignIn.getClient(SignInActivity.this, signInOptions);

        // Initialize variables
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.signupConfirmPassword);
        progressBar = findViewById(R.id.signupProgressBar);
        signUp = findViewById(R.id.signupSignUp);
        login = findViewById(R.id.signupLogin);
//        google = findViewById(R.id.signInGoogle);

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
                                        Toast.makeText(SignInActivity.this, "Sign Up successful!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    else {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(findViewById(R.id.signup), "Password must be more than 6 " +
                                "characters...", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    password.setError("Passwords don't match!");
                    confirmPassword.setError("Passwords don't match!");
                }
            } else {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(findViewById(R.id.signup), "No field should be empty...",
                        Snackbar.LENGTH_LONG).show();
            }
        });

//        google.setOnClickListener(this::onClick);

        login.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, LoginActivity.class)));
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GOOGLE_REQUEST_CODE) {
//            progressBar.setVisibility(View.VISIBLE);
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                task.getResult(ApiException.class);
//                progressBar.setVisibility(View.GONE);
//                finish();
//                startActivity(new Intent(SignInActivity.this, MainActivity.class));
//            } catch (ApiException e) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                throw new RuntimeException(e);
//            }
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // If user is already signed in, jump to the MainActivity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finishAffinity();
        }

//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(SignInActivity.this);
//        if (signInAccount != null) {
//            startActivity(new Intent(SignInActivity.this, MainActivity.class));
//            finishAffinity();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void setUpPermission() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.INTERNET}, 1);
        }
    }

//    @SuppressWarnings("deprecation")
//    private void onClick(View v) {
//        Intent intent = signInClient.getSignInIntent();
//        startActivityForResult(intent, GOOGLE_REQUEST_CODE);
//    }
}