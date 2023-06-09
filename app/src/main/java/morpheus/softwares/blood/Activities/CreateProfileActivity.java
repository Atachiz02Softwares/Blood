package morpheus.softwares.blood.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.Models.Links;
import morpheus.softwares.blood.R;

public class CreateProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 20;
    private final String[] BLOODGROUPS = new Links().getBloodGroups();
    private final String[] GENOTYPES = new Links().getGenotypes();
    private final String[] ROLES = new Links().getRoles();
    CircleImageView profilePic;
    Uri profilePicture;
    EditText name, address, nationality, postCode, phoneNumber;
    AutoCompleteTextView roles;
    ArrayAdapter<String> roleAdapter;
    AutoCompleteTextView bloodGroups;
    ArrayAdapter<String> bloodGroupsAdapter;
    AutoCompleteTextView genotypes;
    ArrayAdapter<String> genotypesAdapter;
    RadioGroup genderGroup;
    RadioButton male, female;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        profilePic = findViewById(R.id.createProfileProfilePic);
        name = findViewById(R.id.createProfileName);
        address = findViewById(R.id.createProfileAddress);
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

//        String man = String.valueOf(male.getText());
//        String woman = String.valueOf(female.getText());

        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });

        roleAdapter = new ArrayAdapter<>(this, R.layout.list_items, ROLES);
        roles.setAdapter(roleAdapter);

        bloodGroupsAdapter = new ArrayAdapter<>(this, R.layout.list_items, BLOODGROUPS);
        bloodGroups.setAdapter(bloodGroupsAdapter);

        genotypesAdapter = new ArrayAdapter<>(this, R.layout.list_items, GENOTYPES);
        genotypes.setAdapter(genotypesAdapter);
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