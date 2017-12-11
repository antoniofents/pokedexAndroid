package com.example.afentanes.pokedexandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();
        Button login = getActivity().findViewById(R.id.login_button);
        login.setOnClickListener(view -> {
            TextView loginText = getActivity().findViewById(R.id.login_text);
            TextView password = getActivity().findViewById(R.id.password_text);
            FirebaseAuth firebaseInstance = FirebaseAuth.getInstance();
            firebaseInstance.signInWithEmailAndPassword(String.valueOf(loginText.getText()), String.valueOf(password.getText()))
                    .addOnFailureListener(task ->{
                        task.getMessage();
                    })
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseInstance.getCurrentUser();
                        }

                    });


        });
    }



}
