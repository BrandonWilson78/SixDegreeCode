package app.sixdegree.view.activity.sendcontacts;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.fetchcontacts.Data;
import app.sixdegree.network.responses.fetchcontacts.FetchContactsRes;
import app.sixdegree.network.responses.followingres.FollowingRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.chatchat.adapters.FriendMsgAdapter;
import app.sixdegree.view.activity.home_module.HomeActivity;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AllContacts extends BaseActivity {
ProgressBar progressBar;
    RecyclerView rv_list;
    ContactsAdapter contactsAdapter;
    List<Data> dataList = new ArrayList<>();
AppSession appSession;
    TextView txt_top_header;
 Context context=this;
 TextView dummytext;
 String contacts="";
 String contact_text="";

    List<String> contact_list=new ArrayList<>(  );
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_contacts );
        appSession=new AppSession( this );
        rv_list = (RecyclerView) findViewById( R.id.rv_list );
        dummytext = (TextView) findViewById( R.id.dummytext );
        progressBar = (ProgressBar) findViewById( R.id.pbar );
         txt_top_header = (TextView) findViewById( R.id.txt_top_header );

        LinearLayoutManager layoutManager = new LinearLayoutManager( context );
        rv_list.setLayoutManager( layoutManager );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( displayMetrics );
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        contactsAdapter = new ContactsAdapter( dataList, context, width,appSession );
        rv_list.setAdapter( contactsAdapter );
        contactsAdapter.notifyDataSetChanged();
        findViewById( R.id.back ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
               onBackPressed();
            }
        } );


        requestContactPermission();



        if(isNetworkConnected()){
         //   sendContacts();


            if(contact_list.size()!=0){
                sendContacts();
            }

        }else {
            createInternetMsgToast();
        }
    }


    public void sendContacts() {
        progressBar.setVisibility( View.VISIBLE );



        if(contact_list.size()!=0){
            contact_text= TextUtils.join( ",",contact_list ) ;

        }
        Log.e("contacts",contact_text);

        apiService.sendContacts(appSession.getToken() ,contact_text )
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<FetchContactsRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(FetchContactsRes res) {
                        dataList.clear();
                        if (res.getStatus()) {
                            dataList.addAll(res.getData());
                            contactsAdapter.notifyDataSetChanged();
                        }



                        if(res.getData().size()==0){
                            dummytext.setVisibility( View.VISIBLE );
                            rv_list.setVisibility( View.INVISIBLE );
                        }else {
                            dummytext.setVisibility( View.INVISIBLE );
                            rv_list.setVisibility( View.VISIBLE );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility( View.GONE );
                    }
                });


    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query( ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        contacts =  pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("name", "Name: "+name);
                        Log.i("contacts", "Phone Number: " + contacts);
                        contacts = contacts.replaceAll("[()\\s-]+", "");


                        contact_list.add( contacts );

                        Log.i("add fre", "Phone Numbe1r: " + contact_list);
                    }
                    pCur.close();
                }
            }
            Log.i("add fre", "Phone Numb2er: " + contact_list);
        }
        if(cur!=null){
            cur.close();
            Log.i("add fre", "Phone Numbe3r: " + contact_list);
            if(contact_list.size()==0){
                dummytext.setVisibility( View.VISIBLE );
                dummytext.setText( "No Contacts found in your Contact list." );
            }else {
                dummytext.setVisibility( View.GONE );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContactList();
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

}



    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                getContactList();
            }
        } else {
            getContactList();
        }
    }

    public  void gotToHomeActvity(String from,String id,String status,String friendstatus){
        Intent intent=new Intent( this, HomeActivity.class );
        intent.putExtra( "from",from );
        intent.putExtra( "id", id) ;
        intent.putExtra( "status", status) ;
        intent.putExtra( "friendstatus", friendstatus) ;
        startActivity(intent);
    }
    public  void refreshActivity(){
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }
}
