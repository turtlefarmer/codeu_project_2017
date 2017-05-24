package codeu.chat.database;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import codeu.chat.util.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DatabaseAccess {
    // TODO: access service account key file via some type of config (xml) file

    private final String account = "/home/greg/Dropbox/google/codeu_project_2017/third_party/project--7615521414167755910-firebase-adminsdk-d9m2o-fb06241f95.json";
    private final String databaseUrl = "https://project--7615521414167755910.firebaseio.com";

    private final static Logger.Log LOG = Logger.newLog(DatabaseAccess.class);

    public DatabaseReference initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(account);
            LOG.info("Using service account key: %s", account);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);
            LOG.info("Service account key valid");
            LOG.info("Database initialized");
        } catch (FileNotFoundException e) {
            LOG.error("Failed to load service account key:%s ", e.toString());
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference();
    }
}
