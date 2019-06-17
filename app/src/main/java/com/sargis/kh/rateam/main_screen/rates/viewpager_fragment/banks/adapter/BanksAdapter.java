package com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks.adapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sargis.kh.rateam.App;
import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.databinding.LayoutRecyclerViewBankBinding;
import com.sargis.kh.rateam.detail_screen.DetailActivity;
import com.sargis.kh.rateam.enums.CurrencyTypeEnum;
import com.sargis.kh.rateam.enums.ExchangeTypeEnum;
import com.sargis.kh.rateam.helpers.FilterHelperOrganization;
import com.sargis.kh.rateam.helpers.HelperOrganization;
import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.models.organizations_data.Transaction;
import com.sargis.kh.rateam.utils.Constants;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanksAdapter extends RecyclerView.Adapter<BanksAdapter.DataAdapterViewHolder> {

    private Map<String, Organization> organizationMap;
    private List<Organization> organizationList;
    private List<String> organizationIdList;

    private float maxPurchaseValue;
    private float minSaleValue;

    private ExchangeTypeEnum exchangeType;
    private CurrencyTypeEnum currencyType;

    public BanksAdapter(ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType) {
        organizationMap = new HashMap<>();
        organizationList = new ArrayList();
        organizationIdList = new ArrayList<>();

        this.exchangeType = exchangeType;
        this.currencyType = currencyType;
    }

    @NonNull
    @Override
    public DataAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRecyclerViewBankBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_recycler_view_bank,
                parent,false);

        return new DataAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapterViewHolder holder, int position) {
        holder.bindData(organizationList.get(position), organizationIdList.get(position));
    }

    @Override
    public int getItemCount() {
        return organizationMap.size();
    }

    public void updateData(Map<String, Organization> organizationMap, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType) {

        if (organizationMap == null)
            organizationMap = new HashMap<>();

        this.organizationMap = organizationMap;
        this.exchangeType = exchangeType;
        this.currencyType = currencyType;

        organizationList.clear();
        organizationIdList.clear();

        organizationList = HelperOrganization.getOrganizationList(organizationMap);
        organizationIdList = HelperOrganization.getOrganizationIdList(organizationMap);

        maxPurchaseValue = FilterHelperOrganization.getMaxPurchaseValue(organizationList, exchangeType, currencyType);
        minSaleValue = FilterHelperOrganization.getMinSaleValue(organizationList, exchangeType, currencyType);
        notifyDataSetChanged();
    }

    public Map<String, Organization> getData() {
        return organizationMap;
    }

    public class DataAdapterViewHolder extends RecyclerView.ViewHolder {

        private LayoutRecyclerViewBankBinding binding;

        public DataAdapterViewHolder(LayoutRecyclerViewBankBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(final Organization organization, final String organizationId) {

            Picasso.get().load(Constants.BANK_LOGO_URL + organization.logo)
                    .placeholder(R.drawable.icon_bank)
                    .into(binding.imageView);

            binding.setBankName(organization.title);

            populatePurchaseAndSaleTextViews(organization, exchangeType, currencyType, binding);

            binding.setOnItemClickListener(v -> {
                Intent intent = new Intent(App.getAppContext(), DetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString(Constants.BUNDLE_ORGANIZATION_ID, organizationId);
                bundle.putSerializable(Constants.BUNDLE_ORGANIZATION_DETAIL, organization);
                intent.putExtras(bundle);
                App.getAppContext().startActivity(intent);
            });
        }
    }

    private void populatePurchaseAndSaleTextViews(Organization organization, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType, LayoutRecyclerViewBankBinding binding) {

        Transaction transaction = null;

        switch (exchangeType) {
            case Cash:
                transaction = organization.currencyMap.get(currencyType.toString()).cash;
                binding.setPurchase(transaction.buy.toString());
                binding.setSale(transaction.sell.toString());
                break;
            case NonCash:
                transaction = organization.currencyMap.get(currencyType.toString()).nonCash;
                binding.setPurchase(transaction.buy.toString());
                binding.setSale(transaction.sell.toString());
                break;
        }

        if (maxPurchaseValue == transaction.buy) {
            binding.setColorPurchase(App.getAppContext().getResources().getColor(R.color.colorPrimary));
        } else {
            binding.setColorPurchase(App.getAppContext().getResources().getColor(android.R.color.black));
        }

        if (minSaleValue == transaction.sell) {
            binding.setColorSale(App.getAppContext().getResources().getColor(R.color.colorPrimary));
        } else {
            binding.setColorSale(App.getAppContext().getResources().getColor(android.R.color.black));
        }

    }

}