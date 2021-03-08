package MakeUs.Moira.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private AmazonS3 s3Client;

    @Value(value = "${S3_ACCESS_KEY}")
    private String accessKey;

    @Value(value = "${S3_SECRET_KEY}")
    private String secretKey;

    @Value(value = "${cloud.aws.s3.bucket}")
    private String bucket;

    @Value(value = "${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(MultipartFile file){
        String key = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(file.getBytes().length);
            s3Client.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), objMeta)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s3Client.getUrl(bucket, key).toString();
    }


    public void delete(String key){
        boolean isExistObject = s3Client.doesObjectExist(bucket, key);
        if (isExistObject == true) {
            s3Client.deleteObject(bucket, key);
        }
    }
}