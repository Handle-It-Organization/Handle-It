package cpp.scottl.com.handleit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

public class QuestionViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_viewer);
////////////////////////////////////////////////////////////////////////////////THIS IS WHERE IT BROKE
        Intent myIntent = getIntent();
        Bitmap myBitmap = (Bitmap) myIntent.getParcelableExtra("BitmapImage");

        BitmapDrawable ob = new BitmapDrawable(getResources(), myBitmap);
       // byte[] imageAsBytes = Base64.decode(bmString, Base64.DEFAULT);
        Log.i("Tag", "After setting the image1");
      //  Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        ImageView imageView = (ImageView) findViewById(R.id.image_question_viewer);
     //   imageView.setImageURI();
       // imageView.setBackgroundDrawable(ob);
       // imageView.setImageBitmap(myBitmap);
    }
}
