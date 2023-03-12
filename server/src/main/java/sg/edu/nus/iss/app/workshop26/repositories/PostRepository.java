package sg.edu.nus.iss.app.workshop26.repositories;

import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.app.workshop26.models.Post;

@Repository
public class PostRepository {
    
    @Autowired
    private MongoTemplate template;

    public ObjectId insertPost(Post post) {
        Document insertedDoc = template.insert(post.toDocument(), "post");
        return insertedDoc.getObjectId("_id");
    }

    public Optional<Post> getPost(String postId) {
        Criteria c = Criteria.where("postId").is(postId);
        Query query = Query.query(c);
        Document result = template.findOne(query, Document.class, "post");
        if (result == null)
            return Optional.empty();
        return Optional.of(Post.create(result));
    }
}
