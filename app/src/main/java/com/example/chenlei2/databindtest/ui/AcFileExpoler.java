package com.example.chenlei2.databindtest.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.chenlei2.databindtest.BR;
import com.example.chenlei2.databindtest.CyrucApplication;
import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.model.db.MFile;
import com.example.chenlei2.databindtest.model.db.MMediaFile;
import com.example.chenlei2.databindtest.model.util.DbManager;
import com.example.chenlei2.databindtest.model.util.DbOrmHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.chenlei2.databindtest.model.db.MFile.TYPE.*;

public class AcFileExpoler extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_file_expoler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                               .setAction("Action", null).show();
                                   }
                               }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ac_file_expoler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private List<MFile> values = new ArrayList<>();

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            int caseValue = getArguments().getInt(ARG_SECTION_NUMBER);
            DbOrmHelper dbOrmHelper = DbManager.getInstance().getOrmHelper(CyrucApplication.DB_NAME);
            switch (caseValue){
                case  1:
                    try {
                        List<MMediaFile> mediaFiles = dbOrmHelper.getDaoEx(MMediaFile.class).queryBuilder().where().eq("type", audio).query();
                        values.addAll(mediaFiles);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case  2:
                    try {
                        List<MMediaFile> mediaFiles = dbOrmHelper.getDaoEx(MMediaFile.class).queryBuilder().where().eq("type", video).query();
                        values.addAll(mediaFiles);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case  3:
                    try {
                        List<MFile> mediaFiles = dbOrmHelper.getDaoEx(MFile.class).queryBuilder().where().eq("type", img).query();
                        values.addAll(mediaFiles);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        /*    View rootView = inflater.inflate(R.layout.fragment_ac_file_expoler, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/


            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fg_file_explore, container, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new FileAdapter(values, getActivity()));
            return recyclerView;
        }
    }

    public static class FileAdapter extends RecyclerView.Adapter {
        List<MFile> values;
        Context context;

        FileAdapter(List<MFile> values, Context context) {
            this.values = values;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                    .from(context), R.layout.item_common_file, parent, false);
            ViewHolder holder = new ViewHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder holder1 = (ViewHolder) holder;
            final MFile mFile = values.get(position);
            holder1.getBinding().setVariable(BR.file, mFile);
            holder1.getBinding().executePendingBindings();
            ((ViewHolder) holder).getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (mFile.getType()){
                        case audio:
                            Intent intent = new Intent(context,AcAudioPlay.class);
                            intent.putExtra(AcAudioPlay.KEY_MUSIC,mFile);
                            context.startActivity(intent);
                            break;
                        case video:
                            break;
                        case img:
                            break;
                    }

                }
            });

        }


        @Override
        public int getItemCount() {
            return values.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ViewDataBinding binding;

            public ViewHolder(View itemView) {
                super(itemView);
            }

            public ViewDataBinding getBinding() {
                return this.binding;
            }

            public void setBinding(ViewDataBinding binding) {
                this.binding = binding;
            }
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "音频";
                case 1:
                    return "视频";
                case 2:
                    return "图片";
            }
            return null;
        }
    }
}
