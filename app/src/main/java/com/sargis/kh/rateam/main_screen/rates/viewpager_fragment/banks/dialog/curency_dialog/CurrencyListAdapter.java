package com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks.dialog.curency_dialog;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sargis.kh.rateam.App;
import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.databinding.LayoutRecyclerViewCurrencyListBinding;
import com.sargis.kh.rateam.enums.CurrencyTypeEnum;
import com.sargis.kh.rateam.models.CurrencyListDataModel;

import java.util.List;

public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.DataAdapterViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CurrencyTypeEnum currencyType);
    }
    private final OnItemClickListener listener;

    private List<CurrencyListDataModel> dataModels;
    private CurrencyTypeEnum selectedCurrencyType;

    public CurrencyListAdapter(OnItemClickListener listener, List<CurrencyListDataModel> currencyList, CurrencyTypeEnum selectedCurrencyType) {
        this.listener = listener;
        this.dataModels = currencyList;
        this.selectedCurrencyType = selectedCurrencyType;
    }

    @NonNull
    @Override
    public DataAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutRecyclerViewCurrencyListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_recycler_view_currency_list,
                parent,false);

        return new DataAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapterViewHolder holder, int position) {
        holder.bind(listener, dataModels.get(position), selectedCurrencyType.equals(CurrencyTypeEnum.values()[position]) ? true : false);
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }


    public class DataAdapterViewHolder extends RecyclerView.ViewHolder {

        private LayoutRecyclerViewCurrencyListBinding binding;

        public DataAdapterViewHolder(LayoutRecyclerViewCurrencyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final OnItemClickListener listener, final CurrencyListDataModel dataModel, boolean isCheckBoxVisible) {

            Drawable d = App.getAppContext().getResources().getDrawable(dataModel.getIconId());
            binding.setDrawableCurrency(d);

            binding.setCurrency(dataModel.getCurrency().toString());
            binding.setCurrencyDescription(dataModel.getCurrencyDescription());
            binding.setIsSelected(isCheckBoxVisible);

            binding.setOnItemClickListener(v -> {
                CurrencyTypeEnum currencyType = CurrencyTypeEnum.values()[getAdapterPosition()];
                listener.onItemClick(currencyType);
            });
        }

    }
}