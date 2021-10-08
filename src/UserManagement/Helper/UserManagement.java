package UserManagement.Helper;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.Helper.AES256Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class UserManagement extends AES256Util {
    private static final String BASE_URL = "https://identitytoolkit.googleapis.com/v1/";
    private static final String OPERATION_AUTH = "accounts:signInWithPassword";

    private final String firebaseKey;
    private static UserManagement instance = null;
    private static FirebaseOptions options;

    protected static UserRecord userRecord = null;
    protected final Firestore db;
    protected Preferences signInPrefs = Preferences.userNodeForPackage(UserManagement.class);

    public UserManagement() {
        firebaseKey = "AIzaSyBE6icm5bCka2H9g3eUUIA-NRz19hmL5-U";

        if (options == null){
            try{
                FileInputStream serviceAccount =
                        new FileInputStream("src/resources/include/moonlader-serviceAccount.json");

                options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://moonlader-4a071-default-rtdb.firebaseio.com/")
                        .build();

                FirebaseApp.initializeApp(options);
            } catch(IOException e){
                e.printStackTrace();
            }

        }

        db = FirestoreClient.getFirestore();
    }

    protected static UserManagement getInstance() throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        if(instance == null){
            instance = new UserManagement();
        }

        return instance;
    }

    protected int signIn(String email, String password) throws Exception{
        HttpURLConnection urlRequest = null;

        try{
            URL url = new URL(BASE_URL + OPERATION_AUTH + "?key=" + firebaseKey);
            urlRequest = (HttpURLConnection)url.openConnection();
            urlRequest.setRequestMethod("POST");
            urlRequest.setDoOutput(true);
            urlRequest.setRequestProperty("Content-Type", "application/json");
            String data = "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}".formatted(email, password);
            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            OutputStream stream = urlRequest.getOutputStream();
            stream.write(out);

            userRecord = FirebaseAuth.getInstance().getUserByEmail(email);

            urlRequest.disconnect();

        } catch(Exception e){
            e.printStackTrace();

            return 0;
        } finally{
            assert urlRequest != null;
            urlRequest.disconnect();
        }

        return urlRequest.getResponseCode();
    }

    protected boolean signUp(String email, String password, String nickName){
        boolean signUpResult = false;
        UserRecord.CreateRequest request = new UserRecord.CreateRequest();

        request.setEmail(email);
        request.setPassword(password);

        try{
            UserRecord record = FirebaseAuth.getInstance().createUser(request);

            if(record.getUid() != null && !record.getUid().equals("")){
                Map<String, Object> userData = new HashMap<>();
                userData.put("mail", encrypt(email));
                userData.put("nickName", encrypt(nickName));

                try{
                    db.collection("Users").document(record.getUid()).set(userData);

                    signUpResult = true;
                }  catch(Exception e){
                    e.printStackTrace();
                }


            }
        }  catch(Exception ex){
            ex.printStackTrace();
        }

        return signUpResult;
    }

    protected void registerAutoSignIn(String email, String password){
        signInPrefs.put("email", email);
        signInPrefs.put("password", password);
    }

    public boolean signOut(){
        boolean result = false;

        userRecord = null;
        instance = null;
        signInPrefs.remove("email");
        signInPrefs.remove("password");

        if(userRecord == null && instance == null && signInPrefs.get("email", "").equals("") && signInPrefs.get("password", "").equals("")){
            result = true;
        }

        else{
            result = false;
        }

        return result;
    }

    protected String getAccountInfo(String token) throws Exception{
        HttpURLConnection urlRequest = null;
        String email = null;

        try{
            URL url = new URL(BASE_URL);
            urlRequest = (HttpURLConnection) url.openConnection();
            urlRequest.setDoOutput(true);
            urlRequest.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStream stream = urlRequest.getOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(stream, "UTF-8");
            streamWriter.write("{\"idToken\":\""+token+"\"}");
            streamWriter.flush();
            streamWriter.close();
            urlRequest.connect();

            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) urlRequest.getContent()));
            JsonObject rootobj = root.getAsJsonObject();

            email = rootobj.get("users").getAsJsonArray().get(0).getAsJsonObject().get("email").getAsString();
        }  catch(Exception e){
            return null;
        }  finally{
            assert urlRequest != null;
            urlRequest.disconnect();
        }

        return email;
    }
}
