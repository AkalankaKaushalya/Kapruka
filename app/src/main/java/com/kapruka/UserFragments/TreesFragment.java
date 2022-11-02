package com.kapruka.UserFragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Adapters.MonthAdapter;
import com.kapruka.Adapters.MyTreeAdapter;
import com.kapruka.Models.MonthModel;
import com.kapruka.Models.MyTreeModel;
import com.kapruka.R;
import com.zyp.cardview.YcCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TreesFragment extends Fragment {

    RecyclerView MyTreeRc, DateRc;
    List<MyTreeModel> myTreeModelList;
    MyTreeAdapter myTreeAdapter;
    List<MonthModel> monthModelList;
    MonthAdapter monthAdapter;
    private RelativeLayout SR, RR, CocnutTab, MonthTab;
    private YcCardView CocnutCard, MonthCard;
    private TextView SaleTv, RentTv;
    private ExtendedFloatingActionButton AddCocnut, AddMonth;
    private ArrayAdapter<CharSequence> firstcat_adapter;
    private FirebaseAuth firebaseAuth;
    private LottieAnimationView lottieAnimationView, lottieAnimationView2;
    private String ages;
    private ProgressDialog progressDialog;

    public TreesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trees, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        lottieAnimationView = view.findViewById(R.id.nolottie);
        lottieAnimationView2 = view.findViewById(R.id.nolottie1);
        CocnutCard = view.findViewById(R.id.saleCv);
        MonthCard = view.findViewById(R.id.rentCv);
        SR = view.findViewById(R.id.sr);
        RR = view.findViewById(R.id.rr);
        SaleTv = view.findViewById(R.id.saleTv);
        RentTv = view.findViewById(R.id.rentTv);
        CocnutTab = view.findViewById(R.id.treeRR);
        MonthTab = view.findViewById(R.id.monthRR);
        AddCocnut = view.findViewById(R.id.addmycocount);
        AddMonth = view.findViewById(R.id.addmonth);
        MyTreeRc = view.findViewById(R.id.tree_recycler_view);
        DateRc = view.findViewById(R.id.month_recycler_view);

        CocnutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CocnutTab.setVisibility(View.VISIBLE);
                MonthTab.setVisibility(View.GONE);
                SR.setBackground(getResources().getDrawable(R.drawable.shape_home_catogry, null));
                RR.setBackgroundColor(getResources().getColor(R.color.white));
                SaleTv.setTextColor(getResources().getColor(R.color.white));
                RentTv.setTextColor(getResources().getColor(R.color.purple_500));
            }
        });

        MonthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CocnutTab.setVisibility(View.GONE);
                MonthTab.setVisibility(View.VISIBLE);
                RR.setBackground(getResources().getDrawable(R.drawable.shape_home_catogry, null));
                SR.setBackgroundColor(getResources().getColor(R.color.white));
                RentTv.setTextColor(getResources().getColor(R.color.white));
                SaleTv.setTextColor(getResources().getColor(R.color.purple_500));
            }
        });

        AddCocnut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final ProgressDialog progressDialog = new ProgressDialog(getContext());
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dilog_add_cocnut);

                EditText id = dialog.findViewById(R.id.Treeid);
                EditText height = dialog.findViewById(R.id.heightTree);
                Spinner age = dialog.findViewById(R.id.age_tree);
                Button add = dialog.findViewById(R.id.addtreeBtn);

                firstcat_adapter = ArrayAdapter.createFromResource(getContext(), R.array.array_agetree, R.layout.spinner_layout);
                firstcat_adapter.setDropDownViewResource(com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item);
                age.setAdapter(firstcat_adapter);
                age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ages = age.getSelectedItem().toString();
                        if (ages.equals("පොල් ගසේ වයස")) {
                            Toast.makeText(getContext(), "Select age of tree", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String treeid = id.getText().toString();
                        String treeheight = height.getText().toString();
                        if (treeheight.isEmpty()) {
                            Toast.makeText(getContext(), "Enter Tree height", Toast.LENGTH_SHORT).show();
                        } else if (treeid.isEmpty()) {
                            Toast.makeText(getContext(), "Enter ID", Toast.LENGTH_SHORT).show();
                        } else if (ages.equals("Select age of the coconut tree")) {
                            Toast.makeText(getContext(), "Selct Age", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.setMessage("Uploading your Terr Please Wait");
                            progressDialog.show();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("TreeID", "" + treeid);
                            hashMap.put("UserID", "" + firebaseAuth.getUid());
                            hashMap.put("TreeHeight", "" + treeheight);
                            hashMap.put("TreeAge", "" + ages);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.child(firebaseAuth.getUid()).child("MyTree").child(treeid)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Tree Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Not Tree Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });

        AddMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dilog_add_month);

                DatePicker date = dialog.findViewById(R.id.monthTv);
                Button addmonth = dialog.findViewById(R.id.addmonthBtn);

                addmonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int year = date.getYear();
                        int month = date.getMonth();
                        String date = year + "-" + month;
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Month", "" + date);
                        hashMap.put("UserID", firebaseAuth.getUid());
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                        ref.child(firebaseAuth.getUid()).child("Months").child(date)
                                .setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Month Updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Not Tree Updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });

        lodeMyTree();
        lodeMonth();

        return view;
    }

    private void lodeMyTree() {
        myTreeModelList = new ArrayList<>();
        DatabaseReference mytreeRf = FirebaseDatabase.getInstance().getReference("Users");
        mytreeRf.child(firebaseAuth.getUid()).child("MyTree").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myTreeModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MyTreeModel ytVideoModel = ds.getValue(MyTreeModel.class);
                    if (ytVideoModel != null) {
                        myTreeModelList.add(ytVideoModel);
                        myTreeAdapter = new MyTreeAdapter(myTreeModelList, getActivity());
                        MyTreeRc.setAdapter(myTreeAdapter);
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

    private void lodeMonth() {
        monthModelList = new ArrayList<>();
        DatabaseReference mytreeRf = FirebaseDatabase.getInstance().getReference("Users");
        mytreeRf.child(firebaseAuth.getUid()).child("Months").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                monthModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MonthModel monthModel = ds.getValue(MonthModel.class);
                    if (monthModel != null) {
                        monthModelList.add(monthModel);
                        monthAdapter = new MonthAdapter(monthModelList, getActivity());
                        DateRc.setAdapter(monthAdapter);
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
}