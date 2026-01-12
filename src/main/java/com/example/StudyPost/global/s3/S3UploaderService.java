package com.example.StudyPost.global.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploaderService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();// 오리지널 파일 이름
        String storeFileName = createStoreFileName(originalFilename);// 파일 이름 중복 방지 위해 UUID를 붙힘.
        String key = dirName + "/" + storeFileName; //

        PutObjectRequest putObjectRequest = PutObjectRequest.builder() // S3에 파일전송
                .bucket(bucket)
                .key(key)
                .contentType(multipartFile.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(key)).toExternalForm();
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        try {
            int pos = originalFilename.lastIndexOf(".");
            return originalFilename.substring(pos + 1);
        } catch (StringIndexOutOfBoundsException e) {
            // 확장자가 없는 경우 처리
            return "";
        }
    }
}
