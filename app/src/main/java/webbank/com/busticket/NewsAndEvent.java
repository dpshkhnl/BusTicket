package webbank.com.busticket;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Dpshkhnl on 2017-05-01.
 */

public class NewsAndEvent extends HomeActivity {

    WebView webview;
    ProgressDialog progressBar;
    private static final String TAG = "Main";
    Handler uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.drawer_content, frameLayout);
        setTitle("News And Event");

        webview = (WebView) findViewById(R.id.webNewsEvent);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        progressBar = ProgressDialog.show(NewsAndEvent.this, "", "Loading...");
        String primeDiv = "content_display";
        String html = new String();


        new BackgroundWorker().execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
       // navigationView.getMenu().getItem(7).setChecked(true);
    }


    private class BackgroundWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            getDarewod();
            return null;
        }

        public void getDarewod() {

            try {
                Document htmlDocument = Jsoup.connect("http://www.databankbooking.com/information/archive/news-and-events").get();
                Element element = htmlDocument.select("#content_display").first();

                // replace body with selected element
                htmlDocument.body().empty().append(element.toString());
                final String html = htmlDocument.toString();

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        webview.loadData(html, "text/html", "UTF-8");
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}