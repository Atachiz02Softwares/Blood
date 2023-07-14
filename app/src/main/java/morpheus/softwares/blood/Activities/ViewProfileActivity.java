package morpheus.softwares.blood.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import morpheus.softwares.blood.R;

public class ViewProfileActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

    ImageView profilePic;
    TextView fullName, location, bloodGrp, addr, zipCode, sex, status, gene;

    ExtendedFloatingActionButton extendedFab;
    FloatingActionButton callFab, chatFab;
    boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        appBarLayout = findViewById(R.id.viewProfileAppBar);
        collapsingToolbarLayout = findViewById(R.id.viewProfileCollapsingToolbar);
        toolbar = findViewById(R.id.viewProfileToolbar);
        profilePic = findViewById(R.id.viewProfilePic);
        fullName = findViewById(R.id.viewProfileName);
        location = findViewById(R.id.viewProfileLocation);
        bloodGrp = findViewById(R.id.viewProfileBloodGroup);
        addr = findViewById(R.id.viewProfileAddress);
        zipCode = findViewById(R.id.viewProfilePostalCode);
        sex = findViewById(R.id.viewProfileGender);
        status = findViewById(R.id.viewProfileRole);
        gene = findViewById(R.id.viewProfileGenotype);
        extendedFab = findViewById(R.id.viewProfileFab);
        callFab = findViewById(R.id.viewProfileFabCall);
        chatFab = findViewById(R.id.viewProfileFabSMS);
        isAllFabsVisible = false;

//        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedTitle);

        String profilePicture = String.valueOf(getIntent().getStringExtra("profilePicture")),
                name = String.valueOf(getIntent().getStringExtra("name")),
                address = String.valueOf(getIntent().getStringExtra("address")),
                state = String.valueOf(getIntent().getStringExtra("state")),
                nationality = String.valueOf(getIntent().getStringExtra("nationality")),
                role = String.valueOf(getIntent().getStringExtra("role")),
                genotype = String.valueOf(getIntent().getStringExtra("genotype")),
                bloodGroup = String.valueOf(getIntent().getStringExtra("bloodGroup")),
                gender = String.valueOf(getIntent().getStringExtra("gender")),
                postCode = String.valueOf(getIntent().getStringExtra("postCode")),
                phoneNumber = String.valueOf(getIntent().getStringExtra("phoneNumber"));

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            // Check if the collapsing toolbar is fully collapsed
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                // Collapsed state
                collapsingToolbarLayout.setTitle(name);
            } else {
                // Expanded state or in-between
                collapsingToolbarLayout.setTitle("");
            }
        });

        Glide.with(ViewProfileActivity.this).load(Uri.parse(profilePicture)).placeholder(R.drawable.avatar).into(profilePic);
        fullName.setText(name);
        addr.setText(address);
        location.setText(String.format("%s, %s", state, nationality));
        status.setText(role);
        gene.setText(genotype);
        bloodGrp.setText(bloodGroup);
        sex.setText(gender);
        zipCode.setText(postCode);

        callFab.setVisibility(View.GONE);
        chatFab.setVisibility(View.GONE);
        extendedFab.shrink();

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

        callFab.setOnClickListener(v -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

        chatFab.setOnClickListener(v -> {
            Intent chat = new Intent(Intent.ACTION_SENDTO);
            chat.setData(Uri.parse("smsto:" + phoneNumber));
            startActivity(chat);
        });
    }
}