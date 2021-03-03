package app.sixdegree.view.activity.trailsmodule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.internal.Utility;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivityAddNewTripViewBinding;
import app.sixdegree.network.responses.gettripcategories.Data;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.trailsmodule.previoustrips.PreviousTrips;
import app.sixdegree.view.adapter.TaglistAdapter;
import app.sixdegree.view.tagmodule.FriendListForTagActivity;
import app.sixdegree.viewModel.AddEditTrailVm;
import app.sixdegree.viewModel.AddTripVm;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddNewTrip extends BaseActivity {
    ActivityAddNewTripViewBinding binding;
    AddTripVm addTripVm;
    TextView title;
    TextView cover_lbl;
    TextView ptrips;
    Toolbar tool;
    PowerMenu powerMenu;
    ImageView ivAddPicture;
    List<String> list = new ArrayList<>();
    List<Data> tripCatList = new ArrayList<>();
    //handling button click toast
    Boolean whocan = false;
    Boolean mapstyle = false;
    Boolean cat = false;
    List<String> sharingoptionslist = new ArrayList<>();
    RelativeLayout rl_textimg;
    ProgressDialog pd;
    TextView tagnames;
    String friendsid = "";
    String friendsnames = "";
    ProgressDialog progress;

    //handling button click toast
    @BindingAdapter({"errorMsg"})
    public static void abc(View view, String message) {
        if (message != null) {
            showSnackBar((Activity) view.getContext(), view.getId(), message);
        }
        //goToNextScreen(LocationPermissionActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_trip);
        View view = binding.getRoot();
        TextView add_trips_label = view.findViewById(R.id.add_trips_label);
        cover_lbl = view.findViewById(R.id.cover_lbl);
        rl_textimg = view.findViewById(R.id.rl_textimg);
        title = view.findViewById(R.id.title);
        tagnames = view.findViewById(R.id.tagnames);
        tool = view.findViewById(R.id.tool);
        ivAddPicture = view.findViewById(R.id.ivAddPicture);
        ptrips = view.findViewById(R.id.ptrips);


        progress = new ProgressDialog(this);


        rl_textimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddNewTrip.this, FriendListForTagActivity.class);


                if (addTripVm.getFriendsid() == null) {
                    intent.putExtra("friendsids", "");
                } else {

                    intent.putExtra("friendsids", addTripVm.getFriendsid());
                }


                Log.e("idsss--", "in in to f" + addTripVm.getFriendsid());

                startActivityForResult(intent, 100);

            }
        });


//if(getIntent().hasExtra( "friends_ids" )){
//    friendsid=getIntent().getStringExtra( "friends_ids" );
//    friendsnames=getIntent().getStringExtra( "friends_names" );
//
//
//    Log.e( "idsssss","--"+friendsid );
//    Log.e( "friendsnames","--"+friendsnames );
//}

        if (getIntent().hasExtra("trip_id")) {
            binding.title.setText("Edit Trip");
            binding.btnSave.setText("Update Trip");



            addTripVm = new AddTripVm(getSession().getToken(), getIntent().getStringExtra("trip_id"), friendsid);
        } else {
            binding.title.setText("New Trip");
            binding.btnSave.setText("Save");
            addTripVm = new AddTripVm(getSession().getToken(), "", friendsid);
        }
        binding.setViewModel(addTripVm);


        if (addTripVm.getFriendsnames().equals("")) {
            tagnames.setVisibility(View.GONE);
        } else {
            tagnames.setVisibility(View.VISIBLE);
        }

        Boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            tool.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            title.setText("Edit Trip");
            add_trips_label.setVisibility(View.GONE);
            cover_lbl.setText("Tap to change photo");
        }
        title.setOnClickListener(v -> {
            onBackPressed();
        });


        ptrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), PreviousTrips.class);
                intent.putExtra("isEdit", false);
                intent.putExtra("id", friendsid);
                v.getContext().startActivity(intent);
            }
        });
        cover_lbl.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(3, 4)
                    .start(this);

        });

        binding.ivbg.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(3, 4)
                    .start(this);

        });


        //get trail cat list
        addTripVm.tripCat.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
            tripCatList = data;
            for (int i = 0; i < data.size(); i++) {
                list.add(data.get(i).getName());

            }

        }).subscribe();
        //isTagNamesVisible
        addTripVm.isTagNamesVisible.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {

            if (data) {
                tagnames.setVisibility(View.VISIBLE);
            } else {
                tagnames.setVisibility(View.GONE);
            }


        }).subscribe();

        //get sharing options cat list
        addTripVm.sharingoptionslist.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {
            sharingoptionslist = data;


        }).subscribe();

        //observes api add trail to trip
        addTripVm.isFinished.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {

            if (data) {
                finish();

                Toast.makeText(context, "Trip added successfully", Toast.LENGTH_SHORT).show();
            }
        }).subscribe();
        //observes api add trail to trip
        addTripVm.isUpdatedFinished.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {

            if (data) {
                finish();

                Toast.makeText(context, "Trip updated successfully", Toast.LENGTH_SHORT).show();
            }
        }).subscribe();

        addTripVm.loader.subscribeOn(AndroidSchedulers.mainThread()).doOnNext(data -> {

            if (data) {
                createProgressDialog();
            } else {
                dismissProgressDialog();
            }

        }).subscribe();


        addTripVm.loader.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(trail -> {
                    if (trail) {
                        createProgressDialog();
                    } else {
                        dismissProgressDialog();
                    }

                }, throwable -> {
                    Log.e("User data ", throwable.getMessage());
                    dismissProgressDialog();
                });


        // set trail picture
        ivAddPicture.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4, 3)
                    .start(this);


        });


        binding.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartDate();
            }
        });


        binding.endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEndDate();
            }
        });


        binding.whoCanSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<String> list = new ArrayList<>();
