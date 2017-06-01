package codeu.chat.database.model;

/**
 * Created by greg on 6/1/17.
 */
public class MessageModel {
    String id;
    String author;
    String content;
    String creation;

    public MessageModel(String author, String content, String creation) {
        this.author = author;
        this.content = content;
        this.creation = creation;
    }
    // Default constructor needs to be explicitly defined for FireBase
    public MessageModel() {}
}
