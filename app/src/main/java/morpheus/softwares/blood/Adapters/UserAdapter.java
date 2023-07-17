package morpheus.softwares.blood.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import morpheus.softwares.blood.Activities.ViewProfileActivity;
import morpheus.softwares.blood.Models.User;
import morpheus.softwares.blood.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> {
    Context context;
    ArrayList<User> users;

    public UserAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.users = arrayList;
    }

    @NonNull
    @Override
    public UserAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_view, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.Holder holder, int position) {
        User user = users.get(position);

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

        holder.NAME.setText(name);
        holder.ADDRESS.setText(address);
        holder.GENDER.setText(gender);
        holder.ROLE.setText(role);
        holder.BLOODGROUP.setText(bloodGroup);

        Glide.with(context).load(profilePicture).placeholder(R.drawable.avatar).into(holder.PROFILEPICTURE);

        holder.itemView.setOnClickListener(v -> {
            Intent viewProfile = new Intent(context, ViewProfileActivity.class);
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
            context.startActivity(viewProfile);
        });

        holder.CALL.setOnClickListener(v -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(call);
        });

        holder.CHAT.setOnClickListener(v -> {
            Intent chat = new Intent(Intent.ACTION_SENDTO);
            chat.setData(Uri.parse("smsto:" + phoneNumber));
            context.startActivity(chat);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * Filter out searched word from list and display onTextChanged...
     */
    @SuppressLint("NotifyDataSetChanged")
    public void filter(ArrayList<User> filteredUsers) {
        this.users = filteredUsers;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private final CircleImageView PROFILEPICTURE;
        private final TextView NAME, ADDRESS, GENDER, ROLE, BLOODGROUP;
        private final ImageView CALL, CHAT;

        public Holder(@NonNull View itemView) {
            super(itemView);

            PROFILEPICTURE = itemView.findViewById(R.id.mainProfilePic);
            NAME = itemView.findViewById(R.id.mainName);
            ADDRESS = itemView.findViewById(R.id.mainAddress);
            GENDER = itemView.findViewById(R.id.mainGender);
            ROLE = itemView.findViewById(R.id.mainRole);
            BLOODGROUP = itemView.findViewById(R.id.mainBloodGroup);
            CALL = itemView.findViewById(R.id.mainCall);
            CHAT = itemView.findViewById(R.id.mainChat);
        }
    }
}