package com.maibai.cash.fragment;

import android.view.View;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseFragment;
import com.maibai.cash.view.BubbleSeekBar;


public class HomeFragment extends BaseFragment {

    private BubbleSeekBar bubble_seekbar_home;

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void findViews(View rootView) {
        bubble_seekbar_home = (BubbleSeekBar) rootView.findViewById(R.id.bubble_seekbar_home);
        initBubbleSeekBar();
    }

    @Override
    protected void setListensers() {

    }

    @Override
    protected void initVariable() {

    }

    private void initBubbleSeekBar() {
        bubble_seekbar_home.getConfigBuilder()
                .min(500)
                .max(3000)
//                .progress(20)
                .sectionCount(5)
//                .trackColor(ContextCompat.getColor(getContext(), R.color.color_gray))
//                .secondTrackColor(ContextCompat.getColor(getContext(), R.color.color_blue))
//                .thumbColor(ContextCompat.getColor(getContext(), R.color.color_blue))
//                .showSectionText()
//                .sectionTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
//                .sectionTextSize(18)
//                .showThumbText()
//                .thumbTextColor(ContextCompat.getColor(getContext(), R.color.color_red))
//                .thumbTextSize(18)
//                .bubbleColor(ContextCompat.getColor(getContext(), R.color.color_green))
//                .bubbleTextSize(18)
                .showSectionMark()
                .seekBySection()
                .autoAdjustSectionMark()
                .sectionTextPosition(BubbleSeekBar.TextPosition.BOTTOM_SIDES)
                .build();
    }

}
