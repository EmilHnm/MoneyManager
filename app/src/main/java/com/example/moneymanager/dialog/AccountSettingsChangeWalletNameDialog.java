package com.example.moneymanager.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.moneymanager.R;
import com.example.moneymanager.constant.SharedPrefConstant;

public class AccountSettingsChangeWalletNameDialog extends AppCompatDialogFragment {
    private EditText et_new_wallet_name;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_account_settings_change_wallet_name, null);
        et_new_wallet_name = view.findViewById(R.id.account_tools_change_wallet_name_et_new_wallet_name);
        builder.setView(view)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Đổi tên", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();
    }
    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_NEUTRAL);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean closeDialog = false;
                    String newWalletName = et_new_wallet_name.getText().toString();
                    if (!newWalletName.isEmpty()) {
                        closeDialog = true;

                        //Lấy list user
                        SharedPreferences sharedPreferencesUsersList = getContext().getSharedPreferences(SharedPrefConstant.USERS_LIST, Context.MODE_PRIVATE);
                        SharedPreferences.Editor usersListEditor = sharedPreferencesUsersList.edit();

                        //Lấy tổng số người dùng
                        SharedPreferences sharedPreferencesUsersTotal = getContext().getSharedPreferences(SharedPrefConstant.USERS_TOTAL, Context.MODE_PRIVATE);
                        int usersTotal = sharedPreferencesUsersTotal.getInt(SharedPrefConstant.USERS_TOTAL_VALUE, 0);

                        //Lấy người dùng đang đăng nhập
                        SharedPreferences sharedPreferenceSigningIn = getContext().getSharedPreferences(SharedPrefConstant.SIGNING_IN, Context.MODE_PRIVATE);
                        SharedPreferences.Editor signingInEditor = sharedPreferenceSigningIn.edit();

                        //Lấy username người dùng đang đăng nhập
                        String signinginUsername = sharedPreferenceSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");

                        for (int i = 1; i <= usersTotal; i++) {
                            //Lấy người dùng trong list theo thứ tự
                            String usernameInList = sharedPreferencesUsersList.getString(String.format("%s_%d", SharedPrefConstant.USER_USERNAME, i), "");
                            if(usernameInList.equals(signinginUsername)){
                                //Update wallet name trong list user
                                usersListEditor.putString(String.format("%s_%d", SharedPrefConstant.USER_WALLET_NAME, i), newWalletName);
                                usersListEditor.apply();
                                //Update wallet name trong signingin
                                signingInEditor.putString(SharedPrefConstant.SIGNING_IN_WALLET_NAME, newWalletName);
                                signingInEditor.apply();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(),"Không được để tên ví trống",Toast.LENGTH_LONG).show();
                    }
                    if (closeDialog)
                        d.dismiss();
                }
            });
        }
    }
}