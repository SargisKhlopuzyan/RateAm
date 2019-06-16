package com.sargis.kh.rateam.main_screen.rates.viewpager_fragment.banks.dialog.exchenge_type_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.enums.ExchangeTypeEnum;
import com.sargis.kh.rateam.utils.Constants;

public class ExchangeTypeDialogFragment extends DialogFragment {

    private ExchangeTypeEnum exchangeType;

    private RadioGroup radioGroup;

    public ExchangeTypeDialogFragment() { }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.fragment_dialog_exchange_type, null);
        dialogBuilder.setView(dialogView);

        initSubViews(dialogView);
        populateSubViews();
        setListeners();

        Dialog dialog = dialogBuilder.setCancelable(true).setTitle(getString(R.string.exchange_type)).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();

        return dialog;
    }

    private void initSubViews(View rootView) {
        radioGroup = rootView.findViewById(R.id.radioGroup);
    }

    private void populateSubViews() {
        int intValue = getArguments().getInt(Constants.BUNDLE_EXCHANGE_TYPE,0);
        this.exchangeType = ExchangeTypeEnum.values()[intValue];
        radioGroup.check(getRadioButtonIdByExchangeType(exchangeType));
    }

    public int getRadioButtonIdByExchangeType(ExchangeTypeEnum exchangeType) {
        if (exchangeType == null)
            return 0;
        switch (exchangeType) {
            case Cash:
                return R.id.radioButtonCash;
            case NonCash:
                return R.id.radioButtonNonCash;
            default:
                return 0;
        }
    }

    private void setListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                int intValue = 0;

                switch (checkedId) {
                    case R.id.radioButtonCash:
                        exchangeType = ExchangeTypeEnum.Cash;
                        intValue = exchangeType.ordinal();

                        bundle.putInt(Constants.BUNDLE_EXCHANGE_TYPE, intValue);
                        intent.putExtras(bundle);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        dismiss();
                        break;
                    case R.id.radioButtonNonCash:
                        exchangeType = ExchangeTypeEnum.NonCash;
                        intValue = exchangeType.ordinal();

                        bundle.putInt(Constants.BUNDLE_EXCHANGE_TYPE, intValue);
                        intent.putExtras(bundle);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        dismiss();
                        break;
                }
            }
        });
    }

}