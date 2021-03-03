package app.sixdegree.view.activity.chatchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import app.sixdegree.network.responses.getinboxres.Data;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.CircleTransform;
import app.sixdegree.utils.GlideCircleWithBorder;
import app.sixdegree.view.activity.chatchat.ChatDetails;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    Context context;
    int width = 0;
    private List<Data> chatUserList;
    AppSession appSession;

    public ChatAdapter(List<Data> dataList, Context context, int width,AppSession appSession) {
        this.chatUserList = dataList;
        this.context = context;
        this.width = width;
        this.appSession=appSession;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.message_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Data chatM = chatUserList.get(position);


        String sourceString = "<b>" + chatM.getFrom_name() + "</b> " + " "+chatM.getLast_message();
         holder.name.setText( Html.fromHtml(sourceString));
         holder.unread.setText("2");
        int count = chatM.getUnseen_messages_count();
        if (chatM.getFrom_user_picture() != null) {
          /*  Picasso.with(context).load(chatM.getFrom_user_picture())
                    .transform(new CircleTransform())
                    .into(holder.img);*/


            Glide.with(context)
                    .load(chatM.getFrom_user_picture())
                    .apply( RequestOptions.circleCropTransform())
                    .transform(new GlideCircleWithBorder(context, 2, Color.parseColor("#4fb89b")))

                    .into(holder.img);

        } else {

        }
        if (count < 10) {
            if (count == 0) {
                holder.unread.setVisibility(View.GONE);
            }
            holder.unread.setText("" + count);
        } else {
            holder.unread.setText(String.valueOf(count));
        }



        if(chatM.getLast_message_time()!=null){


            String date = chatM.getLast_message_time();
            SimpleDateFormat input = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            SimpleDateFormat output = new SimpleDateFormat( "dd MMMM" );
            try {
                Date dd = input.parse( date );                 // parse input
                holder.time.setText( output.format( dd ) );    // format output
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetails.class);
                intent.putExtra("name", chatUserList.get(position).getFrom_name());
                intent.putExtra("image", chatUserList.get(position).getFrom_user_picture());
                intent.putExtra("chat_group_id", String.valueOf(chatUserList.get(position).getConversation_id()));
                intent.putExtra("from_user_id",String.valueOf(appSession.getData().getId()));
                intent.putExtra("to_user_id", String.valueOf(chatUserList.get(position).getFrom_user_id()));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    public void updateList(List<Data> list) {
        chatUserList = list;
        notifyDataSetChanged();
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
             time = view.findViewById(R.id.date);
            unread = view.findViewById(R.id.unread);

            parent = view.findViewById(R.id.parent);

        }
    }

}
