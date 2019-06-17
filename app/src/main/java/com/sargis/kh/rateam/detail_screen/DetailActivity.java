package com.sargis.kh.rateam.detail_screen;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.databinding.ActivityDetailBinding;
import com.sargis.kh.rateam.detail_screen.adapter.DetailViewBranchListAdapter;
import com.sargis.kh.rateam.detail_screen.adapter.DetailViewCurrencyListAdapter;
import com.sargis.kh.rateam.network.data_controller.DetailViewDataController;
import com.sargis.kh.rateam.models.response_branches.Branch;
import com.sargis.kh.rateam.models.response_branches.ResponseBranches;
import com.sargis.kh.rateam.dialogs.ErrorMessageDialogFragment;
import com.sargis.kh.rateam.enums.DaysOfWeekEnum;
import com.sargis.kh.rateam.enums.ExchangeTypeEnum;
import com.sargis.kh.rateam.helpers.HelperBranch;
import com.sargis.kh.rateam.models.organizations_data.Organization;
import com.sargis.kh.rateam.utils.Constants;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements OrganizationDetailContract.View, DetailViewBranchListAdapter.BranchSelectedInterface {

    ActivityDetailBinding binding;

    private final int REQUEST_PHONE_CALL = 0;

    private OrganizationDetailContract.Presenter detailViewPresenter;
    private DetailViewDataController detailViewDataController;

    private Organization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        detailViewPresenter = new DetailViewPresenter(this);
        detailViewDataController = DetailViewDataController.getInstance();

        String organizationId = getIntent().getStringExtra(Constants.BUNDLE_ORGANIZATION_ID);
        organization = (Organization) getIntent().getSerializableExtra(Constants.BUNDLE_ORGANIZATION_DETAIL);
        detailViewDataController.setOrganizationId(organizationId);

        setListeners();

        setSupportActionBar(binding.toolbar);

        binding.setOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            int scrollRange = -1;

            if (verticalOffset == 0) {
                binding.swipeRefreshLayout.setEnabled(true);
            } else if (!binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setEnabled(false);
            }

            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }

            if (scrollRange + verticalOffset == 0 && !binding.getIsTitleVisible()) {
                binding.setTitle(organization.title);
                binding.setIsTitleVisible(true);
            } else if(scrollRange + verticalOffset != 0 && binding.getIsTitleVisible()) {
                binding.setTitle("");
                binding.setIsTitleVisible(false);
            }
        });

        LiveData<ResponseBranches> liveData = DetailViewDataController.getInstance().getData();
        liveData.observe(this, value -> updateData(value));

        setupSwipeRefreshLayout();
        populateCurrencyRecyclerViews();
        populateBranchesRecyclerViews();
        setOrganizationName();
        setupRadioButtons();

        if (liveData.getValue() == null) {
            detailViewPresenter.getOrganizationDetailData(detailViewDataController.getOrganizationId());
        }
    }

    private void setListeners() {
        binding.setOnRefreshListener(() -> detailViewPresenter.getOrganizationDetailData(detailViewDataController.getOrganizationId()));
    }

    private void setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setProgressViewOffset(false, 0, 300);
    }

    private void setOrganizationName() {
        binding.setOrganizationName(organization.title);
        Picasso.get().load(Constants.BANK_LOGO_URL + organization.logo)
                .placeholder(R.drawable.icon_bank)
                .into(binding.circleImageView);
    }

    @Override
    public void updateData(ResponseBranches responseBranches) {

        DetailViewBranchListAdapter dataAdapter = (DetailViewBranchListAdapter) binding.scrollingContent.recyclerViewBranchList.getAdapter();

        List<Branch> branchList = HelperBranch.getBranchList(responseBranches);
        if (dataAdapter != null) {
            dataAdapter.updateData(branchList);
        }

        Branch branch = HelperBranch.getHeadBranch(responseBranches, 1);
        if (branch == null && branchList.size() > 0) {
            branch = branchList.get(0);
        }
        updateBranchInfo(branch);

        // Stop refresh animation
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void displayError(String errorMessage) {
        // Stop refresh animation
        binding.swipeRefreshLayout.setRefreshing(false);
        showErrorDialog(errorMessage);
    }

    private void setupRadioButtons() {
        binding.setOnCheckedChangeListener((group, checkedId) -> {
            DetailViewCurrencyListAdapter dataAdapter = (DetailViewCurrencyListAdapter) binding.scrollingContent.recyclerViewCurrencyList.getAdapter();
            if (dataAdapter == null) {
                return;
            }

            switch (checkedId) {
                case R.id.radioButtonCash:
                    dataAdapter.setExchangeType(ExchangeTypeEnum.Cash);
                    break;
                case R.id.radioButtonNonCash:
                    dataAdapter.setExchangeType(ExchangeTypeEnum.NonCash);
                    break;
            }
        });
    }

    private void populateCurrencyRecyclerViews() {
        binding.scrollingContent.recyclerViewCurrencyList.setHasFixedSize(true);
        binding.scrollingContent.recyclerViewCurrencyList.setLayoutManager(new LinearLayoutManager(this));
        DetailViewCurrencyListAdapter detailViewCurrencyListAdapter = new DetailViewCurrencyListAdapter(organization, detailViewDataController.getExchangeType());
        binding.scrollingContent.recyclerViewCurrencyList.setAdapter(detailViewCurrencyListAdapter);
    }

    private void populateBranchesRecyclerViews() {
        binding.scrollingContent.recyclerViewBranchList.setHasFixedSize(true);
        binding.scrollingContent.recyclerViewBranchList.setLayoutManager(new LinearLayoutManager(this));
        DetailViewBranchListAdapter detailViewCurrencyListAdapter = new DetailViewBranchListAdapter(this);
        binding.scrollingContent.recyclerViewBranchList.setAdapter(detailViewCurrencyListAdapter);
    }

    @Override
    public void onBranchListItemClicked(Branch branch) {
        updateBranchInfo(branch);
        binding.appBar.setExpanded(true);
        binding.scrollingContent.nestedScrollView.scrollTo(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        detailViewDataController.setData(null);
    }

    private void updateBranchInfo(Branch branch) {
        if (branch == null) {
            return;
        }
        String title = branch.title.am != null ? branch.title.am : branch.title.ru;
        title = title != null ? title : branch.title.en;
        title = title != null ? title : "";

        String address = branch.address.am != null ? branch.address.am : branch.address.ru;
        address = address != null ? address : branch.address.en;
        address = address != null ? address : "";

        String contacts = branch.contacts;
        contacts = contacts.replaceAll("[() ]","");
        contacts = (contacts.charAt(0) != '+' ? "+" : "") + contacts;

        String workingDayTime = "";
        for (int i = 0; i < branch.workhours.size(); i++) {
            if (i != 0) {
                workingDayTime += "\n";
            }
            String workingDays = branch.workhours.get(i).days.trim();
            if (workingDays.contains("-")) {
                workingDays = DaysOfWeekEnum.values()[Integer.parseInt("" + workingDays.charAt(0)) - 1].toString() + " - " + DaysOfWeekEnum.values()[Integer.parseInt("" + workingDays.charAt(2))- 1].toString();
                workingDays = workingDays.substring(0, 1).toUpperCase() + workingDays.substring(1);
            } else {
                workingDays = DaysOfWeekEnum.values()[Integer.parseInt("" + workingDays.charAt(0))- 1].toString();
                workingDays = workingDays.substring(0, 1).toUpperCase() + workingDays.substring(1);
            }

            workingDayTime += workingDays + "   " + branch.workhours.get(i).hours;
        }

        binding.setOrganizationTitle(title);
        binding.setOrganizationAddress(address);
        SpannableString content = new SpannableString(contacts);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        binding.setOrganizationPhoneNumbers(content.toString());
        binding.setOrganizationWorkingDaysHours(workingDayTime);


        if (!contacts.isEmpty()) {
            binding.textViewOrganizationPhoneNumbers.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    makeACall();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeACall();
                }
            }
        }
    }

    private void makeACall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        final String uri = "tel:" + binding.textViewOrganizationPhoneNumbers.getText().toString().trim();
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void showErrorDialog(String errorMessage) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("ErrorMessageDialogFragment");
        if (prev != null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_ERROR_MESSAGE, errorMessage);

        ft.addToBackStack(null);
        DialogFragment dialogFragment = new ErrorMessageDialogFragment();
        dialogFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(dialogFragment,"ErrorMessageDialogFragment").commit();
    }

    @Override
    public void onDestroy() {
        detailViewDataController.getData().removeObservers(this);
        super.onDestroy();
    }

}