//                list.add("My Friends");
//                list.add("Public");
//                list.add("Friends of Friends");
//


                whocan = true;
                cat = false;
                mapstyle = false;

                if (sharingoptionslist.size() != 0) {
                    show(sharingoptionslist, binding.whoCanSee);
                }

            }
        });


        binding.tvMapStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whocan = false;

                cat = false;
                mapstyle = true;
                List<String> list = new ArrayList<>();
                list.add("roadmap");
                list.add("satellite");
                list.add("hybrid");
                list.add("terrain");

                show(list, binding.tvMapStyle);
            }
        });

        binding.categoryTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                whocan = false;
                cat = true;
                mapstyle = false;

                show(list, binding.categoryTv);

            }
        });

    }


    public void setStartDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        addTripVm.setStartdate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Log.e("  end date", "--" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            String textSelected = item.getTitle();


            if (whocan) {
                addTripVm.setWhoCanSee(textSelected);
            } else if (mapstyle) {
                addTripVm.setMapStyle(textSelected);
            } else {
                addTripVm.setCategory(textSelected);
            }
            powerMenu.dismiss();
        }


    };

    public void setEndDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        addTripVm.setEnddate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Log.e("  end date", "--" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void show(List<String> list, View view) {
        List<PowerMenuItem> l1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            l1.add(new PowerMenuItem(list.get(i), false));
        }
        powerMenu = new PowerMenu.Builder(this)
                .addItemList(l1)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)

                .setTextColor(ContextCompat.getColor(this, R.color.black))
                .setTextGravity(Gravity.CENTER)

                .setTextTypeface(Typeface.create(getResources().getFont(R.font.semibold), Typeface.NORMAL))

                .setSelectedTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
        powerMenu.showAtCenter(view);

    }

    public void createProgressDialog() {


        if (pd != null) {
            pd.show();
        } else {
            pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setMessage("Please Wait...");
            pd.show();
        }

    }


    public void dismissProgressDialog() {


        /*if (pd != null) {
            pd.dismiss();
        }*/
        try {

            if (pd != null)
                if (pd.isShowing()) ;
            pd.dismiss();
        } catch (WindowManager.BadTokenException e) {

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            friendsid = data.getStringExtra("friends_ids");
            friendsnames = data.getStringExtra("friends_names");


            Log.e("idsssss", "--" + friendsid);
            Log.e("friendsnames", "--" + friendsnames);

            addTripVm.setFriendsid(friendsid);
            addTripVm.setFriendsnames(friendsnames);


            if (addTripVm.getFriendsnames().equals("")) {
                tagnames.setVisibility(View.GONE);
            } else {
                tagnames.setVisibility(View.VISIBLE);
            }


        } else {
            addTripVm.onActivityResult(requestCode, resultCode, data);
        }

    }


    public int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }
}
