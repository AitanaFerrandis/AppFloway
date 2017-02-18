package paisdeyann.floway.Threads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by caboc on 18/02/2017.
 */

public class PonerFotoEnUnImageView extends AsyncTask<Object, Object, Object> {

    Bitmap bitmap;
    Bitmap bitmapCuadrada;
    Context contexto;

    @Override
    protected Object doInBackground(Object... params) {

        // params[0] imageView
        // params[1] String con la url de la imagen
        // params[2] contexto


        if(conecta(params[2])){

            bitmapCuadrada = conseguirBitmap((String)params[1]);

        }



        return (ImageView)params[0];
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        Drawable d = new BitmapDrawable(((ImageView)o).getResources(), bitmapCuadrada);
        bitmap = getRoundedCornerBitmap(d,true);


        ((ImageView)o).setImageBitmap(bitmap);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }


    public boolean conecta(Object view){

        boolean respuesta = false;

        ConnectivityManager connMgr = (ConnectivityManager) ((Context)view).getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            respuesta = true;

        }

        return respuesta;
    }

    public Bitmap conseguirBitmap(String cadena){

        Bitmap bitmapRetorno=null;
        HttpURLConnection conn;

        URL url = null;
        try {
            url = new URL(cadena);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();		// código devuelto por web Service
            InputStream is = conn.getInputStream();

            bitmapRetorno = BitmapFactory.decodeStream(is);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmapRetorno;
    }

    public static Bitmap getRoundedCornerBitmap( Drawable drawable, boolean square) {
        int width = 0;
        int height = 0;

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap() ;

        if(square){
            if(bitmap.getWidth() < bitmap.getHeight()){
                width = bitmap.getWidth();
                height = bitmap.getWidth();
            } else {
                width = bitmap.getHeight();
                height = bitmap.getHeight();
            }
        } else {
            height = bitmap.getHeight();
            width = bitmap.getWidth();
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = 90;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}
