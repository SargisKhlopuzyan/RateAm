package com.sargis.kh.rateam.main_screen;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.databinding.ActivityMainBinding;
import com.sargis.kh.rateam.decoration_fragment.EmptyFragment;
import com.sargis.kh.rateam.main_screen.rates.RatesFragment;
import com.sargis.kh.rateam.main_screen.viewpager.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private int[] tabIcons = {
            R.drawable.transaction_selecor,
            R.drawable.rates_selecor,
            R.drawable.calculator_selecor,
            R.drawable.more_selecor
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.viewPager.setOffscreenPageLimit(4);
        setupViewPager(binding.viewPager);

        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EmptyFragment(), getResources().getString(R.string.transaction));
        adapter.addFragment(new RatesFragment(), getResources().getString(R.string.rates));
        adapter.addFragment(new EmptyFragment(), getResources().getString(R.string.calculator));
        adapter.addFragment(new EmptyFragment(), getResources().getString(R.string.more));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    private void setupTabIcons() {
        binding.tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        binding.tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        binding.tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        binding.tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

}
