package com.example.jojo.appstartguide;
 

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private SharedPreferences preferences;
    private List<View> views;
    private static final int[] pics = { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4} ;
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    //底部小点图片
    private ImageView[]  dots;
    private int currentIndex;
    private static final String IMG_URL= "http://121.42.187.192/images/bgimage.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //派生到我的代码片

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        //判断程序是否第一次在本手机上运行
        isFirstRun();
        //初始化viewPager的各个界面
        initViewList();
        //初始化底部的小点
        initDots();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];


        //循环去的小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设置为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }


    private Bitmap getBitmapFromUrl(String imgUrl) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(imgUrl);
            InputStream is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private void initViewList() {
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //初始化引导图片列表
        Bitmap bitmap = getBitmapFromUrl(IMG_URL);

        for (int i = 0; i < pics.length; i++) {
            ImageView iv =  new ImageView(this);
            iv.setLayoutParams(mParams);
            //iv.setImageResource(pics[i]);
            iv.setImageBitmap(bitmap);
            iv.invalidate();
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager) ;
        //初始化 Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        //绑定回调
        vp.setOnPageChangeListener(this);
    }

    private void isFirstRun() {
        preferences = getSharedPreferences("count", MODE_WORLD_READABLE);

    }

    /*private void isFirstRun() {
        preferences = getSharedPreferences("count", MODE_WORLD_READABLE) ;
        int count = preferences.getInt("count", 0);

        //判断程序是第几次运行,如果是第一次运行则跳转到引导页面
        if(count != 0) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),TwoPageActivity.class);
            startActivity(intent);
            this.finish();
        }
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", ++count);
        //提交修改
        editor.commit();
    }*/



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //
    }

    /*
    * 设置当前引导页面
    *
    * */
    private void setCurView(int position) {
        if (position < 0 || position > pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    /*
    * 设置当前引导页的小店选中
    *
    * */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length-1 || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }


    //当滑动状态改变时调用

    @Override
    public void onPageSelected(int position) {
        //设置底部小点选中状态
        setCurDot(position);
    }



   /* public void onPageSelected(int position) {
        //设置底部小点选中状态
        setCurDot(position);
    }*/

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int state) {
        //
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }
}
