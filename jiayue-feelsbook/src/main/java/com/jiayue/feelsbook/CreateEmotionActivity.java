package com.jiayue.feelsbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.jiayue.feelsbook.bean.Emotion;
import com.jiayue.feelsbook.constans.Config;
import com.jiayue.feelsbook.EmotionAndComment.EmotionAndComment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Create or edit a emotion
 */
public class CreateEmotionActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    public static final int REQUEST_CODE = 1024;

    public static void start(Activity aty) {
        Intent intent = new Intent(aty, CreateEmotionActivity.class);
        aty.startActivityForResult(intent, REQUEST_CODE);
    }

    public static void start(Activity aty, Emotion emotion) {
        Intent intent = new Intent(aty, CreateEmotionActivity.class);
        intent.putExtra("emotion", emotion);
        aty.startActivityForResult(intent, REQUEST_CODE);
    }

    private boolean Edit = false;
    private Emotion emotion_1 = null;
    private Calendar calendar;

    private TextView TITLE;
    private TextView DATE;
    private TextView TIME;
    private EditText TEXT;
    private FlexboxLayout flexboxLayout;
    private EmotionAndComment EmotionAndComment;

    private List<RadioButton> buttons;
    private int which = -1;

    /**
     * Create or edit a emotion
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        if (getIntent().hasExtra("emotion")) {
            Edit = true;
            emotion_1 = (Emotion) getIntent().getSerializableExtra("emotion");
        }

        calendar = Calendar.getInstance();
        EmotionAndComment = new EmotionAndComment(this);

        initViews();
    }

    /**
     * Create or edit button and back image
     */

    private void initViews() {
        TITLE = findViewById(R.id.title);
        TITLE.setText(Edit ? "Edit" : "Create");
        findViewById(R.id.Go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * set calendar
         */
        DATE = findViewById(R.id.date);
        TIME = findViewById(R.id.time);
        TEXT = findViewById(R.id.comment);
        flexboxLayout = findViewById(R.id.select_mood);

        DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateEmotionActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "tag");
            }
        });
        TIME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CreateEmotionActivity.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                );
                tpd.show(getFragmentManager(), "tag");
            }
        });

        /**
         * confirm button and show(get) creating time
         * https://stackoverflow.com/questions/6842245/converting-date-time-to-24-hour-format
         *
         */
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        if (Edit) {
            TEXT.setText(emotion_1.get_text());

            try {
                Date date = new SimpleDateFormat(Config.strDATE).parse(emotion_1.get_time());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        DATE.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        TIME.setText(new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()));


        buttons = new ArrayList<>();
        for (int i = 0; i < Config.strEMOTION.length; i++) {
            String name = Config.strEMOTION[i];
            final RadioButton radioButton = new RadioButton(this);
            radioButton.setText(Config.strEMOTION[i]);
            radioButton.setTag(i);
            if (Edit) {
                if (name.equals(emotion_1.get_mood())) {
                    radioButton.setChecked(true);
                    which = i;
                }
            }

            radioButton.setOnClickListener(push);
            flexboxLayout.addView(radioButton);
            if (radioButton.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) radioButton.getLayoutParams();
                p.setMargins(20, 20, 20, 20);
                radioButton.requestLayout();
            }

            buttons.add(radioButton);
        }
    }

    private void create() {
        if (which < 0) {
            Toast.makeText(this, getResources().getText(R.string.add_emotion_hint_1), Toast.LENGTH_SHORT).show();
            return;
        }

        String emotionName = Config.strEMOTION[which];
        SimpleDateFormat sdf = new SimpleDateFormat(Config.strDATE);
        String date = sdf.format(calendar.getTime());
        String text = TEXT.getText().toString().trim();

        Emotion emotion = new Emotion();
        emotion.set_mood(emotionName);
        emotion.set_time(date);
        emotion.set_text(text);

        if (Edit) {
            EmotionAndComment.update(emotion_1.get_time(), emotion);
        } else {
            EmotionAndComment.create(emotion);
        }

        setResult(RESULT_OK);
        finish();
    }


    private View.OnClickListener push = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            which = (int) v.getTag();
            for (int i = 0; i < buttons.size(); i++) {
                if (i == which) {
                    buttons.get(i).setChecked(true);
                } else {
                    buttons.get(i).setChecked(false);
                }
            }
        }
    };


    /**
     * sort emotion list by time
     * https://stackoverflow.com/questions/6842245/converting-date-time-to-24-hour-format
     *
     * @param view
     * @param year
     * @param month
     * @param day
     */


    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        calendar.set(year, month, day);
        DATE.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, second);

        TIME.setText(new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()));
    }

}
