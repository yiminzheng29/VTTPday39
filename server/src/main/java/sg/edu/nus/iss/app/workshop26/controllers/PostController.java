package sg.edu.nus.iss.app.workshop26.controllers;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.app.workshop26.models.Post;
import sg.edu.nus.iss.app.workshop26.services.PostService;

@Controller
@RequestMapping(path="/post")
public class PostController {
    
    private Logger logger = Logger.getLogger(PostController.class.getName());

    @Autowired
    private PostService postSvc;

    @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> postPostJson(@RequestPart MultipartFile image, @RequestPart String email, @RequestPart String title, @RequestPart String text) {

        Post post = new Post();
        post.setEmail(email);
        post.setTitle(title);
        post.setText(text);

        Optional<String> opt = postSvc.createPost(post, image);
        String postId = opt.get();

        logger.log(Level.INFO, "New postId: %s".formatted(postId));

        JsonObject resp = Json.createObjectBuilder()
            .add("postId", postId)
            .build();

        return ResponseEntity.ok(resp.toString());
    }

    @PostMapping
    public String postPost(@RequestPart MultipartFile image, @RequestPart String email,@RequestPart String title, @RequestPart String text, Model model) {

        logger.log(Level.INFO, "File name; %s\n".formatted(image.getOriginalFilename()));

        Post post = new Post();
        post.setEmail(post.getEmail());
        post.setTitle(post.getTitle());
        post.setText(post.getText());

        Optional<String> opt = postSvc.createPost(post, image);
        String postId = opt.get();

        logger.log(Level.INFO, "New postId: %s".formatted(postId));

        return "redirect:/post/%s".formatted(postId);
    }


    @GetMapping(path="{postId}")
    @ResponseBody
    public ResponseEntity<String> getPost(@PathVariable String postId, Model model) {

        Optional<Post> opt = postSvc.getPost(postId);

        if (opt.isEmpty())
            return ResponseEntity.ok("error: postId not found");

        // model.addAttribute("post", opt.get());
        return ResponseEntity.ok(opt.get().toJson().toString());
    }


    @PostMapping(path="{postId}/{vote}")
	@ResponseBody
	public ResponseEntity<String> postLike(@PathVariable String postId, @PathVariable String vote) {

		if ("like".equals(vote.trim().toLowerCase()))
			postSvc.like(postId);

		else if ("dislike".equals(vote.trim().toLowerCase()))
			postSvc.dislike(postId);

		Optional<Post> opt = postSvc.getPost(postId);
		System.out.println(">>>> update likes ----- " + opt.get());

		return ResponseEntity.ok(opt.get().toJson().toString());
	}
// }
}
