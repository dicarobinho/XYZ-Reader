package com.example.xyzreader.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.xyzreader.ArticlesListActivityViewModel;
import com.example.xyzreader.R;
import com.example.xyzreader.adapters.ArticlesListAdapter;
import com.example.xyzreader.databinding.ActivityArticleListBinding;
import com.example.xyzreader.model.Article;
import com.example.xyzreader.utils.Constants;
import com.example.xyzreader.utils.NetworkUtils;
import com.example.xyzreader.utils.ResultsDisplay;

import java.util.List;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticlesListActivity extends AppCompatActivity implements ArticlesListAdapter.ListItemClickListener {

    ActivityArticleListBinding binding;
    private ArticlesListActivityViewModel mViewModel;
    ArticlesListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this).get(ArticlesListActivityViewModel.class);
        mAdapter = new ArticlesListAdapter(this, this);
        setupAdapter();
        setupViewModel();
        getArticlesFromServer(false);

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.refresh) {
                getArticlesFromServer(true);
            }
            return false;
        });
    }

    private void setupAdapter() {
        binding.recyclerView.setEmptyView(binding.emptyView);
        binding.recyclerView.setHasFixedSize(true);

        boolean isLand = getResources().getBoolean(R.bool.isLand);
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isLand || isTablet)
            binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, 1));
        else binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));

        binding.recyclerView.setAdapter(mAdapter);
    }

    private void addOrUpdateArticlesInDb(List<Article> articles) {
        // pentru testare split cu "punct".
        //articles.add(new Article(7, "Test Title", "Text Author", "Mihai Eminescu s-a născut la Botoşani la 15 ianuarie 1850. Este al şapte-lea din cei 11 copii ai căminarului Gheorge Eminovici, provenit dintr-o familie de ţărani români din nordul Moldovei şi al Ralucăi Eminovici, născută Juraşcu, fiică de stolnic din Joldeşti. Îşi petrece copilăria la Botoşani şi Ipoteşti, în casa părinteasca şi prin împrejurimi, într-o totală libertate de mişcare şi de contact cu oamenii şi cu natura. Această stare o evocă cu adîncă nostalgie în poezia de mai târziu (\"Fiind băiat…” sau \"O, rămîi\"). Între 1858 şi 1866, urmează cu intermitenţe şcoala la Cernăuţi. Termină clasa a IV-a clasificat al cinci-lea din 82 de elevi după care face 2 clase de gimnaziu. Părăseşte şcoala în 1863, revine ca privatist în 1865 şi pleacă din nou în 1866. Între timp, e angajat ca funcţionar la diverse instituţii din Botoşani (la tribunal şi primărie) sau pribegeşte cu trupa Tardini-Vlădicescu. 1866 este anul primelor manifestări literare ale lui Eminescu. În ianuarie moare profesorul de limba română Aron Pumnul şi elevii scot o broşură, \"Lăcrămioarele invăţăceilor gimnazişti” , în care apare şi poezia \"La mormîntul lui Aron Pumnul” semnată M.Eminovici. La 25 februarie / 9 martie pe stil nou debutează în revista \"Familia”, din Pesta, a lui Iosif Vulcan, cu poezia \"De-aş avea”. Iosif Vulcan îi schimbă numele în Mihai Eminescu, adoptat apoi de poet şi, mai tîrziu, şi de alţi membri ai familiei sale. În acelaşi an îi mai apar în \"Familia” încă 5 poezii. Din 1866 pînă în 1869, pribegeşte pe traseul Cernăuţi-Blaj-Sibiu-Giurgiu-Bucureşti. De fapt, sunt ani de cunoaştere prin contact direct a poporului, a limbii, a obiceiurilor şi a realităţilor româneşti. A intenţionat să-şi continue studiile, dar nu-şi realizează proiectul. Ajunge sufleor şi copist de roluri în trupa lui Iorgu Caragiali apoi sufleor şi copist la Teatrul Naţional unde îl cunoaşte pe I.L.Caragiale. Continuă să publice în \"Familia\", scrie poezii, drame (Mira), fragmente de roman ,\"Geniu pustiu”, rămase în manuscris; face traduceri din germană. Între 1869 şi 1862 este student la Viena. Urmează ca auditor extraordinar Facultatea de Filozofie şi Drept, dar audiază şi cursuri de la alte facultăţi. Activează în rîndul societăţilor studenţeşti, se împrieteneşte cu Ioan Slavici; o cunoaşte la Viena pe Veronica Micle; începe colaborarea la \"Convorbiri Literare”; debutează ca publicist în ziarul \"Albina” din Pesta. Între 1872 şi 1874 este student la Berlin. Junimea îi acordă o bursă cu condiţia să-şi ia doctoratul în filozofie. Urmează cu regularitate două semestre, dar nu se prezintă la examene. Se întoarce în ţară, trăind la Iaşi între 1874-1877. E director al Bibliotecii Centrale, profesor suplinitor, revizor şcolar pentru judeţele Iaşi şi Vaslui, redactor la ziarul \"Curierul de Iaşi “. Continuă să publice în \"Convorbiri Literare”. Devine bun prieten cu Ion Creangă pe care îl introduce la Junimea. Situaţia lui materială este nesigură; are necazuri în familie; este îndrăgostit de Veronica Micle. În 1877 se mută la Bucureşti, unde pînă în 1883 este redactor, apoi redactor-şef la ziarul \"Timpul“. Desfăşoară o activitate publicistică excepţională, tot aici i se ruinează însă sănătatea. Acum scrie marile lui poeme (Scrisorile, Luceafărul etc.). În iunie 1883, surmenat, poetul se îmbolnăveşte grav, fiind internat la spitalul doctorului Şuţu, apoi la un institut pe lîngă Viena. În decembrie îi apare volumul \"Poezii” , cu o prefaţă şi cu texte selectate de Titu Maiorescu (e singurul volum tipărit în timpul vieţii lui Eminescu). Unele surse pun la îndoială boala lui Eminescu şi vin şi cu argumente în acest sens. În anii 1883-1889 Eminescu scrie foarte puţin sau practic deloc. Mihai  Eminescu se stinge din viaţă în condiţii dubioase şi interpretate diferit în mai multe surse la 15 iunie 1889 (15  iunie, în zori - ora 3) în casa de sănătate a doctorului Şuţu. E înmormîntat la Bucureşti, în cimitirul Bellu; sicriul e dus pe umeri de patru elevi de la Şcoala Normală de Institutori.", "https://media-exp1.licdn.com/dms/image/C4D0BAQHiNSL4Or29cg/company-logo_200_200/0?e=2159024400&v=beta&t=0e00tehBFFtuqgUCfAijpOkoBl89jxOTIe_k9HHpi_4", "https://lh3.googleusercontent.com/G5oF0mhpOcQzFTrU6TDUL0JoAjzRt38weiZKua7L61WVT1z3dPcE9gUu-W2EwtM9cZU", (float) 4.5, "publish date test"));
        mViewModel.addArticles(articles);
    }

    public void getArticlesFromServer(boolean refresh) {
        if (NetworkUtils.isConnected(this)) {
            mViewModel.getArticlesFromServer().observe(this, checkResultDisplay -> {
                if (checkResultDisplay != null) {
                    switch (checkResultDisplay.state) {
                        case ResultsDisplay.STATE_LOADING:
                            loadingStateUi();
                            break;
                        case ResultsDisplay.STATE_ERROR:
                            Toast.makeText(this, R.string.error_loading_articles, Toast.LENGTH_LONG).show();
                            break;
                        case ResultsDisplay.STATE_SUCCESS:
                            List<Article> articles = checkResultDisplay.data;
                            assert articles != null;
                            addOrUpdateArticlesInDb(articles);
                            successStateUi(articles.size(), refresh);
                            break;
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            binding.emptyView.setVisibility(View.INVISIBLE);
            successStateUi(1, false);
        }
    }

    private void loadingStateUi() {
        binding.loadingSpinner.setVisibility(View.VISIBLE);
    }

    private void successStateUi(int articlesListSize, boolean refresh) {
        binding.loadingSpinner.setVisibility(View.GONE);
        if (articlesListSize == 0)
            binding.emptyView.setVisibility(View.VISIBLE);
        if (refresh)
            Toast.makeText(this, R.string.articles_update, Toast.LENGTH_SHORT).show();
    }

    private void setupViewModel() {
        mViewModel = new ViewModelProvider(this).get(ArticlesListActivityViewModel.class);
        mViewModel.getArticles().observe(this, articleEntries -> {
            binding.loadingSpinner.setVisibility(View.GONE);
            mAdapter.setArticles(null);
            mAdapter.setArticles(articleEntries);
        });
    }

    @Override
    public void onItemClickListener(int viewIndex) {
        Intent intent = new Intent(ArticlesListActivity.this, ArticleDetailActivity.class);
        intent.putExtra(Constants.ITEMS_LIST_KEY, viewIndex);
        intent.putExtra(Constants.ADAPTER_ITEM_COUNT_KEY, mAdapter.getItemCount());

        //bundle pentru animatie de explosion
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        this.startActivity(intent, bundle);
    }
}
