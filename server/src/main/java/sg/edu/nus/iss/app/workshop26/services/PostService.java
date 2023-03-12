package sg.edu.nus.iss.app.workshop26.services;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.app.workshop26.models.Post;
import sg.edu.nus.iss.app.workshop26.models.User;
import sg.edu.nus.iss.app.workshop26.repositories.ImageRepository;
import sg.edu.nus.iss.app.workshop26.repositories.PostRepository;
import sg.edu.nus.iss.app.workshop26.repositories.UserRepository;
import sg.edu.nus.iss.app.workshop26.repositories.VoteRepository;

@Service
public class PostService {
    
    private Logger logger = Logger.getLogger(PostService.class.getName());

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ImageRepository imageRepo;

	@Autowired
	private PostRepository postRepo;

	@Autowired
	private VoteRepository voteRepo;

	public void like(String postId) {
		voteRepo.like(postId);
	}
	public void dislike(String postId) {
		voteRepo.dislike(postId);
	}

	public Optional<Post> getPost(String postId) {

		// Find the post
		Optional<Post> opt = postRepo.getPost(postId);
		if (opt.isEmpty())
			return Optional.empty();

		// Get the votes
		Map<String, Integer> votes = voteRepo.getVotes(postId);

		Post post = opt.get();
		post.setLike(votes.get("like"));
		post.setDislike(votes.get("dislike"));

		return Optional.of(post);
	}

	public Optional<String> createPost(Post post, MultipartFile file) {

		// Check if the user is valid
		Optional<User> opt = userRepo.findUserByEmail(post.getEmail());
		if (opt.isEmpty())
			return Optional.empty();

		// Fill the post with pertinent user details
		User user = opt.get();
		post.setName(user.getName());
		post.setUserId(user.getUserId());

		// Set the post date
		post.now();

		// Set a unique post id
		String postId = UUID.randomUUID().toString().substring(0, 8);
		post.setPostId(postId);

		// Upload the image to Spaces
		imageRepo.upload(post, file);

		// Create post in Mongo
		ObjectId objectId = postRepo.insertPost(post);

		logger.log(Level.INFO
				, "postId: %s -> _id: %s".formatted(postId, objectId.toString()));

		voteRepo.initialize(postId);

		// Setup likes and dislike
		return Optional.of(postId);
	}
}