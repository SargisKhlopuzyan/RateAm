package com.sargis.kh.rateam.detail_screen;

import com.sargis.kh.rateam.App;
import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.helpers.HelperNetwork;
import com.sargis.kh.rateam.models.response_branches.ResponseBranches;
import com.sargis.kh.rateam.network.calls.Data;
import com.sargis.kh.rateam.network.calls.GetDataCallback;
import com.sargis.kh.rateam.network.data_controller.DetailViewDataController;

import okhttp3.ResponseBody;

public class DetailViewPresenter implements OrganizationDetailContract.Presenter {

    private OrganizationDetailContract.View detailViewInterface;

    public DetailViewPresenter(OrganizationDetailContract.View detailViewInterface) {
        this.detailViewInterface = detailViewInterface;
    }

    @Override
    public void getOrganizationDetailData(String organizationId) {

        if (!HelperNetwork.isNetworkActive()) {
            detailViewInterface.displayError(App.getAppContext().getString(R.string.no_internet_connection));
            return;
        }

        Data.getOrganizationDetailData(new GetDataCallback<ResponseBranches>() {
            @Override
            public void onSuccess(ResponseBranches dataResponse) {
                DetailViewDataController.getInstance().setData(dataResponse);
            }

            @Override
            public void onError(int errorCode, ResponseBody errorResponse) {
                detailViewInterface.displayError(errorResponse.toString());
            }

            @Override
            public void onFailure(Throwable failure) {
                detailViewInterface.displayError(failure.getMessage());
            }
        }, organizationId);
    }

}
