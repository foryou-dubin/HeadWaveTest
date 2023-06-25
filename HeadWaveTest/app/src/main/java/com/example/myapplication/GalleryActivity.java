package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;
import edu.washington.cs.touchfreelibrary.sensors.ClickSensor;
import edu.washington.cs.touchfreelibrary.utilities.LocalOpenCV;
import edu.washington.cs.touchfreelibrary.utilities.PermissionUtility;

public class GalleryActivity extends AppCompatActivity implements CameraGestureSensor.Listener, ClickSensor.Listener {

    private Integer[] pics = {
            R.drawable.animation1,
            R.drawable.animation2,
            R.drawable.animation3,
            R.drawable.animation4,
            R.drawable.animation5,
            R.drawable.animation6,
            R.drawable.animation7,
            R.drawable.animation8,
            R.drawable.animation9,
            R.drawable.animation10};


    private TextView number;

    /**
     * Gallery which displays images.
     */
    private ViewPager viewPager;
    /**
     * Contains the image
     */
//    private ImageView imageView;

    private int currentView = 0;

    LocalOpenCV loader;

    public class BannerAdapter extends PagerAdapter {

        private Context mContext;
        private Integer[] mList;

        public BannerAdapter(Context context, Integer[] list) {
            mContext = context;
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item_layout, container, false);
            ImageView imageView = view.findViewById(R.id.imageview);
            imageView.setImageResource(mList[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtility.checkCameraPermission(this)) {
            loader = new LocalOpenCV(GalleryActivity.this, GalleryActivity.this, GalleryActivity.this);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_picture);

        if (PermissionUtility.checkCameraPermission(this)) {
            //The third passing in represents a separate click sensor which is not required if you just want the hand motions
            loader = new LocalOpenCV(GalleryActivity.this, GalleryActivity.this, GalleryActivity.this);
        }

        number = (TextView) findViewById(R.id.number);


//        imageView = (ImageView)findViewById(R.id.ImageView01);
//        imageView.setImageResource(pics[currentView]);

        viewPager = (ViewPager) findViewById(R.id.Gallery01);
        viewPager.setAdapter(new BannerAdapter(this, pics));
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setNumber(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        number.setText("1/10");
    }

    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {

    }

    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {

    }

    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentView + 1 >= pics.length) {
                    currentView = 0;
                } else {
                    currentView++;
                }
                viewPager.setCurrentItem(currentView);
            }
        });
    }

    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentView - 1 < 0) {
                    currentView = pics.length - 1;
                } else {
                    currentView--;
                }
                viewPager.setCurrentItem(currentView);
            }
        });
    }

    private void setNumber(int position) {
        number.setText((position + 1) + "/10");
    }

    @Override
    public void onSensorClick(ClickSensor caller) {

    }
}
