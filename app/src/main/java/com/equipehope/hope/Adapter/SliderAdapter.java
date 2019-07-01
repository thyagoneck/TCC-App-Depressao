package com.equipehope.hope.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipehope.hope.App.App;
import com.equipehope.hope.R;

public class SliderAdapter extends PagerAdapter {

    //Array
    public int[] slide_images = {
            R.drawable.logo_tutorial,
            R.drawable.anonimo_tutorial,
            R.drawable.voluntario_tutorial
    };
    public String[] slide_headings = {

            App.getContext().getString(R.string.tutorial_title1),
            App.getContext().getString(R.string.tutorial_title2),
            App.getContext().getString(R.string.tutorial_title3)
    };
    public String[] slide_descs = {
            App.getContext().getString(R.string.tutorial_subtitle1),
            App.getContext().getString(R.string.tutorial_subtitle2),
            App.getContext().getString(R.string.tutorial_subtitle3)
    };


    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (ConstraintLayout) o;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.tutorialImage);
        TextView slideHeading = view.findViewById(R.id.tvTutorialTitle);
        TextView slideDesc = view.findViewById(R.id.tvTutorialSubtitle);


        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDesc.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);

    }
}
