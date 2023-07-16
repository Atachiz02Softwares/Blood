package morpheus.softwares.blood.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.Adapters.UserAdapter;
import morpheus.softwares.blood.Models.Links;
import morpheus.softwares.blood.Models.User;
import morpheus.softwares.blood.R;

public class MainActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView name, email, navName, navPostCode, navAddress, navBloodGroup;
    CircleImageView profilePicture, navProfilePicture;
    EditText search;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    View header;
    AlertDialog alertDialog;
    MaterialAlertDialogBuilder builder;
    Button aboutButton;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    String databaseReference;
    String currentUserId;

    ArrayList<User> users;
    UserAdapter userAdapter;
    RecyclerView recyclerView;

    String currentUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = findViewById(R.id.mainAppBar);
        collapsingToolbarLayout = findViewById(R.id.mainCollapsingToolnar);
        toolbar = findViewById(R.id.homeToolbar);
        profilePicture = findViewById(R.id.homeProfilePic);
        name = findViewById(R.id.homeName);
        email = findViewById(R.id.homeEmail);
        search = findViewById(R.id.homeSearchView);
        drawerLayout = findViewById(R.id.mainDrawer);
        navigationView = findViewById(R.id.mainNavigator);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        actionBarDrawerToggle.syncState();

//        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedTitle);

        // NavigationView items
        header = navigationView.getHeaderView(0);
        navProfilePicture = header.findViewById(R.id.navProfilePic);
        navName = header.findViewById(R.id.navName);
        navAddress = header.findViewById(R.id.navAddress);
        navPostCode = header.findViewById(R.id.navPostCode);
        navBloodGroup = header.findViewById(R.id.navBloodGroup);

        users = new ArrayList<>();
        recyclerView = findViewById(R.id.list);
        userAdapter = new UserAdapter(this, users);
        recyclerView.setHasFixedSize(true);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = new Links().getDatabaseReference();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);

        currentUserId = user.getUid();

        // Load currently signed in user
        if (user != null) {
            String mail = user.getEmail();

            database.getReference().child(databaseReference).child(currentUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Retrieve user data from the dataSnapshot
                            User user = dataSnapshot.getValue(User.class);

                            // Update the UI with user data
                            if (user != null) {
                                currentUserRole = String.valueOf(user.getRole());
                                String userName = String.valueOf(user.getName()).trim();

                                Glide.with(MainActivity.this).load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(profilePicture);
                                Glide.with(MainActivity.this).load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(navProfilePicture);

                                // Navigation views
                                navName.setText(userName);
                                navAddress.setText(user.getAddress());
                                navPostCode.setText(user.getPostCode());
                                navBloodGroup.setText(user.getBloodGroup());

                                // Toolbar views
                                name.setText(userName);
                                email.setText(mail);

                                appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                                    // Check if the collapsing toolbar is fully collapsed
                                    if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                                        // Collapsed state
                                        name.setVisibility(View.GONE);
                                        email.setVisibility(View.GONE);
                                        collapsingToolbarLayout.setTitle(userName);
                                    } else {
                                        // Expanded state or in-between
                                        name.setVisibility(View.VISIBLE);
                                        email.setVisibility(View.VISIBLE);
                                        collapsingToolbarLayout.setTitle("");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, databaseError.getDetails(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            name.setText(R.string.create_profile);
            navName.setText(R.string.create_profile);
        }

        // Load Recycler view with users data
        database.getReference().child(databaseReference).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    // Filter users to display with respect to current user's role...
                    assert user != null;
                    if (!currentUserRole.equals(user.getRole())) users.add(user);
                    else users.add(user);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
//        if (signInAccount != null) {
//            String userName = String.valueOf(signInAccount.getDisplayName()).trim();
//            String mail = String.valueOf(signInAccount.getEmail()).trim();
//
//            Glide.with(getApplicationContext()).load(signInAccount.getPhotoUrl()).placeholder(R.drawable.avatar).into(profilePicture);
//            Glide.with(getApplicationContext()).load(signInAccount.getPhotoUrl()).placeholder(R.drawable.avatar).into(navProfilePicture);
//
//            name.setText(userName);
//            navName.setText(userName);
//            email.setText(mail);
//        } else {
//              name.setText(R.string.create_profile);
//              navName.setText(R.string.create_profile);
//        }

        search.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(MainActivity.this,
                        "Searching for " + String.valueOf(search.getText()).trim() + "...",
                        Toast.LENGTH_LONG).show();
                handled = true;
            }
            return handled;
        });

        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    /**
     * onNavigationItemSelected extracted method reference
     */
    private boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.createProfile)
            if (String.valueOf(name.getText()).isEmpty())
                startActivity(new Intent(MainActivity.this, CreateProfileActivity.class));
            else
                Toast.makeText(MainActivity.this, "Sorry! You can't create multiple accounts." +
                        "..", Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.viewProfile)
            if (user != null) {
                String currentUserId = user.getUid();

                database.getReference().child("Users").child(currentUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Retrieve user data from the dataSnapshot
                                User user = dataSnapshot.getValue(User.class);

                                // Update the UI with user data
                                if (user != null) {
                                    String profilePicture = user.getProfilePicture(),
                                            name = user.getName(),
                                            address = user.getAddress(),
                                            state = user.getState(),
                                            nationality = user.getNationality(),
                                            role = user.getRole(),
                                            genotype = user.getGenotype(),
                                            bloodGroup = user.getBloodGroup(),
                                            gender = user.getGender(),
                                            postCode = user.getPostCode(),
                                            phoneNumber = user.getPhoneNumber();

                                    Intent viewProfile = new Intent(MainActivity.this,
                                            ViewProfileActivity.class);
                                    viewProfile.putExtra("profilePicture", profilePicture);
                                    viewProfile.putExtra("name", name);
                                    viewProfile.putExtra("address", address);
                                    viewProfile.putExtra("state", state);
                                    viewProfile.putExtra("nationality", nationality);
                                    viewProfile.putExtra("role", role);
                                    viewProfile.putExtra("genotype", genotype);
                                    viewProfile.putExtra("bloodGroup", bloodGroup);
                                    viewProfile.putExtra("gender", gender);
                                    viewProfile.putExtra("postCode", postCode);
                                    viewProfile.putExtra("phoneNumber", phoneNumber);
                                    startActivity(viewProfile);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, databaseError.getDetails(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(MainActivity.this, "Please, create a profile first...",
                        Toast.LENGTH_SHORT).show();
            }
        else if (item.getItemId() == R.id.about) {
            builder = new MaterialAlertDialogBuilder(MainActivity.this,
                    R.style.MaterialAlertDialogRounded);

            // Inflate custom_dialog
            View view = getLayoutInflater().inflate(R.layout.about_dialog, null);
            aboutButton = view.findViewById(R.id.aboutButton);

            // Close the AlertDialog on button click
            aboutButton.setOnClickListener(v -> alertDialog.dismiss());

            // Set view to dialog
            builder.setView(view);

            // Create dialog
            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else if (item.getItemId() == R.id.exit) finishAffinity();

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}