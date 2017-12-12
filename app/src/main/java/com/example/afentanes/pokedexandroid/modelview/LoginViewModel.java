package com.example.afentanes.pokedexandroid.modelview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by afentanes on 12/12/17.
 */

public class LoginViewModel extends AndroidViewModel {
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<String> user;

    public void logUser(String user, String password){
        FirebaseAuth firebaseInstance = FirebaseAuth.getInstance();
        firebaseInstance.signInWithEmailAndPassword(user, password)
                .addOnFailureListener(task ->{
                    task.getMessage();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = firebaseInstance.getCurrentUser();
                        getUser().setValue(currentUser.getEmail());
                    }
                });
    }

    public MutableLiveData<String> getUser() {
        if(user==null){
            user= new MutableLiveData<>();
        }
        return user;
    }
}
