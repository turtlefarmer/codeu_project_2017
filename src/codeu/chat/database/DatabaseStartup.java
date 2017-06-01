package codeu.chat.database;

import codeu.chat.database.model.ConversationModel;
import codeu.chat.database.model.UserModel;
import com.google.firebase.database.*;

import java.util.Iterator;

/**
 * Created by greg on 6/1/17.
 */
public class DatabaseStartup {
    private final String USERS_CHILD = "users";
    private final String MESSAGES_CHILD = "messages";
    private final String CONVERSATIONS_CHILD = "conversations";

    // FireBase database reference for class
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    // change to returnable list
    public void loadUsers() {
        DatabaseReference usersRef = database.child(USERS_CHILD);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                while (usersIterator.hasNext()) {
                    UserModel user = usersIterator.next().getValue(UserModel.class);
                    System.out.println("ID: " + user.id);
                    System.out.println("Name: " + user.name);
                    System.out.println("Creation: " + user.creation);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    // change to returnable list
    public void loadConversations() {
        DatabaseReference convosRef = database.child(CONVERSATIONS_CHILD);
        convosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> convosIterator = dataSnapshot.getChildren().iterator();
                while (convosIterator.hasNext()) {
                    ConversationModel convo = convosIterator.next().getValue(ConversationModel.class);
                    System.out.println("ID: " + convo.id);
                    System.out.println("Title: " + convo.title);
                    System.out.println("Owner: " + convo.owner);
                    System.out.println("Creation: " + convo.creation);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
