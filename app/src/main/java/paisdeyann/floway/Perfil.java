package paisdeyann.floway;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;

import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.Objetos.Mensaje;
import paisdeyann.floway.Objetos.Publicacion;
import paisdeyann.floway.Objetos.Usuario;
import paisdeyann.floway.Threads.AdaptadorPublicaciones;
import paisdeyann.floway.Threads.CambiosPerfil;
import paisdeyann.floway.Threads.PonerFotoEnUnImageView;
import paisdeyann.floway.Threads.incrementarPuntuacion;

public class Perfil extends AppCompatActivity implements View.OnClickListener{

    ImageView fotoPerfil;
    TextView textViewNombre;
    TextView textViewUsuario;
    Usuario usuario;
    TextView textViewPoblacion;
    TextView textViewCodigoPostalPerfil;
    TextView textViewHorarioPerfil;
    ImageView imagen;


    Button buttonValoramePerfil;
    RatingBar ratingBar;
    Button botonEnviarMensaje;
    FloatingActionButton fab;
    LinearLayout linearlayoutaesconder;
    View viewRaya;


    private FirebaseAuth firebaseAuth;
    private Firebase mFirebase;
    private String mId;

    ArrayList<Publicacion> publicaciones;
    RecyclerView rv;
    AdaptadorPublicaciones adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Bundle args = getIntent().getBundleExtra("bundle");
        usuario = (Usuario) args.getSerializable("usuario");



        mId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        firebaseAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://flowaychatviajes.firebaseio.com").child("publicaciones/"+usuario.getId_usuario());




        fotoPerfil = (ImageView) findViewById(R.id.imageView2);
        textViewNombre = (TextView) findViewById(R.id.textViewNombrePerfil);
        textViewUsuario = (TextView) findViewById(R.id.textViewUsuarioPerfil);
        imagen = (ImageView) findViewById(R.id.imageView2);
        textViewPoblacion = (TextView) findViewById(R.id.textViewPoblacionPerfil);
        textViewCodigoPostalPerfil = (TextView) findViewById(R.id.textViewCodigoPostalPerfil);
        textViewHorarioPerfil = (TextView) findViewById(R.id.textViewHorarioPerfil);

        buttonValoramePerfil = (Button) findViewById(R.id.buttonValoramePerfil);
        ratingBar =  (RatingBar) findViewById(R.id.ratingBar);
        botonEnviarMensaje = (Button) findViewById(R.id.botonEnviarMensaje);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        linearlayoutaesconder = (LinearLayout) findViewById(R.id.linearlayoutaesconder);
        viewRaya = (View) findViewById(R.id.viewRaya);

        buttonValoramePerfil.setOnClickListener(this);
        fab.setOnClickListener(this);

        textViewNombre.setText(usuario.getNombre()+" "+usuario.getApellidos());
        textViewUsuario.setText(usuario.getUsuario());
        textViewPoblacion.setText(usuario.getPoblacion());
        textViewCodigoPostalPerfil.setText(usuario.getCp());
        textViewHorarioPerfil.setText(usuario.getHorario());

        Object[] objetos = new Object[3];
        objetos[0] = fotoPerfil;
        objetos[1] = usuario.getFoto();
        objetos[2] = getApplicationContext();

        PonerFotoEnUnImageView myThread = new PonerFotoEnUnImageView();
        myThread.execute(objetos);


        if(Conexion.usuarioActivo.getId_usuario() == usuario.getId_usuario()){

            textViewHorarioPerfil.setOnClickListener(this);
            textViewCodigoPostalPerfil.setOnClickListener(this);
            textViewPoblacion.setOnClickListener(this);
            textViewNombre.setOnClickListener(this);
            imagen.setOnClickListener(this);

            textViewUsuario.setOnClickListener(this);




            buttonValoramePerfil.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            botonEnviarMensaje.setVisibility(View.GONE);
            linearlayoutaesconder.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            viewRaya.setVisibility(View.GONE);


        }else{






        }

        /****** recycler viewwww *******/


