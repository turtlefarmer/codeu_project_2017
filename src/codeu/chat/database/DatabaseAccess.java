package codeu.chat.database;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by greg on 3/26/17.
 */
public class DatabaseAccess {
    private final String account = "path/to/serviceAccountKey.json";
    private final String databaseUrl = "https://project--7615521414167755910.firebaseio.com";

    public DatabaseReference initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(account);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException ex) {
            // TODO: Add logging capability
            System.out.println("Failed to load service account key: " + ex.toString());
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference();
    }
}
