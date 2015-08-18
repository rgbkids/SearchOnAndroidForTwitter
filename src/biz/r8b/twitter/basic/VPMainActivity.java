package biz.r8b.twitter.basic;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;


/**
 * ぬるぬる動く
 */
public class VPMainActivity extends FragmentActivity {
    MyFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_main);

        // ViewPager のインスタンスを取得
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        // FragmentPagerAdapter を継承したクラスのアダプターを作成し、ViewPager にセット
        adapter = new MyFragmentPagerAdapter(this, viewPager);

        // タブ選択時のActivityの設定
        adapter.addTab(TwQueryListActivity.class, null, "Atab");
//        adapter.addTab(TwQueryListActivity.class, null, "Btab");
//        adapter.addTab(TwQueryListActivity.class, null, "Ctab");
        viewPager.setAdapter(adapter);

        // タブ境界線設定
        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
        // タブの下線を有効
        pagerTabStrip.setDrawFullUnderline(true);
        // Tabのライン色の設定
        pagerTabStrip.setTabIndicatorColor(Color.BLUE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    /**
     * PageAdapter。
     * タブ内の切り替え制御、表示Fragment情報管理を行う。
     */
    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private final Context mContext;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        /**
         * タブ内の表示するv4.Fragment、引数、タイトルの保持を行う
         */
        private static final class TabInfo {
            private final Class<?> clazz;
            private final Bundle args;
            private final String tabTitle;

            /**
             * タブ内のActivity、引数を設定する。
             * @param clazz タブ内のv4.Fragment
             * @param args タブ内のv4.Fragmentに対する引数
             * @param tabTitle タブに表示するタイトル
             */
            TabInfo(Class<?> clazz, Bundle args, String tabTitle) {
                this.clazz = clazz;
                this.args = args;
                this.tabTitle = tabTitle;
            }
        }

        public MyFragmentPagerAdapter(FragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mViewPager = pager;
            mViewPager.setAdapter(this);
        }

        /**
         * タブ内に起動するActivity、引数、タイトルを設定する
         * @param clazz 起動するv4.Fragmentクラス
         * @param args v4.Fragmentに対する引数
         * @param tabTitle タブのタイトル
         */
        public void addTab(Class<?> clazz, Bundle args, String tabTitle) {
            TabInfo info = new TabInfo(clazz, args, tabTitle);
            mTabs.add(info);
            notifyDataSetChanged();
        }

        /**
         * タイトルを返す。
         * フレームワークからの呼び出しがされるため、任意に呼び出す必要はない。
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).tabTitle;
        }

        /**
         * タブの総数を返す。
         * フレームワークからの呼び出しがされるため、任意に呼び出す必要はない。
         */
        @Override
        public int getCount() {
            return mTabs.size();
        }

        /**
         * タブ内のv4.Fragmentを返す。
         * フレームワークからの呼び出しがされるため、任意に呼び出す必要はない。
         */
        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clazz.getName(), info.args);
        }


        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

    }
}