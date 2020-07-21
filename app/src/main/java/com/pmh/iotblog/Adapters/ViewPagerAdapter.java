package com.pmh.iotblog.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.pmh.iotblog.R;

public class ViewPagerAdapter extends PagerAdapter {

    private  Context context;
    private  LayoutInflater inflater;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    private int images[] ={
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
    };

    private String titles[] ={
      "Step 1",
      "Step 2",
      "Step 3"
    };

    private String descs[] ={
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    };


    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v   = inflater.inflate(R.layout.view_pager,container,false);

        // init views
        ImageView imageView = v.findViewById(R.id.imgViewPager);
        TextView textTitle  = v.findViewById(R.id.txtTitleViewPager);
        TextView textDesc   = v.findViewById(R.id.txtDescViewPager);

        imageView.setImageResource(images[position]);
        textTitle.setText(titles[position]);
        textDesc.setText(descs[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
