package paisdeyann.floway;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Iterator;

import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.FragmentsTabs.MapViewFragment;
import paisdeyann.floway.FragmentsTabs.PantallaChat;
import paisdeyann.floway.FragmentsTabs.PantallaTransacciones;
import paisdeyann.floway.Objetos.Conversacion;


public class Menu_Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    FloatingActionsMenu menuMultipleActions;
    MapViewFragment map;
    int positionAnt=0;
    boolean pasCon = false;



    ArrayList<Conversacion> conversaciones = new ArrayList<Conversacion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__principal);
        map=new MapViewFragment();
        map.setContext(getApplicationContext(),this);
        //------ArrayMensajes------------------------------------------------------

        DatabaseReference conversacionesBBDD = FirebaseDatabase.getInstance().getReference().child("Conversaciones");


        conversacionesBBDD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){

                    Conversacion c = ((DataSnapshot) i.next()).getValue(Conversacion.class);
                    Log.d("prueba","estoy al principio "+c.getChat()+" "+c.getId1()+" "+c.getId2());



                    if(Conexion.usuarioActivo.getId_usuario() == c.getId1() || Conexion.usuarioActivo.getId_usuario() == c.getId2()){

                        Log.d("prueba","añado una conversacion");
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
        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);








        //listener de los tabs para las animaciones
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
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
        menuMultipleActions.setVisibility(View.INVISIBLE);


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

        } else if (id == R.id.publicaciones) {

        } else if (id == R.id.invita) {

        } else if (id == R.id.transaction) {

        }
        else if (id == R.id.calcula) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //metodo donde se gestionan los fragments que van dentro de cada tab
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        //aqui se añaden las vistas a cada tab
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    View rootView = inflater.inflate(R.layout.fragment_pantalla_chat, container, false);
                    return rootView;
                case 3:
                    View rootView3= inflater.inflate(R.layout.fragment_pantalla_transacciones, container, false);
                    return rootView3;
            }
            return null;
        }



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
                    fragment = map;
                    return fragment;
            }

                return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
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
