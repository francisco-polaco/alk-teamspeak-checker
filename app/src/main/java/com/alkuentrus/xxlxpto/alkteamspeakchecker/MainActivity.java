package com.alkuentrus.xxlxpto.alkteamspeakchecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private static final String WEB_TS_CHECKER = "http://web.ist.utl.pt/~ist179719/ts/mobile.html";
    private WebView mWebView;
    private FloatingActionButton mRefreshButton;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mWebView = (WebView) findViewById(R.id.webView);
        mRefreshButton = (FloatingActionButton) findViewById(R.id.refresh);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        prepareWebView();

        setEventOnClickListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkInternetConnection(true);

    }

    private void setEventOnClickListeners(){
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //your method to refresh content
                checkInternetConnection(false);
            }
        });
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection(false);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove_empty) {
            mWebView.loadUrl("javascript:TSV.ViewerScript.Loader.toggleEmptyChannels(1072685)");
            item.setChecked(!item.isChecked());
        }

        return super.onOptionsItemSelected(item);
    }
    private void prepareWebView(){
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { // 19
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
    }

    private void checkInternetConnection(boolean onStart){
        if(!AppStatus.isInternetAvainable(getApplicationContext())){
           showDialog();
        }else {
            if(onStart) mWebView.loadUrl(WEB_TS_CHECKER);
            else mWebView.loadUrl("javascript:TSV.ViewerScript.Loader.refresh(1072685)");
        }
        if(mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private void showDialog(){
        String message = getResources().getString(R.string.message_connection_dialog);
        String positiveMsg = getResources().getString(R.string.positivemsg_connection_dialog);
        String negativeMsg = getResources().getString(R.string.negativemsg_connection_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.recreate();
                    }
                })
                /*.setNeutralButton(neutralMsg, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })*/
                .setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
