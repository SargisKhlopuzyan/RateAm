package com.sargis.kh.rateam.utils;

public interface Constants {
    String BASE_URL = "http://rate.am";
    String BANKS_INFORMATION_URL = "http://rate.am/ws/mobile/v2/rates.ashx?lang=en";
    String BANK_INFORMATION_URL = "http://rate.am/ws/mobile/v2/branches.ashx?id=";
    String BANK_LOGO_URL = "https://rate.am/images/organization/logo/";

    String BUNDLE_ERROR_MESSAGE = "ERROR_MESSAGE";
    String BUNDLE_EXCHANGE_TYPE = "EXCHANGE_TYPE";
    String BUNDLE_CURRENCY_TYPE = "CURRENCY_TYPE";
    String BUNDLE_ORGANIZATION_ID = "ORGANIZATION_ID";
    String BUNDLE_ORGANIZATION_DETAIL = "ORGANIZATION_DETAIL";

    int REQUEST_CODE_EXCHANGE_TYPE = 0;
    int REQUEST_CODE_CURRENCY_TYPE = 1;
}
