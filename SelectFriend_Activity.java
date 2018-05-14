package com.example.user.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.music.chat.Message_Activity;
import com.example.user.music.model.ChatModel;
import com.example.user.music.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectFriend_Activity extends AppCompatActivity {
ChatModel chatModel = new ChatModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend_);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.selectFriend_recyclerview);
        recyclerView.setAdapter(new SelectRecyclerviewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageButton back_btn = (ImageButton)findViewById(R.id.back_imgbutton);
        TextView invite = (TextView)findViewById(R.id.invite_txt_btn);


        //친구목록에서 체크된사람들로 채팅방을 만드는 코드.
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                chatModel.users.put(myUid,true);

                FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel);
            }
        });



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectFriend_Activity.this,friend_Activity.class);
                startActivity(intent);
            }
        });

    }


    public class SelectRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<UserModel> userModels;

        public SelectRecyclerviewAdapter() {
            userModels = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();

                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){


                        UserModel userModel = snapshot.getValue(UserModel.class);

                        if(userModel.uid.equals(myUid)){
                            continue;
                        }
                        userModels.add(userModel);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item_select,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            Glide.with
                    (holder.itemView.getContext())
                    .load(userModels.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((SelectFriend_Activity.SelectRecyclerviewAdapter.CustomViewHolder)holder).imageView);
            ((SelectFriend_Activity.SelectRecyclerviewAdapter.CustomViewHolder)holder).textView.setText(userModels.get(position).userName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SelectFriend_Activity.this, Message_Activity.class);
                    intent.putExtra("destinationUid",userModels.get(position).uid);
                    startActivity(intent);
                }
            });

            ((CustomViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //체크된 상태
                    if(b){
                        chatModel.users.put(userModels.get(position).uid,true);

                    }//체크안된 상태
                    else{
                        chatModel.users.remove(userModels.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textView;
            public CheckBox checkBox;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView)view.findViewById(R.id.friend_img);
                textView = (TextView)view.findViewById(R.id.friend_name_txt);
                checkBox = (CheckBox)view.findViewById(R.id.friend_item_checkbox);
            }
        }
    }


}
