package morpheus.softwares.blood.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import morpheus.softwares.blood.Adapters.UserAdapter;
import morpheus.softwares.blood.Models.User;
import morpheus.softwares.blood.R;

public class MainActivity extends AppCompatActivity {
    TextView name, email, navigationName;
    EditText search;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
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

        name = findViewById(R.id.homeName);
        navigationName = findViewById(R.id.navName);
        email = findViewById(R.id.homeEmail);
        toolbar = findViewById(R.id.homeToolbar);
        search = findViewById(R.id.homeSearchView);
        drawerLayout = findViewById(R.id.mainDrawer);
        navigationView = findViewById(R.id.mainNavigator);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.list);
        userAdapter = new UserAdapter(this, users);

        recyclerView.setHasFixedSize(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        actionBarDrawerToggle.syncState();
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (signInAccount != null) {
            String userName = String.valueOf(signInAccount.getDisplayName()).trim();
            String mail = String.valueOf(signInAccount.getEmail()).trim();

            if (userName.equals("null") || userName.isEmpty()) {
                name.setText(mail);
                navigationName.setText(mail);
            } else {
                name.setText(userName);
                navigationName.setText(userName);
            }
            email.setText(mail);
        }

        user = mAuth.getCurrentUser();

        if (user != null) {
            String userName = String.valueOf(user.getDisplayName()).trim();
            String mail = String.valueOf(user.getEmail()).trim();

            if (userName.equals("null") || userName.isEmpty()) {
                name.setText(mail);
//                navigationName.setText(mail);
            } else {
                name.setText(userName);
//                navigationName.setText(userName);
            }
            email.setText(mail);
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
            else if (item.getItemId() == R.id.beADonor)
                Toast.makeText(MainActivity.this, "Be a donor", Toast.LENGTH_SHORT).show();
            else if (item.getItemId() == R.id.findDonor)
                Toast.makeText(MainActivity.this, "Find a donor", Toast.LENGTH_SHORT).show();
            else if (item.getItemId() == R.id.exit) finishAffinity();

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }
}