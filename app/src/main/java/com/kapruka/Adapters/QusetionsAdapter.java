package com.kapruka.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kapruka.Models.PostCommnetsModel;
import com.kapruka.Models.QusetionModel;
import com.kapruka.R;
import com.kapruka.ShowCityActivity;
import com.kapruka.SingelImageViewActivity;
import com.kapruka.SingelVideoViewActivity;
import com.kapruka.databinding.RowQusetionpostBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class QusetionsAdapter extends RecyclerView.Adapter<QusetionsAdapter.QusetionsHolder> {

    public List<QusetionModel> qusetionModelList;
    RowQusetionpostBinding binding;
    FirebaseAuth firebaseAuth;
    Context context;
    SimpleExoPlayer exoPlayer;
    int likecount;
    String Usertype;
    List<PostCommnetsModel> postCommnetsModels;
    PostCommnentsAdapter postCommnentsAdapter;
    private Boolean testclick = false;


    public QusetionsAdapter(List<QusetionModel> qusetionModelList, Context context) {
        this.qusetionModelList = qusetionModelList;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public QusetionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowQusetionpostBinding.inflate(LayoutInflater.from(context), parent, false);
        return new QusetionsHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull QusetionsHolder holder, int position) {
        QusetionModel qusetionModel = qusetionModelList.get(position);
        String qusetionid = qusetionModel.getQusetionID();
        String userid = qusetionModel.getUserID();
        String title = qusetionModel.getQusetionTitle();
        String image = qusetionModel.getQusetionImage();
        String video = qusetionModel.getQusetionVideo();
        String city = qusetionModel.getCity();

        holder.Title.setText(title);
        holder.CityTv.setText(city);

        if (image.equals("img")) {
            holder.Iamge.setVisibility(View.GONE);
        } else {
            holder.Iamge.setVisibility(View.VISIBLE);
            Picasso.get().load(image).into(holder.Iamge);
        }

        if (video.equals("video")) {
            holder.videoView.setVisibility(View.GONE);
        } else {
            holder.videoView.setVisibility(View.VISIBLE);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(context).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context);
            Uri videoo = Uri.parse(video);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("Qusetion");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoo, dataSourceFactory, extractorsFactory, null, null);
            binding.videoVv.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        }

        DatabaseReference userdataref = FirebaseDatabase.getInstance().getReference("Users");
        userdataref.child(userid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = "" + snapshot.child("Name").getValue();
                        String ProfilePic = "" + snapshot.child("Image").getValue();
                        holder.UserName.setText(username);
                        if (ProfilePic.equals("img")) {
                            holder.ProfileIv.setImageDrawable(context.getResources().getDrawable(R.drawable.img_profile, null));
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        DatabaseReference userdattaref = FirebaseDatabase.getInstance().getReference("Users");
        userdattaref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Usertype = "" + snapshot.child("UserType").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(qusetionid));
        String data = DateFormat.format("dd/MM/yyyy", calendar).toString();
        holder.Date.setText(data);

        holder.Iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingelImageViewActivity.class);
                intent.putExtra("ImageUrl", image);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingelVideoViewActivity.class);
                intent.putExtra("VideoUrl", video);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        holder.getlikebuttonstatus(qusetionid, firebaseAuth.getUid());
        holder.LikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testclick = true;
                DatabaseReference likereference = FirebaseDatabase.getInstance().getReference("PostLikes");
                likereference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (testclick == true) {
                            if (snapshot.child(qusetionid).hasChild(firebaseAuth.getUid())) {
                                likereference.child(qusetionid).child(firebaseAuth.getUid()).removeValue();
                                testclick = false;
                            } else {
                                likereference.child(qusetionid).child(firebaseAuth.getUid()).setValue(true);
                                testclick = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        holder.Comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_post_comment);

                RecyclerView CommnetRc = dialog.findViewById(R.id.commentRcv);
                EditText Commnet = dialog.findViewById(R.id.commnetEt);
                ImageButton sendBtn = dialog.findViewById(R.id.commnetsendBtn);
                TextView LikeCount = dialog.findViewById(R.id.likeCountTv);
                ImageButton LikeBtn = dialog.findViewById(R.id.likeBtn);
                int clikecount;


                lodeCommmnet(qusetionid, CommnetRc);
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                getCommnetlikebuttonstatus(qusetionid, firebaseAuth.getUid(), LikeCount, LikeBtn);

                LikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testclick = true;
                        DatabaseReference likereference = FirebaseDatabase.getInstance().getReference("PostLikes");
                        likereference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (testclick == true) {
                                    if (snapshot.child(qusetionid).hasChild(firebaseAuth.getUid())) {
                                        likereference.child(qusetionid).child(firebaseAuth.getUid()).removeValue();
                                        testclick = false;
                                    } else {
                                        likereference.child(qusetionid).child(firebaseAuth.getUid()).setValue(true);
                                        testclick = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });

                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String commnetText = Commnet.getText().toString();
                        if (commnetText.isEmpty()) {

                        } else {
                            String timestamp = "" + System.currentTimeMillis();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("CommnetID", "" + timestamp);
                            hashMap.put("PostID", "" + qusetionid);
                            hashMap.put("TimesTamp", "" + timestamp);
                            hashMap.put("Comment", "" + commnetText);
                            hashMap.put("UserID", "" + firebaseAuth.getUid());

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Qusetion");
                            ref.child(qusetionid).child("Comments").child(timestamp)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Commnet.setText(null);
                                            Toast.makeText(context, "Commnent added..", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Failed Commnent " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

        holder.CityTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowCityActivity.class);
                intent.putExtra("QCity", city);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.MoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.MoreBtn, Gravity.END);

                    if (userid.equals(firebaseAuth.getUid())) {
                        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
                        //popupMenu.getMenu().add(Menu.NONE,1,0,"Edite");
                    } else if (Usertype.equals("admin")) {
                        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int id = menuItem.getItemId();
                            if (id == 0) {
                                // Delete Post
                                if (userid.equals(firebaseAuth.getUid()) || Usertype.equals("admin")) {
                                    beginDelete(qusetionid, image, video);
                                } else {
                                    Toast.makeText(context, "Your Not Uplode this Post \n You Can't Delete it", Toast.LENGTH_SHORT).show();
                                }

                            }
                            return false;
                        }
                    });
                    popupMenu.show();


                }
            }
        });


    }

    private void beginDelete(String qusetionid, String image, String video) {
        if (!image.equals("img") && !video.equals("video")) {
            deleteImgeVideo(qusetionid, image, video);
        } else if (!image.equals("img") && video.equals("video")) {
            deleteImge(qusetionid, image);
        } else if (image.equals("img") && !video.equals("video")) {
            deleteVideo(qusetionid, video);
        } else {
            deleteText(qusetionid);
        }
    }

    private void deleteText(String qusetionid) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        Query fquery = FirebaseDatabase.getInstance().getReference("Qusetion").orderByChild("QusetionID").equalTo(qusetionid);
        fquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteVideo(String qusetionid, String video) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(video);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query fquery = FirebaseDatabase.getInstance().getReference("Qusetion").orderByChild("QusetionID").equalTo(qusetionid);
                        fquery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ds.getRef().removeValue();
                                }
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteImge(String qusetionid, String image) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(image);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query fquery = FirebaseDatabase.getInstance().getReference("Qusetion").orderByChild("QusetionID").equalTo(qusetionid);
                        fquery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ds.getRef().removeValue();
                                }
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteImgeVideo(String qusetionid, String image, String video) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(image);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        StorageReference videoRef = FirebaseStorage.getInstance().getReferenceFromUrl(video);
                        videoRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Query fquery = FirebaseDatabase.getInstance().getReference("Qusetion").orderByChild("QusetionID").equalTo(qusetionid);
                                        fquery.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    ds.getRef().removeValue();
                                                }
                                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return qusetionModelList.size();
    }

    private void getCommnetlikebuttonstatus(String postid, String uid, TextView likeCount, ImageButton likeBtn) {
        DatabaseReference likereference = FirebaseDatabase.getInstance().getReference("PostLikes");
        likereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postid).hasChild(uid)) {
                    likecount = (int) snapshot.child(postid).getChildrenCount();
                    likeCount.setText(likecount + " likes");
                    likeBtn.setImageResource(R.drawable.ic_tik_like);
                } else {
                    likecount = (int) snapshot.child(postid).getChildrenCount();
                    likeCount.setText(likecount + " likes");
                    likeBtn.setImageResource(R.drawable.ic_like_boder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void lodeCommmnet(String postid, RecyclerView commnetRc) {
        postCommnetsModels = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Qusetion");
        ref.child(postid).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postCommnetsModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PostCommnetsModel postCommnetsModel = ds.getValue(PostCommnetsModel.class);
                    postCommnetsModels.add(postCommnetsModel);
                }
                postCommnentsAdapter = new PostCommnentsAdapter(postCommnetsModels, context);
                commnetRc.setAdapter(postCommnentsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    class QusetionsHolder extends RecyclerView.ViewHolder {
        TextView Title, UserName, Date, LikecountTv, CityTv;
        CircleImageView ProfileIv;
        ImageView Iamge;
        PlayerView videoView;
        ImageButton LikeBtn, Comments, MoreBtn;

        public QusetionsHolder(@NonNull View itemView) {
            super(itemView);
            Title = binding.captionsTv;
            CityTv = binding.cityTv;
            UserName = binding.usernameTv;
            Date = binding.dateTv;
            ProfileIv = binding.profileIv;
            Iamge = binding.captionsimageIv;
            videoView = binding.videoVv;
            MoreBtn = binding.moreBtn;

            LikecountTv = binding.likeCountTv;
            LikeBtn = binding.likeBtn;
            Comments = binding.commentBtn;

        }

        public void getlikebuttonstatus(final String postkey, final String userid) {
            DatabaseReference likereference = FirebaseDatabase.getInstance().getReference("PostLikes");
            likereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postkey).hasChild(userid)) {
                        likecount = (int) snapshot.child(postkey).getChildrenCount();
                        LikecountTv.setText(likecount + " likes");
                        LikeBtn.setImageResource(R.drawable.ic_tik_like);
                    } else {
                        likecount = (int) snapshot.child(postkey).getChildrenCount();
                        LikecountTv.setText(likecount + " likes");
                        LikeBtn.setImageResource(R.drawable.ic_like_boder);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }


}
