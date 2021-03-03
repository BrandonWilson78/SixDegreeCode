package app.sixdegree.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.sixdegree.R;

public class AppUtils {

    public static void htmlTextView(String s, TextView view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_COMPACT));
        } else {
            view.setText(Html.fromHtml(s));
        }
    }

    public static int getScreenWidth(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;

    }

    public static int getScreenHeight(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static void loadPicasso(String profilePic,ImageView view) {


        if(profilePic!=null&&!profilePic.isEmpty()){
            Picasso.with(view.getContext())
                    .load(profilePic)
                    .transform(new CircleTransform())
                    .into(view);
        }

    }


    private Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    public static Bitmap loadBitmapFromView(View v) {

        if (v.getMeasuredHeight() <= 0) {
            v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }
    public static Bitmap loadBitmapFromViewwithborder(View v,int borderSize) {

        if (v.getMeasuredHeight() <= 0) {
            v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth()+borderSize * 2, v.getMeasuredHeight()+borderSize * 2, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            c.drawColor(Color.WHITE);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }
    public static void loadPicasso(Uri uri,ImageView view) {
        Picasso.with(view.getContext())
                .load(uri)
                .transform(new CircleTransform())

                .into(view);
    }

    public static void picassoLoadBgHttp(View view, String coverPic) {
        Picasso.with(view.getContext())
                .load(coverPic)
                .centerCrop()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }




                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.e("error", "error loading drawable");

                    }
                });
    }

    public static void picassoLoadBgUri(RelativeLayout view, Uri uri) {
        Picasso.with(view.getContext())
                .load(uri)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }


                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.e("error", "error loading drawable");

                    }
                });
    }

    public static void picassoLoadBgUriInImg(ImageView view, Uri uri) {
        Picasso.with(view.getContext())
                .load(uri)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }


                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.e("error", "error loading drawable");

                    }
                });
    }
