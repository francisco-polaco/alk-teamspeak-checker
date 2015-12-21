package com.alkuentrus.xxlxpto.alkteamspeakchecker;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String WEB_TS_CHECKER = "http://web.ist.utl.pt/~ist179719/ts/mobile.html";
    private WebView mWebView;
    private FloatingActionButton mRefreshButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mWebView = (WebView) findViewById(R.id.webView);
        mRefreshButton = (FloatingActionButton) findViewById(R.id.refresh);

        prepareWebView();

        setEventOnClickListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkConnectionInternet();

    }

    private void setEventOnClickListeners(){
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mWebView.reload();
                checkConnectionInternet();
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
       /* TODO: Quando implementar o acerca descomentar isto
       if (id == R.id.action_about) {
            Intent i = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(i);
        }*/

        return super.onOptionsItemSelected(item);
    }
    private void prepareWebView(){

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

    }

    private void checkConnectionInternet(){
        /*
        Verifica se ha ligacao a internet, se houver carrega o endereço da pagina da applet,
        senao apresenta o dialogo de aviso.
         */
        if(!isInternetAvainable()){
           showDialog();
        }else {
            mWebView.loadUrl(WEB_TS_CHECKER);
        }
    }

    private boolean isInternetAvainable(){
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
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
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })
                .setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
