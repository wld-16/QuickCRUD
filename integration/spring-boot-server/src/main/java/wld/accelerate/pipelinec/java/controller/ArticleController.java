package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.Article;
import wld.accelerate.pipelinec.java.model.ArticleModel;
import wld.accelerate.pipelinec.java.service.ArticleService;
import java.util.List;

@RestController
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@GetMapping("/article/{id}")
	public ResponseEntity<ArticleModel> getArticle(@PathVariable Integer id) {
		return ResponseEntity.ok(ArticleModel.fromArticle(articleService.findById(id)));
	}
	@GetMapping("/article/")
	public ResponseEntity<List<ArticleModel>> getAllArticle() {
		return ResponseEntity.ok(articleService.findAll().stream().map(ArticleModel::fromArticle).toList());
	}
	@PostMapping("/article/")
	public ResponseEntity<ArticleModel> saveArticle(ArticleModel articleModel) {
		Article article = articleService.createArticle(articleModel);
		return ResponseEntity.ok(ArticleModel.fromArticle(article));
	}
	@PostMapping("/article/{id}")
	public ResponseEntity<ArticleModel> updateArticle(@PathVariable Integer id, ArticleModel articleModel) {
		Article article = articleService.updateArticle(id, articleModel);
		return ResponseEntity.ok(ArticleModel.fromArticle(article));
	}
}