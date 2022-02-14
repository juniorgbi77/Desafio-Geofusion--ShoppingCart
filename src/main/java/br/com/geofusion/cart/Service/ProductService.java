package br.com.geofusion.cart.Service;

import br.com.geofusion.cart.Model.Product;
import br.com.geofusion.cart.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> readAll(){
        return repository.findAll();
    }

    public Optional<Product> read(Long id) {
        return repository.findById(id);
    }

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product update(Long id, Product newProduct) {
        return repository.findById(id)
                .map(product -> {
                    product.setDescription(newProduct.getDescription());
                    return repository.save(product);
                })
                .orElseGet(() -> {
                    return null;
                });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }


}
