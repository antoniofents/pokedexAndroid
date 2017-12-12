package com.example.afentanes.pokedexandroid;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.afentanes.pokedexandroid.databinding.LoginActivityBinding;
import com.example.afentanes.pokedexandroid.modelview.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding.view.RxView;


public class LoginActivity extends AppCompatActivity implements LifecycleOwner {

    private LifecycleRegistry lifecycleRegistry;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    LoginActivityBinding loginActivityBinding;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleRegistry=new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);
        loginActivityBinding= DataBindingUtil.setContentView(this, R.layout.login_activity);
        viewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
        loginActivityBinding.setLoginViewModel(viewModel);
    }

    public void addObservers(){
        viewModel.getUser().observe(this, value ->{
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", value);
            startActivity(intent);
        });
        //add zip event
        RxView.clicks(findViewById(R.id.login_button)).subscribe(view  -> {
            TextView userTextView= findViewById(R.id.login_text);
            TextView passwordView= findViewById(R.id.password_text);
            viewModel.logUser(String.valueOf(userTextView.getText()),String.valueOf(passwordView.getText()));
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
        addObservers();
            showLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleRegistry.markState(Lifecycle.State.RESUMED);
    }


    public void showLogin() {

    }

}
