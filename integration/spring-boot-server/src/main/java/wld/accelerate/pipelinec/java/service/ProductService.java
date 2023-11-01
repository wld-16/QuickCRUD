package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.Product;
import wld.accelerate.pipelinec.java.model.ProductModel;
import wld.accelerate.pipelinec.java.repository.ProductRepository;
import java.util.List;
@Service
public class ProductService {
	@Autowired
	public ProductRepository productRepository;


	public Product findById(Integer id) {
		return productRepository.findById(id).orElseThrow();
	}
	public List<Product> findAll() {
		return productRepository.findAll();
	}
	public Product createProduct(ProductModel productModel){
		Product product = ProductModel.toProduct(productModel);
		return productRepository.save(product);
	}
	public Product updateProduct(Integer id, ProductModel productModel){
		Product product = findById(id);
		product.setName(product.getName());
		product.setDescription(product.getDescription());
		return productRepository.save(product);
	}
}