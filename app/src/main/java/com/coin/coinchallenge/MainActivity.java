package com.coin.coinchallenge;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static Context context;
    private static MainActivity instance;

    private DataDownloadAsyncTask task;
    private CreditCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        task = new DataDownloadAsyncTask(new DataDownloadAsyncTask.DataDownloadCallback() {
            @Override
            public void onDataCompiled(List<CreditCard> data) {
                //since additions/ deletions are unknown, just remake the list every time.
                adapter.clear();
                adapter.addAll(data);
                adapter.notifyDataSetChanged();
            }
        });
        task.execute();

        //Make sure there is never an empty list (this is just for empty list cases)
        List<CreditCard> cards = new ArrayList<CreditCard>();
        final CreditCard card = new CreditCard();
        cards.add(card);

        ListView cardListView = (ListView) findViewById(R.id.cc_list_view);
        adapter = new CreditCardAdapter(context, cards);
        cardListView.setAdapter(adapter);

    }

    public static MainActivity getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reload) {
            task = null;
            task = new DataDownloadAsyncTask(new DataDownloadAsyncTask.DataDownloadCallback() {
                @Override
                public void onDataCompiled(List<CreditCard> data) {
                    adapter.clear();
                    adapter.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            });
            task.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
