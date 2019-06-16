package com.sargis.kh.rateam.network.data_controller;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.sargis.kh.rateam.enums.CurrencyTypeEnum;
import com.sargis.kh.rateam.enums.ExchangeTypeEnum;
import com.sargis.kh.rateam.enums.SortOrderEnum;
import com.sargis.kh.rateam.enums.SortTypeEnum;
import com.sargis.kh.rateam.models.organizations_data.Organization;

import java.util.Map;

public class BanksDataController {

    private static BanksDataController instance = null;

    private BanksDataController() { }

    public static BanksDataController getInstance() {
        if (instance == null) {
            synchronized (BanksDataController.class) {
                if (instance == null) {
                    instance = new BanksDataController();
                }
            }
        }
        return instance;
    }


    // LiveData
    private MutableLiveData<Map<String, Organization>> liveData = new MutableLiveData<>();

    public LiveData<Map<String, Organization>> getData() {
        return liveData;
    }

    public void setData(Map<String, Organization> data) {
        liveData.setValue(data);
    }


    //Exchange Type
    private ExchangeTypeEnum exchangeType = ExchangeTypeEnum.Cash;

    public void setExchangeType(ExchangeTypeEnum exchangeType) {
        this.exchangeType = exchangeType;
    }

    public ExchangeTypeEnum getExchangeType() {
        return exchangeType;
    }


    //Currency Type
    private CurrencyTypeEnum currencyType = CurrencyTypeEnum.USD;

    public void setCurrencyType(CurrencyTypeEnum currencyType) {
        this.currencyType = currencyType;
    }

    public CurrencyTypeEnum getCurrencyType() {
        return currencyType;
    }


    //Sorted By
    private SortTypeEnum sortType = SortTypeEnum.Unsorted;

    public void setSortType(SortTypeEnum sortedBy) {
        this.sortType = sortedBy;
    }

    public SortTypeEnum getSortType() {
        return sortType;
    }


    //Sort Order For Purchase
    private SortOrderEnum sortOrderPurchase = SortOrderEnum.Unsorted;

    public void setSortOrderForPurchase(SortOrderEnum sortOrder) {
        this.sortOrderPurchase = sortOrder;
    }

    public SortOrderEnum getSortOrderForPurchase() {
        return sortOrderPurchase;
    }


    //Sort Order For Sale
    private SortOrderEnum sortOrderSale = SortOrderEnum.Unsorted;

    public void setSortOrderForSale(SortOrderEnum sortOrder) {
        this.sortOrderSale = sortOrder;
    }

    public SortOrderEnum getSortOrderForSale() {
        return sortOrderSale;
    }

}
