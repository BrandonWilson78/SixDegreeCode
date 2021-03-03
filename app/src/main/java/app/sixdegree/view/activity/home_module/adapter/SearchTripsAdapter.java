package app.sixdegree.view.activity.home_module.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.getprevioustrips.Data;
import app.sixdegree.network.responses.sixdegreesearch.TripsofMatchedUser;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.trailsmodule.TrailDetailsActivity;


public class SearchTripsAdapter extends RecyclerView.Adapter<SearchTripsAdapter.MyViewHolder> {
    List<TripsofMatchedUser> data = new ArrayList<>();
    Context context;

    public SearchTripsAdapter( Context context , List<TripsofMatchedUser> data ) {

        this.context=context;
        this.data=data;
     }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previous_trips_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TripsofMatchedUser dataModel = data.get(position);


        holder.name.setText( dataModel.getName() );

        if(data.size()!=0 ){
            holder.trails.setText( String.valueOf(dataModel.getTrailsCount()) );
        }else {
            holder.trails.setText( "0");
        }

        Glide.with(context)
                .load(dataModel.getTripPicture())

                .into(holder.image_bg);

        holder.ll_parent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent=new Intent( context, TrailDetailsActivity.class );
                intent.putExtra( "id",String.valueOf(dataModel.getId() ));
                intent.putExtra( "user_id",String.valueOf(dataModel.getUserId() ));
                intent.putExtra( "tagged","");
                context.startActivity( intent );
            }
        } );

        holder.date.setText( AppUtils.getMonthYear(dataModel.getStartDate()) );

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
ImageView image_bg;
ImageView iv_go;
TextView name;
TextView trails;
TextView date;
LinearLayout ll_parent;

        public MyViewHolder(View view) {
            super(view);

image_bg=view.findViewById( R.id.image_bg );
            name=view.findViewById( R.id.name );
            date=view.findViewById( R.id.date );
            trails=view.findViewById( R.id.trails );
            ll_parent=view.findViewById( R.id.ll_parent );
            iv_go=view.findViewById( R.id.iv_go );
         }


    }

}