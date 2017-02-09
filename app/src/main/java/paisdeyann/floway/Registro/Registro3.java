package paisdeyann.floway.Registro;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import paisdeyann.floway.R;
import paisdeyann.floway.Threads.InsertarUsuario;

public class Registro3 extends AppCompatActivity {
    ImageView imageView;
    Button btnempezar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_3);

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

        btnempezar = (Button) findViewById(R.id.ButtonContinuarTercerReg);

        btnempezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardaPreferencias();
                insertarUsuario();

            }
        });

    }

    public void intentPicture(View v){
        //imageView.setImageDrawable(null);
        Crop.pickImage(this);

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

        SharedPreferences mySharedPreferences = getSharedPreferences(Registro1.PREFS, Activity.MODE_PRIVATE);
        // $nombre,$apellidos,$usuario,$password,$poblacion,$cp,$puntuacion,$horario,$data

        String nombre = mySharedPreferences.getString("Nombre","");
        String apellidos = mySharedPreferences.getString("Apellidos","");
        String usuario = mySharedPreferences.getString("Usuario","");
        String password = mySharedPreferences.getString("Contraseña","");
        String poblacion = mySharedPreferences.getString("Población","");
        String cp = mySharedPreferences.getString("CP","");
        int puntuacion = 0;
        String horario = mySharedPreferences.getString("Horario","");
        String data = "";

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
}

