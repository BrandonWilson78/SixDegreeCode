package app.sixdegree.view.activity.savedtrips;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.sixdegree.R;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.Country;
import app.sixdegree.network.responses.getallsavedpitstops.Pitstop;
import app.sixdegree.network.responses.gettaggedtrips.Data;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.trailsmodule.SIngleTrailView;
import app.sixdegree.view.activity.trailsmodule.TrailDetailsActivity;
import app.sixdegree.view.activity.trailsmodule.adapters.TrailAdapter;
import app.sixdegree.viewModel.TrailRowVm;
import app.sixdegree.viewModel.TripDetailsVm;


public class BookmarkedtripsAdapter extends RecyclerView.Adapter<BookmarkedtripsAdapter.MyViewHolder> {
    ArrayList<Pitstop> data = new ArrayList<>();
    Context context;
    CountriesDao dao;
    TripDetailsVm tripDetailsVm;
    String trail_count = "0";
    TrailRowVm trailRowVm;
    String token;
    String user_id = "";
    String tagged = "";
    String tripenddate = "";
    AppSession appSession;

    public BookmarkedtripsAdapter(ArrayList<Pitstop> data, CountriesDao dao, Context context /*TripDetailsVm tripDetailsVm, */, String token, AppSession appSession/*,String user_id,AppSession appSession,String tripenddate,String tagged*/) {

        this.dao = dao;
        this.tripDetailsVm = tripDetailsVm;
        this.user_id = user_id;
        this.tagged = tagged;
        this.token = token;
        this.data = data;

        this.appSession = appSession;
        this.context = context;

    }

    public CountriesDao getDao() {
        return dao;
    }

    public void setData(String flag, String count, MyViewHolder holder) {
        AppUtils.setFlag(holder.iv_flag, flag);

        trail_count = count;
        Log.e("size of pit", "---" + trail_count);


    }

    public void getTasks(String country, MyViewHolder holder

    ) {

        if (country == null) {
            setData("n/a", String.valueOf(getItemCount() - 2), holder);
            return;
        }

        class GetTasks extends AsyncTask<Void, Void, Country> {

            @Override
            protected Country doInBackground(Void... voids) {


                return getDao().findByName(country.toLowerCase());
            }

            @Override
            protected void onPostExecute(Country tasks) {
                super.onPostExecute(tasks);
                if (tasks != null) {
                    setData(tasks.getFlag(), "", holder);

                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_saaaaaved, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Pitstop dataModel = data.get(position);
        getTasks(data.get(position).getCountry(), holder);
        holder.loc_name.setText(data.get(position).getCountry());
        holder.title.setText(data.get(position).getName());
        holder.tv_likes.setText(String.valueOf(data.get(position).getTotalLikes()));
        holder.tv_comment.setText(String.valueOf(data.get(position).getTotalComments()));

        Glide.with(context)
                .load(dataModel.getPitstopPicture())
                .centerCrop()
                .into(holder.image);
        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SIngleTrailView.class);
                intent.putExtra("trail_id", String.valueOf(dataModel.getTrailId()));
                intent.putExtra("user_id", String.valueOf(dataModel.getUserId()));
                intent.putExtra("session_id", String.valueOf(appSession.getData().getId()));
                context.startActivity(intent);
            }
        });
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SIngleTrailView.class);
                intent.putExtra("trail_id", String.valueOf(dataModel.getTrailId()));
                intent.putExtra("user_id", String.valueOf(dataModel.getUserId()));
                intent.putExtra("session_id", String.valueOf(appSession.getData().getId()));
                context.startActivity(intent);
            }
        });

        holder.starfill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BookmarkedTrips) context).removebookmark(String.valueOf(dataModel.getTrailId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_flag;


        ImageView iv_go;
        ImageView image;
        ImageView starfill;
        TextView title;
        TextView loc_name;
        TextView trails;
        TextView tv_likes;
        TextView tv_comment;
        TextView date;
        RelativeLayout ll1;
        LinearLayout ll;
        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);

            iv_flag = view.findViewById(R.id.flag);
            title = view.findViewById(R.id.title);
            loc_name = view.findViewById(R.id.loc_name);
            parent = view.findViewById(R.id.parent);
            image = view.findViewById(R.id.image);
            starfill = view.findViewById(R.id.starfill);
            date = view.findViewById(R.id.date);
            trails = view.findViewById(R.id.trails);
            tv_likes = view.findViewById(R.id.tv_likes);
            tv_comment = view.findViewById(R.id.tv_comment);
            ll1 = view.findViewById(R.id.ll1);
            ll = view.findViewById(R.id.ll);
            iv_go = view.findViewById(R.id.iv_go);
        }


    }


}