package cpp.scottl.com.handleit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cpp.scottl.com.handleit.login_reg.LoginActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int CAMERA_REQUEST = 10;
    private Toolbar toolbar;
    private TabLayout myTabLayout;
    private ViewPager mypager;
    private MyPagerAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This is for the firebase
        Firebase.setAndroidContext(this);
        // Create firebase object ref
        Firebase myFirebaseRef = new Firebase("https://shining-heat-9080.firebaseio.com/");
        myFirebaseRef.child("student/030").setValue("student demo");  // Creates new record
        myFirebaseRef.child("student/010/name").setValue("new name from app"); // Mods the existing name
        // Create a new class Student and have getters and setters of name and age. then create object
        // and pass it to the firebase. it will fill in all the right data
                    //  Student student = new Student("Scott", 16);
                    // myFirebaseRef.child("student/040").setValue(student);
        //You can have a loop that will set this also.

        myFirebaseRef.child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Test", dataSnapshot.getKey() + " : " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        myAdapter = new MyPagerAdapter(getSupportFragmentManager());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mypager = (ViewPager) findViewById(R.id.pager);
        mypager.setAdapter(myAdapter);
        myTabLayout.setTabsFromPagerAdapter(myAdapter);
        myTabLayout.setupWithViewPager(mypager);
        mypager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myTabLayout));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the user choose ok then enter.
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST) {
                // This is where we are hearing back from the camera
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");//This is how we access the image the camera takes

            }
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }
        // Used to launch the login activity when the item is clicked
        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.nav_notifications) {
            // Handle the camera action
        } else if (id == R.id.nav_upload) {

        } else if (id == R.id.nav_pre_post) {

        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";

        public MyFragment(){

        }

        public static MyFragment newInstance(int pageNumber) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber);
            myFragment.setArguments(arguments);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            /*if (pageNumber == 2) {
                Fragment videoFragment = new Fragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.EventImage, videoFragment).commit();
            }*/
            TextView myText = new TextView(getActivity());
          //  myText.setText("I am the text inside this fragment "+pageNumber);
           // myText.setGravity(Gravity.CENTER);
            return myText;
        }

    }
    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MainActivity.MyFragment myFragment = MainActivity.MyFragment.newInstance(position);
            return myFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tempStr = "";
            if(position==0){
                tempStr = "Search";
            }else if (position == 1){
                tempStr = "Hot Fixes";
            }else if (position == 2) {
                tempStr = "Post Question";
            }else
                tempStr = "";
            return tempStr;
        }

    }
}
