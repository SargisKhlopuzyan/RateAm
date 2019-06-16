package com.sargis.kh.rateam.detail_screen;

import com.sargis.kh.rateam.models.response_branches.ResponseBranches;

public interface OrganizationDetailContract {

    interface View {
        void updateData(ResponseBranches responseBranches);
        void displayError(String errorMessage);
    }

    interface Presenter {
        void getOrganizationDetailData(String organizationId);
    }

}