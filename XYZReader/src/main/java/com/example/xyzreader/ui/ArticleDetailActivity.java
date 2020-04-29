package com.example.xyzreader.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xyzreader.ArticleDetailActivityViewModel;
import com.example.xyzreader.databinding.ActivityArticleDetailBinding;
import com.example.xyzreader.utils.Constants;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    ActivityArticleDetailBinding binding;
    ArticleDetailActivityViewModel mViewModel;

    private static int selectedArticleIndex;
    private static int adapterItemsNumber;
    MyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityArticleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this).get(ArticleDetailActivityViewModel.class);

        if (getIntent() != null && getIntent().hasExtra(Constants.ITEMS_LIST_KEY) && getIntent().hasExtra(Constants.ADAPTER_ITEM_COUNT_KEY)) {
            selectedArticleIndex = getIntent().getIntExtra(Constants.ITEMS_LIST_KEY, -1);
            adapterItemsNumber = getIntent().getIntExtra(Constants.ADAPTER_ITEM_COUNT_KEY, -1);
            setupMyPagerAdapter();
        }

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                binding.actionUp.animate()
                        .alpha((state == ViewPager.SCROLL_STATE_IDLE) ? 1f : 0f)
                        .setDuration(300);
            }

            public void onPageSelected(int position) {
                updateUpButtonPosition();
            }
        });

        binding.actionUp.setOnClickListener(view -> onBackPressed());
    }

    private void updateUpButtonPosition() {
        int upButtonNormalBottom = binding.actionUp.getHeight();
        int mSelectedItemUpButtonFloor = Integer.MAX_VALUE;
        binding.actionUp.setTranslationY(Math.min(mSelectedItemUpButtonFloor - upButtonNormalBottom, 0));
    }

    private void setupMyPagerAdapter() {
        mPagerAdapter = new MyPagerAdapter(ArticleDetailActivity.this);
        binding.viewPager.setAdapter(mPagerAdapter);
        binding.viewPager.setCurrentItem(selectedArticleIndex - 1, false);
        binding.viewPager.setOffscreenPageLimit(adapterItemsNumber);
    }

    private static class MyPagerAdapter extends FragmentStateAdapter {

        MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return ArticleDetailFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return adapterItemsNumber;
        }
    }
}
