package com.example.user.music.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.music.R;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private ArrayList<ChatItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ChatItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_message, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */

        TextView comments = (TextView) convertView.findViewById(R.id.messageItem_textView_message);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        ChatItem myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */

        comments.setText(myItem.getComment());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }

    public void addItem(String comments) {

        ChatItem mItem = new ChatItem();

        /* MyItem에 아이템을 setting한다. */

        mItem.setComment(comments);


        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);
    }
}