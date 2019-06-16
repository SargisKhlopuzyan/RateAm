package com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks.dialog.curency_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.databinding.FragmentDialogCurrencyListBinding;
import com.sargis.kh.rateam.databinding.FragmentDialogErrorMessageBinding;
import com.sargis.kh.rateam.models.CurrencyListDataModel;
import com.sargis.kh.rateam.enums.CurrencyTypeEnum;
import com.sargis.kh.rateam.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class CurrencyListDialogFragment extends DialogFragment implements CurrencyListAdapter.OnItemClickListener {

    private FragmentDialogCurrencyListBinding binding;
    private CurrencyTypeEnum currencyType;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_currency_list, null, false);
        dialogBuilder.setView(binding.getRoot());

        populateSubViews();

        Dialog dialog = dialogBuilder.setCancelable(true).create();

        return dialog;
    }

    private void populateSubViews() {
        int intValue = getArguments().getInt(Constants.BUNDLE_CURRENCY_TYPE,0);
        this.currencyType = CurrencyTypeEnum.values()[intValue];
        setupRecyclerView();
    }


    public void setupRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CurrencyListAdapter adapter = new CurrencyListAdapter(this, getCurrencyList(), currencyType);
        binding.recyclerView.setAdapter(adapter);
    }

    private List<CurrencyListDataModel> getCurrencyList() {
        String[] currencyDescriptionList = getResources().getStringArray(R.array.currency_description_list);
        TypedArray currencyIconList = getResources().obtainTypedArray(R.array.currency_icon_list);

        List<CurrencyListDataModel> currencyListDataModelList = new ArrayList<>();
        for (int i = 0; i < CurrencyTypeEnum.values().length; i++) {
            currencyListDataModelList.add(new CurrencyListDataModel(CurrencyTypeEnum.values()[i], currencyDescriptionList[i], currencyIconList.getResourceId(i, -1)));
        }
        return currencyListDataModelList;
    }

    @Override
    public void onItemClick(CurrencyTypeEnum currencyType) {
        int intValue = currencyType.ordinal();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putInt(Constants.BUNDLE_CURRENCY_TYPE, intValue);
        intent.putExtras(bundle);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}