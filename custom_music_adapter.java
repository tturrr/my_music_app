package com.example.user.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class custom_music_adapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<music_item_list> music_item_listArrayList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private Boolean flag =  true;


    public custom_music_adapter(Context context, int layout, ArrayList<music_item_list> music_item_listArrayList) {
        this.context = context;
        this.layout = layout;
        this.music_item_listArrayList = music_item_listArrayList;
    }

    @Override
    public int getCount() {
        return music_item_listArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return music_item_listArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHoder viewHoder;
        if(convertView == null){
            viewHoder = new ViewHoder();
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  layoutInflater.inflate(R.layout.custom_music_item,null);
            viewHoder.txt_Name = (TextView)convertView.findViewById(R.id.txt_Name);
            viewHoder.txt_Signer = (TextView)convertView.findViewById(R.id.txt_Singer);
            viewHoder.play_img = (ImageView)convertView.findViewById(R.id.play_Img);
            viewHoder.stop_img = (ImageView)convertView.findViewById(R.id.stop_Img);

            convertView.setTag(viewHoder);
        }else{
            viewHoder = (ViewHoder) convertView.getTag();
        }
        final music_item_list music_item_list = music_item_listArrayList.get(position);
        viewHoder.txt_Name.setText(music_item_list.getName());
        viewHoder.txt_Signer.setText(music_item_list.getSinger());

        //음악 재생 및 일시정지
        viewHoder.play_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    mediaPlayer = MediaPlayer.create(context, music_item_list.getSong());
                    flag = false;
                } if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    viewHoder.play_img.setImageResource(R.drawable.play_icon);
                } else {
                    mediaPlayer.start();
                    viewHoder.play_img.setImageResource(R.drawable.pause_icon);
                }

            }
        });
        //음악정지
        viewHoder.stop_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    Toast.makeText(context,"노래가 종료되었습니다.",Toast.LENGTH_SHORT).show();
                    flag = true;
                }
                viewHoder.play_img.setImageResource(R.drawable.play_icon);

            }
        });

        return convertView;
    }



    private class ViewHoder{
        TextView txt_Name, txt_Signer;
        ImageView play_img,stop_img;
    }
}
