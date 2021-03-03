package app.sixdegree.view.activity.chatchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.getfriendslisting.Data;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.CircleTransform;
import app.sixdegree.utils.GlideCircleWithBorder;
import app.sixdegree.view.activity.chatchat.ChatDetails;


public class FriendMsgAdapter extends RecyclerView.Adapter<FriendMsgAdapter.MyViewHolder> {

    Context context;
    int width = 0;
    private List<Data> chatUserList;
    AppSession appSession;

    public FriendMsgAdapter( List<Data> dataList, Context context, int width, AppSession appSession) {
        this.chatUserList = dataList;
        this.context = context;
        this.width = width;
        this.appSession=appSession;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.friendmsgrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {



        if(chatUserList.get( position ).getFromUserId()==chatUserList.get( position ).getToUserId()){
            holder.parent.setVisibility( View.GONE );
        }else {
            holder.parent.setVisibility( View.VISIBLE );
        }

         holder.name.setText( chatUserList.get( position ).getFriendName() );

        if (chatUserList.get( position ).getFriendImage() != null) {
    /*        Picasso.with(context).load(chatUserList.get( position ).getFriendImage())
                    .transform(new CircleTransform())
                    .into(holder.img);*/



            Glide.with(context)
                    .load(chatUserList.get(position).getFriendImage())
                    .apply( RequestOptions.circleCropTransform())
                    .transform(new GlideCircleWithBorder(context, 2, Color.parseColor("#4fb89b")))

                    .into(holder.img);

        } else {

        }


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetails.class);
                intent.putExtra("name", chatUserList.get(position).getFriendName());
                intent.putExtra("image", chatUserList.get(position).getFriendImage());
                intent.putExtra("chat_group_id", "0");
                intent.putExtra("from_user_id",String.valueOf(appSession.getData().getId()));
                intent.putExtra("to_user_id", String.valueOf(chatUserList.get(position).getFriendId()));
                context.startActivity(intent);
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


        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name_comment);


            parent = view.findViewById(R.id.parent);

        }
    }

}
