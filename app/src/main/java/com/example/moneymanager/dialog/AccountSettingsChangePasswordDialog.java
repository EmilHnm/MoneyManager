package com.example.moneymanager.dialog;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.moneymanager.R;
import com.example.moneymanager.constant.SharedPrefConstant;

public class AccountSettingsChangePasswordDialog extends AppCompatDialogFragment {
    EditText et_old_password, et_new_password, et_new_repassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_account_settings_change_password, null);
        et_old_password = view.findViewById(R.id.account_tools_change_wallet_name_et_old_password);
        et_new_password = view.findViewById(R.id.account_tools_change_wallet_name_et_new_password);
        et_new_repassword = view.findViewById(R.id.account_tools_change_wallet_name_et_new_repassword);
        builder.setView(view)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Đổi mật khẩu", new DialogInterface.OnClickListener() {
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
                    String oldPassword = et_old_password.getText().toString();
                    String newPassword = et_new_password.getText().toString();
                    String newRePassword = et_new_repassword.getText().toString();

                    //Check rỗng 3 biến trên
                    if (!oldPassword.isEmpty() && !newPassword.isEmpty() && !newRePassword.isEmpty()) {
                        //Check mật khẩu mới và nhập lại mật khẩu mới trùng nhau
                        if (newPassword.equals(newRePassword)) {
                            //Lấy list user
                            SharedPreferences sharedPreferencesUsersList = getContext().getSharedPreferences(SharedPrefConstant.USERS_LIST, Context.MODE_PRIVATE);
                            SharedPreferences.Editor usersListEditor = sharedPreferencesUsersList.edit();

                            //Lấy tổng số người dùng
                            SharedPreferences sharedPreferencesUsersTotal = getContext().getSharedPreferences(SharedPrefConstant.USERS_TOTAL, Context.MODE_PRIVATE);
                            int usersTotal = sharedPreferencesUsersTotal.getInt(SharedPrefConstant.USERS_TOTAL_VALUE, 0);


                            //Lấy tên người dùng đang đăng nhập
                            SharedPreferences sharedPreferenceSigningIn = getContext().getSharedPreferences(SharedPrefConstant.SIGNING_IN, Context.MODE_PRIVATE);
                            String signingInUsername = sharedPreferenceSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");

                            for (int i = 1; i <= usersTotal; i++) {
                                //Lấy tên người dùng trong list
                                String usernameInList = sharedPreferencesUsersList.getString(String.format("%s_%d", SharedPrefConstant.USER_USERNAME, i), "");
                                //Check username đăng nhập trùng với username trong list
                                if (signingInUsername.equals(usernameInList)) {
                                    //Check mật khẩu cũ người dùng nhập với mật khẩu hiện tại
                                    if (oldPassword.equals(sharedPreferencesUsersList.getString(String.format("%s_%d", SharedPrefConstant.USER_PASSWORD,i), ""))) {
                                        //Lưu mật khẩu mới
                                        usersListEditor.putString(String.format("%s_%d", SharedPrefConstant.USER_PASSWORD, i), newPassword);
                                        usersListEditor.apply();
                                        closeDialog = true;
                                    }
                                    else
                                        Toast.makeText(getContext(),"Mật khẩu cũ không đúng",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else
                            Toast.makeText(getContext(),"Nhập lại mật khẩu không trùng với mật khẩu",Toast.LENGTH_LONG).show();

                    }
                    else
                        Toast.makeText(getContext(),"Không được trường nào trống",Toast.LENGTH_LONG).show();

                    if (closeDialog)
                        d.dismiss();
                }
            });
        }
    }
}
