package Firebase;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import java.io.*;

public class FirebaseConfig {
    private final Storage storage;
    private final String bucketURL = "car-api-d7cf3.appspot.com";

    public FirebaseConfig() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
        storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build().getService();
    }

    public String uploadImage(String imageName, InputStream imageData) {
        try {
            BlobId blobId = BlobId.of(bucketURL, imageName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
            storage.create(blobInfo, imageData);
            System.out.println("Image uploaded successfully to Firebase Storage: " + imageName);
            return "https://firebasestorage.googleapis.com/v0/b/"+this.bucketURL+"/o/"+ imageName +"?alt=media";
        } catch (Exception exception) {
            System.out.println("Error uploading image to Firebase Storage: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }

}