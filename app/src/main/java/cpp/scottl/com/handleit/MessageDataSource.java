package cpp.scottl.com.handleit;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Scott on 2/13/2016.
 */
public class MessageDataSource {
    public static final Firebase sRef = new Firebase("https://shining-heat-9080.firebaseio.com/");
    public static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddmmss");
    public static final String TAG = "MessageDataSource";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_SENDER = "sender";

    public static void saveMessage(Message message, String convoId) {
        Date date = message.getmDate();
        String key = sDateFormat.format(date);
        HashMap<String, String> msg = new HashMap<>();
        msg.put(COLUMN_TEXT,message.getmText());
        msg.put(COLUMN_SENDER, "XYZ");
        sRef.child(convoId).child(key).setValue(msg);

    }

    public static MessageListener addMessagesListener(String convoId, final MessagesCallbacks callbacks){
        MessageListener listener = new MessageListener(callbacks);
        sRef.child(convoId).addChildEventListener(listener);
        return listener;

    }
    public static void stop(MessageListener listener){
        sRef.removeEventListener(listener);
    }

    public static class MessageListener implements ChildEventListener {

        private MessagesCallbacks messagesCallbacks;

        public MessageListener(MessagesCallbacks messagesCallbacks) {
            this.messagesCallbacks = messagesCallbacks;
        }

        public static void saveMessage(Message message, String convoId){
            Date date = message.getmDate();
            String key = sDateFormat.format(date);
            HashMap<String, String> msg = new HashMap<>();
            msg.put(COLUMN_TEXT, message.getmText());
            msg.put(COLUMN_SENDER,"Ajay");
            sRef.child(convoId).child(key).setValue(msg);
        }





        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            HashMap<String,String> msg = (HashMap<String,String>) dataSnapshot.getValue();
            Message message = new Message();
            message.setmSender(msg.get(COLUMN_SENDER));
            message.setmText(msg.get(COLUMN_TEXT));
            try {
                message.setmDate(sDateFormat.parse(dataSnapshot.getKey()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (messagesCallbacks != null) {
                messagesCallbacks.onMessageAdded(message);
            }
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
    }

    public interface MessagesCallbacks{
        public void onMessageAdded(Message message);
    }
}
