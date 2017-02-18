package paisdeyann.floway;



import android.content.Intent;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.FragmentsTabs.MapViewFragment;
import paisdeyann.floway.FragmentsTabs.PantallaChat;
import paisdeyann.floway.FragmentsTabs.PantallaTransacciones;
import paisdeyann.floway.Objetos.Conversacion;
import paisdeyann.floway.Threads.PonerFotoEnUnImageView;


public class Menu_Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PantallaTransacciones.OnFragmentInteractionListener, PantallaChat.OnFragmentInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    FloatingActionsMenu menuMultipleActions;
    MapViewFragment map;
    PantallaChat chat;
    PantallaTransacciones transacciones;
    int positionAnt=0;
    boolean pasCon = false;

    ImageView imagenCabeceraNavigationDrawer;


    ArrayList<Conversacion> conversaciones = new ArrayList<Conversacion>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__principal);

        Log.d("prueba","empiezo el menu_principal");

        /*
        imagenCabeceraNavigationDrawer = (ImageView) findViewById(R.id.imageView);

        Object[] objetos = new Object[3];
        objetos[0] = imagenCabeceraNavigationDrawer;
        objetos[1] = Conexion.usuarioActivo.getFoto();
        objetos[2] = getApplicationContext();

        PonerFotoEnUnImageView myThreadFoto = new PonerFotoEnUnImageView();
        myThreadFoto.execute(objetos);
*/
        NavigationView navigation;

        navigation = (NavigationView) findViewById(R.id.nav_view);
        View v = navigation.getHeaderView(0);
        ImageView imagen = (ImageView) v.findViewById(R.id.imageView);
        TextView textoNombre = (TextView) v.findViewById(R.id.textViewNombreNavigation);
        TextView textoApellidos = (TextView) v.findViewById(R.id.textViewApellidosNavigation);

        Object[] objetos = new Object[3];
        objetos[0] = imagen;
        objetos[1] = Conexion.usuarioActivo.getFoto();
        objetos[2] = getApplicationContext();

        PonerFotoEnUnImageView myThreadFoto = new PonerFotoEnUnImageView();
        myThreadFoto.execute(objetos);


        textoNombre.setText(Conexion.usuarioActivo.getNombre());
        textoApellidos.setText(Conexion.usuarioActivo.getApellidos());



        map=new MapViewFragment();
        map.setContext(getApplicationContext(),this);



        chat = new PantallaChat();
        chat.setContext(getApplicationContext(),this);

        transacciones = new PantallaTransacciones();
        //------ArrayMensajes------------------------------------------------------

        DatabaseReference conversacionesBBDD = FirebaseDatabase.getInstance().getReference().child("Conversaciones");


        conversacionesBBDD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){

                    Conversacion c = ((DataSnapshot) i.next()).getValue(Conversacion.class);
                   // Log.d("prueba","estoy al principio "+c.getChat()+" "+c.getId1()+" "+c.getId2());



                    if(Conexion.usuarioActivo.getId_usuario() == c.getId1() || Conexion.usuarioActivo.getId_usuario() == c.getId2()){


                        conversaciones.add(c);

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //------Seccion Navigation---------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //------------------Seccion de Tabs----------------------------------------------------
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //añado iconos a los tabs
        final int[] ICONS = new int[]{
                R.drawable.chat,
                R.drawable.coche,
                R.drawable.monedero
        };
        tabLayout.getTabAt(0).setIcon(ICONS[1]);
        tabLayout.getTabAt(1).setIcon(ICONS[0]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);








        //listener de los tabs para las animaciones
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        muestraBoton();
                        positionAnt = position;
                        break;
                    default:
                        if (positionAnt==1) {
                            desapareceBoton();
                        }
                        positionAnt = position;
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //parte de los botones fab
        final View actionB = findViewById(R.id.boton_b);
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        com.getbase.floatingactionbutton.FloatingActionButton actionC = new com.getbase.floatingactionbutton.FloatingActionButton(getBaseContext());
        actionC.setTitle("Elegir Pasajero / Conductor");
        actionC.setImageDrawable(getResources().getDrawable(R.drawable.mostrar));

        //boton desconectar

        com.getbase.floatingactionbutton.FloatingActionButton desc = ( com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_a);
        desc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(map.conectado==1){
                    if(menuMultipleActions.isExpanded())menuMultipleActions.collapse();
                map.desconecta();
                    Toast.makeText(Menu_Principal.this, "Estas desconectado", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(menuMultipleActions.isExpanded())menuMultipleActions.collapse();
                    map.conecta();
                    Toast.makeText(Menu_Principal.this, "Estas conectado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //boton multimenu, no tocar
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

            }
        });

        actionB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pasCon = !pasCon;

                //boton eres pasajero
                if(pasCon){
                    //cambia el texto y el color
                    //actionB.setDrawingCacheBackgroundColor(getResources().getColor(R.color.pink));
                    if(menuMultipleActions.isExpanded())menuMultipleActions.collapse();
                    actionB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
                    actionB.setBackground(getResources().getDrawable(R.drawable.pasajero_android));
                    map.pintaConductores();
                    Toast.makeText(Menu_Principal.this, "Eres Pasajero", Toast.LENGTH_SHORT).show();
                }
                //boton eres conductor
                else{
                    if(menuMultipleActions.isExpanded())menuMultipleActions.collapse();
                    actionB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    actionB.setBackground(getResources().getDrawable(R.drawable.coche));
                    map.pintaPasajeros();
                    Toast.makeText(Menu_Principal.this, "Eres Conductor", Toast.LENGTH_SHORT).show();
                }
            }
        });



        menuMultipleActions.addButton(actionC);



    }

    public Menu_Principal() {
        super();
    }

    //esto no se para que sirve pero no hay que tocarlo
    @Override
    public void onNavFragmentInteraction(String string) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /* getters y setters array mensajes --*/
    public ArrayList<Conversacion> getConversaciones() {
        return conversaciones;
    }

    public void setConversaciones(ArrayList<Conversacion> conversaciones) {
        this.conversaciones = conversaciones;
    }

    public void addConversacion(Conversacion c){
        conversaciones.add(c);
    }
    /* hasta aki --*/

    //animaciones
    public void muestraBoton(){
        menuMultipleActions.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        menuMultipleActions.setVisibility(View.VISIBLE);

    }
    public void desapareceBoton(){

        menuMultipleActions.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        menuMultipleActions.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu__principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.busca) {
            // Handle the camera action
        } else if (id == R.id.message) {

        } else if (id == R.id.puntuacion) {

            Intent intent = new Intent(getApplicationContext(), PuntuacionActivity.class);
            startActivity(intent);

        } else if (id == R.id.perfil) {

            Intent intent = new Intent(getApplicationContext(),Perfil.class);
            startActivity(intent);

        } else if (id == R.id.invita) {
            Intent intent = new Intent(getApplicationContext(),Invitar_Amigos.class);
            startActivity(intent);

        } else if (id == R.id.transaction) {

        }
        else if (id == R.id.calcula) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //metodo para gestionar los tabs, no tocar nada de aqui
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 1:
                    fragment = chat;
                    return fragment;
                case 0:
                    fragment = map;
                    return fragment;
                case 2:
                    fragment = transacciones;
                    return fragment;
            }

                return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return null;
                case 1:
                    return null;
                case 2:
                    return null;
            }
            return null;
        }

    }

}
