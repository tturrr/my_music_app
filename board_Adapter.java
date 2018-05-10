package com.example.user.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class board_Adapter extends BaseAdapter{

    Context context;

    ArrayList<board_List_Item> list_itemArrayList;
    ViewHolder viewholder;


    public board_Adapter(Context context, ArrayList<board_List_Item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;

    }

    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.board_list_item,null);
            viewholder = new ViewHolder();


            viewholder.title_textView = (TextView)convertView.findViewById(R.id.title_txt);
            viewholder.board_img = (ImageView)convertView.findViewById(R.id.picture_img);


            convertView.setTag(viewholder);

        }else{
            viewholder = (ViewHolder)convertView.getTag();
        }
        viewholder.title_textView.setText(list_itemArrayList.get(position).getTitle());
        viewholder.board_img.setImageURI(list_itemArrayList.get(position).getBod_img());







        return convertView;
    }


    class ViewHolder{

        TextView title_textView;
        ImageView board_img;
        CheckBox checkBox;

    }

}
