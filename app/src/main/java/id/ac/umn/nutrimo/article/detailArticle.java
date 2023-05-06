package id.ac.umn.nutrimo.article;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import id.ac.umn.nutrimo.R;


public class detailArticle extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String url;
    WebView article_wv;
    WebSettings webSettings;


    public detailArticle() {
        // Required empty public constructor
    }
    public detailArticle(String url) {
        this.url = url;
    }
    public static detailArticle newInstance(String param1, String param2) {
        detailArticle fragment = new detailArticle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_detail_article, container, false);
        article_wv = view.findViewById(R.id.article_wv);
        article_wv.setWebViewClient(new WebViewClient());
        article_wv.getSettings().setDomStorageEnabled(true);
        article_wv.getSettings().setLoadsImagesAutomatically(true);
        article_wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        article_wv.loadUrl(url);
        return view;

    }



    public void onBackPressed(){
        AppCompatActivity activity=(AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.articleWrapper, new recfragment()).addToBackStack(null).commit();

    }
}