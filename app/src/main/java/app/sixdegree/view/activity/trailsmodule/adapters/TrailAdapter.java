package app.sixdegree.view.activity.trailsmodule.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CpuUsageInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.hfrecyclerview.HFRecyclerView;

import app.sixdegree.R;
import app.sixdegree.databinding.ItemTrailFooterBinding;
import app.sixdegree.databinding.ItemTrailHeaderBinding;
import app.sixdegree.databinding.TrailsRowBinding;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.Country;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.gettripdetailsnewres.Trail;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.trailsmodule.PlanNextTripActivity;
import app.sixdegree.view.activity.trailsmodule.TrailDetailsActivity;
import app.sixdegree.view.activity.trailsmodule.previoustrips.PreviousTrips;
import app.sixdegree.viewModel.TrailRowVm;
import app.sixdegree.viewModel.TripDetailsVm;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class TrailAdapter extends HFRecyclerView<Trail> {
    ItemTrailFooterBinding itemTrailFooterBinding;
    public ItemTrailHeaderBinding headerBinding;
    CountriesDao dao;
    TripDetailsVm tripDetailsVm;
    String trail_count = "0";
    TrailRowVm trailRowVm;
    String token;
    String user_id = "";
    String tagged = "";
    String tripenddate = "";
    AppSession appSession;
  public   Context context;

    public TrailAdapter(CountriesDao dao, TripDetailsVm tripDetailsVm, String token, String user_id, AppSession appSession,
                        String tripenddate, String tagged,Context context) {
        super(true, true);
        this.dao = dao;
        this.tripDetailsVm = tripDetailsVm;
        this.user_id = user_id;
        this.tagged = tagged;
        this.appSession = appSession;
        this.context=context;

    }

    public CountriesDao getDao() {
        return dao;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            trailRowVm = new TrailRowVm(getItem(position), getDao(), tripDetailsVm, token, user_id, appSession);
            itemViewHolder.binding.setViewModel(trailRowVm);
            ((ItemViewHolder) holder).binding.ll.setTag(position);
            if (position == getItemCount() - 2) {
                ((ItemViewHolder) holder).binding.arrow.setVisibility(View.INVISIBLE);
            }


            if (tagged.equals("tagged")) {
                ((ItemViewHolder) holder).binding.ivDelete.setVisibility(View.GONE);
                ((ItemViewHolder) holder).binding.rvStar.setVisibility(View.VISIBLE);
            } else {
                if (user_id.equals(String.valueOf(appSession.getData().getId()))) {

                    ((ItemViewHolder) holder).binding.ivDelete.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).binding.rvStar.setVisibility(View.GONE);
                } else {
                    ((ItemViewHolder) holder).binding.ivDelete.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).binding.rvStar.setVisibility(View.VISIBLE);
                }
            }


            //
            if (getItem(position).getSaved()) {
                ((ItemViewHolder) holder).binding.starfill.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).binding.starborder.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).binding.starfill.setVisibility(View.GONE);
                ((ItemViewHolder) holder).binding.starborder.setVisibility(View.VISIBLE);
            }


            ((ItemViewHolder) holder).binding.starfill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    trailRowVm.removebookmark(String.valueOf(getItem(position).getId()), ((ItemViewHolder) holder).binding.starborder,
                            ((ItemViewHolder) holder).binding.starfill, view);

                }
            });
            ((ItemViewHolder) holder).binding.starborder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                     trailRowVm.savePitstop(String.valueOf(getItem(position).getId()), ((ItemViewHolder) holder).binding.starborder,
                            ((ItemViewHolder) holder).binding.starfill, view);


                }
            });

            ((ItemViewHolder) holder).binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Delete Pitstop");
                    builder.setMessage("Are you sure you want to delete this Pitsop?");
                    builder.setPositiveButton("Delete", (dialog, which) -> {

                        ((TrailDetailsActivity) v.getContext()).deleteTrail(String.valueOf(getItem(position).getId()));

                    });

                    builder.setNegativeButton("cancel", (dialog, which) -> {

                    });
                    builder.show();

                }
            });