        publicaciones = new ArrayList<Publicacion>();
/*
        for(int x = 0; x < 10; x++){

            publicaciones.add(new Publicacion(x,x,"Maria "+x,"fecha "+x,"mensaje "+x));

        }
*/

        rv = (RecyclerView) findViewById(R.id.recyclerViewPublicaciones);

        RecyclerView.LayoutManager rvLM = new LinearLayoutManager(this);
        rv.setLayoutManager(rvLM);

        adaptador = new AdaptadorPublicaciones(publicaciones);
        rv.setAdapter(adaptador);







        mFirebase.addChildEventListener(new ChildEventListener() {

            @Override

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("prueba","llego al listener de publicaciones para bajarla");
                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    try {
                        Log.d("prueba","entro en el meollo de la cuestion");
                        Publicacion publicacion = dataSnapshot.getValue(Publicacion.class);
                        publicacion.imprimir();
                        publicaciones.add(publicacion);
                        rv.scrollToPosition(publicaciones.size()-1);
                        adaptador.notifyItemInserted(publicaciones.size()-1);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        /******* hasta aki *********/

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.textViewUsuarioPerfil:    // usuario y password

                LayoutInflater linf = LayoutInflater.from(this);
                final View inflator = linf.inflate(R.layout.dialogoedittext, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Cambiar Usuario Password");
                alert.setMessage("Elige nuevo usuario y password");
                alert.setView(inflator);

                final EditText editTextUsuario = (EditText) inflator.findViewById(R.id.editTextNombreDialogo);
                final EditText editTextPasswordAntiguo = (EditText) inflator.findViewById(R.id.editTextPasswordAntiguo);
                final EditText editTextNuevoPassword = (EditText) inflator.findViewById(R.id.editTextPasswordNuevoDialogo);


                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                            String user=editTextUsuario.getText().toString();
                            String passwordAntiguo=editTextPasswordAntiguo.getText().toString();
                            String passwordNuevo=editTextNuevoPassword.getText().toString();

                        if(!user.equals("") || !passwordAntiguo.equals("") || !passwordNuevo.equals("")) {

                            if(passwordAntiguo.equals(usuario.getPassword())){
                                cambiarUsuarioPassword(user, passwordAntiguo, passwordNuevo);
                            }else{
                                Toast.makeText(Perfil.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }


                        }else{
                            Toast.makeText(Perfil.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alert.show();

                break;

            case R.id.imageView2:   // cambiar foto

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
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
                        Crop.pickImage(Perfil.this);

                    }
                });
                builder.create().show();



                break;

            case R.id.textViewNombrePerfil:

                LayoutInflater linf2 = LayoutInflater.from(this);
                final View inflator2 = linf2.inflate(R.layout.dialogedittextnombreapellidos, null);
                AlertDialog.Builder alert2 = new AlertDialog.Builder(this);

                alert2.setTitle("Cambiar Nombre y Apellidos");
                alert2.setMessage("Introduce nuevo nombre y apellidos");
                alert2.setView(inflator2);

                final EditText editTextNombre = (EditText) inflator2.findViewById(R.id.editTextNombreDialogoUsuario);
                final EditText editTextApellidos = (EditText) inflator2.findViewById(R.id.editTextApellidosDialogo);



                alert2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                        String nombre=editTextNombre.getText().toString();
                        String apellidos=editTextApellidos.getText().toString();


                        if(!nombre.equals("") || !apellidos.equals("")) {

                                cambiarNombreApellidos(nombre,apellidos);
                        }else{
                            Toast.makeText(Perfil.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alert2.show();

                break;

            case R.id.textViewPoblacionPerfil:

                LayoutInflater linf3 = LayoutInflater.from(this);
                final View inflator3 = linf3.inflate(R.layout.dialogedittextpoblacion, null);
                AlertDialog.Builder alert3 = new AlertDialog.Builder(this);

                alert3.setTitle("Cambiar Población");
                alert3.setMessage("Elige nueva Población");
                alert3.setView(inflator3);

                final EditText editTextPoblacionPerfil = (EditText) inflator3.findViewById(R.id.editTextPoblacionPerfil);

                alert3.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                        String poblacion=editTextPoblacionPerfil.getText().toString();


                        if(!poblacion.equals("")) {

                            cambiarCualquierCampoUnico("poblacion",poblacion);
                            textViewPoblacion.setText(poblacion);
                            Conexion.usuarioActivo.setPoblacion(poblacion);

                        }else{
                            Toast.makeText(Perfil.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alert3.show();



                break;

            case R.id.textViewCodigoPostalPerfil:

                LayoutInflater linf4 = LayoutInflater.from(this);
                final View inflator4 = linf4.inflate(R.layout.dialogoedittextcodigopostal, null);
                AlertDialog.Builder alert4 = new AlertDialog.Builder(this);

                alert4.setTitle("Cambiar Código Postal");
                alert4.setMessage("Elige nuevo Código Postal");
                alert4.setView(inflator4);

                final EditText editTextCPPerfil = (EditText) inflator4.findViewById(R.id.editTextCPPerfil);

                alert4.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                        String codigoPostal=editTextCPPerfil.getText().toString();


                        if(!codigoPostal.equals("")) {

                            cambiarCualquierCampoUnico("codigoPostal",codigoPostal);
                            textViewCodigoPostalPerfil.setText(codigoPostal);
                            Conexion.usuarioActivo.setCp(codigoPostal);

                        }else{
                            Toast.makeText(Perfil.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert4.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alert4.show();

                break;

            case R.id.textViewHorarioPerfil:

                LayoutInflater linf5 = LayoutInflater.from(this);
                final View inflator5 = linf5.inflate(R.layout.dialogoradiobuttonperfilhorario, null);
                AlertDialog.Builder alert5 = new AlertDialog.Builder(this);

                alert5.setTitle("Cambiar Horario");
                alert5.setMessage("Elige nuevo Horario");
                alert5.setView(inflator5);

                final RadioButton radioButtonTarde = (RadioButton) inflator5.findViewById(R.id.radioButtonTarde);
                final RadioButton radioButtonMañana = (RadioButton) inflator5.findViewById(R.id.radioButtonMañana);

                alert5.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                        String horario="";

                        if(radioButtonTarde.isChecked()){
                            horario = "tarde";
                        }else{
                            horario = "manana";
                        }


                        if(!horario.equals("")) {

                            cambiarCualquierCampoUnico("horario",horario);
                            textViewHorarioPerfil.setText(horario);
                            Conexion.usuarioActivo.setHorario(horario);

                        }else{
                            Toast.makeText(Perfil.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert5.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alert5.show();

                break;

            case R.id.buttonValoramePerfil:

                int numEstrellas = (int)ratingBar.getRating();

                //params[0] contexto
                //params[1] puntuacion
                //params[2] id usuario a modificar

                Object[] objetos = new Object[3];

                objetos[0] = getApplicationContext();
                objetos[1] = numEstrellas;
                objetos[2] = usuario.getId_usuario();

                incrementarPuntuacion incrementarPuntos = new incrementarPuntuacion();
                incrementarPuntos.execute(objetos);

                Toast.makeText(this, "Gracias por tu valoracion", Toast.LENGTH_SHORT).show();
                buttonValoramePerfil.setEnabled(false);
                ratingBar.setEnabled(false);

                break;

            case R.id.fab:




                LayoutInflater linf6 = LayoutInflater.from(this);
                final View inflator6 = linf6.inflate(R.layout.dialogocogerpublicacion, null);
                AlertDialog.Builder alert6 = new AlertDialog.Builder(this);

                alert6.setTitle("Publicaciones");
                alert6.setMessage("Escribe tu opinión acerca de este usuario");
                alert6.setView(inflator6);

                final EditText editTextMensajePublicacion = (EditText) inflator6.findViewById(R.id.editTextMensajePublicacion);

                alert6.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                        String mensaje=editTextMensajePublicacion.getText().toString();


                        if(!mensaje.equals("")) {

                            Publicacion publicacion = new Publicacion(Conexion.usuarioActivo.getId_usuario(),usuario.getId_usuario(),Conexion.usuarioActivo.getNombre(),""+new Timestamp(System.currentTimeMillis()),mensaje);
                            mFirebase.push().setValue(publicacion);

                        }else{
                            Toast.makeText(Perfil.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert6.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alert6.show();





               // Toast.makeText(this, "has apretado el action button", Toast.LENGTH_SHORT).show();
                //int usuarioEmisor, int usuarioReceptor, String nombreUsuarioEmisor, String fecha, String mensaje


                break;

        }

    }


    public void cambiarCualquierCampoUnico(String indice, String valor){

        //params[0] contexto
        //params[1] indice campo
        //params[2] valorNuevo

        Object[] objetos = new Object[5];


        objetos[0] = getApplicationContext();
        objetos[1] = indice;
        objetos[2] = valor;
        objetos[3] = "olduser";
        objetos[4] = ""+usuario.getId_usuario();


        CambiosPerfil myThread = new CambiosPerfil();
        myThread.execute(objetos);


    }

    public void cambiarNombreApellidos(String nombre, String apellidos){

        //params[0] contexto
        //params[1] indice campo
        //params[2] valorNuevo

        Object[] objetos = new Object[5];


        objetos[0] = getApplicationContext();
        objetos[1] = "nombre";
        objetos[2] = nombre;
        objetos[3] = "olduser";
        objetos[4] = ""+usuario.getId_usuario();


        CambiosPerfil myThread = new CambiosPerfil();
        myThread.execute(objetos);

        Object[] objetos2 = new Object[5];

        objetos2[0] = getApplicationContext();
        objetos2[1] = "apellidos";
        objetos2[2] = apellidos;
        objetos2[3] = "olduser";
        objetos2[4] = ""+usuario.getId_usuario();

        CambiosPerfil myThread2 = new CambiosPerfil();
        myThread2.execute(objetos2);

        Conexion.usuarioActivo.setNombre(nombre);
        Conexion.usuarioActivo.setApellidos(apellidos);
        textViewNombre.setText(nombre+" "+apellidos);

    }


    public void cambiarUsuarioPassword(String user, String passwordViejo, String passwordNuevo){

       // Toast.makeText(this, usuario+passwordViejo+passwordViejo, Toast.LENGTH_SHORT).show();

        //params[0] contexto
        //params[1] indice campo
        //params[2] valorNuevo

        Object[] objetos = new Object[5];


        objetos[0] = getApplicationContext();
        objetos[1] = "usuario";
        objetos[2] = user;
        objetos[3] = "olduser";
        objetos[4] = ""+usuario.getId_usuario();


        CambiosPerfil myThread = new CambiosPerfil();
        myThread.execute(objetos);

        Object[] objetos2 = new Object[5];

        objetos2[0] = getApplicationContext();
        objetos2[1] = "password";
        objetos2[2] = passwordNuevo;
        objetos2[3] = "olduser";
        objetos2[4] = ""+usuario.getId_usuario();

        CambiosPerfil myThread2 = new CambiosPerfil();
        myThread2.execute(objetos2);

        Conexion.usuarioActivo.setPassword(passwordNuevo);
        Conexion.usuarioActivo.setUsuario(user);
        textViewUsuario.setText(user);

    }

    public void cambiarImagen(String urlImagenDescargar){

        Object[] objetos = new Object[5];



        objetos[0] = getApplicationContext();
        objetos[1] = "foto";
        objetos[2] = ""+urlImagenDescargar;
        objetos[3] = "olduser";
        objetos[4] = ""+usuario.getId_usuario();


        CambiosPerfil myThread = new CambiosPerfil();
        myThread.execute(objetos);

        Log.d("prueba","la url de la nueva imagen es: "+urlImagenDescargar);

        Conexion.usuarioActivo.setFoto(urlImagenDescargar);


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


            imagen.setImageBitmap(fotoEnviar2);

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

                    String urlImagenDescargar = ""+downloadUrl;
                    cambiarImagen(urlImagenDescargar);
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

                imagen.setImageDrawable(roundedDrawable);
                //imageView.setImageURI(Crop.getOutput(result));
            }catch (FileNotFoundException e){

            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
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
