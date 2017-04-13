package codeu.chat.database.Model;

import codeu.chat.common.Uuid;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by greg on 3/26/17.
 */
public class ConversationModel {
    public final String id;
    public final String owner;
    public final String creation;
    public final String title;
    public final Collection<String> users = new HashSet<>();
    public String firstMessage;
    public String lastMessage ;

    public  ConversationModel(String id, String owner, String creation, String title) {
        this.id = id;
        this.owner = owner;
        this.creation = creation;
        this.title = title;
    }
}
