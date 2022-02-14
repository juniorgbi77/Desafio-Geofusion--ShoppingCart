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
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> read(){
        return service.readAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> read(@PathVariable("id") Long id) {
        return service.read(id);
    }

    @PostMapping("/")
    public ResponseEntity<Product> create(@RequestBody Product product) throws URISyntaxException {
        Product createdProduct = service.create(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getCode())
                .toUri();

        return ResponseEntity.created(uri)
                .body(createdProduct);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable Long id) {
        Product updatedProduct = service.update(id, product);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedProduct);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
