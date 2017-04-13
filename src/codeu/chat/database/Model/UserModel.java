package codeu.chat.database.Model;


/**
 * Created by greg on 3/26/17.
 */
public class UserModel {
    public String name;
    public String creation;

    public UserModel(String name, String creation) {
        this.name = name;
        this.creation = creation;
    }

    public  UserModel() {

    }
}
