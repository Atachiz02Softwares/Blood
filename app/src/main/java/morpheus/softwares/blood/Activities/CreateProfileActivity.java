package morpheus.softwares.blood.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.Models.Links;
import morpheus.softwares.blood.Models.User;
import morpheus.softwares.blood.R;

public class CreateProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 20;
    private final String[] BLOODGROUPS = new Links().getBloodGroups();
    private final String[] GENOTYPES = new Links().getGenotypes();
    private final String[] ROLES = new Links().getRoles();
    CircleImageView profilePic;
    Uri profilePicture;
    EditText name, address, state, nationality, postCode, phoneNumber;
    AutoCompleteTextView roles;
    ArrayAdapter<String> roleAdapter;
    AutoCompleteTextView bloodGroups;
    ArrayAdapter<String> bloodGroupsAdapter;
    AutoCompleteTextView genotypes;
    ArrayAdapter<String> genotypesAdapter;
    RadioGroup genderGroup;
    RadioButton male, female;
    ProgressBar progressBar;
    Button createProfile;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    StorageTask uploadTask;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        profilePic = findViewById(R.id.createProfileProfilePic);
        name = findViewById(R.id.createProfileName);
        address = findViewById(R.id.createProfileAddress);
        state = findViewById(R.id.createProfileState);
        nationality = findViewById(R.id.createProfileCountry);
        postCode = findViewById(R.id.createProfilePostCode);
        phoneNumber = findViewById(R.id.createProfilePhone);
        genderGroup = findViewById(R.id.createProfileGender);
        roles = findViewById(R.id.createProfileRole);
        bloodGroups = findViewById(R.id.createProfileBloodGroup);
        genotypes = findViewById(R.id.createProfileGenotype);
        male = findViewById(R.id.createProfileMale);
        female = findViewById(R.id.createProfileFemale);
        progressBar = findViewById(R.id.createProfileProgressBar);
        createProfile = findViewById(R.id.createProfileCreateProfile);

        checkProfileStatus(createProfile);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        roleAdapter = new ArrayAdapter<>(this, R.layout.list_items, ROLES);
        roles.setAdapter(roleAdapter);

        bloodGroupsAdapter = new ArrayAdapter<>(this, R.layout.list_items, BLOODGROUPS);
        bloodGroups.setAdapter(bloodGroupsAdapter);

        genotypesAdapter = new ArrayAdapter<>(this, R.layout.list_items, GENOTYPES);
        genotypes.setAdapter(genotypesAdapter);

        storageReference = FirebaseStorage.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });

        createProfile.setOnClickListener(v -> {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(CreateProfileActivity.this, "Account creation in progress...",
                        Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);

                if (profilePicture != null) {
                    StorageReference reference = storageReference.child(System.currentTimeMillis() +
                            "." + getFileExtension(profilePicture));
                    uploadTask = reference.putFile(profilePicture)
                            .addOnSuccessListener(taskSnapshot -> {
                                String fullName = String.valueOf(name.getText()).trim();
                                String addr = String.valueOf(address.getText()).trim();
                                String st = String.valueOf(state.getText()).trim();
                                String nation = String.valueOf(nationality.getText()).trim();
                                String postalCode = String.valueOf(postCode.getText()).trim();
                                String phone = String.valueOf(phoneNumber.getText()).trim();

                                String gender = male.isChecked() ? String.valueOf(male.getText()).trim() :
                                        female.isChecked() ? String.valueOf(female.getText()).trim() : null;

                                String role = String.valueOf(roles.getText());
                                String bloodGroup = String.valueOf(bloodGroups.getText());
                                String genotype = String.valueOf(genotypes.getText());
                                String uid = mAuth.getUid();

                                user = new User(String.valueOf(profilePicture), fullName, addr,
                                        st, nation, role, genotype, bloodGroup, postalCode, phone, gender);
                                String uploadID = databaseReference.push().getKey();

                                assert uploadID != null;
                                databaseReference.child(uploadID).setValue(user);

                                Handler handler = new Handler();
                                handler.postDelayed(() -> progressBar.setProgress(0), 500); // 0.5 seconds post delay
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(CreateProfileActivity.this, "Profile created " +
                                        "successfully!", Toast.LENGTH_LONG).show();

                                setProfileStatus();
                                createProfile.setEnabled(false);
                            }).addOnFailureListener(e -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(CreateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }).addOnProgressListener(snapshot -> {
                                double progress =
                                        100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                                progressBar.setProgress((int) progress);
                            });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateProfileActivity.this, "Please select a profile picture...",
                            Toast.LENGTH_SHORT).show();
                }
            }
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

    /**
     * Returns the registered extension for the given Uri. Note that some Uri(s) map to
     * multiple extensions. This call will return the most common extension for the given Uri.
     */
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * Saves the status of 'Create Profile' page to 'true' or 'false' whenever an account has been
     * created.
     */
    private void setProfileStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("profileStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("status", true);
        editor.apply();
    }

    /**
     * Checks the profile status of the device and enables the createProfile button or disables,
     * depending on the saved status in the specified Shared Preferences.
     */
    private void checkProfileStatus(Button button) {
        SharedPreferences sharedPreferences = getSharedPreferences("profileStatus", MODE_PRIVATE);

        boolean status = sharedPreferences.getBoolean("status", false);
        button.setEnabled(status);
    }
}