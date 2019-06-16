package com.sargis.kh.rateam.network.calls;

import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.models.response_branches.ResponseBranches;

import java.util.Map;

public class Data {

    public static void getOrganizationsData(GetDataCallback<Map<String, Organization>> dataCallback) {
        AsynchronousRequests.getOrganizationsData(dataCallback);
    }

    public static void getOrganizationDetailData(GetDataCallback<ResponseBranches> dataCallback, String organizationId) {
        AsynchronousRequests.getOrganizationDetailData(dataCallback, organizationId);
    }

}
