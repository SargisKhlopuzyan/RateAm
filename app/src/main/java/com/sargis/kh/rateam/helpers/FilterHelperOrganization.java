package com.sargis.kh.rateam.helpers;

import com.sargis.kh.rateam.enums.CurrencyTypeEnum;
import com.sargis.kh.rateam.enums.ExchangeTypeEnum;
import com.sargis.kh.rateam.enums.SortOrderEnum;
import com.sargis.kh.rateam.models.organizations_data.Currency;
import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.models.organizations_data.Transaction;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class FilterHelperOrganization {


    private FilterHelperOrganization() {
    }


    private static boolean checkIfCurrencyMapContainsExchangeTypeAndCurrencyType(Map<String, Currency> currency, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType) {

        if (currency == null) {
            return false;
        }
        if (currency.containsKey(currencyType.toString())) {
            Transaction transaction;
            if (exchangeType == ExchangeTypeEnum.Cash) {
                transaction = currency.get(currencyType.toString()).cash;
            } else {
                transaction = currency.get(currencyType.toString()).nonCash;
            }
            if (transaction == null) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private static boolean checkIfCurrencyContainsExchangeType(Currency currency, ExchangeTypeEnum exchangeType) {

        if (currency == null) {
            return false;
        }

        if (exchangeType == ExchangeTypeEnum.Cash) {
            if (currency.cash != null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (currency.nonCash != null) {
                return true;
            } else {
                return false;
            }
        }
    }


    public static Map<String, Organization> getOrganizationFilteredMapByExchangeTypeAndCurrencyType(Map<String, Organization> organizationMap, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType) {

        if (organizationMap == null)
            return null;

        Map<String, Organization> filteredOrganizationMap = new HashMap<>();

        for (Map.Entry<String, Organization> organizationEntry: organizationMap.entrySet()) {
            if (checkIfCurrencyMapContainsExchangeTypeAndCurrencyType(organizationEntry.getValue().currencyMap, exchangeType, currencyType)) {
                filteredOrganizationMap.put(organizationEntry.getKey(), organizationEntry.getValue());
            }
        }

        return filteredOrganizationMap;
    }

    public static Map<CurrencyTypeEnum, Transaction> getOrganizationCurrencyMapByExchangeType(Organization organization, ExchangeTypeEnum exchangeType) {

        if (organization == null || organization.currencyMap == null)
            return null;

        Map<CurrencyTypeEnum, Transaction> filteredOrganizationMapByExchangeType = new HashMap<>();

        for (Map.Entry<String, Currency> currencyMapEntry: organization.currencyMap.entrySet()) {

            if (checkIfCurrencyContainsExchangeType(currencyMapEntry.getValue(), exchangeType)) {
                if (exchangeType == ExchangeTypeEnum.Cash) {
                    if (currencyMapEntry.getValue().cash != null) {
                        filteredOrganizationMapByExchangeType.put(getCurrencyTypeByString(currencyMapEntry.getKey()), currencyMapEntry.getValue().cash);
                    }
                } else {
                    if (currencyMapEntry.getValue().nonCash != null) {
                        filteredOrganizationMapByExchangeType.put(getCurrencyTypeByString(currencyMapEntry.getKey()), currencyMapEntry.getValue().nonCash);
                    }
                }
            }
        }
        return filteredOrganizationMapByExchangeType;
    }


    private static CurrencyTypeEnum getCurrencyTypeByString(String exchangeTypeString) {
        for (int i = 0; i < CurrencyTypeEnum.values().length; i++) {
            if (CurrencyTypeEnum.values()[i].toString().equals(exchangeTypeString)) {
                return CurrencyTypeEnum.values()[i];
            }
        }
        return null;
    }


    public static TreeMap<String, Organization> sortMapForPurchase(Map<String, Organization> map, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType, SortOrderEnum sortOrder){
        Comparator<String> comparator = new ComparatorForPurchase(map, exchangeType, currencyType, sortOrder);
        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.
        TreeMap<String, Organization> result = new TreeMap<String, Organization>(comparator);
        result.putAll(map);
        return result;
    }

    public static TreeMap<String, Organization> sortMapForSale(Map<String, Organization> map, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType, SortOrderEnum sortOrder){
        Comparator<String> comparator = new ComparatorForSale(map, exchangeType, currencyType, sortOrder);
        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.
        TreeMap<String, Organization> result = new TreeMap<String, Organization>(comparator);
        result.putAll(map);
        return result;
    }


    static class ComparatorForPurchase implements Comparator<String>{

        Map<String, Organization> map = new HashMap<String, Organization>();
        ExchangeTypeEnum exchangeType;
        CurrencyTypeEnum currencyType;
        SortOrderEnum sortOrder;

        public ComparatorForPurchase(Map<String, Organization> map, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType, SortOrderEnum sortOrder){
            this.map.putAll(map);
            this.exchangeType = exchangeType;
            this.currencyType = currencyType;
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(String s1, String s2) {

            Currency currency1 = map.get(s1).currencyMap.get(currencyType.toString());
            Currency currency2 = map.get(s2).currencyMap.get(currencyType.toString());

            switch (exchangeType) {
                case Cash:
                    if (sortOrder == SortOrderEnum.Ascending) {
                        if (currency1.cash.buy >= currency2.cash.buy) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (currency1.cash.buy <= currency2.cash.buy) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                case NonCash:
                    if (sortOrder == SortOrderEnum.Ascending) {
                        if (currency1.nonCash.buy >= currency2.nonCash.buy) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (currency1.nonCash.buy <= currency2.nonCash.buy) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
            }
            return 1;
        }
    }

    static class ComparatorForSale implements Comparator<String>{

        Map<String, Organization> map = new HashMap<String, Organization>();
        ExchangeTypeEnum exchangeType;
        CurrencyTypeEnum currencyType;
        SortOrderEnum sortOrder;

        public ComparatorForSale(Map<String, Organization> map, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType, SortOrderEnum sortOrder){
            this.map.putAll(map);
            this.exchangeType = exchangeType;
            this.currencyType = currencyType;
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(String s1, String s2) {

            Currency currency1 = map.get(s1).currencyMap.get(currencyType.toString());
            Currency currency2 = map.get(s2).currencyMap.get(currencyType.toString());

            switch (exchangeType) {
                case Cash:
                    if (sortOrder == SortOrderEnum.Ascending) {
                        if (currency1.cash.sell >= currency2.cash.sell) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (currency1.cash.sell <= currency2.cash.sell) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                case NonCash:
                    if (sortOrder == SortOrderEnum.Ascending) {
                        if (currency1.nonCash.sell >= currency2.nonCash.sell) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (currency1.nonCash.sell <= currency2.nonCash.sell) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
            }
            return 1;
        }
    }



    // Method for getting the maximum value
    public static float getMaxPurchaseValue(List<Organization> organizationList, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType){

        if (organizationList == null || organizationList.size() == 0) {
            return 0;
        }

        Organization organizationWithMaxPurchaseValue = organizationList.get(0);

        switch (exchangeType) {
            case Cash:
                for(int i=1; i < organizationList.size(); i++){
                    if(organizationList.get(i).currencyMap.get(currencyType.toString()).cash.buy > organizationWithMaxPurchaseValue.currencyMap.get(currencyType.toString()).cash.buy){
                        organizationWithMaxPurchaseValue = organizationList.get(i);
                    }
                }
                return organizationWithMaxPurchaseValue.currencyMap.get(currencyType.toString()).cash.buy;
            case NonCash:
                for(int i=1; i < organizationList.size(); i++){
                    if(organizationList.get(i).currencyMap.get(currencyType.toString()).nonCash.buy > organizationWithMaxPurchaseValue.currencyMap.get(currencyType.toString()).nonCash.buy){
                        organizationWithMaxPurchaseValue = organizationList.get(i);
                    }
                }
                return organizationWithMaxPurchaseValue.currencyMap.get(currencyType.toString()).nonCash.buy;
        }
        return 0;
    }

    // Method for getting the minimum value
    public static float getMinSaleValue(List<Organization> organizationList, ExchangeTypeEnum exchangeType, CurrencyTypeEnum currencyType){

        if (organizationList == null || organizationList.size() == 0) {
            return 0;
        }

        Organization organizationWithMinSaleValue = organizationList.get(0);
        switch (exchangeType) {
            case Cash:
                for(int i = 1; i < organizationList.size(); i++){
                    if(organizationList.get(i).currencyMap.get(currencyType.toString()).cash.sell < organizationWithMinSaleValue.currencyMap.get(currencyType.toString()).cash.sell){
                        organizationWithMinSaleValue = organizationList.get(i);
                    }
                }
                return organizationWithMinSaleValue.currencyMap.get(currencyType.toString()).cash.sell;
            case NonCash:
                for(int i = 1; i < organizationList.size(); i++){
                    if(organizationList.get(i).currencyMap.get(currencyType.toString()).nonCash.sell < organizationWithMinSaleValue.currencyMap.get(currencyType.toString()).nonCash.sell){
                        organizationWithMinSaleValue = organizationList.get(i);
                    }
                }
                return organizationWithMinSaleValue.currencyMap.get(currencyType.toString()).nonCash.sell;
        }
        return 0;
    }
}
