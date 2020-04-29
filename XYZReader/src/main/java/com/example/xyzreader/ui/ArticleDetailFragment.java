package com.example.xyzreader.ui;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xyzreader.ArticleDetailActivityViewModel;
import com.example.xyzreader.R;
import com.example.xyzreader.adapters.ArticleDetailFragmentAdapter;
import com.example.xyzreader.databinding.FragmentArticleDetailBinding;
import com.example.xyzreader.model.Article;
import com.example.xyzreader.utils.Constants;
import com.example.xyzreader.utils.DatesUtils;
import com.example.xyzreader.utils.SharedPreferenceUtils;
import com.example.xyzreader.utils.TextDocumentUtils;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticlesListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends ListFragment {

    private FragmentArticleDetailBinding binding;
    private ArticleDetailActivityViewModel mViewModel;

    private int selectedItemPosition = -1;
    private int recyclerViewPosition = 0;

    private Article currentArticle;
    private View mRootView;

    private boolean itemSuccessfullyGotFromDb = false;

    private ArticleDetailFragmentAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    static ArticleDetailFragment newInstance(int position) {
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.POSITION, position);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ArticleDetailActivityViewModel.class);

        assert getArguments() != null;
        if (getArguments().containsKey(Constants.POSITION)) {
            selectedItemPosition = getArguments().getInt(Constants.POSITION);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArticleDetailBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();

        getSpecificItemFromDb();

        binding.shareFab.setOnClickListener(v -> {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType(getString(R.string.application_txt));
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(TextDocumentUtils.generateTxtFile(getActivityCast(),
                    currentArticle.getTitle() + getString(R.string.txt_extension),
                    currentArticle.getBody())));

            if (intentShareFile.resolveActivity(getActivityCast().getPackageManager()) != null)
                startActivity(Intent.createChooser(intentShareFile, getString(R.string.Article)));
            else Toast.makeText(getContext(), R.string.no_app_found, Toast.LENGTH_SHORT).show();
        });

        // In functie de cat am dat scroll, se verifica daca nu cumva am ajuns sau nu am depasit ultima pozitie
        // la care am ramas ultima data. Toate aceste verificari se fac pentru a sti cand dori msa afisam butonul de
        // "scroll down", acel buton care ne duce la ultimul paragraf citit.
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    recyclerViewPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                    int sharedPreferencePosition = SharedPreferenceUtils.getIdFromSharedPreference(getContext(), currentArticle.getTitle());

                    if (recyclerViewPosition >= sharedPreferencePosition) {
                        SharedPreferenceUtils.saveToSharedPreference(getContext(), recyclerViewPosition, currentArticle.getTitle());
                        binding.goToLastTextFragment.setVisibility(View.GONE);
                    } else if (recyclerViewPosition + Integer.parseInt(getResources().getString(R.string.recyclerView_position_error)) < sharedPreferencePosition) {
                        binding.goToLastTextFragment.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        binding.goToLastTextFragment.setOnClickListener(v -> {
            if (SharedPreferenceUtils.getIdFromSharedPreference(getContext(), currentArticle.getTitle()) != -1) {
                binding.recyclerView.scrollToPosition(SharedPreferenceUtils.getIdFromSharedPreference(getContext(), currentArticle.getTitle()) + 4);
                binding.appBarLayout.setExpanded(false);
                binding.goToLastTextFragment.setVisibility(View.GONE);
            }
        });

        return mRootView;
    }

    private void getSpecificItemFromDb() {
        itemSuccessfullyGotFromDb = false;
        mViewModel.getSpecificArticleFromDb(selectedItemPosition + 1).observe(getViewLifecycleOwner(), articleFromDb -> {
            if (articleFromDb != null && !itemSuccessfullyGotFromDb) {
                itemSuccessfullyGotFromDb = true;
                currentArticle = articleFromDb;
                setupGoToTextFragmentButton();
                mAdapter = new ArticleDetailFragmentAdapter(getContext(), currentArticle.getTitle());
                setupAdapter();
                bindViews();
            }
        });
    }

    private ArticleDetailActivity getActivityCast() {
        return (ArticleDetailActivity) getActivity();
    }

    private void setupAdapter() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivityCast()));
        binding.recyclerView.setAdapter(mAdapter);
    }

    private void setupGoToTextFragmentButton() {
        if (SharedPreferenceUtils.getIdFromSharedPreference(getContext(), currentArticle.getTitle()) != -1) {
            binding.goToLastTextFragment.setVisibility(View.VISIBLE);
        } else binding.goToLastTextFragment.setVisibility(View.GONE);
    }

    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        if (currentArticle != null) {
            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);

            binding.toolbarContainer.setTitle(currentArticle.getTitle());

            Date publishedDate = DatesUtils.formatDate(currentArticle.getPublishedDate());
            if (!publishedDate.before(DatesUtils.START_OF_EPOCH.getTime())) {
                binding.toolbarContainer.setSubtitle(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + getString(R.string.by_font)
                                + currentArticle.getAuthor()
                                + getString(R.string._font)));

            } else {
                // If date is before 1902, just show the string
                binding.toolbarContainer.setSubtitle(Html.fromHtml(
                        DatesUtils.outputFormat().format(publishedDate) + getString(R.string.by_font)
                                + currentArticle.getAuthor()
                                + getString(R.string._font)));
            }

            Picasso.get().load(currentArticle.getPhoto()).into(binding.photo);

            // Impartim continutul articolului in array de strings.
            // Acel string de caractere dupa care se face despartirea text-ului poate sa difere.
            // Textele pe care le avem in API au toate cam aceeasi configuratie, mai exact exista cel putin un "Constant.SPLIT_BODY_TEXT".
            // O problema apare atunci cand text-ul preluat din API, este curat. In acel caz, despartirea text-ului se poate face la "punct".
            // Aceasta problema a fost rezolvata cu urmatorul "if" prezent mai jos.
            // Consider ca poate nu e solutia cea mai optima dar va fi testata.
            // Daca inca mai exista acest comentariu aici, inseamna ca solutia a fost testata si totodata functioneaza. :)
            String[] texts;
            if (currentArticle.getBody().contains(Constants.DEFAULT_SPLIT_BODY_TEXT)) {
                texts = currentArticle.getBody().split(Constants.DEFAULT_SPLIT_BODY_TEXT);
            } else {
                texts = currentArticle.getBody().split(Constants.SIMPLE_SPLIT_BODY_TEXT);

                //posibil sa fie o solutie ineficienta (se poate lua ca si o solutie temporara)
                for (int i = 0; i < texts.length; i++)
                    texts[i] += ".";
            }
            // populam lista de string-uri care va fi legata de adaptor
            List<String> strings = new ArrayList<>(Arrays.asList(texts));

            mAdapter.setTexts(strings);

        } else {
            mRootView.setVisibility(View.GONE);
            binding.toolbar.setTitle(Constants.NA);
        }
    }
}
