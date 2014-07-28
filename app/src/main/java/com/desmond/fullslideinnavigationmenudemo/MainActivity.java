package com.desmond.fullslideinnavigationmenudemo;

/**
 * Based on tutorial & library
 * LINK: http://www.michenux.net/android-sliding-menu-part-1-657.html
 * LINK: https://github.com/jfeinstein10/SlidingMenu
 *
 * Library Import Instruction:
 * LINK: http://www.crowbarsolutions.com/importing-libraries-into-android-studio/
 */


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private SlidingMenu mSlidingMenu;
    private int mPositionOpened = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer();
//        attachListViewToDrawer();
        attachExpandableListView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mSlidingMenu.toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSlidingMenu.isMenuShowing()) {
            mSlidingMenu.toggle();
        } else {
            super.onBackPressed();
        }
    }

    private void setupDrawer() {
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setBackgroundResource(R.color.purple_dark);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
    }

    private void attachListViewToDrawer() {
        ListView drawerList = (ListView) LayoutInflater.from(this).inflate(R.layout.navigation_listview, null);
        drawerList.setAdapter(new NavigationDrawerAdapter(this));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSlidingMenu.toggle();
            }
        });

        mSlidingMenu.setMenu(drawerList);
    }

    private void attachExpandableListView() {
        List<Section> sectionList = createMenu();

        final ExpandableListView expandableListView =
                (ExpandableListView) LayoutInflater.from(this).inflate(R.layout.navigation_expandablelistview, null);

        // http://stackoverflow.com/questions/5132699/android-how-to-change-the-position-of-expandablelistview-indicator
        // to set the group indicator to the position require
        expandableListView.setGroupIndicator(null);

        NavigationSectionListAdapter sectionListAdapter = new NavigationSectionListAdapter(this, sectionList);
        expandableListView.setAdapter(sectionListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (expandableListView.isGroupExpanded(mPositionOpened)) {
                    expandableListView.collapseGroup(mPositionOpened);

                    if (mPositionOpened == groupPosition)
                        return true;
                }
                mPositionOpened = groupPosition;
                expandableListView.expandGroup(groupPosition);
                expandableListView.smoothScrollToPositionFromTop(groupPosition, 0, 500);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                mSlidingMenu.toggle();
                Handler handler = new Handler();

                switch ((int) id) {
                    case 801:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent fadeInActivity = new Intent(MainActivity.this, FadeInActivity.class);
                                startActivity(fadeInActivity);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }, 300);
                        break;
                    case 802:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent slideInActivity = new Intent(MainActivity.this, LeftToRightActivity.class);
                                startActivity(slideInActivity);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            }
                        }, 300);
                        break;
                    case 803:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent fadeInActivity = new Intent(MainActivity.this, SlideBtmToTopActivity.class);
                                startActivity(fadeInActivity);
                                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                            }
                        }, 300);
                        break;
                }

                return true;
            }
        });


        mSlidingMenu.setMenu(expandableListView);
    }

    private List<Section> createMenu() {
        List<Section> sectionList = new ArrayList<Section>();

        Section oDemoSection = new Section("Demos");
        oDemoSection.addSectionItem(101,"List/Detail (Fragment)", "slidingmenu_friends");
        oDemoSection.addSectionItem(102, "Airport (AsyncTask)", "slidingmenu_airport");

        Section oGeneralSection = new Section("General");
        oGeneralSection.addSectionItem(201, "Settings", "slidingmenu_settings");
        oGeneralSection.addSectionItem(202, "Rate this app", "slidingmenu_rating");
        oGeneralSection.addSectionItem(203, "Eula", "slidingmenu_eula");
        oGeneralSection.addSectionItem(204, "Quit", "slidingmenu_quit");

        Section oSettingSection = new Section("Settings");
        oSettingSection.addSectionItem(301, "Settings", "slidingmenu_settings");
        oSettingSection.addSectionItem(302, "Rate this app", "slidingmenu_rating");
        oSettingSection.addSectionItem(303, "Eula", "slidingmenu_eula");
        oSettingSection.addSectionItem(304, "Quit", "slidingmenu_quit");

        Section oMoreSection = new Section("More");
        oMoreSection.addSectionItem(401, "Settings", "slidingmenu_settings");
        oMoreSection.addSectionItem(402, "Rate this app", "slidingmenu_rating");
        oMoreSection.addSectionItem(403, "Eula", "slidingmenu_eula");
        oMoreSection.addSectionItem(404, "Quit", "slidingmenu_quit");

        Section oFunSection = new Section("Fun");
        oFunSection.addSectionItem(501, "Settings", "slidingmenu_settings");
        oFunSection.addSectionItem(502, "Rate this app", "slidingmenu_rating");
        oFunSection.addSectionItem(503, "Eula", "slidingmenu_eula");
        oFunSection.addSectionItem(504, "Quit", "slidingmenu_quit");

        Section oConnectSection = new Section("Connect");
        oConnectSection.addSectionItem(601, "Settings", "slidingmenu_settings");
        oConnectSection.addSectionItem(602, "Rate this app", "slidingmenu_rating");
        oConnectSection.addSectionItem(603, "Eula", "slidingmenu_eula");
        oConnectSection.addSectionItem(604, "Quit", "slidingmenu_quit");

        Section oCallSection = new Section("Call");
        oCallSection.addSectionItem(701, "Settings", "slidingmenu_settings");
        oCallSection.addSectionItem(702, "Rate this app", "slidingmenu_rating");
        oCallSection.addSectionItem(703, "Eula", "slidingmenu_eula");
        oCallSection.addSectionItem(704, "Quit", "slidingmenu_quit");

        Section oPlaySection = new Section("Play");
        oPlaySection.addSectionItem(701, "Settings", "slidingmenu_settings");
        oPlaySection.addSectionItem(702, "Rate this app", "slidingmenu_rating");
        oPlaySection.addSectionItem(703, "Eula", "slidingmenu_eula");
        oPlaySection.addSectionItem(704, "Quit", "slidingmenu_quit");

        Section oQuitSection = new Section("Quit");
        oQuitSection.addSectionItem(701, "Settings", "slidingmenu_settings");
        oQuitSection.addSectionItem(702, "Rate this app", "slidingmenu_rating");
        oQuitSection.addSectionItem(703, "Eula", "slidingmenu_eula");
        oQuitSection.addSectionItem(704, "Quit", "slidingmenu_quit");

        Section oStopSection = new Section("Stop");
        oStopSection.addSectionItem(701, "Settings", "slidingmenu_settings");
        oStopSection.addSectionItem(702, "Rate this app", "slidingmenu_rating");
        oStopSection.addSectionItem(703, "Eula", "slidingmenu_eula");
        oStopSection.addSectionItem(704, "Quit", "slidingmenu_quit");

        Section oRepeatSection = new Section("Repeat");
        oRepeatSection.addSectionItem(701, "Settings", "slidingmenu_settings");
        oRepeatSection.addSectionItem(702, "Rate this app", "slidingmenu_rating");
        oRepeatSection.addSectionItem(703, "Eula", "slidingmenu_eula");
        oRepeatSection.addSectionItem(704, "Quit", "slidingmenu_quit");

        Section oAnimationSection = new Section("Animation");
        oAnimationSection.addSectionItem(801, "Fade", "slidingmenu_settings");
        oAnimationSection.addSectionItem(802, "Slide from Left", "slidingmenu_rating");
        oAnimationSection.addSectionItem(803, "Slide from bottom", "slidingmenu_eula");

        sectionList.add(oDemoSection);
        sectionList.add(oGeneralSection);
        sectionList.add(oSettingSection);
        sectionList.add(oMoreSection);
        sectionList.add(oFunSection);
        sectionList.add(oConnectSection);
        sectionList.add(oPlaySection);
        sectionList.add(oQuitSection);
        sectionList.add(oStopSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oAnimationSection);

        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);
        sectionList.add(oRepeatSection);


        return sectionList;
    }
}
