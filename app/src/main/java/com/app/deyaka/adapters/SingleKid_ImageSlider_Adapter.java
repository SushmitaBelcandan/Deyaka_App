package com.app.deyaka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.app.deyaka.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class SingleKid_ImageSlider_Adapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public SingleKid_ImageSlider_Adapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myImageLayout = inflater.inflate(R.layout.slider_single_kid, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        myImage.setClipToOutline(true); //for rounded image corner it will clip image with rounded drawable background
        final String fileUrl = images.get(position);
        if (!fileUrl.equals("NA")) {
            Glide.with(context).load(fileUrl).error(R.drawable.broken_image).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(myImage);
        } else {
            Glide.with(context).load(R.drawable.broken_image).error(R.drawable.broken_image).into(myImage);
        }
        view.addView(myImageLayout, 0);

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
