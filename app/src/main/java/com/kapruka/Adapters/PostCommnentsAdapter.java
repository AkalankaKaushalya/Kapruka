package com.kapruka.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Models.PostCommnetsModel;
import com.kapruka.R;
import com.kapruka.databinding.RowCommnetsBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostCommnentsAdapter extends RecyclerView.Adapter<PostCommnentsAdapter.PostCommnetsHolder> {
    public List<PostCommnetsModel> postCommnetsModelList;
    RowCommnetsBinding binding;

    Context context;
    int likecount;
    private final Boolean testclick = false;

    public PostCommnentsAdapter(List<PostCommnetsModel> postCommnetsModelList, Context context) {
        this.postCommnetsModelList = postCommnetsModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostCommnetsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCommnetsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PostCommnetsHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommnetsHolder holder, int position) {
        PostCommnetsModel codeModel = postCommnetsModelList.get(position);
        String commnetid = codeModel.getCommnetID();
        String postid = codeModel.getPostID();
        String userid = codeModel.getUserID();
        String commnet = codeModel.getComment();
        String timestamp = codeModel.getTimesTamp();

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timestamp));

        String data = DateFormat.format("yyyy M dd - hh:mm", calendar).toString();


        holder.CommnetTv.setText(commnet);
        holder.CommnetTime.setText(data);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(userid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String UserName = "" + snapshot.child("Name").getValue();
                        String ProfilePic = "" + snapshot.child("Image").getValue();
                        String userType = "" + snapshot.child("UserType").getValue();
                        holder.UserNameTv.setText(UserName);
                        if (ProfilePic.equals("img")) {
                            Picasso.get().load(String.valueOf(context.getResources().getDrawable(R.drawable.img_profile, null))).into(holder.ProfileIv);
                        } else {
                            Picasso.get().load(ProfilePic).into(holder.ProfileIv, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                        }

                        if (userType.equals("user")) {
                            holder.DasicnationTv.setTextColor(context.getResources().getColor(R.color.green_200));
                            holder.DasicnationTv.setText("Farmers");
                        } else {
                            holder.DasicnationTv.setTextColor(context.getResources().getColor(R.color.yello_200));
                            holder.DasicnationTv.setText("Officers");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


    }

    @Override
    public int getItemCount() {
        return postCommnetsModelList.size();
    }


    class PostCommnetsHolder extends RecyclerView.ViewHolder {
        TextView UserNameTv, CommnetTime, CommnetTv, DasicnationTv, LikeCount;
        CircleImageView ProfileIv;
        ImageButton LikeBtn;

        public PostCommnetsHolder(@NonNull View itemView) {
            super(itemView);
            UserNameTv = binding.usernameTv;
            ProfileIv = binding.profileIv;
            CommnetTv = binding.commnetTv;
            CommnetTime = binding.timeTv;
            DasicnationTv = binding.decicnationTv;

        }

//        public void getlikebuttonstatus(final String postkey, final String userid) {
//            DatabaseReference likereference = FirebaseDatabase.getInstance().getReference("PostLikes");
//            likereference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child(postkey).hasChild(userid)) {
//                        likecount = (int) snapshot.child(postkey).getChildrenCount();
//                        LikeCount.setText(likecount + " likes");
//                        LikeBtn.setImageResource(R.drawable.ic_tik_like);
//                    } else {
//                        likecount = (int) snapshot.child(postkey).getChildrenCount();
//                        LikeCount.setText(likecount + " likes");
//                        LikeBtn.setImageResource(R.drawable.ic_like_boder);
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }

    }

}
