package cpp.scottl.com.handleit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Scott on 2/21/2016.
 */
public class CommentListViewAdapter extends BaseAdapter{

    private Context myContext;
    private ArrayList<HashMap<String,String>> buildingArray;
    private static LayoutInflater inflater = null;
    private TextView userComTextView;
    private TextView category;
    private TextView date;
    private TextView answer_complete;
    private TextView usernameTextView;


    public CommentListViewAdapter(Context context, ArrayList<HashMap<String,String>>data) {
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
            view = inflater.inflate(R.layout.comment_list_row, null);
            userComTextView = (TextView)view.findViewById(R.id.userComTextView);
            usernameTextView = (TextView) view.findViewById(R.id.commentUsername);
            date = (TextView) view.findViewById(R.id.commentDate);

            HashMap<String, String> myHashMap = new HashMap<>();
            myHashMap = buildingArray.get(position);
            userComTextView.setText(myHashMap.get("comment"));
            usernameTextView.setText(myHashMap.get("username"));
            date.setText(myHashMap.get("date"));

    //        iconImage.setImageDrawable(myContext.getResources().getDrawable(R.drawable.ic_perm_media));
    //        expandImage.setImageDrawable(myContext.getResources().getDrawable(R.drawable.ic_navigate_next_black));
        }
        return view;
    }
}

