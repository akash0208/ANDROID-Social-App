package com.example.ongraph.socialapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView profile_name, place, home;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView profile_image;
    AppSharedPref appSharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_profile, container, false);

        appSharedPref = new AppSharedPref(getActivity());
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        profile_name = (TextView) view.findViewById(R.id.profile_name);
        place = (TextView) view.findViewById(R.id.profile_live);
        home = (TextView) view.findViewById(R.id.profile_home);
        profile_image = (ImageView) view.findViewById(R.id.profile_image);

        String name = appSharedPref.getName();
        profile_name.setText(name);

        String post=appSharedPref.getImage();

        if (post!=null)
            Picasso.with(getActivity()).load(post).into(profile_image);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new Follower_Fragment(), "Followers");
        adapter.addFragment(new Following_Fragment(), "Followings");
        viewPager.setAdapter(adapter);
    }


}
class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}