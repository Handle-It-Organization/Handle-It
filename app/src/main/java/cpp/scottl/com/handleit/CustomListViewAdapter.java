package cpp.scottl.com.handleit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Scott on 2/7/2016.
 */
public class CustomListViewAdapter extends BaseAdapter {
    private Context myContext;
    private ArrayList<HashMap<String,String>> buildingArray;
    private static LayoutInflater inflater = null;
    private TextView title;
    private TextView category;
    private TextView date;
    private TextView answer_complete;
    private ImageView iconImage;


    public CustomListViewAdapter(Context context, ArrayList<HashMap<String,String>>data) {
        myContext = context;
        buildingArray = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return buildingArray.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_row, null);
            title = (TextView)view.findViewById(R.id.video_title_txt);
            category = (TextView)view.findViewById(R.id.category_name);
            date = (TextView)view.findViewById(R.id.date_txt);
            answer_complete = (TextView)view.findViewById(R.id.answer_complete_txt);


            iconImage = (ImageView)view.findViewById(R.id.help_item_image);
            ImageView expandImage = (ImageView)view.findViewById(R.id.more_info);

            HashMap<String, String> myHashMap = new HashMap<>();
            myHashMap = buildingArray.get(position);
            title.setText(myHashMap.get("title"));
            category.setText(myHashMap.get("category"));
            date.setText(myHashMap.get("date"));
            iconImage.setImageBitmap(base64ToBitmap(myHashMap.get("photo")));
            expandImage.setImageDrawable(myContext.getResources().getDrawable(R.drawable.ic_navigate_next_black));
        }
        return view;
    }
    // This decodes Base64 and returns a Bitmap image to be used
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
