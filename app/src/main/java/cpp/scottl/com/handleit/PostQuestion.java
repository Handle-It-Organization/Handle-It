package cpp.scottl.com.handleit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PostQuestion extends AppCompatActivity {
    private Button upVideoButton;
    private Button upPhotoButton;
    private Button upTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);

        upVideoButton = (Button) findViewById(R.id.upload_video_button);
        upPhotoButton = (Button) findViewById(R.id.upload_photo_button);
        upTextButton = (Button) findViewById(R.id.upload_text_button);

        upVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostQuestion.this, QuestionViewerActivity.class);
                intent.putExtra("uploadType", "video");
                startActivity(intent);
            }
        });
        upPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostQuestion.this, CreateQuestion.class);
                intent.putExtra("uploadType", "photo");
                intent.putExtra("photo", "none");
                startActivity(intent);
            }
        });
        upTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostQuestion.this, QuestionViewerActivity.class);
                intent.putExtra("uploadType", "photo");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PostQuestion.this, StartActivity.class);
        startActivity(intent);
    }
}
