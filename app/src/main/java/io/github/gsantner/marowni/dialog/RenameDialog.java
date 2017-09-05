package io.github.gsantner.marowni.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.File;

import io.github.gsantner.marowni.R;
import io.github.gsantner.marowni.model.Constants;
import io.github.gsantner.marowni.util.AppSettings;

public class RenameDialog extends DialogFragment {

    private EditText newNameField;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final File file = new File(getArguments().getString(Constants.SOURCE_FILE));

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        AlertDialog.Builder dialogBuilder = setUpDialog(file, inflater);
        AlertDialog dialog = dialogBuilder.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        newNameField = (EditText) dialog.findViewById(R.id.new_name);
        return dialog;
    }

    private AlertDialog.Builder setUpDialog(final File file, LayoutInflater inflater) {

        View dialogView;
        AlertDialog.Builder dialogBuilder;
        if (AppSettings.get().isDarkThemeEnabled()) {
            dialogView = inflater.inflate(R.layout.rename_dialog_dark, null);
            dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.Base_Theme_AppCompat_Dialog);
        } else {
            dialogView = inflater.inflate(R.layout.rename_dialog, null);
            dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.Base_Theme_AppCompat_Light_Dialog);
        }

        dialogBuilder.setTitle(getResources().getString(R.string.rename));
        dialogBuilder.setView(dialogView);

        ((EditText) dialogView.findViewById(R.id.new_name)).setText(file.getName());

        dialogBuilder.setPositiveButton(getString(android.R.string.ok), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendBroadcast(newNameField.getText().toString(), file);
                    }
                });

        dialogBuilder.setNegativeButton(getString(android.R.string.cancel), new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return dialogBuilder;
    }

    private void sendBroadcast(String name, File file) {
        Intent broadcast = new Intent();
        broadcast.setAction(Constants.RENAME_DIALOG_TAG);
        broadcast.putExtra(Constants.RENAME_NEW_NAME, name);
        broadcast.putExtra(Constants.SOURCE_FILE, file.getAbsolutePath());
        getActivity().sendBroadcast(broadcast);
    }
}