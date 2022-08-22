package com.example.projectfinalapp;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class activity_number extends AppCompatActivity {
    Button done;
    TextView pop;
    TextView lang;
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number1);
        done = findViewById(R.id.id_end);
        pop = findViewById(R.id.id_pop);
        lang = findViewById(R.id.id_lang);
        count = findViewById(R.id.id_count);

        ConstraintLayout constraintLayout = findViewById(R.id.layout2);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        double num = getIntent().getDoubleExtra("test1", 0.0 );
        pop.setText("Popularity: " + num + "");
        String str2 = getIntent().getStringExtra("test2");
        lang.setText("Original Language: " + str2);
        int total = getIntent().getIntExtra("test3", 0);
        count.setText("Vote Count: " + total);

        done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });






    }

}
