package com.example.xyzreader.adapters;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xyzreader.R;
import com.example.xyzreader.databinding.ListItemArticleBinding;
import com.example.xyzreader.model.Article;
import com.example.xyzreader.utils.Constants;
import com.example.xyzreader.utils.DatesUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ViewHolder> {

    private final ListItemClickListener mOnClickListener;
    private Context context;

    public interface ListItemClickListener {
        void onItemClickListener(int viewIndex);
    }

    private List<Article> mArticles = new ArrayList<Article>() {
    };

    public ArticlesListAdapter(Context context, ListItemClickListener listener) {
        this.context = context;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        return new ViewHolder(view);
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
        notifyDataSetChanged();
    }

//    public List<Article> getArticles() {
//        return mArticles;
//    }

    @Override
    public int getItemCount() {
        return mArticles != null ? mArticles.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Article currentArticle = mArticles.get(position);

        Picasso.get().load(currentArticle.getThumb()).into(holder.binding.thumbnail);

        holder.binding.articleTitle.setText(currentArticle.getTitle());

        Date publishedDate = DatesUtils.formatDate(currentArticle.getPublishedDate());

        if (!publishedDate.before(DatesUtils.START_OF_EPOCH.getTime())) {
            holder.binding.articleSubtitle.setText(Html.fromHtml(DateUtils.getRelativeTimeSpanString(publishedDate.getTime(),
                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString() +
                    Constants.HTML_BR + context.getString(R.string.by) +
                    currentArticle.getAuthor()));
        } else {
            holder.binding.articleSubtitle.setText(Html.fromHtml(DatesUtils.outputFormat().format(publishedDate) +
                    Constants.HTML_BR + context.getString(R.string.by) + currentArticle.getAuthor()));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ListItemArticleBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = ListItemArticleBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int viewIndex = getAdapterPosition();
            mOnClickListener.onItemClickListener(mArticles.get(viewIndex).getId());
        }
    }
}


