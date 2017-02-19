package paisdeyann.floway.Registro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;

import paisdeyann.floway.R;
import paisdeyann.floway.Threads.InsertarUsuario;

public class Registro3 extends AppCompatActivity {
    ImageView imageView;
    Button btnempezar;
    Button botonBuscarFoto;
    Registro3 activity;
    TextView txtNombre;

    SharedPreferences mySharedPreferences;

    String urlImagenDescargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_3);

        mySharedPreferences = getSharedPreferences(Registro1.PREFS, Activity.MODE_PRIVATE);

        activity = this;

        //extraemos el drawable en un bitmap
        Drawable originalDrawable = getResources().getDrawable(R.drawable.login);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        txtNombre = (TextView) findViewById(R.id.textViewNombre);
        txtNombre.setText(mySharedPreferences.getString("Nombre",""));

        imageView = (ImageView) findViewById(R.id.imageView2);

        imageView.setImageDrawable(roundedDrawable);

        btnempezar = (Button) findViewById(R.id.ButtonContinuarTercerReg);

        btnempezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardaPreferencias();
                insertarUsuario();

            }
        });
        botonBuscarFoto = (Button) findViewById(R.id.ButtonBuscaFoto);
        botonBuscarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageView.setImageDrawable(null);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Elige como quieres coger la foto").setTitle("coger foto");
                builder.setPositiveButton("Camara", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

               Toast.makeText(getApplicationContext(), "HAS DADO CAMARA", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(intent,1);

                }
                    }
                }).setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(getApplicationContext(), "HAS DADO GALERIA", Toast.LENGTH_SHORT).show();
                        Crop.pickImage(activity);
                    }
                });
                builder.create().show();


            }
        });

    }

    public void intentPicture(View v){


    }
    public void roundImage(){
        //extraemos el drawable en un bitmap
        Drawable originalDrawable = getResources().getDrawable(R.drawable.login);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        imageView = (ImageView) findViewById(R.id.imageView2);

        imageView.setImageDrawable(roundedDrawable);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }

        if(requestCode == 1){
            //Toast.makeText(getApplicationContext(), "vuelvo", Toast.LENGTH_SHORT).show();
            Bitmap fotoEnviar = (Bitmap) result.getExtras().get("data");

           // RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), fotoEnviar);

            //asignamos el CornerRadius
          //  roundedDrawable.setCornerRadius(fotoEnviar.getHeight());

            Drawable d = new BitmapDrawable(getResources(), fotoEnviar);
            Bitmap fotoEnviar2 = getRoundedCornerBitmap(d,true);


            imageView.setImageBitmap(fotoEnviar2);

                        /* toco firebase subir fotos */
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            String mensajeFecha = ""+timestamp;
            String mensaje = mensajeFecha.replace(" ","");
            mensaje = mensaje.replace("-","");
            mensaje = mensaje.replace(":","");
            mensaje = mensaje.replace(".","");


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://flowaychatviajes.appspot.com");

            StorageReference fotoReference = storageRef.child(mensaje+"-foto.jpg");


/*
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();
            */
            Bitmap bitmap = fotoEnviar;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = fotoReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    urlImagenDescargar = ""+downloadUrl;
                }
            });






            /* dejo de tocar */
        }

    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {
                ContentResolver cr = getApplicationContext().getContentResolver();
                Uri uri = Crop.getOutput(result);
                Drawable originalDrawable = Drawable.createFromStream(cr.openInputStream(uri) , "Perfil");
                Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
                //creamos el drawable redondeado
                RoundedBitmapDrawable roundedDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

                //asignamos el CornerRadius
                roundedDrawable.setCornerRadius(originalBitmap.getHeight());

                imageView.setImageDrawable(roundedDrawable);
                //imageView.setImageURI(Crop.getOutput(result));
            }catch (FileNotFoundException e){

            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void guardaPreferencias(){
        SharedPreferences mySharedPreferences = getSharedPreferences(Registro1.PREFS, Registro1.MODE_APPEND);
        //guardamos todas las preferencias con el editor
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        Bitmap bm = BitmapFactory.decodeFile("/data/misc/wallpaper/imagen1.jpeg");

        //editor.putString("Imagen", bm.toString());
        editor.putString("Imagen","");

        editor.commit();




    }

    public void insertarUsuario(){


        // $nombre,$apellidos,$usuario,$password,$poblacion,$cp,$puntuacion,$horario,$data

        String nombre = mySharedPreferences.getString("Nombre","");
        String apellidos = mySharedPreferences.getString("Apellidos","");
        String usuario = mySharedPreferences.getString("Usuario","");
        String password = mySharedPreferences.getString("Contraseña","");
        String poblacion = mySharedPreferences.getString("Población","");
        String cp = mySharedPreferences.getString("CP","");
        int puntuacion = 0;
        String horario = mySharedPreferences.getString("Horario","");
        String data = urlImagenDescargar;





        //Toast.makeText(this, nombre+apellidos+usuario+password+poblacion+cp+horario, Toast.LENGTH_SHORT).show();


        Object[] objetos = new Object[10];
        objetos[0] = nombre;
        objetos[1] = apellidos;
        objetos[2] = usuario;
        objetos[3] = password;
        objetos[4] = poblacion;
        objetos[5] = cp;
        objetos[6] = puntuacion;
        objetos[7] = horario;
        objetos[8] = data;
        objetos[9] = imageView;


        InsertarUsuario insertarUsuario = new InsertarUsuario();
        insertarUsuario.execute(objetos);

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

