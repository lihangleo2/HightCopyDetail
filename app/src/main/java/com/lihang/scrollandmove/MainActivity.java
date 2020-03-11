package com.lihang.scrollandmove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.lihang.scrollandmove.adapter.CommentsAdapter;
import com.lihang.scrollandmove.databinding.ActivityMainBinding;
import com.lihang.scrollandmove.fragment.ExplainFragment;
import com.lihang.scrollandmove.fragment.MyFragment;
import com.lihang.scrollandmove.utils.ButtonClickUtils;
import com.lihang.scrollandmove.utils.DensityUtils;
import com.yuruiyin.appbarlayoutbehavior.AppBarLayoutBehavior;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;
    ActivityMainBinding binding;
    float comments_height;

    private boolean isScrolling = false;
    private boolean isCommentClick = false;


    private ArrayList<TextView> textViews = new ArrayList<>();

    //关于下方fragment的切换
    private static final int HOME_ONE = 0;
    private static final int HOME_TWO = 1;
    private static final int HOME_THREE = 2;
    private int index;
    private int currentTabIndex = 0;

    MyFragment fragment_one;
    ExplainFragment fragment_two;
    ExplainFragment fragment_three;

    private TextView[] mTabs;
    private TextView[] mTabs_second;
    private FragmentManager manager;
    private ArrayList<Fragment> list_fragment = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setListener();
        initFragment();
        initArray();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.txtStatus.getLayoutParams();
        layoutParams.height = DensityUtils.getStatusBarHeight();
        mImmersionBar = ImmersionBar.with(this).statusBarDarkFont(true);
        mImmersionBar.init();
        initProduct();
    }

    private void setListener() {
        binding.setOnClickListener(this);
        binding.appBar.addOnOffsetChangedListener(this);
        //评论的点击
        binding.txtComment.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (binding.relativeTitle.getVisibility() == View.VISIBLE && !isScrolling && !binding.txtComment.isSelected()) {
                                isCommentClick = true;
                                binding.nestedScrollView.scrollTo(0, 0);
                                binding.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(binding.coordinator, binding.appBar, ev);

                                setAppBarLayoutOffset(binding.appBar, (int) -comments_height);
                                binding.linearTherother.setVisibility(View.GONE);
                                selectTitle(binding.txtComment);
                            }
                            break;
                    }
                    return false;
                }
        );


        //商品的点击
        binding.txtProduct.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!binding.txtProduct.isSelected()) {
                                isScrolling = true;
                                binding.nestedScrollView.scrollTo(0, 0);
                                binding.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(binding.coordinator, binding.appBar, ev);
                                binding.appBar.setExpanded(true, true);
                                selectTitle(binding.txtProduct);
                            }
                            break;
                    }
                    return false;
                }
        );

        //详情的点击
        binding.txtDetail.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (binding.relativeTitle.getVisibility() == View.VISIBLE && !isScrolling && !binding.txtDetail.isSelected()) {
                                binding.nestedScrollView.scrollTo(0, 0);
                                binding.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(binding.coordinator, binding.appBar, ev);
                                setAppBarLayoutOffset(binding.appBar, -(int) (binding.appBar.getTotalScrollRange() - getResources().getDimension(R.dimen.dp_50) - DensityUtils.getStatusBarHeight()));
                                selectTitle(binding.txtDetail);
                            }
                            break;
                    }
                    return false;
                }
        );

        binding.nestedScrollView.setFadingEdgeLength(0);
    }

    private void initProduct() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        CommentsAdapter commentsAdapter = new CommentsAdapter();
        commentsAdapter.setDataList(arrayList);
        binding.recyclerViewComment.setAdapter(commentsAdapter);

        //这个方法是在获取商品详情接口后调用的。目的是填充数据，且测量评论区所占高度
        Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            measure(binding.appBar.getTotalScrollRange());
        });
    }


    private void initFragment() {
        manager = getSupportFragmentManager();
        mTabs = new TextView[3];
        mTabs[0] = binding.txtTopBase;
        mTabs[1] = binding.txtTopExplain;
        mTabs[2] = binding.txtTopFuwu;


        mTabs_second = new TextView[3];
        mTabs_second[0] = binding.txtBottomBase;
        mTabs_second[1] = binding.txtBottomExplain;
        mTabs_second[2] = binding.txtBottomFuwu;

        fragment_one = new MyFragment();
        fragment_two = new ExplainFragment(1);
        fragment_three = new ExplainFragment(2);

        list_fragment.add(fragment_one);
        list_fragment.add(fragment_two);
        list_fragment.add(fragment_three);
        switchFragment(R.id.txt_top_base);
    }


    private void initArray() {
        textViews.add(binding.txtProduct);
        textViews.add(binding.txtDetail);
        textViews.add(binding.txtComment);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (Math.abs(i) >= 10) {
            if (binding.relativeTitle.getVisibility() == View.GONE) {
                binding.relativeTitle.setVisibility(View.VISIBLE);
                binding.txtStatus.setVisibility(View.VISIBLE);

                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_detail_come);
                binding.relativeTitle.setAnimation(animation);
                binding.txtStatus.setAnimation(animation);
                animation.start();
            }
        } else {
            if (binding.relativeTitle.getVisibility() == View.VISIBLE) {
                isScrolling = false;
                binding.relativeTitle.setVisibility(View.GONE);
                binding.txtStatus.setVisibility(View.INVISIBLE);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_detail_go);
                binding.relativeTitle.setAnimation(animation);
                binding.txtStatus.setAnimation(animation);
                animation.start();
            }
        }


        if (comments_height != 0 && Math.abs(i) >= comments_height && Math.abs(i) < appBarLayout.getTotalScrollRange() - getResources().getDimension(R.dimen.dp_50) - DensityUtils.getStatusBarHeight()) {
            //选中评论
            if (!binding.txtComment.isSelected()) {
                selectTitle(binding.txtComment);
            }
            binding.linearTherother.setVisibility(View.GONE);
            //选中
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange() - getResources().getDimension(R.dimen.dp_50) - DensityUtils.getStatusBarHeight()) {
            //选中详情
            if (!isCommentClick) {
                if (!binding.txtDetail.isSelected()) {
                    selectTitle(binding.txtDetail);
                }
                binding.linearTherother.setVisibility(View.VISIBLE);
            }
            isCommentClick = false;


        } else {
            if (!binding.txtProduct.isSelected()) {
                selectTitle(binding.txtProduct);
            }
            binding.linearTherother.setVisibility(View.GONE);
        }

    }


    public void measure(int total) {
        if (comments_height == 0) {
            comments_height = total - getResources().getDimension(R.dimen.dp_100) - DensityUtils.getStatusBarHeight() - binding.recyclerViewComment.getHeight();
        }
    }


    public void switchFragment(int id) {
        FragmentTransaction ft = manager.beginTransaction();
        TextView relativeLayout = (TextView) findViewById(id);
        String tag = (String) relativeLayout.getTag();
        Fragment f = manager.findFragmentByTag(tag);
        if (f == null) {
            int num = Integer.parseInt(tag);
            ft.add(R.id.framLayout, list_fragment.get(num), tag);
        }

        for (int i = 0; i < list_fragment.size(); i++) {
            Fragment fragment = list_fragment.get(i);
            if (fragment.getTag() != null) {
                if (fragment.getTag().equals(tag)) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }
        ft.commitAllowingStateLoss();
        switch (id) {
            case R.id.txt_top_base://首页
                index = HOME_ONE;
                break;
            case R.id.txt_top_explain:
                index = HOME_TWO;
                break;
            case R.id.txt_top_fuwu:
                index = HOME_THREE;
                break;
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs_second[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        mTabs_second[index].setSelected(true);
        currentTabIndex = index;
    }


    public void selectTitle(TextView textView) {
        for (int i = 0; i < textViews.size(); i++) {
            if (textView == textViews.get(i)) {
                if (!textViews.get(i).isSelected()) {
                    textViews.get(i).setSelected(true);
                    textViews.get(i).setScaleX(1.3f);
                    textViews.get(i).setScaleY(1.3f);
                }
            } else {
                if (textViews.get(i).isSelected()) {
                    textViews.get(i).setSelected(false);
                    textViews.get(i).setScaleX(1.0f);
                    textViews.get(i).setScaleY(1.0f);
                }
            }

        }
    }


    /**
     * 设置appbar偏移量
     *
     * @param appBar
     * @param offset
     */
    public void setAppBarLayoutOffset(AppBarLayout appBar, int offset) {
        CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {

            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();


            if (topAndBottomOffset != offset) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(appBarLayoutBehavior.getTopAndBottomOffset(), offset);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int offetOther = (int) animation.getAnimatedValue();
                        appBarLayoutBehavior.setTopAndBottomOffset(offetOther);
                        if (binding.relativeTitle.getVisibility() == View.GONE) {
                            binding.relativeTitle.setVisibility(View.VISIBLE);
                            binding.txtStatus.setVisibility(View.VISIBLE);

                            Animation animation_appBarScroll = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_detail_come);
                            binding.relativeTitle.setAnimation(animation_appBarScroll);
                            binding.txtStatus.setAnimation(animation_appBarScroll);
                            animation_appBarScroll.start();
                        }


//                        if (Math.abs(offetOther) >= linearScroll_height - getResources().getDimension(R.dimen.dp_50) - DensityUtils.getStatusBarHeight()) {
//                            binding.linearTherother.setVisibility(View.VISIBLE);
//                        } else {
//                            binding.linearTherother.setVisibility(View.GONE);
//                        }


                    }
                });
                valueAnimator.start();
            }
        }
    }

    @Override
    public void onClick(View v) {
        //防止快速点击
        if (ButtonClickUtils.isFastClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.txt_bottom_base:
            case R.id.txt_top_base:
                switchFragment(R.id.txt_top_base);
                break;


            case R.id.txt_bottom_explain:

            case R.id.txt_top_explain:
                switchFragment(R.id.txt_top_explain);
                break;


            case R.id.txt_bottom_fuwu:
            case R.id.txt_top_fuwu:
                switchFragment(R.id.txt_top_fuwu);
                break;
        }
    }
}
