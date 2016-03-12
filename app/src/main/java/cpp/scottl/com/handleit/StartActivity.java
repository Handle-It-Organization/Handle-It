package cpp.scottl.com.handleit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cpp.scottl.com.handleit.login_reg.LoginActivity;

public class StartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private CustomListViewAdapter customListViewAdapter;
    public static final int CAMERA_REQUEST = 10;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Firebase myFirebaseRef;
    private TransferUtility transferUtility;
    private boolean backPressedOnce;
    private ArrayList<HashMap<String,String>> arrayList = null;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    private Intent questionIntent;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),      /* get the context for the application */
                "us-east-1:88cc3a68-3fc1-46a4-9dd7-4f67c57b979d", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        transferUtility = new TransferUtility(s3, getApplicationContext());
/*  THIS IS FROM CLASS ON 2/29/16. GETS PHOTO FROM PHONE AND SEND IT TO AN IMAGEVIEW.

        if(getIntent() != null && getIntent().getAction().equals(Intent.ACTION_SEND)){
            final ImageView imageView = findViewById(R.id.imageview);
            Uri imageUri = (Uri)getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
            Picasso.with(this).load(imageUri).into(imageView);
        }
*/


        // This is for the firebase
        Firebase.setAndroidContext(this);
        // Create firebase object ref
        myFirebaseRef = new Firebase("https://shining-heat-9080.firebaseio.com/");
        /*
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
*/
    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                if (sharedPref.contains("username")) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);

                    *//*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
*//*
                  *//*  Intent myIntent = new Intent(StartActivity.this, CreateQuestion.class);
                    startActivity(myIntent);*//*
                } else
                    Toast.makeText(StartActivity.this, "You must be logged in to make a post!",
                            Toast.LENGTH_LONG).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                TextView drawerUsername = (TextView) findViewById(R.id.drawerUserName);
                TextView drawerEmailAddress = (TextView) findViewById(R.id.drawerEmailAddress);
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                drawerUsername.setText(sharedPref.getString("username",""));
                drawerEmailAddress.setText(sharedPref.getString("email",""));
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        arrayList = popCustomListViewAdapter();

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int myPosition = position;
                        HashMap<String, String> data = arrayList.get(position);
                        Intent intent = new Intent(StartActivity.this, QuestionViewerActivity.class);
                        intent.putExtra("snapshotLocation", data.get("snapshotLocation"));
                        startActivity(intent);

                        String itemClickId = listView.getItemAtPosition(myPosition).toString();
                        if (position == 0) {
                            //Intent i = new Intent(StartActivity.this, MainActivity.class);
                            //startActivity(i);
                        }
                    }
                }
        );
    }


    // This is called when FAB is clicked and the user clicks OK after taking a picture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* Old one used
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String bitmapString = bitmapToBase64(imageBitmap);
            Intent intent = new Intent(StartActivity.this, CreateQuestion.class);
            intent.putExtra("photo", bitmapString);
            intent.putExtra("uploadType", "photo");
            startActivity(intent);
        }*/
        //Check that request code matches ours:
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            //Get our saved file into a bitmap object:
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);

           // startActivity(new Intent(StartActivity.this,CreateQuestion.class).putExtra("photo", bitmap).putExtra("uploadType","photo"));
            Intent qIntent = new Intent(StartActivity.this, CreateQuestion.class);
            String temp = bitmapToBase64(bitmap);
            qIntent.putExtra("photo", temp);
            qIntent.putExtra("uploadType", "photo");
            Log.i("HELLO", "3");
            startActivity(qIntent);

        }
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedOnce) {
                /*super.onBackPressed();
                return;*/
                finish();
            }
            this.backPressedOnce = true;
            Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedOnce = false;
                }
            }, 2000);
        }
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
            finish();
            return true;
        }
        if (id == R.id.action_logout) {
            myFirebaseRef.unauth();
            SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

/*        if (id == R.id.nav_notifications) {
            // Handle the camera action
        } else if (id == R.id.nav_upload) {

        } else if (id == R.id.nav_pre_post) {

        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_send) {

        } else */if (id == R.id.nav_create_post) {
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            if(sharedPref.contains("username")) {
                Intent intent = new Intent(StartActivity.this, PostQuestion.class);
                startActivity(intent);
            }else
                Toast.makeText(StartActivity.this,"You must be logged in to make a post!",
                        Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    // This converts image to Base64 so I can store it in Firebase
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // This decodes Base64 and returns a Bitmap image to be used
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList = popCustomListViewAdapter();
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        MenuItem login = menu.findItem(R.id.action_login);
        if(sharedPref.contains("username")) {
            login.setVisible(false);
        } else {
            login.setVisible(true);
        }
        MenuItem logout = menu.findItem(R.id.action_logout);
        if(sharedPref.contains("username")) {
            logout.setVisible(true);
        } else {
            logout.setVisible(false);
        }
        return true;
    }

    private ArrayList<HashMap<String,String>> popCustomListViewAdapter() {
        listView = (ListView) findViewById(R.id.list);
        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        myFirebaseRef.child("question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> data = new HashMap<>();

                for(DataSnapshot photoSnapshot : dataSnapshot.child("photo").getChildren()){
                    for(DataSnapshot childSnap : photoSnapshot.getChildren()){
                        data = new HashMap<>();
                        data.put("title", childSnap.child("title").getValue().toString());
                        data.put("category", childSnap.child("category").getValue().toString());
                        data.put("date", childSnap.child("date").getValue().toString());
                        data.put("answer_complete", childSnap.child("complete").getValue().toString());
                        data.put("snapshotLocation","question/photo/"+childSnap.child("category")
                                .getValue().toString() + "/" + childSnap.child("title")
                                .getValue().toString() );
                        data.put("photo", childSnap.child("photo").getValue().toString());
                        arrayList.add(data);
                    }
                }
//to add to git
                //Setup adapter
                customListViewAdapter = new CustomListViewAdapter(getApplicationContext(), arrayList);
                listView.setAdapter(customListViewAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return arrayList;
    }
}
