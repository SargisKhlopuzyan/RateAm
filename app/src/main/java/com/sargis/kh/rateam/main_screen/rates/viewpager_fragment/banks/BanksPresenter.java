package com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks;

import com.sargis.kh.rateam.App;
import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.helpers.HelperNetwork;
import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.network.calls.Data;
import com.sargis.kh.rateam.network.calls.GetDataCallback;
import com.sargis.kh.rateam.network.data_controller.BanksDataController;

import java.util.Map;

import okhttp3.ResponseBody;

public class BanksPresenter implements OrganizationsContract.Presenter {

    private OrganizationsContract.View viewInterface;

    public BanksPresenter(OrganizationsContract.View viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void getOrganizationsData() {

        if (!HelperNetwork.isNetworkActive()) {
            viewInterface.displayError(App.getAppContext().getString(R.string.no_internet_connection));
            return;
        }

        Data.getOrganizationsData(new GetDataCallback<Map<String, Organization>>() {
            @Override
            public void onSuccess(Map<String, Organization> dataResponse) {
                BanksDataController.getInstance().setData(dataResponse);
            }

            @Override
            public void onError(int errorCode, ResponseBody errorResponse) {
                viewInterface.displayError(errorResponse.toString());
            }

            @Override
            public void onFailure(Throwable failure) {
                viewInterface.displayError(failure.getMessage());
            }
        });

    }
}
