package webbank.com.busticket;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by Dpshkhnl on 2017-05-01.
 */

public class ContactUs extends HomeActivity {

    WebView webview;
    ProgressDialog progressBar;
    private static final String TAG = "Main";
    Handler uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.drawer_content, frameLayout);
        setTitle("Contact Us");

        webview = (WebView) findViewById(R.id.webNewsEvent);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        progressBar = ProgressDialog.show(ContactUs.this, "", "Loading...");
        String primeDiv = "content_display";
        String html = new String();


        new BackgroundWorker().execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
    }


    private class BackgroundWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            getDarewod();
            return null;
        }

        public void getDarewod() {

            try {
                Document htmlDocument = Jsoup.connect("http://www.databankbooking.com/information/single/contact-us").get();
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