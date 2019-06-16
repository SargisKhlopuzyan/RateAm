package com.sargis.kh.rateam.helpers;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.enums.CurrencyTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelperCurrencyIcons {

    public static List<Integer> getCurrencyIconList() {
        List<Integer> drawableList = new ArrayList<>();
        drawableList.add(R.drawable.ic_united_states);
        drawableList.add(R.drawable.ic_european_union);
        drawableList.add(R.drawable.ic_russia);
        drawableList.add(R.drawable.ic_australia);
        drawableList.add(R.drawable.ic_canada);
        drawableList.add(R.drawable.ic_switzerland);
        drawableList.add(R.drawable.ic_united_kingdom);
        drawableList.add(R.drawable.ic_georgia);
        drawableList.add(R.drawable.ic_japan);
        drawableList.add(R.drawable.ic_gold);
        return drawableList;
    }

    public static HashMap<CurrencyTypeEnum, Integer> getCurrencyIconIdHashMapByCurrencyType() {
        HashMap<CurrencyTypeEnum, Integer> hashMap = new HashMap<>();
        hashMap.put(CurrencyTypeEnum.USD, R.drawable.ic_united_states);
        hashMap.put(CurrencyTypeEnum.EUR, R.drawable.ic_european_union);
        hashMap.put(CurrencyTypeEnum.RUR, R.drawable.ic_russia);
        hashMap.put(CurrencyTypeEnum.AUD, R.drawable.ic_australia);
        hashMap.put(CurrencyTypeEnum.CAD, R.drawable.ic_canada);
        hashMap.put(CurrencyTypeEnum.CHF, R.drawable.ic_switzerland);
        hashMap.put(CurrencyTypeEnum.GBP, R.drawable.ic_united_kingdom);
        hashMap.put(CurrencyTypeEnum.GEL, R.drawable.ic_georgia);
        hashMap.put(CurrencyTypeEnum.JPY, R.drawable.ic_japan);
        hashMap.put(CurrencyTypeEnum.XAU, R.drawable.ic_gold);
        return hashMap;
    }
}
