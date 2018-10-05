package com.jiayue.feelsbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jiayue.feelsbook.EmotionAndComment.EmotionAndComment;
import com.jiayue.feelsbook.bean.Emotion;
import com.jiayue.feelsbook.constans.Config;

import java.util.LinkedList;
import java.util.List;

/**
 * emotion statistics
 */
public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, StatisticsActivity.class);
        context.startActivity(intent);
    }

    private List<TextView> textViewList = new LinkedList<>();

    /**
     * count the number of feelings
     *
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initViews();

        try {
            int[] counts = new int[Config.strEMOTION.length];
            EmotionAndComment emotionAndComment = new EmotionAndComment(this);
            List<Emotion> emotionList = emotionAndComment.getList();
            for (int i = 0; i < emotionList.size(); i++) {
                Emotion emotion = emotionList.get(i);
                for (int j = 0; j < Config.strEMOTION.length; j++) {
                    if (Config.strEMOTION[j].equals(emotion.get_mood())) {
                        int a = counts[j];
                        counts[j] = a + 1;
                    }
                }
            }
            for (int i = 0; i < textViewList.size(); i++) {
                textViewList.get(i).setText(counts[i] + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * show back image,mood and number
     *
     */
    private void initViews() {
        findViewById(R.id.go_back).setOnClickListener(this);

        LinearLayout contianer = findViewById(R.id.container);

        for (int i = 0; i < Config.strEMOTION.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.list_item_statistics, null);
            contianer.addView(view);

            TextView name = view.findViewById(R.id.moods);
            TextView number = view.findViewById(R.id.numbers);

            name.setText(Config.strEMOTION[i]);
            textViewList.add(number);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_back:
                finish();
                break;
        }
    }
}
