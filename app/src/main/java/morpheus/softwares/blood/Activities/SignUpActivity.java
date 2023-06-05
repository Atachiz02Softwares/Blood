package morpheus.softwares.blood.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.R;

public class SignUpActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 10;
    Uri profilePicture;
    CircleImageView profilePic;
    TextView email, password, confirmPassword, login;
    Button signUp;
    CircleImageView google;

//    FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Toast.makeText(SignUpActivity.this, "Touch the ImageView to add your profile picture...",
                Toast.LENGTH_LONG).show();

//        authentication = FirebaseAuth.getInstance();

        profilePic = findViewById(R.id.signupProfilePic);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.signupConfirmPassword);
        signUp = findViewById(R.id.signupSignUp);
        login = findViewById(R.id.signupLogIn);
        google = findViewById(R.id.signupGoogle);

//        if (authentication.getCurrentUser() != null) {
//            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//            finishAffinity();
//        }

        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });

        signUp.setOnClickListener(v -> {
            String mail = email.getText().toString();
            String pass = password.getText().toString();
            String cPass = confirmPassword.getText().toString();

            if ((!mail.isEmpty()) && (!pass.isEmpty()) && (!cPass.isEmpty())) {
                if (pass.equals(cPass)) {
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
}