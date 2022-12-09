package com.kapruka.UserFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.SplashActivity;
import com.kapruka.YourProblemActivity;
import com.kapruka.databinding.FragmentProfileBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    //ExtendedFloatingActionButton adminBtn;
    FragmentProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    // List<UserModel> userModelList;
    // UserAdapter userAdapter;
    //RecyclerView userRv;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(LayoutInflater.from(getContext()), container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        loadeUser();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), SplashActivity.class));
                getActivity().finish();
            }
        });

        binding.fabAskadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), YourProblemActivity.class));
                getActivity().finish();
            }
        });

        return binding.getRoot();
//        adminBtn = view.findViewById(R.id.adminpanelBtn);
//        userRv = view.findViewById(R.id.user_recycler_view);
//        AdminAddPostButtonActivetor();
//
//
//        adminBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AdminActivity.class));
//            }
//        });
//
//        userModelList = new ArrayList<>();
//        DatabaseReference diseaseRf = FirebaseDatabase.getInstance().getReference("Users");
//        diseaseRf.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userModelList.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    UserModel qusetionModel = ds.getValue(UserModel.class);
//                    if (qusetionModel != null) {
//                        userModelList.add(qusetionModel);
//                        userAdapter = new UserAdapter(userModelList, getActivity());
//                        userRv.setAdapter(userAdapter);
//                        //NotYourCity.setVisibility(View.GONE);
//                    } else {
//                        //NotYourCity.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        return view;
//    }
//
//    private void AdminAddPostButtonActivetor() {
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        DatabaseReference chakeadminRf = FirebaseDatabase.getInstance().getReference("Users");
//        chakeadminRf.child(user.getUid()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String userType = "" + snapshot.child("UserType").getValue();
//                        if (userType.equals("user")) {
//                            adminBtn.setVisibility(View.GONE);
//                        } else if (userType.equals("admin")) {
//                            adminBtn.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void loadeUser() {
        DatabaseReference userdataref = FirebaseDatabase.getInstance().getReference("Users");
        userdataref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = "" + snapshot.child("Name").getValue();
                        String email = "" + snapshot.child("Email").getValue();
                        String city = "" + snapshot.child("City").getValue();
                        String ProfilePic = "" + snapshot.child("Image").getValue();
                        String userType = "" + snapshot.child("UserType").getValue();
                        binding.usernameTv.setText(username);
                        binding.emailTv.setText(email);
                        binding.usertypeTv.setText(userType);
                        binding.cityTv.setText(city);
                        if (ProfilePic.equals("img")) {
                            ///binding.profileIv.setImageDrawable(getResources().getDrawable(R.drawable.img_profile, null));
                        } else {
                            Picasso.get().load(ProfilePic).into(binding.profileIv, new Callback() {
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