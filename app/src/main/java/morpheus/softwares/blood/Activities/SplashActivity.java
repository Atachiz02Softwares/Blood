package morpheus.softwares.blood.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import morpheus.softwares.blood.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int TIMEOUT = 2000;
    ImageView appIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appIcon = findViewById(R.id.icon);

        // Bounce animation of the App Icon
        appIcon.animate().setDuration(1000).setInterpolator(new BounceInterpolator())
                .translationY(300);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            finish();
        }, TIMEOUT);
    }
}