package org.hspconsortium.platform.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.*;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hspconsortium.platform.authorization.repository.impl.FirebaseUserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class FirebaseTokenService {

    private Log log = LogFactory.getLog(FirebaseTokenService.class);

    private FirebaseApp firebaseApp;

    @Value("${hspc.platform.firebase.projectName}")
    private String firebaseProject;

    @Value("${hspc.platform.firebase.databaseUrl}")
    private String firebaseDatabaseUrl;

    private final Semaphore dataAccessSemaphore = new Semaphore(0);
    private FirebaseUserProfileDto userProfileDto = null;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private EncryptionService encryptionService;

    @PostConstruct
    private void initFirebase() {
        InputStream firebaseCredentials = null;
        try {
            InputStream encryptedFirebaseCredentials = null;
            encryptedFirebaseCredentials = resourceLoader.getResource("classpath:firebase-key_" + firebaseProject + ".json").getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(encryptedFirebaseCredentials));
            String encryptedFirebaseCredentialsString = buffer.lines().collect(Collectors.joining("\n"));

            String firebaseCredentialsString = encryptionService.decrypt(encryptedFirebaseCredentialsString);
            //String firebaseCredentialsString = encryptedFirebaseCredentialsString;
            //log.info(firebaseCredentialsString);
            //log.info(encryptionService.encrypt(encryptedFirebaseCredentialsString));

            firebaseCredentials = new ByteArrayInputStream(firebaseCredentialsString.getBytes());
            encryptedFirebaseCredentials.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(firebaseCredentials))
                .setDatabaseUrl(firebaseDatabaseUrl)
                .build();

        firebaseApp = FirebaseApp.initializeApp(options);
    }

    public FirebaseToken validateToken(String firebaseJwt) {
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            Task<FirebaseToken> varifiedToken = firebaseAuth.verifyIdToken(firebaseJwt);
            Tasks.await(varifiedToken);
            return varifiedToken.getResult();
        } catch (Throwable ex) {
            log.info(ex.getMessage());
            //ex.printStackTrace();
            log.info("Expired token value: " + firebaseJwt);
            return null;
        }
    }

    public FirebaseUserProfileDto getUserProfileInfo(String email) {
        log.trace("FirebaseTokenService.getUserProfileInfo "+email);
        userProfileDto = null;
        final FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp);
        DatabaseReference ref = database.getReference("users");

        ref.orderByChild("email").startAt(email).endAt(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() < 1) {
                    dataAccessSemaphore.release();
                    return;
                }
                userProfileDto = dataSnapshot.getChildren().iterator().next().getValue(FirebaseUserProfileDto.class);
                dataAccessSemaphore.release();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DatabaseError err = databaseError;
                log.error(err.getMessage());
                dataAccessSemaphore.release();
            }
        });

        try {
            dataAccessSemaphore.acquire();
            return userProfileDto;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userProfileDto;
    }
}
