package cpp.scottl.com.handleit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Matrix;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateQuestion extends AppCompatActivity {

    private TextView questionTitle, questionDesc;
    private ImageView questionPhoto;
    private Button questionSubmit, questionChooseImage, qTakePhoto, rotatePhoto;
    private Spinner questionCategorySpin;
    private Firebase firebase;
    private String spinVal, title, description;
    private Bitmap selectedBitmap;
    private final static int SELECT_PHOTO = 12345;
    private int angle = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        // This is for the firebase
        Firebase.setAndroidContext(this);
        // Create firebase object ref
        firebase = new Firebase("https://shining-heat-9080.firebaseio.com/");

        questionTitle = (TextView) findViewById(R.id.question_title);
        questionDesc = (TextView) findViewById(R.id.question_desc);
        questionPhoto = (ImageView) findViewById(R.id.question_photo);
        questionSubmit = (Button) findViewById(R.id.question_submit);
        questionChooseImage = (Button) findViewById(R.id.question_image_picker);
        questionCategorySpin = (Spinner) findViewById(R.id.question_category_spinner);
        rotatePhoto = (Button) findViewById(R.id.question_photo_rotate);
        qTakePhoto = (Button) findViewById(R.id.question_image_taker);

        // Grabs the photo string from the intent that was passed
        Bundle extras = getIntent().getExtras();

        if (extras.getString("photo").equals("none")) {
            Log.i("INIF", "in if");
        }else{
            String passedPhoto = extras.getString("photo");
            selectedBitmap = base64ToBitmap(passedPhoto);
            questionPhoto.setImageBitmap(selectedBitmap);
        }


        // Create an adapter from the string array resource and use
        // android's inbuilt layout file simple_spinner_item
        // that represents the default spinner in the UI
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        // Set the layout to use for each dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionCategorySpin.setAdapter(adapter);

        rotatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                angle += 90;
                Bitmap rotatedBitmap = rotateImage(selectedBitmap, angle);
                questionPhoto.setImageBitmap(rotatedBitmap);
            }
        });
        qTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
                /*Used b4 the code above that was gotten from internet 3/9/16
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }*/
            }
        });

        questionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                title = questionTitle.getText().toString().trim();
                description = questionDesc.getText().toString().trim();
                String uploadType = intent.getStringExtra("uploadType");
                spinVal = String.valueOf(questionCategorySpin.getSelectedItem()).trim();
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String username = sharedPref.getString("username", "");
                String userId = sharedPref.getString("uid", "");
                String userEmail = sharedPref.getString("email", "");
                String complete = "No";

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
                String date = mdformat.format(calendar.getTime());

//I NEED TO FIX THE PREMISSION FOR FIREBASE. ONLY REGISTERED USERS CAN POST A QUESTION!!!!!!!!!!!!!!!!!!!
                if(title.isEmpty() || description.isEmpty()){
                    Toast.makeText(CreateQuestion.this, "You must enter a title and description.", Toast.LENGTH_LONG).show();
                }else{
                    Question question = new Question(title, description, spinVal.toLowerCase(),
                            username, userId, userEmail, complete, date, bitmapToBase64(selectedBitmap));
                    Log.i("LLLL", "question/" + uploadType.trim() + "/" + spinVal.toLowerCase()
                            + "/" + title);
                    firebase.child("question/" + uploadType.trim()+"/"+spinVal.toLowerCase()
                            +"/"+title).setValue(question);
                    Intent myIntent = new Intent(CreateQuestion.this, StartActivity.class);
                    startActivity(myIntent);

                }

            }
        });

        questionChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TESTS", "0");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                Log.i("TESTS", "1");
                photoPickerIntent.setType("image/*");
                Log.i("TESTS", "2");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    private Bitmap rotateImage(Bitmap sourceImage, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(sourceImage, 0, 0, sourceImage.getWidth(), sourceImage.getHeight(), matrix, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check that request code matches ours:
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            //Get our saved file into a bitmap object:
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
            selectedBitmap = bitmap;
            questionPhoto.setImageBitmap(bitmap);
        }
        // This is for when the user takes the picture
        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            selectedBitmap = extras.getParcelable("data");
            //selectedBitmap = (Bitmap) extras.get("data");
            questionPhoto.setImageBitmap(selectedBitmap);
        }*/
        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            Log.i("TESTS", "3");
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            selectedBitmap = BitmapFactory.decodeFile(imagePath, options);
            questionPhoto.setImageBitmap(selectedBitmap);
            Log.i("TESTS", "4");

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateQuestion.this, StartActivity.class);
        startActivity(intent);
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
}