//            ((ItemViewHolder) holder).binding.ll.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    return true;
//                }
//
//            });


            // new AsyncTaskExample(((ItemViewHolder) holder).ll1).execute();
            /*Picasso.with(mContext).load("https://www.goabroad.com/section_cloudinary/gaplabs/image/upload/images2/program_content/how-to-choose-the-best-adventure-travel-tour-companies-3-1504752803.jpg").into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    ((ItemViewHolder) holder).binding.ll1.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });*/

        } else if (holder instanceof HeaderViewHolder) {
            //     ((HeaderViewHolder) holder).binding.setViewModel(new HeaderVm(dao, flag, String.valueOf((getItemCount() - 2))));
        } else if (holder instanceof FooterViewHolder) {

        }
    }

    //region Override Get ViewHolder
    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {

        return new ItemViewHolder(inflater.inflate(R.layout.trails_row, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.item_trail_header, parent, false));
    }
    //endregion

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.item_trail_footer, parent, false));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void setData(String flag, String count) {
      //  AppUtils.setFlag(headerBinding.flag, flag);
/*if (context!=null){
    Glide.with(context)
            .load(flag)
            .into(headerBinding.flag);




}*/

        ((TrailDetailsActivity)context).setFlagImage(flag,headerBinding.flag);


        headerBinding.locName.setText(count + "+");
        trail_count = count;
        Log.e("size of pit", "---" + trail_count);


        if (trail_count.equals("0")) {

            headerBinding.tvStart.setText("Swipe to start");
        } else {
            headerBinding.tvStart.setText("Swipe to see pitstops");
        }
    }

    public void settripdate(String date) {
        // Log.e("date in adapter-","--"+date);


        tripenddate = date;

        if (tripenddate.equals("")) {
            itemTrailFooterBinding.tvDate.setText("");
        } else {
            itemTrailFooterBinding.tvDate.setText(AppUtils.getFormatDate(tripenddate));
        }

        notifyDataSetChanged();
    }

    public void getTasks(String country, String count) {

        if (country == null) {
            setData("n/a", String.valueOf(getItemCount() - 2));
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
                    setData(tasks.getFlag(), count);

                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    //region ViewHolder Header and Footer
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TrailsRowBinding binding;

        ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View itemView) {
            super(itemView);
            headerBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(v -> {
                tripDetailsVm.setDisplayMap(false);
            });


        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {


        FooterViewHolder(View view) {
            super(view);
            itemTrailFooterBinding = DataBindingUtil.bind(view);

            if (tagged.equals("tagged")) {
                itemTrailFooterBinding.footerL.setVisibility(View.GONE);
                itemTrailFooterBinding.planNext.setVisibility(View.GONE);
            } else {
                itemTrailFooterBinding.footerL.setVisibility(View.VISIBLE);
                itemTrailFooterBinding.planNext.setVisibility(View.VISIBLE);
            }

            itemTrailFooterBinding.footerL.setOnClickListener(v -> {

                if (tagged.equals("tagged")) {

                } else {
                    if (user_id.equals(String.valueOf(appSession.getData().getId()))) {
                        Intent intent = new Intent(v.getContext(), PlanNextTripActivity.class);
                        intent.putExtra("isEdit", 0);
                        intent.putExtra("id", tripDetailsVm.getTripid());
                        v.getContext().startActivity(intent);
                    }
                }


//
//                if(user_id.equals( String.valueOf( appSession.getData().getId() ) )){
//                    Intent intent = new Intent(v.getContext(), PlanNextTripActivity.class);
//                    intent.putExtra("isEdit", 0);
//                    intent.putExtra("id", tripDetailsVm.getTripid());
//                    v.getContext().startActivity(intent);
//                }

            });
            itemTrailFooterBinding.planNext.setOnClickListener(v -> {
//                if(user_id.equals( String.valueOf( appSession.getData().getId() ) )){
//                    Intent intent = new Intent(v.getContext(), PlanNextTripActivity.class);
//                    intent.putExtra("isEdit", 0);
//                    intent.putExtra("id", tripDetailsVm.getTripid());
//                    v.getContext().startActivity(intent);
//                }


                if (tagged.equals("tagged")) {

                } else {
                    if (user_id.equals(String.valueOf(appSession.getData().getId()))) {
                        Intent intent = new Intent(v.getContext(), PlanNextTripActivity.class);
                        intent.putExtra("isEdit", 0);
                        intent.putExtra("id", tripDetailsVm.getTripid());
                        v.getContext().startActivity(intent);
                    }
                }

            });


        }
    }


}
