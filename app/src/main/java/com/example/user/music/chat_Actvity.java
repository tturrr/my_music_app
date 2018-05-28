package com.example.user.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.music.chat.MessageActivity;
import com.example.user.music.model.ChatModel;
import com.example.user.music.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;


public class chat_Actvity extends AppCompatActivity {

    ImageButton back_btn;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__actvity);
        Intent intent = getIntent();
        final String login_id = intent.getStringExtra("login_id");

        back_btn = (ImageButton)findViewById(R.id.back_imgbutton);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.chat_recyclerview);

        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //뒤로가기 버튼.
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chat_Actvity.this,MainActivity.class);
                intent.putExtra("login_id",login_id);
                startActivity(intent);
            }
        });
    }


    class ChatRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ChatModel> chatModels = new ArrayList<>();
        private String uid;
        private ArrayList<String> destinationUsers = new ArrayList<>();


        public ChatRecyclerViewAdapter() {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chatModels.clear();
                    for (DataSnapshot item :dataSnapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_item,parent,false);
            return new CustomViewholder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            final CustomViewholder customViewholder = (CustomViewholder)holder;
            String destinationUid  = null;

            //채팅방에 유저 체크
            for(String user: chatModels.get(position).users.keySet()){
                if(!user.equals(uid)){
                    destinationUid = user;
                    destinationUsers.add(destinationUid);
                }
            }
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserModel userModel =  dataSnapshot.getValue(UserModel.class);
                    Glide.with(customViewholder.itemView.getContext())
                            .load(userModel.profileImageUrl)
                            .apply(new RequestOptions().circleCrop())
                            .into(customViewholder.chatrooom_img);

                    customViewholder.chatroom_title.setText(userModel.userName);
                 notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //메세지를 내림차순으로 정렬후 마지막 메세지의 키값을 가져옴
            Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);
            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
            customViewholder.chatroom_lastmessage.setText(chatModels.get(position).comments.get(lastMessageKey).message);

            customViewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid",destinationUsers.get(position));

                    startActivity(intent);





                }
            });


            //TimeStamp
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            long unixTime = (long) chatModels.get(position).comments.get(lastMessageKey).timestamp;
            Date date = new Date(unixTime);
            customViewholder.chatroom_txt_time.setText(simpleDateFormat.format(date));






        }





        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewholder extends RecyclerView.ViewHolder {

            public ImageView chatrooom_img;
            public TextView chatroom_title;
            public TextView chatroom_lastmessage;
            public TextView chatroom_txt_time;
            public CustomViewholder(View view) {
                super(view);
                chatrooom_img = (ImageView)view.findViewById(R.id.chatroomitem_img);
                chatroom_title = (TextView)view.findViewById(R.id.chatroomitem_txt_title);
                chatroom_lastmessage = (TextView)view.findViewById(R.id.chatroom_txt_lastmessase);
                chatroom_txt_time = (TextView)view.findViewById(R.id.chatroom_txt_time);
            }
        }
    }

}
