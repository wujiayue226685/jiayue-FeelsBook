package com.jiayue.feelsbook.EmotionAndComment;


import android.content.Context;
import android.content.SharedPreferences;

import com.jiayue.feelsbook.bean.Emotion;
import com.jiayue.feelsbook.constans.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * emotion access
 */
public class EmotionAndComment {

    private SharedPreferences sharedPreferences;

    public EmotionAndComment(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.strDATA, Context.MODE_PRIVATE);
    }

    /**
     * get emotion list from SharedPreferences and sort
     *
     * @return
     */
    public List<Emotion> getList() {
        List<Emotion> resultList = new ArrayList<>();
        try {
            String jsonStr = sharedPreferences.getString("emotion_list", "[]");
            JSONArray ja = new JSONArray(jsonStr);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String Mood = jo.optString("type");
                String Time = jo.optString("createTime");
                String Text = jo.optString("comment");

                Emotion emotion = new Emotion();
                emotion.set_mood(Mood);
                emotion.set_time(Time);
                emotion.set_text(Text);
                resultList.add(emotion);

                sortList(resultList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * create a emotion
     *
     * @param emotion
     */
    public void create(Emotion emotion) {
        List<Emotion> list = getList();
        list.add(emotion);
        sortList(list);
        save(list);
    }

    /**
     * delete a emotion by createTime
     *
     * @param createTime
     */
    public void delete(String createTime) {
        List<Emotion> list = getList();
        for (int i = 0; i < list.size(); i++) {
            if (createTime.equals(list.get(i).get_time())) {
                list.remove(i);
                break;
            }
        }
        save(list);
    }

    /**
     * update a emotion by time
     *
     * @param time
     * @param emotion
     */
    public void update(String time, Emotion emotion) {
        List<Emotion> list = getList();
        for (int i = 0; i < list.size(); i++) {
            if (time.equals(list.get(i).get_time())) {
                list.set(i, emotion);
                break;
            }
        }
        save(list);
    }


    /**
     * save emotion lists to SharedPreferences
     *
     * @param lists
     */
    private void save(List<Emotion> lists) {
        try {
            JSONArray ja = new JSONArray();
            for (int i = 0; i < lists.size(); i++) {
                Emotion emotion = lists.get(i);
                JSONObject jo = new JSONObject();
                jo.put("type", emotion.get_mood());
                jo.put("createTime", emotion.get_time());
                jo.put("comment", emotion.get_text());
                ja.put(jo);
            }
            String jsonStr = ja.toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("emotion_list", jsonStr);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * sort emotion list by time
     * https://stackoverflow.com/questions/6842245/converting-date-time-to-24-hour-format
     *
     * @param targetList
     * @return
     */
    private List<Emotion> sortList(List<Emotion> targetList) {
        //sort by create time
        Collections.sort(targetList, new Comparator<Emotion>() {
            @Override
            public int compare(Emotion o1, Emotion o2) {
                try {
                    Long time_1 = new SimpleDateFormat(Config.strDATE).parse(o1.get_time()).getTime();
                    Long time_2 = new SimpleDateFormat(Config.strDATE).parse(o2.get_time()).getTime();
                    return time_2.compareTo(time_1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return targetList;
    }


}
