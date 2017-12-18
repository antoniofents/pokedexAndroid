package com.example.afentanes.pokedexandroid.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;



public class AnimationUtil {



    public static void animate(RecyclerView.ViewHolder holder, boolean scrollDown){
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator  animatorTranslaterVertical = ObjectAnimator.ofFloat(holder.itemView, "translationY", scrollDown?200:-200,0 );
        animatorTranslaterVertical.setDuration(1000);
        animatorSet.playTogether(animatorTranslaterVertical);
        animatorSet.start();

    }
}
