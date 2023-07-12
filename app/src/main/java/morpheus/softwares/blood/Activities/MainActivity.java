package morpheus.softwares.blood.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import morpheus.softwares.blood.Models.User;
import morpheus.softwares.blood.R;

public class MainActivity extends AppCompatActivity {
    TextView name, email, navName, navPostCode, navAddress, navBloodGroup;
    CircleImageView profilePicture, navProfilePicture;
    EditText search;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    View header;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;

    ArrayList<User> users;
    UserAdapter userAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profilePicture = findViewById(R.id.homeProfilePic);
        name = findViewById(R.id.homeName);
        email = findViewById(R.id.homeEmail);
        toolbar = findViewById(R.id.homeToolbar);
        search = findViewById(R.id.homeSearchView);
        drawerLayout = findViewById(R.id.mainDrawer);
        navigationView = findViewById(R.id.mainNavigator);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        actionBarDrawerToggle.syncState();
        setSupportActionBar(toolbar);

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("profileStatus", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "Blood Bank");

        if (role.equals("Donor"))
            role = "Recipient";
        else if (role.equals("Recipient"))
            role = "Donor";
        else role = "Blood Bank";

        database.getReference().child("Users").child(role).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);

                    assert user != null;

                    Glide.with(getApplicationContext()).load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(profilePicture);
                    Glide.with(getApplicationContext()).load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(navProfilePicture);

                    name.setText(user.getName());
                    navName.setText(user.getName());
                    navAddress.setText(user.getAddress());
                    navPostCode.setText(user.getPostCode());
                    navBloodGroup.setText(user.getBloodGroup());
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
//            if (userName.equals("null") || userName.isEmpty()) {
//                name.setText(mail);
//                navName.setText(mail);
//            } else {
//                name.setText(userName);
//                navName.setText(userName);
//            }
//            email.setText(mail);
//        }

        user = mAuth.getCurrentUser();

        if (user != null) {
            String currentUserId = user.getUid();
            String mail = user.getEmail();

            database.getReference().child("Users").child(role).child(currentUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Retrieve user data from the dataSnapshot
                            User user = dataSnapshot.getValue(User.class);

                            // Update the UI with user data
                            if (user != null) {
                                String userName = String.valueOf(user.getName()).trim();

                                Glide.with(getApplicationContext()).load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(profilePicture);
                                Glide.with(getApplicationContext()).load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(navProfilePicture);

                                if (userName.equals("null") || userName.isEmpty()) {
                                    navName.setText("Create profile...");
                                } else {
                                    navName.setText(userName);
                                }
                                
                                navAddress.setText(user.getAddress());
                                email.setText(mail);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                        }
                    });
        }

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

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.createProfile)
                startActivity(new Intent(MainActivity.this, CreateProfileActivity.class));
            else if (item.getItemId() == R.id.viewProfile)
                startActivity(new Intent(MainActivity.this, ViewProfileActivity.class));
            else if (item.getItemId() == R.id.about)
                Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
            else if (item.getItemId() == R.id.exit) finishAffinity();

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }
}