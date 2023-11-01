package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.Product;

public class ProductModel {
	private Long id;
	private String name;
	private String description;
	public static ProductModel fromProduct(Product product) {
		ProductModel productModel = new ProductModel();
		productModel.name = product.getName();
		productModel.description = product.getDescription();
		return productModel;
	}
	public static Product toProduct(ProductModel productModel){
		Product product = new Product();
		productModel.setName(product.getName());
		productModel.setDescription(product.getDescription());
		return product;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}