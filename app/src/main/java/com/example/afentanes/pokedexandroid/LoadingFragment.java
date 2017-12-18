package com.example.afentanes.pokedexandroid;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;



public class LoadingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.loading_fragment, container, false);
        Glide.with(getActivity()).load("http://g00glen00b.be/wp-content/uploads/2016/12/application-loader.gif").into((ImageView) inflate.findViewById(R.id.loading_view));

        return inflate;
    }


}
