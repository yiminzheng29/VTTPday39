package sg.edu.nus.iss.app.workshop26.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import sg.edu.nus.iss.app.workshop26.models.Post;

@Repository
public class ImageRepository {
    
    private Logger logger = Logger.getLogger(ImageRepository.class.getName());

    @Value("${spaces.bucket}")
	private String spacesBucket;

	@Value("${spaces.endpoint.url}")
	private String spacesEndpointUrl;

    
    @Autowired
    private AmazonS3 s3;

    public boolean upload(Post post, MultipartFile file) {

        Map<String, String> userData = new HashMap<>();
        userData.put("name", post.getName());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            PutObjectRequest putReq = new PutObjectRequest(spacesBucket, post.getPostId(), file.getInputStream(), metadata);
            putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putReq);
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, "Put S3", ex);
            return false;
        }
        String imageUrl = "https://%s.%s/%s".formatted(spacesBucket, spacesEndpointUrl, post.getPostId());

        post.setImageUrl(imageUrl);
        
        return true;
    }
}
