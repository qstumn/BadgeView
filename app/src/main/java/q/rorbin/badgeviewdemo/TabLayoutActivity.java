package q.rorbin.badgeviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 作者：warm
 * 时间：2017-12-21 13:39
 * 描述：
 */

public class TabLayoutActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tl;
    private ViewPager vp;
    private Button bt;
    private Button btHide;
    List<Badge> badges;
    private String[] titles = {"标题一", "标题二", "标题三", "标题四", "标题五"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);
        tl = (TabLayout) findViewById(R.id.tl);
        vp = (ViewPager) findViewById(R.id.vp);
        bt = (Button) findViewById(R.id.bt);
        btHide= (Button) findViewById(R.id.bt_hide);
        bt.setOnClickListener(this);
        btHide.setOnClickListener(this);
        vp.setAdapter(new VpAdapter(getSupportFragmentManager()));
        tl.setupWithViewPager(vp);
        badges = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {

            Badge badge = new QBadgeView(tl.getContext())
                    .bindTarget(((ViewGroup) tl.getChildAt(0)).getChildAt(i))
                    .setBadgeGravity(Gravity.TOP | Gravity.END)
                    .setBadgeTextSize(12, true)
                    .setBadgePadding(3, true)
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {

                        }
                    });
            badge.setBadgeNumber(1);
            badges.add(badge);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt:
                int i = badges.get(tl.getSelectedTabPosition()).getBadgeNumber();
                badges.get(tl.getSelectedTabPosition()).setBadgeNumber(++i);
                break;
            case R.id.bt_hide:
                for ( Badge badge :badges) {
                    badge.hide(true);
                }
                break;
        }

    }


    class VpAdapter extends FragmentStatePagerAdapter {


        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new MyFragment();
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}
