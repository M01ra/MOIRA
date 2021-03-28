package MakeUs.Moira.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Service
public class FcmInitializer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init(){
        String firebaseConfigPath = "/home/ec2-user/app/config/makeus-moira-firebase-service-key.json";

        GoogleCredentials googleCredentials = null;
        try {
            googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream());
        } catch (IOException e) {
            return;
        }
        FirebaseOptions options = FirebaseOptions.builder()
                                                 .setCredentials(googleCredentials)
                                                 .build();
        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
            logger.info("파이어베이스 초기화 완료");
        }
    }
}
