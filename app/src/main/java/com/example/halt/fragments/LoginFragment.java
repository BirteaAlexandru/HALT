package com.example.halt.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.Objects;

public class LoginFragment extends Fragment {
    private EditText emailEt, passwordEt;
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
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = view.findViewById(R.id.email);
        passwordEt = view.findViewById(R.id.password);
        Button loginButton = view.findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(this.getActivity());
        TextView signUpTV = view.findViewById(R.id.sign_up_tv);
        TextView changePasswordTV = view.findViewById(R.id.forgot_password_tv);
        loginButton.setOnClickListener(v -> login());
        signUpTV.setOnClickListener(v -> authenticationActivityFragmentCommunication.openSignUpFragment());
        changePasswordTV.setOnClickListener(v -> authenticationActivityFragmentCommunication.openChangePasswordFragment());
        return view;
    }

    private void login() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEt.setError(Constants.EMAIL_ERROR_MESSAGE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEt.setError(Constants.PASSWORD_ERROR_MESSAGE);
            return;
        }
        if (!isValidEmail(email)) {
            emailEt.setError(Constants.INVALID_EMAIL_ERROR_MESSAGE);
        }
        progressDialog.setMessage(Constants.PROGRESS_DIALOG_MESSAGE);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this.requireActivity(), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), Constants.SUCCESSFUL_LOGIN_MESSAGE, Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.SHARED_PREFERENCES_USER_EMAIL, Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putString("email", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
                sharedPreferencesEditor.apply();
                authenticationActivityFragmentCommunication.openDashboardActivity();
            } else {
                Toast.makeText(getActivity(), Constants.FAILED_LOGIN_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        });
    }


    private Boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
