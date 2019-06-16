package com.sargis.kh.rateam.network.calls;

import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.models.response_branches.ResponseBranches;
import com.sargis.kh.rateam.network.APIService;
import com.sargis.kh.rateam.network.RetrofitClientInstance;
import com.sargis.kh.rateam.utils.Constants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsynchronousRequests {

    public static void getOrganizationsData(GetDataCallback<Map<String, Organization>> dataCallback) {
        APIService service = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        Call<Map<String, Organization>> call = service.getOrganizationData(Constants.BANKS_INFORMATION_URL);
        call.enqueue(new Callback<Map<String, Organization>>() {
            @Override
            public void onResponse(Call<Map<String, Organization>> call, Response<Map<String, Organization>> dataResponse) {
                if (dataResponse.isSuccessful()) {
                    dataCallback.onSuccess(dataResponse.body());
                } else {
                    dataCallback.onError(dataResponse.code(), dataResponse.errorBody());
                }

            }

            @Override
            public void onFailure(Call<Map<String, Organization>> call, Throwable t) {
                dataCallback.onFailure(t);
            }
        });
    }

    public static void getOrganizationDetailData(GetDataCallback<ResponseBranches> dataCallback, String organizationId) {
        APIService service = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        Call<ResponseBranches> call = service.getBranchesData(getUrlByOrganizationId(organizationId));
        call.enqueue(new Callback<ResponseBranches>() {
            @Override
            public void onResponse(Call<ResponseBranches> call, Response<ResponseBranches> dataResponse) {

                if (dataResponse.isSuccessful()) {
                    dataCallback.onSuccess(dataResponse.body());
                } else {
                    dataCallback.onError(dataResponse.code(), dataResponse.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBranches> call, Throwable t) {
                dataCallback.onFailure(t);
            }
        });
    }

    private static String getUrlByOrganizationId(String organizationId) {
        return Constants.BANK_INFORMATION_URL + organizationId;
    }
}
