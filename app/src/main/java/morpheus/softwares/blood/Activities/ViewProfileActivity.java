package morpheus.softwares.blood.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import morpheus.softwares.blood.R;

public class ViewProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    ExtendedFloatingActionButton extendedFab;
    FloatingActionButton callFab, chatFab;
    boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        toolbar = findViewById(R.id.viewProfileToolbar);
        drawerLayout = findViewById(R.id.viewProfileDrawer);
        navigationView = findViewById(R.id.viewProfileNavigator);
        extendedFab = findViewById(R.id.viewProfileFab);
        callFab = findViewById(R.id.viewProfileFabCall);
        chatFab = findViewById(R.id.viewProfileFabSMS);
        isAllFabsVisible = false;

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        actionBarDrawerToggle.syncState();
        setSupportActionBar(toolbar);

        callFab.setVisibility(View.GONE);
        chatFab.setVisibility(View.GONE);
        extendedFab.shrink();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.createProfile)
                startActivity(new Intent(ViewProfileActivity.this, CreateProfileActivity.class));
            else if (item.getItemId() == R.id.viewProfile)
                Toast.makeText(ViewProfileActivity.this, "Innit", Toast.LENGTH_SHORT).show();
            else if (item.getItemId() == R.id.about)
                Toast.makeText(ViewProfileActivity.this, "About", Toast.LENGTH_SHORT).show();
            else if (item.getItemId() == R.id.exit) finishAffinity();

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

        extendedFab.setOnClickListener(v -> {
            if (!isAllFabsVisible) {
                extendedFab.extend();
                callFab.setVisibility(View.VISIBLE);
                chatFab.setVisibility(View.VISIBLE);
                isAllFabsVisible = true;
            } else {
                extendedFab.shrink();
                callFab.setVisibility(View.GONE);
                chatFab.setVisibility(View.GONE);
                isAllFabsVisible = false;
            }
        });

        callFab.setOnClickListener(v -> Toast.makeText(ViewProfileActivity.this, "Call",
                Toast.LENGTH_SHORT).show());
        chatFab.setOnClickListener(v -> Toast.makeText(ViewProfileActivity.this, "Chat",
                Toast.LENGTH_SHORT).show());
    }
}