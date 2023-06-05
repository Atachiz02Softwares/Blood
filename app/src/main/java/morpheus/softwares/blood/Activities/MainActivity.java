package morpheus.softwares.blood.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import morpheus.softwares.blood.R;

public class MainActivity extends AppCompatActivity {
    ImageView icon;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icon = findViewById(R.id.loginIcon);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();
        }

        icon.setOnClickListener(v -> {
            // Signs out the current user and clears it from the disk cache.
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();

            Toast.makeText(MainActivity.this, "Sign out successful!",
                    Toast.LENGTH_LONG).show();
        });
    }
}