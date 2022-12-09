package com.kapruka;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.kapruka.UserAdapters.UserViewPageAdapter;
import com.kapruka.UserFragments.HomeFragment;
import com.kapruka.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chipNavigationBar = findViewById(R.id.navigation);
        setUpViewPage();
        if (savedInstanceState == null) {
            chipNavigationBar.setItemSelected(R.id.nav_home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();  // Butoom Navigation Eka Allrady Home Fragment eka Select Wena Thana
        }
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.nav_home:
                        binding.fragmentContainer.setCurrentItem(0);
                        //fragment = new HomeFragment();
                        break;
                    case R.id.nav_questions:
                        binding.fragmentContainer.setCurrentItem(1);
                        //fragment = new FavaritFragment();
                        break;

                    case R.id.nav_profile:
                        binding.fragmentContainer.setCurrentItem(2);
                        //fragment = new ProfileFragment();
                        break;

//                    case R.id.nav_shop:
//                        binding.fragmentContainer.setCurrentItem(3);
//                        //fragment = new ProfileFragment();
//                        break;

                    case R.id.nav_trre:
                        binding.fragmentContainer.setCurrentItem(3);
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
        UserViewPageAdapter viewPageAdapter = new UserViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.fragmentContainer.setAdapter(viewPageAdapter);
        binding.fragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

//                    case 3:
//                        binding.navigation.setItemSelected(R.id.nav_shop, true);

                    case 3:
                        binding.navigation.setItemSelected(R.id.nav_trre, true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}