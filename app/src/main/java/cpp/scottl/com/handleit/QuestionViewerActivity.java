package cpp.scottl.com.handleit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DateKeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QuestionViewerActivity extends ActionBarActivity {
    private ListView listView;
    private CommentListViewAdapter commentListViewAdapter;
    private EditText comTextView;
    private Button comButton;
    private ArrayList<HashMap<String, String>> commentArrayL;
    private HashMap<String,String> commentHashM;
    private Firebase firebase;
    private TextView postDesc;
    private TextView postTitle;
    private TextView postCategory;
    private TextView postDate;
    private TextView postComplete;
    private TextView postUsername;
    private ImageView postImage;
    private int commentCountInt;
    private ArrayList<HashMap<String,String>> arrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_viewer);
        //https://shining-heat-9080.firebaseio.com/user_name_01/photo
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // This is for the firebase
        Firebase.setAndroidContext(this);
        // Create firebase object ref
        firebase = new Firebase("https://shining-heat-9080.firebaseio.com/");

        Intent myIntent = getIntent();
        final String snapshotLocation = myIntent.getStringExtra("snapshotLocation");
        postDesc = (TextView) findViewById(R.id.post_desc);
        postTitle = (TextView) findViewById(R.id.video_title_question);
        postCategory = (TextView) findViewById(R.id.category_name_question);
        postDate = (TextView) findViewById(R.id.date_txt_question);
        postComplete = (TextView) findViewById(R.id.answer_complete_txt_question);
        postUsername = (TextView) findViewById(R.id.username_question);
        postImage = (ImageView) findViewById(R.id.image_question_viewer);
        listView = (ListView) findViewById(R.id.comment_list);

        firebase.child(snapshotLocation+"/photo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Bitmap myBitmap = base64ToBitmap((String) dataSnapshot.getValue());
                postImage.setImageBitmap(myBitmap);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        firebase.child(snapshotLocation).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postDesc.setText(dataSnapshot.child("description").getValue().toString());
                postTitle.setText(dataSnapshot.child("title").getValue().toString());
                postCategory.setText(dataSnapshot.child("category").getValue().toString());
                postDate.setText(dataSnapshot.child("date").getValue().toString());
                postComplete.setText(dataSnapshot.child("complete").getValue().toString());
                postUsername.setText(dataSnapshot.child("username").getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        arrayList = popCommentListViewAdapter(snapshotLocation);

// DO LATER! ADD COMMENT TO THE LIST AFTER ONE GETS ADDED. UPDATE THE COUNTER
/*        firebase.child(snapshotLocation+"/comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<String, String> data = new HashMap<>();

                for(DataSnapshot commentSnapshot : dataSnapshot.child(String.valueOf(commentCountInt)).getChildren()){
                    //for(DataSnapshot childSnap : photoSnapshot.getChildren()){
                    data = new HashMap<>();
                    data.put("comment", commentSnapshot.child("comment").getValue().toString());
                    data.put("date", commentSnapshot.child("date").getValue().toString());
                    data.put("username", commentSnapshot.child("username").getValue().toString());
                    arrayList.add(data);
                    Log.i("COUNTS", String.valueOf(commentCountInt));
                    commentCountInt += 1;
                }

                //Setup adapter
                commentListViewAdapter = new CommentListViewAdapter(getApplicationContext(), arrayList);
                listView.setAdapter(commentListViewAdapter);
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
        });*/


        ImageView imageView = (ImageView) findViewById(R.id.image_question_viewer);
                //   imageView.setImageURI();
                // imageView.setBackgroundDrawable(ob);
                // imageView.setImageBitmap(myBitmap);
     /*    listView = (ListView) findViewById(R.id.question_listview);
       commentArrayL = new ArrayList<>();
        commentHashM = new HashMap<>();*/
        comButton = (Button) findViewById(R.id.sendMessageButton);
        comTextView = (EditText) findViewById(R.id.newMessageEditText);

        comButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                if (sharedPref.contains("username")) {
                    if (!comTextView.getText().toString().trim().equals("")) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
                        String date = mdformat.format(calendar.getTime());
                        firebase.child(snapshotLocation + "/comments/" + commentCountInt + "/comment").setValue(comTextView.getText().toString());
                        firebase.child(snapshotLocation + "/comments/" + commentCountInt + "/" + "date/").setValue(date.toString());
                        firebase.child(snapshotLocation + "/comments/" + commentCountInt + "/username").setValue(sharedPref.getString("username", ""));
                        comTextView.setText("");
                        arrayList = popCommentListViewAdapter(snapshotLocation);

                    }
                } else
                    Toast.makeText(QuestionViewerActivity.this, "You must be logged in to make a comment!",
                            Toast.LENGTH_LONG).show();

            }
        });
      /*  //Setup adapter
        commentListViewAdapter = new CommentListViewAdapter(getApplicationContext(), arrayList);
        listView.setAdapter(commentListViewAdapter);*/

    }

            public void hideKeyboard(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(comTextView.getWindowToken(), 0);
            }

            @Override
            protected void onDestroy() {
                super.onDestroy();
            }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private ArrayList<HashMap<String,String>> popCommentListViewAdapter(String ssLocation) {
        commentCountInt = 0;
        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        firebase.child(ssLocation).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> data = new HashMap<>();

                if (dataSnapshot.hasChild("comments")) {
                    for (DataSnapshot commentSnapshot : dataSnapshot.child("comments").getChildren()) {
                        data = new HashMap<>();
                        data.put("comment", commentSnapshot.child("comment").getValue().toString());
                        data.put("date", commentSnapshot.child("date").getValue().toString());
                        data.put("username", commentSnapshot.child("username").getValue().toString());
                        arrayList.add(data);
                        commentCountInt += 1;
                    }

                    //Setup adapter
                    commentListViewAdapter = new CommentListViewAdapter(getApplicationContext(), arrayList);
                    listView.setAdapter(commentListViewAdapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return arrayList;
    }
}
