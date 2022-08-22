package com.example.projectfinalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {


    ListView listView;
    ArrayList<MovieInfo> list;
    TextView releaseDate;
    TextView vote;
    TextView description;
    public static  String KEY_LIST = "1";
    public static  String KEY_RELEASE = "2";
    public static  String KEY_VOTE = "3";
    public static  String KEY_DESCRIPTION = "4";
    public static  String KEY_POS = "5";
    String strRelease;
    String strVote;
    int pos = -1;

    String title1;
    double voteAv;
    String summary;
    String dateRelease;
    String thumbnail;
    String lang;
    double pop;
    int count;
    public CustomAdapter customAdapter;

    static final int numberCode = 1234;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.id_listView);
        list = new ArrayList<>();
        releaseDate = findViewById(R.id.id_release);
        vote = findViewById(R.id.id_vote);
        description = findViewById(R.id.id_description);


        AsyncThread run = new AsyncThread();
        run.execute();

        ConstraintLayout constraintLayout = findViewById(R.id.layout1);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        if(savedInstanceState != null){
            list = (ArrayList<MovieInfo>) savedInstanceState.getSerializable(KEY_LIST);
            strRelease = savedInstanceState.getString(KEY_RELEASE);
            releaseDate.setText(strRelease);
            strVote = savedInstanceState.getString(KEY_VOTE);
            vote.setText(strVote);
            pos = savedInstanceState.getInt(KEY_POS);
            if (pos>=0) {
                if (description != null)
                    description.setText(list.get(pos).getDescription());
            }

        }

        customAdapter = new CustomAdapter(this, R.layout.adapter_custom, list);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                pos = position;
                releaseDate.setText("Release Date: " + list.get(pos).getReleaseDate());
                vote.setText("Vote Average: " + list.get(pos).getVote());
                if(description!=null)
                    description.setText(list.get(pos).getDescription());
                customAdapter.notifyDataSetChanged();

            }

        });

    }//OnCreate

    public class AsyncThread extends AsyncTask<Void, Void, Void> {
        String data = "";

        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/upcoming?api_key=3527bbbf77accd7f898e9095ba053026&language=en-US&page=1");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                    Log.d("data", data);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                JSONObject json = new JSONObject(data);
                int i = 0;
                list.clear();
                while (i < 15) {
                    title1 = json.getJSONArray("results").getJSONObject(i).getString("title");
                    Log.d("title", title1);
                    voteAv = json.getJSONArray("results").getJSONObject(i).getDouble("vote_average");
                    Log.d("vote", voteAv + "");
                    summary = json.getJSONArray("results").getJSONObject(i).getString("overview");
                    Log.d("summary", summary);
                    dateRelease = json.getJSONArray("results").getJSONObject(i).getString("release_date");
                    Log.d("date", dateRelease);
                    thumbnail = json.getJSONArray("results").getJSONObject(i).getString("poster_path");
                    Log.d("poster", thumbnail);
                    String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;
                    lang = json.getJSONArray("results").getJSONObject(i).getString("original_language");
                    Log.d("lang", lang);
                    pop = json.getJSONArray("results").getJSONObject(i).getInt("popularity");
                    Log.d("popu", pop + "");
                    count = json.getJSONArray("results").getJSONObject(i).getInt("vote_count");
                    Log.d("count", count + "");
                    MovieInfo movie1 = new MovieInfo(poster, title1, summary, dateRelease, voteAv, lang, pop, count);
                    list.add(movie1);
                    Log.d("i", i + "");
                    i++;
                }


                customAdapter.notifyDataSetChanged();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_LIST, list);
        outState.putString(KEY_RELEASE, releaseDate.getText().toString());
        outState.putString(KEY_VOTE, vote.getText().toString());
        if(description!=null) {
            outState.putString(KEY_DESCRIPTION, description.getText().toString());
        }
        outState.putInt(KEY_POS, pos);
    }

    public class CustomAdapter extends ArrayAdapter<MovieInfo>{
        List<MovieInfo> list;
        Context context;
        int xmlResource;


        public CustomAdapter(@NonNull Context context, int resource, @NonNull List<MovieInfo> objects) {
            super(context, resource, objects);
            this.context = context;
            xmlResource = resource;
            list = objects;

        }

        @NonNull        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View adapterView = layoutInflater.inflate(xmlResource, null);
            TextView name = adapterView.findViewById(R.id.id_name);
            ImageView imageView = adapterView.findViewById(R.id.imageView);
            name.setText(list.get(position).getName());


            if(position == pos){
                name.setTextColor(Color.parseColor("#1e90ff"));
            }

            Button begin = adapterView.findViewById(R.id.id_begin);
            final MovieInfo movieInfo = getItem(position);
            begin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, activity_number.class);
                    intent.putExtra("test1", movieInfo.getPopularity());
                    intent.putExtra("test2", movieInfo.getLanguage());
                    intent.putExtra("test3", movieInfo.getCount());
                    startActivityForResult(intent, numberCode);

                }
            });
            Picasso.get().load(movieInfo.getImage()).into(imageView);


            return adapterView;

        }





    }


}
