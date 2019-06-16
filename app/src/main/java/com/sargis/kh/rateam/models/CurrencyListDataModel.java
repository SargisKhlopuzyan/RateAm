package com.sargis.kh.rateam.models;

import com.sargis.kh.rateam.enums.CurrencyTypeEnum;

public class CurrencyListDataModel {

    private CurrencyTypeEnum currencyType;

    private String currencyDescription;

    private int iconId;


    public CurrencyListDataModel(CurrencyTypeEnum currencyType, String currencyDescription, int iconId) {
        this.currencyType = currencyType;
        this.currencyDescription = currencyDescription;
        this.iconId = iconId;
    }

    public CurrencyTypeEnum getCurrency() {
        return currencyType;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public int getIconId() {
        return iconId;
    }

}
