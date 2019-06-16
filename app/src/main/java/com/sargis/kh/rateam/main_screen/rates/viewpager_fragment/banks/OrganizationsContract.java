package com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks;

import com.sargis.kh.rateam.models.organizations_data.Organization;

import java.util.Map;

public interface OrganizationsContract {

    interface View {
        void updateOrganizationData(Map<String, Organization> organizationMap);
        void displayError(String errorMessage);
    }

    interface Presenter {
        void getOrganizationsData();
    }

}