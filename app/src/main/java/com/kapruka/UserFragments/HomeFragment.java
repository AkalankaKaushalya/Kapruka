package com.kapruka.UserFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Adapters.DiseaseAdapter;
import com.kapruka.Adapters.YtVideoAdapter;
import com.kapruka.Models.DiseaseModel;
import com.kapruka.Models.YtVideoModel;
import com.kapruka.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    List<DiseaseModel> diseaseModelList;
    DiseaseAdapter diseaseAdapter;

    List<YtVideoModel> ytVideoModelList;
    YtVideoAdapter ytVideoAdapter;
    private RecyclerView DiseaseRv, YtVideosRv;
    private FirebaseAuth firebaseAuth;
    private TextView Username;
    private CircleImageView ProfileIv;
    private LottieAnimationView lottieAnimationView, lottieAnimationView2;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        DiseaseRv = view.findViewById(R.id.disease_recycler_view);
        YtVideosRv = view.findViewById(R.id.ytvideos_recycler_view);
        lottieAnimationView = view.findViewById(R.id.nolottie);
        lottieAnimationView2 = view.findViewById(R.id.nolottie1);
        Username = view.findViewById(R.id.usernameTv);
        ProfileIv = view.findViewById(R.id.profileIv);
        lodeDisease();
        lodeYtVideo();
        loadeUser();

        return view;
    }

    private void lodeDisease() {
        diseaseModelList = new ArrayList<>();
        DatabaseReference diseaseRf = FirebaseDatabase.getInstance().getReference("Disease");
        diseaseRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diseaseModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DiseaseModel diseaseModel = ds.getValue(DiseaseModel.class);
                    if (diseaseModel != null) {
                        diseaseModelList.add(diseaseModel);
                        diseaseAdapter = new DiseaseAdapter(diseaseModelList, getActivity());
                        DiseaseRv.setAdapter(diseaseAdapter);
                        lottieAnimationView.setVisibility(View.GONE);
                    } else {
                        lottieAnimationView.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lodeYtVideo() {
        ytVideoModelList = new ArrayList<>();
        DatabaseReference ytvideoRf = FirebaseDatabase.getInstance().getReference("YtVideos");
        ytvideoRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ytVideoModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    YtVideoModel ytVideoModel = ds.getValue(YtVideoModel.class);
                    if (ytVideoModel != null) {
                        ytVideoModelList.add(ytVideoModel);
                        ytVideoAdapter = new YtVideoAdapter(ytVideoModelList, getActivity());
                        YtVideosRv.setAdapter(ytVideoAdapter);
                        lottieAnimationView2.setVisibility(View.GONE);
                    } else {
                        lottieAnimationView2.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadeUser() {
        DatabaseReference userdataref = FirebaseDatabase.getInstance().getReference("Users");
        userdataref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = "" + snapshot.child("Name").getValue();
                        String ProfilePic = "" + snapshot.child("Image").getValue();
                        Username.setText("Hello " + username);
                        if (ProfilePic.equals("img")) {
                            //ProfileIv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.img_profile, null));
                        } else {
                            Picasso.get().load(ProfilePic).into(ProfileIv, new Callback() {
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
    }
}