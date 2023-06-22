package com.example.HealthT.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.HealthT.R;
import com.example.HealthT.constants.Constants;
import com.example.HealthT.interfaces.AuthenticationActivityFragmentCommunication;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {
    private EditText emailEt;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private AuthenticationActivityFragmentCommunication authenticationActivityFragmentCommunication;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AuthenticationActivityFragmentCommunication) {
            authenticationActivityFragmentCommunication = (AuthenticationActivityFragmentCommunication) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reset_password_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = view.findViewById(R.id.email);
        TextView backToLoginTv = view.findViewById(R.id.reset_back_to_login_tv);
        Button changePasswordButton = view.findViewById(R.id.change_password_button);
        progressDialog = new ProgressDialog(this.getActivity());
        changePasswordButton.setOnClickListener(v -> changePassword());
        backToLoginTv.setOnClickListener(v -> authenticationActivityFragmentCommunication.openLoginFragment());
        return view;
    }

    private void changePassword() {
        String email = emailEt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailEt.setError(Constants.EMAIL_ERROR_MESSAGE);
            return;
        }
        if (!isValidEmail(email)) {
            emailEt.setError(Constants.INVALID_EMAIL_ERROR_MESSAGE);
        }

        progressDialog.setMessage(Constants.PROGRESS_DIALOG_MESSAGE);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this.requireActivity(), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), Constants.SUCCESSFUL_CHANGE_PASSWORD_MESSAGE, Toast.LENGTH_SHORT).show();
                authenticationActivityFragmentCommunication.openLoginFragment();
            } else {
                Toast.makeText(getActivity(), Constants.FAILED_CHANGE_PASSWORD_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();

            }
            progressDialog.dismiss();
        });
    }

    private Boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}