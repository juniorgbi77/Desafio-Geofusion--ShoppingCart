package br.com.geofusion.cart.Service;

import br.com.geofusion.cart.Model.Product;
import br.com.geofusion.cart.Model.ShoppingCart;
import br.com.geofusion.cart.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository repository;

    public List<ShoppingCart> readAll(){
        return repository.findAll();
    }

    public ShoppingCart FindByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }

    public ShoppingCart create(ShoppingCart shoppingCart) {
        return repository.save(shoppingCart);
    }

    public ShoppingCart update(ShoppingCart shoppingCart) {
        return repository.save(shoppingCart);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
