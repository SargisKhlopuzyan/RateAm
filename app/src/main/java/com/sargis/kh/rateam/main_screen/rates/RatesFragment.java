package com.sargis.kh.rateam.main_screen.rates;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.databinding.FragmentRatesBinding;
import com.sargis.kh.rateam.decoration_fragment.EmptyFragment;
import com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks.BanksFragment;
import com.sargis.kh.rateam.main_screen.viewpager.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatesFragment extends Fragment {

    private FragmentRatesBinding binding;

    public RatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rates, container, false);
        setupViewPager();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButtonBanks:
                    binding.nonSwipeableViewPager.setCurrentItem(0);
                    break;
                case R.id.radioButtonExchangePoint:
                    binding.nonSwipeableViewPager.setCurrentItem(1);
                    break;
                case R.id.radioButtonCB:
                    binding.nonSwipeableViewPager.setCurrentItem(2);
                    break;
            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new BanksFragment(),null);
        adapter.addFragment(new EmptyFragment(),null);
        adapter.addFragment(new EmptyFragment(),null);
        binding.nonSwipeableViewPager.setAdapter(adapter);
        binding.nonSwipeableViewPager.setOffscreenPageLimit(3);
    }

}