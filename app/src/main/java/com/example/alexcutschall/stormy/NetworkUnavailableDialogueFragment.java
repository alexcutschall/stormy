package com.example.alexcutschall.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class NetworkUnavailableDialogueFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Network Unavailable")
                .setMessage("Sorry, it looks like you aren't connected to the internet!")
                .setPositiveButton("OK", null);
        return builder.create();
    }
}