/*
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;
        int BORDER_COLOR = Color.parseColor("#4fb89b");
         if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }
        Paint paint = new Paint();

        BitmapShader shader = new BitmapShader(output, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(output);
         float r = 100;


        Paint paintBg = new Paint();
        paintBg.setColor(BORDER_COLOR);
        paintBg.setAntiAlias(true);

        // Draw the background circle
        canvas.drawCircle(r, r, r, paintBg);
        *//*if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }*//*

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawCircle(r, r, r - 10, paint);


        return output;
    } */



    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        /*    int BORDER_COLOR = Color.parseColor("#4fb89b");
            int BORDER_RADIUS = 5;
        Bitmap output;
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

          output = Bitmap.createBitmap(source, x, y, size, size);
        if (output != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(120, 120, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        BitmapShader shader = new BitmapShader(output, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = 60;

        // Prepare the background
        Paint paintBg = new Paint();
        paintBg.setColor(BORDER_COLOR);
        paintBg.setAntiAlias(true);

        // Draw the background circle
        canvas.drawCircle(r, r, r, paintBg);

        // Draw the image smaller than the background so a little border will be seen
        canvas.drawCircle(r, r, r - BORDER_RADIUS, paint);

*/
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 10, h + 10, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(20);
        c.drawCircle((w / 2) + 4, (h / 2) + 4,  radius, p);

        return output;
    }
    public static Bitmap getCircular(Bitmap bitmap) {
        /*    int BORDER_COLOR = Color.parseColor("#4fb89b");
            int BORDER_RADIUS = 5;
        Bitmap output;
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

          output = Bitmap.createBitmap(source, x, y, size, size);
        if (output != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(120, 120, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        BitmapShader shader = new BitmapShader(output, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = 60;

        // Prepare the background
        Paint paintBg = new Paint();
        paintBg.setColor(BORDER_COLOR);
        paintBg.setAntiAlias(true);

        // Draw the background circle
        canvas.drawCircle(r, r, r, paintBg);

        // Draw the image smaller than the background so a little border will be seen
        canvas.drawCircle(r, r, r - BORDER_RADIUS, paint);

*/
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 10, h + 10, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3);
        c.drawCircle((w / 2) + 4, (h / 2) + 4,  radius, p);

        return output;
    }



    public static Bitmap getFriendBorder(Bitmap bitmap,int color) {
        /*    int BORDER_COLOR = Color.parseColor("#4fb89b");
            int BORDER_RADIUS = 5;
        Bitmap output;
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

          output = Bitmap.createBitmap(source, x, y, size, size);
        if (output != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(120, 120, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        BitmapShader shader = new BitmapShader(output, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = 60;

        // Prepare the background
        Paint paintBg = new Paint();
        paintBg.setColor(BORDER_COLOR);
        paintBg.setAntiAlias(true);

        // Draw the background circle
        canvas.drawCircle(r, r, r, paintBg);

        // Draw the image smaller than the background so a little border will be seen
        canvas.drawCircle(r, r, r - BORDER_RADIUS, paint);

*/
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 10, h + 10, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Style.STROKE);
        p.setColor(color  );
        p.setStrokeWidth(20);
        c.drawCircle((w / 2) + 4, (h / 2) + 4,  radius, p);

        return output;
    }
    public static Bitmap getCircularBitmapMulticLR(Bitmap bitmap,int color) {
        /*    int BORDER_COLOR = Color.parseColor("#4fb89b");
            int BORDER_RADIUS = 5;
        Bitmap output;
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

          output = Bitmap.createBitmap(source, x, y, size, size);
        if (output != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(120, 120, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        BitmapShader shader = new BitmapShader(output, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = 60;

        // Prepare the background
        Paint paintBg = new Paint();
        paintBg.setColor(BORDER_COLOR);
        paintBg.setAntiAlias(true);

        // Draw the background circle
        canvas.drawCircle(r, r, r, paintBg);

        // Draw the image smaller than the background so a little border will be seen
        canvas.drawCircle(r, r, r - BORDER_RADIUS, paint);

*/
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 80, h + 80, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Style.FILL);

        c.drawCircle((w / 2) + 40, (h / 2) + 40, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 40, 40, p);
        p.setXfermode(null);
        p.setStyle(Style.STROKE);
        p.setColor(color);
        p.setStrokeWidth(40);
        c.drawCircle((w / 2) + 40, (h / 2) + 40,  radius, p);
       Bitmap converetdImage = getResizedBitmap(output, 80);
        return converetdImage;
    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void setFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static String getMonthYear(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getFormatDate(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM YYYY";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getMonth(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
public static void roundImageWithGlide(ImageView view , String url){
    Glide.with(view.getContext())
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .transform(new GlideCircleWithBorder(view.getContext(), 2, Color.parseColor("#4fb89b")))

            .into(view);
}


public static void showcoverpic(ImageView view , String url){
    Glide.with(view.getContext())
            .load(url)
   .into(view);


}



public static void roundImageWithGlidePlaceHolder(ImageView view , String url){

        if(url.isEmpty()){
            Glide.with(view.getContext())
                    .load(R.drawable.logo_blue)
                    .apply(RequestOptions.circleCropTransform())
                    .transform(new GlideCircleWithBorder(view.getContext(), 2, Color.parseColor("#4fb89b")))
                    .placeholder( R.drawable.logo_blue )
                    .into(view);
        }{
        Glide.with(view.getContext())
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .transform(new GlideCircleWithBorder(view.getContext(), 2, Color.parseColor("#4fb89b")))
                .placeholder( R.drawable.logo_blue )
                .into(view);
    }

}
    public static void setImageBg(View view,String url){
        Glide.with(view.getContext()).load(url).into(new SimpleTarget<Drawable>() {
             @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    view.setBackground(resource);

                 }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

                super.onLoadFailed(errorDrawable);
                Glide.with(view.getContext()).load(url)/*.fitCenter().*/.centerCrop().into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(resource);
                        }
                    }
                     @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });

            }
        });
    }


    public static void setFlag(ImageView view, String flag){
        Log.e("flag",flag+"setflagapputil");
        GlideToVectorYou
                .init()
                .with(view.getContext())

                .withListener(new GlideToVectorYouListener() {
                    @Override
                    public void onLoadFailed() {
                     }

                    @Override
                    public void onResourceReady() {
                     }
                })
                 .load(Uri.parse(flag), view);
    }



    public static void setFlagi(ImageView view, String flag){
        Log.e("flag",flag+"setflagapputil");
        GlideToVectorYou
                .init()
                .with(view.getContext())

                .withListener(new GlideToVectorYouListener() {
                    @Override
                    public void onLoadFailed() {
                     }

                    @Override
                    public void onResourceReady() {
                     }
                })
                 .load( Uri.parse( flag ), view);
    }
    public static MarkerOptions createMarker(Context context, LatLng point) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(point);
        int px = 40;
        View markerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
        ImageView bedNumberTextView = markerView.findViewById(R.id.img);
        Picasso.with(context).load("https://pngimage.net/wp-content/uploads/2018/06/traveler-png-2.png").into(bedNumberTextView);
        markerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        markerView.layout(0, 0, px, px);
        markerView.buildDrawingCache();
        Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        markerView.draw(canvas);
        marker.icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap));
        return marker;
    }
}