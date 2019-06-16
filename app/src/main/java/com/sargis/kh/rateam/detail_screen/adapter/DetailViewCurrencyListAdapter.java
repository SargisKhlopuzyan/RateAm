package com.sargis.kh.rateam.detail_screen.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.enums.CurrencyTypeEnum;
import com.sargis.kh.rateam.enums.ExchangeTypeEnum;
import com.sargis.kh.rateam.helpers.FilterHelperOrganization;
import com.sargis.kh.rateam.helpers.HelperCurrencyIcons;
import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.models.organizations_data.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailViewCurrencyListAdapter extends RecyclerView.Adapter<DetailViewCurrencyListAdapter.DataAdapterViewHolder> {

    private ExchangeTypeEnum exchangeType;

    List<CurrencyTypeEnum> currencyTypeForCash;
    List<CurrencyTypeEnum> currencyTypeForNonCash;

    Map<CurrencyTypeEnum, Transaction> transactionMapForCash;
    Map<CurrencyTypeEnum, Transaction> transactionMapForNonCash;

    public DetailViewCurrencyListAdapter(Organization organization, ExchangeTypeEnum exchangeType) {
        this.exchangeType = exchangeType;

        currencyTypeForCash = new ArrayList<>();
        currencyTypeForNonCash = new ArrayList<>();

        transactionMapForCash = FilterHelperOrganization.getOrganizationCurrencyMapByExchangeType(organization, ExchangeTypeEnum.Cash);
        transactionMapForNonCash = FilterHelperOrganization.getOrganizationCurrencyMapByExchangeType(organization, ExchangeTypeEnum.NonCash);

        for (CurrencyTypeEnum currencyType : transactionMapForCash.keySet()) {
            currencyTypeForCash.add(currencyType);
        }

        for (CurrencyTypeEnum currencyType : transactionMapForNonCash.keySet()) {
            currencyTypeForNonCash.add(currencyType);
        }
    }

    @NonNull
    @Override
    public DataAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_view_currency_detail_view, parent, false);
        return new DataAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapterViewHolder holder, int position) {
        switch (exchangeType) {
            case Cash:
                holder.bindData(transactionMapForCash, currencyTypeForCash.get(position));
                break;
            case NonCash:
                holder.bindData(transactionMapForNonCash, currencyTypeForNonCash.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (exchangeType) {
            case Cash:
                return transactionMapForCash.size();
            case NonCash:
                return transactionMapForNonCash.size();
            default:
                return 0;
        }
    }

    public void setExchangeType(ExchangeTypeEnum exchangeType) {
        this.exchangeType = exchangeType;
        notifyDataSetChanged();
    }

    public class DataAdapterViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textViewBankName;
        private TextView textViewBankPurchase;
        private TextView textViewBankSale;

        public DataAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewBankName = itemView.findViewById(R.id.textViewBankName);
            textViewBankPurchase = itemView.findViewById(R.id.textViewPurchase);
            textViewBankSale = itemView.findViewById(R.id.textViewSale);
        }

        public void bindData(Map<CurrencyTypeEnum, Transaction> transactionMap, CurrencyTypeEnum currencyType) {
            HashMap<CurrencyTypeEnum, Integer> currencyIconIdHashMapByCurrencyType = HelperCurrencyIcons.getCurrencyIconIdHashMapByCurrencyType();
            imageView.setImageResource(currencyIconIdHashMapByCurrencyType.get(currencyType));
            textViewBankName.setText(currencyType.toString());

            textViewBankPurchase.setText(transactionMap.get(currencyType).buy.toString());
            textViewBankSale.setText(transactionMap.get(currencyType).sell.toString());
        }
    }

}