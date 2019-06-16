package com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.databinding.FragmentBanksBinding;
import com.sargis.kh.rateam.dialogs.ErrorMessageDialogFragment;
import com.sargis.kh.rateam.enums.CurrencyTypeEnum;
import com.sargis.kh.rateam.enums.ExchangeTypeEnum;
import com.sargis.kh.rateam.enums.SortOrderEnum;
import com.sargis.kh.rateam.enums.SortTypeEnum;
import com.sargis.kh.rateam.helpers.FilterHelperOrganization;
import com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks.adapter.BanksAdapter;
import com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks.dialog.curency_dialog.CurrencyListDialogFragment;
import com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks.dialog.exchenge_type_dialog.ExchangeTypeDialogFragment;
import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.network.data_controller.BanksDataController;
import com.sargis.kh.rateam.utils.Constants;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BanksFragment extends Fragment implements OrganizationsContract.View {

    private FragmentBanksBinding binding;

    private OrganizationsContract.Presenter banksPresenter;
    private BanksDataController banksDataController;


    public BanksFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        banksPresenter = new BanksPresenter(this);
        banksDataController = BanksDataController.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_banks, container, false);

        populateViews();
        setListeners();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveData<Map<String, Organization>> liveData = BanksDataController.getInstance().getData();
        liveData.observe(this, value -> updateOrganizationData(value));

        if (liveData.getValue() == null) {
            banksPresenter.getOrganizationsData();
        }
    }

    @Override
    public void onDestroy() {
        banksDataController.getData().removeObservers(this);
        super.onDestroy();
    }

    @Override
    public void updateOrganizationData(Map<String, Organization> organizationMap) {

        BanksAdapter dataAdapter = (BanksAdapter) binding.recyclerView.getAdapter();
        if (dataAdapter != null) {

            Map<String, Organization> organizationFilteredMap = FilterHelperOrganization.getOrganizationFilteredMapByExchangeTypeAndCurrencyType(organizationMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType());

            if (banksDataController.getSortType() == SortTypeEnum.Purchase) {
                Map<String, Organization> organizationSortedMap = FilterHelperOrganization.sortMapForPurchase(organizationFilteredMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType(), banksDataController.getSortOrderForPurchase());
                dataAdapter.updateData(organizationSortedMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType());
            } else if (banksDataController.getSortType() == SortTypeEnum.Sale) {
                Map<String, Organization> organizationSortedMap = FilterHelperOrganization.sortMapForSale(organizationFilteredMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType(), banksDataController.getSortOrderForSale());
                dataAdapter.updateData(organizationSortedMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType());
            } else {
                dataAdapter.updateData(organizationFilteredMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType());
            }
        }

        // Stop refresh animation
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void displayError(String errorMessage) {
        // Stop refresh animation
        binding.swipeRefreshLayout.setRefreshing(false);
        showErrorDialog(errorMessage);
    }

    private void populateViews() {
        binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_united_states, 0, R.drawable.icons_sort_down_enabled, 0);
        binding.setCurrencyType(CurrencyTypeEnum.USD.toString());
        binding.setExchangeType(getString(R.string.cash));

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BanksAdapter banksAdapter = new BanksAdapter(banksDataController.getExchangeType(), banksDataController.getCurrencyType());
        binding.recyclerView.setAdapter(banksAdapter);

        if (banksDataController.getExchangeType() == ExchangeTypeEnum.Cash) {
            binding.buttonExchangeType.setText(getString(R.string.cash));
        } else {
            binding.buttonExchangeType.setText(getString(R.string.non_cash));
        }

        if (banksDataController.getSortType() == SortTypeEnum.Unsorted) {
            binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_disabled,0);
            binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_disabled,0);
        } else if (banksDataController.getSortType() == SortTypeEnum.Purchase) {
            binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_enabled,0);
            binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_disabled,0);
        } else {
            binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_disabled,0);
            binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_enabled,0);
        }
    }

    private void setListeners() {
        binding.textViewPurchase.setOnClickListener(v -> {

            if (banksDataController.getSortType() == SortTypeEnum.Unsorted) {

                banksDataController.setSortType(SortTypeEnum.Purchase);

                banksDataController.setSortOrderForPurchase(SortOrderEnum.Ascending);
                binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_enabled,0);
                banksDataController.setSortOrderForSale(SortOrderEnum.Descending);
                binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_disabled,0);

            } else if (banksDataController.getSortType() == SortTypeEnum.Purchase) {

                if (banksDataController.getSortOrderForPurchase() == SortOrderEnum.Descending) {
                    banksDataController.setSortOrderForPurchase(SortOrderEnum.Ascending);
                    binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_enabled,0);
                } else {
                    banksDataController.setSortOrderForPurchase(SortOrderEnum.Descending);
                    binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_enabled,0);
                }

            } else {

                if (banksDataController.getSortOrderForPurchase() == SortOrderEnum.Descending) {
                    banksDataController.setSortOrderForPurchase(SortOrderEnum.Ascending);
                    binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_enabled,0);
                    banksDataController.setSortOrderForSale(SortOrderEnum.Descending);
                    binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_disabled,0);
                } else {
                    banksDataController.setSortOrderForPurchase(SortOrderEnum.Descending);
                    binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_enabled,0);
                    banksDataController.setSortOrderForSale(SortOrderEnum.Ascending);
                    binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_disabled,0);
                }
                banksDataController.setSortType(SortTypeEnum.Purchase);

            }
            sortData(banksDataController.getSortOrderForPurchase());
        });

        binding.textViewSale.setOnClickListener(v -> {

            if (banksDataController.getSortType() == SortTypeEnum.Unsorted) {

                banksDataController.setSortType(SortTypeEnum.Sale);

                banksDataController.setSortOrderForSale(SortOrderEnum.Ascending);
                binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_enabled,0);
                banksDataController.setSortOrderForPurchase(SortOrderEnum.Descending);
                binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_disabled,0);

            } else if (banksDataController.getSortType() == SortTypeEnum.Sale) {

                if (banksDataController.getSortOrderForSale() == SortOrderEnum.Descending) {
                    banksDataController.setSortOrderForSale(SortOrderEnum.Ascending);
                    binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_enabled,0);
                } else {
                    banksDataController.setSortOrderForSale(SortOrderEnum.Descending);
                    binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_enabled,0);
                }

            } else {

                if (banksDataController.getSortOrderForSale() == SortOrderEnum.Descending) {
                    banksDataController.setSortOrderForSale(SortOrderEnum.Ascending);
                    binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_enabled,0);
                    banksDataController.setSortOrderForPurchase(SortOrderEnum.Descending);
                    binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_disabled,0);
                } else {
                    banksDataController.setSortOrderForSale(SortOrderEnum.Descending);
                    binding.textViewSale.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_down_enabled,0);
                    banksDataController.setSortOrderForPurchase(SortOrderEnum.Ascending);
                    binding.textViewPurchase.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.icons_sort_up_disabled,0);
                }
                banksDataController.setSortType(SortTypeEnum.Sale);
            }

            sortData(banksDataController.getSortOrderForSale());
        });

        binding.buttonExchangeType.setOnClickListener(v -> {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("ExchangeTypeDialogFragment");
            if (prev != null) {
                return;
            }

            Bundle bundle = new Bundle();
            int intValue = banksDataController.getExchangeType().ordinal();
            bundle.putInt(Constants.BUNDLE_EXCHANGE_TYPE, intValue);

            ft.addToBackStack(null);
            DialogFragment dialogFragment = new ExchangeTypeDialogFragment();
            dialogFragment.setTargetFragment(BanksFragment.this, Constants.REQUEST_CODE_EXCHANGE_TYPE);
            dialogFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().add(dialogFragment,"ExchangeTypeDialogFragment").commit();
        });

        binding.buttonCurrencyType.setOnClickListener(v -> {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("CurrencyListDialogFragment");
            if (prev != null) {
                return;
            }

            Bundle bundle = new Bundle();
            int intValue = banksDataController.getCurrencyType().ordinal();
            bundle.putInt(Constants.BUNDLE_CURRENCY_TYPE, intValue);

            ft.addToBackStack(null);
            DialogFragment dialogFragment = new CurrencyListDialogFragment();
            dialogFragment.setTargetFragment(BanksFragment.this, Constants.REQUEST_CODE_CURRENCY_TYPE);
            dialogFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().add(dialogFragment,"CurrencyListDialogFragment").commit();
        });

        //TODO - Map button functionality not implemented


        binding.swipeRefreshLayout.setOnRefreshListener(() -> banksPresenter.getOrganizationsData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case Constants.REQUEST_CODE_EXCHANGE_TYPE:
                if (resultCode == Activity.RESULT_OK) {
                    handleExchangeTypeChange(data);
                }
                break;
            case Constants.REQUEST_CODE_CURRENCY_TYPE:
                if (resultCode == Activity.RESULT_OK) {
                    handleCurrencyTypeChange(data);
                }
                break;
        }
    }

    private void handleExchangeTypeChange(Intent data) {
        int intValue = data.getExtras().getInt(Constants.BUNDLE_EXCHANGE_TYPE,0);
        ExchangeTypeEnum exchangeType = ExchangeTypeEnum.values()[intValue];
        banksDataController.setExchangeType(exchangeType);

        switch (exchangeType) {
            case Cash:
                binding.setExchangeType(getString(R.string.cash));
                break;
            case NonCash:
                binding.setExchangeType(getString(R.string.non_cash));
                break;
        }

        updateFilteredData();
    }

    private void handleCurrencyTypeChange(Intent data) {
        int intValue = data.getExtras().getInt(Constants.BUNDLE_CURRENCY_TYPE,0);
        CurrencyTypeEnum currencyType = CurrencyTypeEnum.values()[intValue];
        banksDataController.setCurrencyType(currencyType);

        switch (currencyType) {
            case AUD:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_australia, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.AUD.toString());
                break;
            case CAD:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_canada, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.CAD.toString());
                break;
            case XAU:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_gold, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.XAU.toString());
                break;
            case USD:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_united_states, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.USD.toString());
                break;
            case RUR:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_russia, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.RUR.toString());
                break;
            case JPY:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_japan, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.JPY.toString());
                break;
            case GEL:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_georgia, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.GEL.toString());
                break;
            case GBP:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_united_kingdom, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.GBP.toString());
                break;
            case EUR:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_european_union, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.EUR.toString());
                break;
            case CHF:
                binding.buttonCurrencyType.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_switzerland, 0, R.drawable.icons_sort_down_enabled, 0);
                binding.setCurrencyType(CurrencyTypeEnum.CHF.toString());
                break;
        }

        updateFilteredData();
    }

    private void updateFilteredData() {
        Map<String, Organization> organizationMap = banksDataController.getData().getValue();
        updateOrganizationData(organizationMap);
    }

    private void showErrorDialog(String errorMessage) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("ErrorMessageDialogFragment");
        if (prev != null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_ERROR_MESSAGE, errorMessage);

        ft.addToBackStack(null);
        DialogFragment dialogFragment = new ErrorMessageDialogFragment();
        dialogFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().add(dialogFragment,"ErrorMessageDialogFragment").commit();
    }

    private void sortData(SortOrderEnum sortOrder) {
        BanksAdapter dataAdapter = (BanksAdapter) binding.recyclerView.getAdapter();
        if (dataAdapter != null) {
            Map<String, Organization> organizationFilteredMap = dataAdapter.getData();

            if (banksDataController.getSortType() == SortTypeEnum.Purchase) {
                Map<String, Organization> organizationSortedMap = FilterHelperOrganization.sortMapForPurchase(organizationFilteredMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType(), sortOrder);
                dataAdapter.updateData(organizationSortedMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType());
            } else if (banksDataController.getSortType() == SortTypeEnum.Sale) {
                Map<String, Organization> organizationSortedMap = FilterHelperOrganization.sortMapForSale(organizationFilteredMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType(), sortOrder);
                dataAdapter.updateData(organizationSortedMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType());
            } else {
                dataAdapter.updateData(organizationFilteredMap, banksDataController.getExchangeType(), banksDataController.getCurrencyType());
            }
        }
    }

}
