package com.example.jiayu.pengyou_version2;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Jiayu on 4/1/18.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return  requestsFragment;

            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return  chatsFragment;
            case 2:
            FriendsFragment friendsFragment = new FriendsFragment();
            return  friendsFragment;

            default:
                return null;
        }



    }

    @Override
    public int getCount() {

        //return 3 for 3 tabs
        return 3;
    }


    public CharSequence getPageTitle(int position){
        switch (position) {
            case 0:
                return "Requests";
            case 1:
                return "Chats";
            case 2:
                return "Friends";

            default:
                return null;
        }
    }
}
