package br.com.geofusion.cart.Controller;


import br.com.geofusion.cart.Model.Product;
import br.com.geofusion.cart.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> read(){
        return productService.readAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> read(@PathVariable("id") Long id) {
        Product product = productService.read(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(product);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Product> create(@RequestBody Product product) throws URISyntaxException {
        Product createdProduct = productService.create(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getCode())
                .toUri();

        return ResponseEntity.created(uri)
                .body(createdProduct);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable Long id) {
        Product updatedProduct = productService.update(id, product);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedProduct);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
