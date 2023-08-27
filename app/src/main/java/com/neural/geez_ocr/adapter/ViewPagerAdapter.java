package com.neural.geez_ocr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.neural.geez_ocr.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int images[] = {
            R.raw.scanning_phone,
            R.raw.document_scan,
            R.raw.send_file
    };

    int headings[] = {
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three
    };

    int description[] = {
            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three
    };

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return description.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_walkthrough,container,false);

        LottieAnimationView animationView = (LottieAnimationView) view.findViewById(R.id.titleImage);
       // TextView slideHeading = (TextView) view.findViewById(R.id.texttitle);
        TextView slideDesciption = (TextView) view.findViewById(R.id.textdeccription);

       // animationView.setImageResource(images[position]);
       // slideHeading.setText(headings[position]);
        if(position == 1) {
            animationView.setScaleX((float) 1.9);
            animationView.setScaleY((float) 1.7);
        }
        animationView.setAnimation(images[position]);
        animationView.setSpeed((float) 0.6);
        slideDesciption.setText(description[position]);


        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }


}
