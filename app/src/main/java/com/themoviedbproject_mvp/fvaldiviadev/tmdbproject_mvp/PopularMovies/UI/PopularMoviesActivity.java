package com.themoviedbproject_mvp.fvaldiviadev.tmdbproject_mvp.PopularMovies.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.themoviedbproject_mvp.fvaldiviadev.tmdbproject_mvp.Data.Network.Models.PopularMovie;
import com.themoviedbproject_mvp.fvaldiviadev.tmdbproject_mvp.PopularMovies.PopularMoviesContract;
import com.themoviedbproject_mvp.fvaldiviadev.tmdbproject_mvp.PopularMovies.Presenter.OnLoadMorePopularMoviesListener;
import com.themoviedbproject_mvp.fvaldiviadev.tmdbproject_mvp.PopularMovies.Presenter.PopularMoviesPresenter;
import com.themoviedbproject_mvp.fvaldiviadev.tmdbproject_mvp.R;
import com.themoviedbproject_mvp.fvaldiviadev.tmdbproject_mvp.Search.UI.SearchActivity;


public class PopularMoviesActivity extends AppCompatActivity implements PopularMoviesContract.View {



    private TextView tvEmptyView;
    private RecyclerView rvPopularMovieList;
    private PopularMovieListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private PopularMoviesContract.Presenter presenter;

    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO inyección de dependecias con Dagger 2
        presenter=new PopularMoviesPresenter(this);

        loadView();

        presenter.loadPopularMovieList();




    }

    private void loadView(){
        tvEmptyView = (TextView) findViewById(R.id.tv_nomovies);
        rvPopularMovieList = (RecyclerView) findViewById(R.id.rv_popularmovielist);
        handler = new Handler();

        rvPopularMovieList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        rvPopularMovieList.setLayoutManager(linearLayoutManager);

        setAdapter();

    }

    private void setAdapter() {
        adapter = new PopularMovieListAdapter(rvPopularMovieList);

        rvPopularMovieList.setAdapter(adapter);

        adapter.setLoading(true);




        adapter.setOnLoadMorePopularMoviesListener(new OnLoadMorePopularMoviesListener() {
            @Override
            public void onLoadMoreMovies() {
                presenter.onLoadMoreMovies();


            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                presenter.startSearch();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void addToList(PopularMovie popularMovie) {
        adapter.addItem(popularMovie);
    }

    @Override
    public void removeLastElement() {
        adapter.removeLastElement();
    }

    @Override
    public void setLoading(boolean loading) {
        adapter.setLoading(loading);
    }

    @Override
    public void showError(String error) {
        tvEmptyView.append(error);
    }

    @Override
    public void navigateToSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void hideList(boolean hide) {
        if(hide){
            rvPopularMovieList.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        }else{
            rvPopularMovieList.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }
}
