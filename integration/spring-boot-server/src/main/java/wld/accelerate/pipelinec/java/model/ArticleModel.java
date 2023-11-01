package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.Article;

public class ArticleModel {
	private Long id;
	private String name;
	private String number;
	public static ArticleModel fromArticle(Article article) {
		ArticleModel articleModel = new ArticleModel();
		articleModel.name = article.getName();
		articleModel.number = article.getNumber();
		return articleModel;
	}
	public static Article toArticle(ArticleModel articleModel){
		Article article = new Article();
		articleModel.setName(article.getName());
		articleModel.setNumber(article.getNumber());
		return article;
	}

	public String getName(){
		return name;
	}

	public String getNumber(){
		return number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}