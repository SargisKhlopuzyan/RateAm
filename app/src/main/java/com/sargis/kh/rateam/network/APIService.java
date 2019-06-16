package com.sargis.kh.rateam.network;

import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.models.response_branches.ResponseBranches;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface APIService {

    @GET
    Call<ResponseBranches> getBranchesData(@Url String url);

    @GET
    Call<Map<String, Organization>> getOrganizationData(@Url String url);

}
