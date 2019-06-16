package com.sargis.kh.rateam.dialogs;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.databinding.FragmentDialogErrorMessageBinding;
import com.sargis.kh.rateam.utils.Constants;

public class ErrorMessageDialogFragment extends DialogFragment {

    private FragmentDialogErrorMessageBinding binding;

    public ErrorMessageDialogFragment() { }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_error_message, null, false);
        dialogBuilder.setView(binding.getRoot());

        populateSubViews();

        Dialog dialog = dialogBuilder.setCancelable(true).setTitle(getString(R.string.error_title)).setNegativeButton(getString(R.string.cancel), (dialog1, which) -> {}).create();

        return dialog;
    }

    private void populateSubViews() {
        String errorMessage = getArguments().getString(Constants.BUNDLE_ERROR_MESSAGE);
        binding.setErrorMessage(errorMessage);
    }

}