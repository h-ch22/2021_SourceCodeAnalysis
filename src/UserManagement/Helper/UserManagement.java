package UserManagement.Helper;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
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
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UserManagement {
    private static final String BASE_URL = "https://identitytoolkit.googleapis.com/v1/";
    private static final String OPERATION_AUTH = "accounts:signInWithPassword";
    private final String firebaseKey;
    private final Firestore db;
    private final AES256Util aes = new AES256Util();

    private static UserManagement instance = null;
    private static FirebaseOptions options;

    public UserManagement() {
        firebaseKey = "AIzaSyBE6icm5bCka2H9g3eUUIA-NRz19hmL5-U";

        if (options == null){
            try{
                FileInputStream serviceAccount =
                        new FileInputStream("resources/include/moonlader-serviceAccount.json");

                options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("")
                        .build();

                FirebaseApp.initializeApp(options);
            } catch(IOException e){
                e.printStackTrace();
            }

        }

        db = FirestoreClient.getFirestore();
    }

    public static UserManagement getInstance() throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        if(instance == null){
            instance = new UserManagement();
        }

        return instance;
    }

    public int signIn(String email, String password) throws Exception{
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

            System.out.println(urlRequest.getResponseCode() + " " + urlRequest.getResponseMessage());

            urlRequest.disconnect();

        } catch(Exception e){
            e.printStackTrace();

            return 0;
        } finally{
            urlRequest.disconnect();
        }

        return urlRequest.getResponseCode();
    }

    public boolean signUp(String email, String password, String nickName){
        boolean signUpResult = false;
        UserRecord.CreateRequest request = new UserRecord.CreateRequest();

        request.setEmail(email);
        request.setPassword(password);

        try{
            UserRecord record = FirebaseAuth.getInstance().createUser(request);

            if(record.getUid() != null && !record.getUid().equals("")){
                Map<String, Object> userData = new HashMap<>();
                userData.put("mail", aes.encrypt(email));
                userData.put("nickName", aes.encrypt(nickName));

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

    public String getAccountInfo(String token) throws Exception{
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
            urlRequest.disconnect();
        }

        return email;
    }
}
