package com.example.spamdetector;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.caller.info.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    public static Elements title;
    public static ArrayList<String> titleList = new ArrayList<String>();
    public static ArrayAdapter<String> adapter;
    public static ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.rating_list, titleList);
    }

    public static class NewThread extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... arg) {

            Document doc;
            try {
                doc = Jsoup.connect("https://www.neberitrubku.ru/nomer-telefona/"+arg[0]).get();
                title = doc.select("div.ratings ul li");
                titleList.clear();
                if (title.size() == 0){titleList.add(" ");titleList.add("Номер с нейтралльной оценкой");}
                else{
                    titleList.add(" ");
                for (Element li : title) {
                    titleList.add(li.text());
                }}
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String result) {
            lv.setAdapter(adapter);
        }
    }
}

