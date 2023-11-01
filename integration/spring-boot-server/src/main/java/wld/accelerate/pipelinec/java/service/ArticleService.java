package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.Article;
import wld.accelerate.pipelinec.java.model.ArticleModel;
import wld.accelerate.pipelinec.java.repository.ArticleRepository;
import java.util.List;
@Service
public class ArticleService {
	@Autowired
	public ArticleRepository articleRepository;


	public Article findById(Integer id) {
		return articleRepository.findById(id).orElseThrow();
	}
	public List<Article> findAll() {
		return articleRepository.findAll();
	}
	public Article createArticle(ArticleModel articleModel){
		Article article = ArticleModel.toArticle(articleModel);
		return articleRepository.save(article);
	}
	public Article updateArticle(Integer id, ArticleModel articleModel){
		Article article = findById(id);
		article.setName(article.getName());
		article.setNumber(article.getNumber());
		return articleRepository.save(article);
	}
}