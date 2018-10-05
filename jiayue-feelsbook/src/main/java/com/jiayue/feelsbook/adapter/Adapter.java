package com.jiayue.feelsbook.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jiayue.feelsbook.R;
import com.jiayue.feelsbook.bean.Emotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Emotion record list adapter
 *
 */
public class Adapter extends RecyclerView.Adapter<Adapter.item_holder> {


    private List<Emotion> LIST = new ArrayList<>();
    private Listener listener;


    public void setData(List<Emotion> list) {
        this.LIST = list;
        notifyDataSetChanged();
    }

    public List<Emotion> getData() {
        return LIST;
    }


    @Override
    public int getItemCount() {
        return LIST.size();
    }


    /**
     * List item view_holder
     * https://developer.android.com/guide/topics/ui/layout/recyclerview#java
     */
    public static class item_holder extends RecyclerView.ViewHolder {

        TextView mood;
        TextView time_content;
        TextView content;
        Button edit_button, delete_button;

        public item_holder(View itemView) {
            super(itemView);
            mood = itemView.findViewById(R.id.mood);
            time_content = itemView.findViewById(R.id.time_content);
            content = itemView.findViewById(R.id.content);
            edit_button = itemView.findViewById(R.id.edit_button);
            delete_button = itemView.findViewById(R.id.delete_button);
        }
    }

    /**
     * interface delete button and edit button
     * https://stackoverflow.com/questions/37096547/how-to-get-data-from-edit-text-in-a-recyclerview/37096844#37096844
     */
    public interface Listener {

        void Delete(String time, int i);
        void Edit(String time, int i);
    }

    public void setListener(Listener Listener) {
        this.listener = Listener;
    }

    @Override
    public item_holder onCreateViewHolder(ViewGroup viewgroup, int viewType) {
        View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.list_item_emotion, viewgroup, false);
        item_holder vh = new item_holder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(item_holder VH, final int i) {
        final Emotion emotion = LIST.get(i);
        VH.mood.setText(emotion.get_mood());
        VH.time_content.setText(emotion.get_time());

        VH.content.setText(emotion.get_text());
        VH.content.setVisibility(TextUtils.isEmpty(emotion.get_text()) ? View.GONE : View.VISIBLE);

        //https://stackoverflow.com/questions/10903754/input-text-dialog-android
        VH.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage(R.string.delete_emoion_hint)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.Delete(emotion.get_time(), i);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });


        VH.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.Edit(emotion.get_time(), i);
            }
        });
    }

}
