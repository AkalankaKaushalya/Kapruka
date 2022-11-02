package com.kapruka.Test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.kapruka.databinding.RowUserBinding;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    public List<UserModel> userModelList;
    RowUserBinding binding;
    Context context;
    FirebaseAuth firebaseAuth;

    public UserAdapter(List<UserModel> userModelList, Context context) {
        this.userModelList = userModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowUserBinding.inflate(LayoutInflater.from(context), parent, false);
        return new UserHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        UserModel ytVideoModel = userModelList.get(position);
        String userid = ytVideoModel.getUserID();
        String username = ytVideoModel.getName();
        String usertype = ytVideoModel.getUserType();

        holder.UserNameTv.setText(username);
        holder.UserTypeTv.setText(usertype);
//        holder.Delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ProgressDialog progressDialog = new ProgressDialog(context);
//                progressDialog.setMessage("Deleting...");
//                progressDialog.show();
//                Query fquery = FirebaseDatabase.getInstance().getReference("Users")
//                        .child(firebaseAuth.getUid())
//                        .child("Productivity").orderByChild("Month").equalTo(id);
//                fquery.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds: dataSnapshot.getChildren()){
//                            ds.getRef().removeValue();
//                        }
//                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, PodactivityActivity.class);
//                intent.putExtra("UserID", userid);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView UserNameTv, UserTypeTv;
        CircleImageView ProfileImg;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            UserNameTv = binding.usernameTv;
            UserTypeTv = binding.usertypeTv;
            ProfileImg = binding.profileIv;
        }
    }
}
