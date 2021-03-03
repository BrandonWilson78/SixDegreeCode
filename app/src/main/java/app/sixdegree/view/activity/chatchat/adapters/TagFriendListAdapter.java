package app.sixdegree.view.activity.chatchat.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.getfriendslisting.Data;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.CircleTransform;
import app.sixdegree.view.activity.chatchat.ChatDetails;

import static app.sixdegree.view.tagmodule.FriendListForTagActivity.friendsids;
import static app.sixdegree.view.tagmodule.FriendListForTagActivity.friendsname;


public class TagFriendListAdapter extends RecyclerView.Adapter<TagFriendListAdapter.MyViewHolder> {

    Context context;
    int width = 0;
    private List<Data> chatUserList;
    AppSession appSession;
    List<String>items;

    public TagFriendListAdapter( List<Data> dataList,
                                 Context context, int width, AppSession appSession,List<String>items) {
        this.chatUserList = dataList;
        this.context = context;
        this.width = width;
        this.appSession=appSession;
        this.items=items;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.tagfriendrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {



         holder.name.setText( chatUserList.get( position ).getFriendName() );

        if (chatUserList.get( position ).getFriendImage() != null) {
            Picasso.with(context).load(chatUserList.get( position ).getFriendImage())
                    .transform(new CircleTransform())
                    .into(holder.img);
        } else {

        }

        Log.e("items-", "--"+items);

        for (String item : items) {

            if(item.equals( String.valueOf(chatUserList.get( position ).getFriendId()) )){
                holder.checkBox.setChecked( true );
                friendsids.add(String.valueOf(chatUserList.get(position).getFriendId()));
                friendsname.add(String.valueOf(chatUserList.get(position).getFriendName()));
            }


        }

//        if(items==null){
//
//            Log.e("item.sizes-", "--"+items.size());
//        }else {
//
//
//                 if(items.get( position ).equals( String.valueOf(chatUserList.get( position ).getToUserId()))){
//                    holder.checkBox.setChecked( true );
//                    friendsids.add(String.valueOf(chatUserList.get(position).getToUserId()));
//                    friendsname.add(String.valueOf(chatUserList.get(position).getFollowingName()));
//                    Log.e("ids-in loop-", "--"+friendsids);
//                }else {
//                    holder.checkBox.setChecked( false );
//                }
//
//
//
//
//
//         }



        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {


                    friendsids.add(String.valueOf(chatUserList.get(position).getFriendId()));
                    friendsname.add(String.valueOf(chatUserList.get(position).getFriendName()));

                    Log.e("ids--", "--"+friendsids);
                    Log.e("ids--", "--"+friendsname);
                } else {

                    friendsids.remove(String.valueOf(chatUserList.get(position).getFriendId()));
                    friendsname.remove(String.valueOf(chatUserList.get(position).getFriendName()));

                    Log.e("ids--", "--" + friendsids);
                    Log.e("ids--", "--" + friendsname);
                }
            }
        });





    }


    @Override
    public int getItemCount() {
        return chatUserList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView msg;
        TextView time;
        TextView unread;
        CheckBox checkBox;


        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name_comment);
            checkBox = view.findViewById(R.id.tag_check);


            parent = view.findViewById(R.id.parent);

        }
    }

}
