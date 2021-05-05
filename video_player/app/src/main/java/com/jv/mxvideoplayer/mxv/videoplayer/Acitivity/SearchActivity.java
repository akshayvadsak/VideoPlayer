package com.jv.mxvideoplayer.mxv.videoplayer.Acitivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Video;

import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants.MEDIA_LIST;


public class SearchActivity extends AppCompatActivity {

    private Activity activity = SearchActivity.this;
    private Toolbar toolbar;
    private RecyclerView rvSearch;
    private ArrayList<Video> searchResultList = new ArrayList<>();
    private SearchAdapter searchAdapter;
    LinearLayout theme;

    public void theme() {
        theme = findViewById(R.id.theme);
        SharedPreferences settings = getSharedPreferences("pref", 0);
        int id = settings.getInt("theme", 3);
        if (theme != null) {
            if (id == 1) {
                theme.setBackgroundResource(R.drawable.theme2);
            } else if (id == 2) {
                theme.setBackgroundResource(R.drawable.theme3);
            } else if (id == 3) {
                theme.setBackgroundResource(R.drawable.theme4);
            } else if (id == 4) {
                theme.setBackgroundResource(R.drawable.theme5);
            } else if (id == 5) {
                theme.setBackgroundResource(R.drawable.theme6);
            } else if (id == 6) {
                theme.setBackgroundResource(R.drawable.theme7);
            } else if (id == 7) {
                theme.setBackgroundResource(R.drawable.theme8);
            } else if (id == 8) {
                theme.setBackgroundResource(R.drawable.theme9);
            } else if (id == 9) {
                theme.setBackgroundResource(R.drawable.theme10);
            } else {
                theme.setBackgroundResource(R.drawable.theme1);

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        theme();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        theme = findViewById(R.id.theme);
        theme();
        bindToolbar();
        rvSearch = findViewById(R.id.rvSearch);
        rvSearch.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        searchAdapter = new SearchAdapter();
        rvSearch.setAdapter(searchAdapter);

        Collections.sort(MEDIA_LIST, new Comparator<Video>() {

            @Override
            public int compare(Video o1, Video o2) {
                File file1 = new File(o1.getData());
                File file2 = new File(o2.getData());
                return file1.getName().compareToIgnoreCase(file2.getName());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_searchable, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_searchable);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(true);
        searchView.requestFocus();
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocusFromTouch();
        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.hint_search) + "</font>"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResultList.clear();
                for (Video video : MEDIA_LIST) {
                    if (video.getData().toLowerCase().contains(query.toLowerCase())) {
                        searchResultList.add(video);
                    }
                }
                if (searchAdapter != null) {
                    searchAdapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchResultList.clear();
                for (Video video : MEDIA_LIST) {
                    if (video.getData().toLowerCase().contains(newText.toLowerCase())) {
                        searchResultList.add(video);
                    }
                }
                if (searchAdapter != null) {
                    searchAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        return true;
    }

    private void bindToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.txtToolbarTitle)).setText(null);
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.adapter_search_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            File file = new File(searchResultList.get(position).getData());
            holder.txtName.setText(file.getName());
            holder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = searchResultList.get(position).getData();
                    File file = new File(s);
                    if (file.exists()) {
                        Intent intent = new Intent(activity, ExoPlayerActivity.class);
                        intent.setData(Uri.fromFile(file));
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return searchResultList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtName;

            public ViewHolder(View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txtSearchItem);
            }
        }
    }
}