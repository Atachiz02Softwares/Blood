package morpheus.softwares.blood.Adapters;

import android.content.Context;
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

        holder.NAME.setText(user.getName());
        holder.ADDRESS.setText(user.getAddress());
        holder.GENDER.setText(user.getGender());
        holder.ROLE.setText(user.getRole());
        holder.BLOODGROUP.setText(user.getBloodGroup());

        Glide.with(context).load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(holder.PROFILEPICTURE);

        holder.itemView.setOnClickListener(v -> {
        });

        holder.CALL.setOnClickListener(v -> {
        });

        holder.CHAT.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
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