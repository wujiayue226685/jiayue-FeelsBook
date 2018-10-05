package com.jiayue.feelsbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiayue.feelsbook.EmotionAndComment.EmotionAndComment;
import com.jiayue.feelsbook.adapter.Adapter;
import com.jiayue.feelsbook.bean.Emotion;

/**
 * Show all emotion past.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private EmotionAndComment emotionAndComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        emotionAndComment = new EmotionAndComment(this);

        //load data
        adapter.setData(emotionAndComment.getList());
    }

    /**
     * Delete and edit comments, adapters
     *
     */
    private void initViews() {
        findViewById(R.id.create_button).setOnClickListener(this);
        findViewById(R.id.statistics_button).setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter();
        adapter.setListener(new Adapter.Listener() {
            @Override
            public void Delete(String time, int i) {
                emotionAndComment.delete(time);
                adapter.getData().remove(i);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void Edit(String time, int i) {
                Emotion emotion = adapter.getData().get(i);
                if (emotion != null) {
                    CreateEmotionActivity.start(MainActivity.this, emotion);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * start activity
     *
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_button:
                CreateEmotionActivity.start(MainActivity.this);
                break;
            case R.id.statistics_button:
                StatisticsActivity.start(MainActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CreateEmotionActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            adapter.setData(emotionAndComment.getList());
        }
    }
}
