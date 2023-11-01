package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.Product;
import wld.accelerate.pipelinec.java.model.ProductModel;
import wld.accelerate.pipelinec.java.service.ProductService;
import java.util.List;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/product/{id}")
	public ResponseEntity<ProductModel> getProduct(@PathVariable Integer id) {
		return ResponseEntity.ok(ProductModel.fromProduct(productService.findById(id)));
	}
	@GetMapping("/product/")
	public ResponseEntity<List<ProductModel>> getAllProduct() {
		return ResponseEntity.ok(productService.findAll().stream().map(ProductModel::fromProduct).toList());
	}
	@PostMapping("/product/")
	public ResponseEntity<ProductModel> saveProduct(ProductModel productModel) {
		Product product = productService.createProduct(productModel);
		return ResponseEntity.ok(ProductModel.fromProduct(product));
	}
	@PostMapping("/product/{id}")
	public ResponseEntity<ProductModel> updateProduct(@PathVariable Integer id, ProductModel productModel) {
		Product product = productService.updateProduct(id, productModel);
		return ResponseEntity.ok(ProductModel.fromProduct(product));
	}
}