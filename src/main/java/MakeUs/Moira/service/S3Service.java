package MakeUs.Moira.service;

import MakeUs.Moira.advice.exception.S3Exception;
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

    /**
     * S3를 사용하기 위한 Client 객체 생성자
     * S3의 accessKey, secretKey, bucket 이름, region을 통해 생성합니다
     */
    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    /**
     * S3에 파일을 업로드하는 함수
     * @param MultipartFile file : 저장할 파일
     * @param String key : S3에 저장할 파일 명
     *                     ex) 도메인 + '-' + ID + '-' + 파일 명 = "project-2-abc.jpg"
     */
    public String upload(MultipartFile file, String key){
        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(file.getBytes().length);
            s3Client.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), objMeta)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new S3Exception("파일 입출력 에러");
        }
        return s3Client.getUrl(bucket, key).toString();
    }

    /**
     * S3에 파일을 삭제하는 함수
     * @param String key : S3에 삭제할 파일 명
     */
    public void delete(String key){
        boolean isExistObject = s3Client.doesObjectExist(bucket, key);
        if (isExistObject == true) {
            s3Client.deleteObject(bucket, key);
        }
        else{
            throw new S3Exception("존재하지 않는 파일 혹은 이미지");
        }
    }
}