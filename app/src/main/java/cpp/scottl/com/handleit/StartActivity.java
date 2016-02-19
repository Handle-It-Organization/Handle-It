package cpp.scottl.com.handleit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private CustomListViewAdapter customListViewAdapter;
    public static final int CAMERA_REQUEST = 10;
    private ImageView imageView;
    private Firebase myFirebaseRef;
    private TransferUtility transferUtility;


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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StartActivity.this, QuestionViewerActivity.class);
                startActivity(myIntent);

                // this part to save captured image on provided path
    //            File file = new File(Environment.getExternalStorageDirectory(), "my-photo.jpg");
      //          Uri photoPath = Uri.fromFile(file);
        //        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);


/* Uncomment this out to have the camera activated
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
*/

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.list);


        final String[] arrayTitle = new String[]{
                "This is the first title", "second", "third", "fourth",
                "5", "6", "7", "8", "9", "10", "11","12","13","14","15","16"
        };
        final String[] arrayCategory = new String[]{
                "Home", "Outdoor", "Garden", "Bath",
                "Garage", "Kitchen", "Auto", "ss8", "sss9", "ssss10", "sss11","sss12","sss13","sss14","sss15","ss16"
        };
        final String[] arrayDate = new String[]{
                "01/09/2015", "01/09/2015", "01/09/2015", "01/09/2015",
                "01/09/2015", "01/09/2015", "01/09/2015", "8ttt", "9tt", "10ttt", "11ttt","12ttt","13ttt","14ttt","15ttt","16tttt"
        };
        final String[] arrayAnsComplete = new String[]{
                "Yes", "No", "No", "Yes",
                "No", "Yes", "No", "No", "No", "10ttt", "11ttt","12ttt","13ttt","14ttt","15ttt","16tttt"
        };

        ArrayList<HashMap<String, String>> titleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> data = new HashMap<>();
            data.put("title",arrayTitle[i]);
            data.put("category", arrayCategory[i]);
            data.put("date", arrayDate[i]);
            data.put("answer_complete", arrayAnsComplete[i]);
            titleList.add(data);
        }

        listView = (ListView)findViewById(R.id.list);
        //Setup adapter
        customListViewAdapter = new CustomListViewAdapter(getApplicationContext(),titleList);
        listView.setAdapter(customListViewAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int myPosition = position;
                        String itemClickId = listView.getItemAtPosition(myPosition).toString();
                        Toast.makeText(getApplicationContext(), "Id Clicked " + itemClickId, Toast.LENGTH_SHORT).show();

                        if (position == 0) {
                            Toast.makeText(StartActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                            //Intent i = new Intent(StartActivity.this, MainActivity.class);
                            //startActivity(i);
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the user choose ok then enter.
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST) {

                // get bundle
           //     Bundle extras = data.getExtras();
               // Object extras = data.getExtras();
                // get bitmap
                //final File myFile = (File) extras.get("data");
            //    final Bitmap myFile = (Bitmap) extras.get("data");


                // This is where we are hearing back from the camera
 //               final Bitmap cameraImage = (Bitmap) data.getExtras().get("data");//This is how we access the image the camera takes

//                Intent myIntent = new Intent(StartActivity.this, QuestionViewerActivity.class);
  //              myIntent.putExtra("bitmapImage", myFile);
    //            startActivity(myIntent);

//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                cameraImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                cameraImage.recycle();
//                byte[] byteArray = stream.toByteArray();
//                String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
//               myFirebaseRef.child("user_name_01/photo").setValue(imageFile); // Mods the existing name
//                myFirebaseRef.child("user_name_01/photo").addListenerForSingleValueEvent(new ValueEventListener() {



//                    String bucketName = "handleit";
  //                  String key = "photo1";
//                    TransferObserver observer = transferUtility.upload(
//                            bucketName,     /* The bucket to upload to */
//                            key,    /* The key for the uploaded object */
//                            myFile        /* The file where the data to upload exists */
//                    );
    //            File tempFile = null;
      //          TransferObserver observer = transferUtility.download(
        //                bucketName,     /* The bucket to download from */
          //              "Content-Type",    /* The key for the object to download */
            //            tempFile        /* The file to download the object to */
              //  );




/*

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Log.i("Tag", "in onDataChange");
                        String photoData = "";
                        photoData = photoData.substring(dataSnapshot.getValue().toString().indexOf(",") + 1);
                        byte[] imageAsBytes = Base64.decode(photoData, Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                        Log.i("Tag", "After setting the image2");
   //////////////////////////////////////THIS IS WHER IT STARTED BREAKING
                        Intent myIntent = new Intent(StartActivity.this, QuestionViewerActivity.class);
                        myIntent.putExtra("bitmapDecoded", photoData);
                        startActivity(myIntent);
                       // imageView = (ImageView) findViewById(R.id.help_item_image);
                        Log.i("Tag", "After setting the image3");
                       // imageView.setImageBitmap(bmp);
                        Log.i("Tag", "After setting the image4");


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
*/
       //         });
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

        if (id == R.id.nav_notifications) {
            // Handle the camera action
        } else if (id == R.id.nav_upload) {

        } else if (id == R.id.nav_pre_post) {

        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_send) {

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
}
