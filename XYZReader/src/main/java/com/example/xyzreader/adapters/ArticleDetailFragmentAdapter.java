package com.example.xyzreader.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xyzreader.R;
import com.example.xyzreader.databinding.ListItemBodyArticleBinding;
import com.example.xyzreader.utils.Constants;
import com.example.xyzreader.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailFragmentAdapter extends RecyclerView.Adapter<ArticleDetailFragmentAdapter.ViewHolder> {

    private Context context;
    private String articleTitle;

    private List<String> mBodyTexts = new ArrayList<String>() {
    };

    public ArticleDetailFragmentAdapter(Context context, String articleTitle) {
        this.context = context;
        this.articleTitle = articleTitle;
    }

    @NonNull
    @Override
    public ArticleDetailFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_body_article, parent, false);
        return new ViewHolder(view);
    }

    public void setTexts(List<String> texts) {
        mBodyTexts = texts;
        notifyDataSetChanged();
    }

//    public List<String> getBodyTexts() {
//        return mBodyTexts;
//    }

    @Override
    public int getItemCount() {
        return mBodyTexts != null ? mBodyTexts.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleDetailFragmentAdapter.ViewHolder holder, final int position) {
        String currentText = mBodyTexts.get(position);
        holder.binding.bodyText.setText(Html.fromHtml(currentText.replaceAll(Constants.ARTICLE_BODY_TEXT_REPLACE_ALL, Constants.ARTICLE_BODY_TEXT_REPLACEMENT)));
        if (position == 0)
            formatText(holder.binding.bodyText, true);
        else
            formatText(holder.binding.bodyText, false);

        //verificare daca pozitia curenta indica ultimul paragraf citit
        if (position != SharedPreferenceUtils.getIdFromSharedPreference(context, articleTitle)) {
            holder.binding.markLastParagraph.setVisibility(View.GONE);
        } else holder.binding.markLastParagraph.setVisibility(View.VISIBLE);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ListItemBodyArticleBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = ListItemBodyArticleBinding.bind(itemView);
        }
    }

    private void formatText(TextView itemTextView, boolean firstItem) {
        if (firstItem) {
            float scale = context.getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (Constants.STANDARD_MARGIN * scale + 0.5f);
            itemTextView.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, 0);
        }
        itemTextView.setTypeface(Typeface.createFromAsset(context.getResources().getAssets(), context.getString(R.string.path_rosario_regular_ttf)));
    }
}
