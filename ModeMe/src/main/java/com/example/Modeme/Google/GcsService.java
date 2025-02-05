package com.example.Modeme.Google;
//import com.google.api.client.http.FileContent;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.model.File;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.Collections;
//
//@Service
//public class GoogleDriveService {
//
//    private final Drive driveService;
//
//    public GoogleDriveService(GoogleDriveConfig googleDriveConfig) throws IOException {
//        this.driveService = googleDriveConfig.getDriveService();
//    }
//
//    public String uploadFile(java.io.File file, String folderId) throws IOException {
//        File fileMetadata = new File();
//        fileMetadata.setName(file.getName());
//        fileMetadata.setParents(Collections.singletonList(folderId));
//
//        FileContent mediaContent = new FileContent("image/jpeg", file);
//        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
//                .setFields("id, webContentLink, webViewLink")
//                .execute();
//
//        return "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();
//    }
//}


import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Service
public class GcsService {

	 private final Storage storage;
	    private final String bucketName = "modeme_image_111";

	    public GcsService() throws IOException {

	        this.storage = StorageOptions.newBuilder()
	                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream("src/main/resources/credentials.json"))
	    	    		    .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-platform")))
	                .build()
	                .getService();
	    }

	    public String uploadFile(MultipartFile file) throws IOException {
	        String fileName = file.getOriginalFilename();
	        BlobId blobId = BlobId.of(bucketName, fileName);
	        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
	        storage.create(blobInfo, file.getBytes());

	        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
	    }
}

