package com.example.halt.fragments;

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

import com.example.halt.R;
import com.example.halt.constants.Constants;
import com.example.halt.interfaces.AuthenticationActivityFragmentCommunication;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {
    private EditText emailEt, passwordEt, confirmPasswordEt;
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
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = view.findViewById(R.id.email);
        passwordEt = view.findViewById(R.id.password);
        confirmPasswordEt = view.findViewById(R.id.confirm_password);
        Button signUpButton = view.findViewById(R.id.sign_up_button);
        progressDialog = new ProgressDialog(this.getActivity());
        TextView signInTV = view.findViewById(R.id.sign_in_tv);
        signUpButton.setOnClickListener(v -> register());
        signInTV.setOnClickListener(v -> authenticationActivityFragmentCommunication.openLoginFragment());
        return view;
    }

    private void register() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String confirmPassword = confirmPasswordEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEt.setError(Constants.EMAIL_ERROR_MESSAGE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEt.setError(Constants.PASSWORD_ERROR_MESSAGE);
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            passwordEt.setError(Constants.CONFIRM_PASSWORD_ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEt.setError(Constants.PASSWORDS_NOT_MATCHING_ERROR_MESSAGE);
        }
        if (!isValidEmail(email)) {
            emailEt.setError(Constants.INVALID_EMAIL_ERROR_MESSAGE);
        }
        if (password.length() < 6) {
            passwordEt.setError(Constants.PASSWORD_LENGTH_ERROR_MESSAGE);
        }
        progressDialog.setMessage(Constants.PROGRESS_DIALOG_MESSAGE);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this.requireActivity(), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), Constants.SUCCESSFUL_REGISTER_MESSAGE, Toast.LENGTH_SHORT).show();
                authenticationActivityFragmentCommunication.openLoginFragment();
            } else {
                Toast.makeText(getActivity(), Constants.FAILED_REGISTER_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();

            }
            progressDialog.dismiss();
        });
    }

    private Boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
