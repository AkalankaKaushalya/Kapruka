package com.kapruka.AdminFragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.kapruka.R;
import com.kapruka.UserAdapters.AdminViewPageAdapter;
import com.kapruka.UserFragments.HomeFragment;
import com.kapruka.databinding.ActivityAdminBoardBinding;

public class AdminBoardActivity extends AppCompatActivity {
    ActivityAdminBoardBinding binding;
    private FragmentManager fragmentManager;
    private ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chipNavigationBar = findViewById(R.id.navigation);


        setUpViewPage();
        if (savedInstanceState == null) {
            chipNavigationBar.setItemSelected(R.id.nav_home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_containera_admin, homeFragment).commit();  // Butoom Navigation Eka Allrady Home Fragment eka Select Wena Thana
        }
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.nav_home:
                        binding.fragmentContaineraAdmin.setCurrentItem(0);
                        //fragment = new HomeFragment();
                        break;
                    case R.id.nav_questions:
                        binding.fragmentContaineraAdmin.setCurrentItem(1);
                        //fragment = new FavaritFragment();
                        break;
                    case R.id.nav_profile:
                        binding.fragmentContaineraAdmin.setCurrentItem(2);
                        //fragment = new ProfileFragment();
                        break;
                    case R.id.nav_dashboard:
                        binding.fragmentContaineraAdmin.setCurrentItem(3);
                        //fragment = new ProfileFragment();
                        break;
                }
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                } else {
                }
            }
        });
    }

    private void setUpViewPage() {
        AdminViewPageAdapter viewPageAdapter = new AdminViewPageAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.fragmentContaineraAdmin.setAdapter(viewPageAdapter);
        binding.fragmentContaineraAdmin.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.navigation.setItemSelected(R.id.nav_home, true);
                        break;
                    case 1:
                        binding.navigation.setItemSelected(R.id.nav_questions, true);
                        break;
                    case 2:
                        binding.navigation.setItemSelected(R.id.nav_profile, true);
                        break;
                    case 3:
                        binding.navigation.setItemSelected(R.id.nav_dashboard, true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}