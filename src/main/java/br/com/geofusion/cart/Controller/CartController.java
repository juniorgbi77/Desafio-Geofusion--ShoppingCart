package br.com.geofusion.cart.Controller;

import br.com.geofusion.cart.Factory.ShoppingCartFactory;
import br.com.geofusion.cart.Model.Product;
import br.com.geofusion.cart.Model.ShoppingCart;
import br.com.geofusion.cart.Service.ProductService;
import br.com.geofusion.cart.Service.ShoppingCartService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private ShoppingCartFactory shoppingCartFactory;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ProductService productService;


    @GetMapping("/")
    public ResponseEntity<List<ShoppingCart>> readAll(){
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartService.readAll());
    }

    @GetMapping("/getAverageTicket")
    public BigDecimal getAverageTicket(){
        return shoppingCartFactory.getAverageTicketAmount();
    }

    @PostMapping("/{clientId}")
    public ResponseEntity<ShoppingCart> createCart(@PathVariable String clientId){
        ShoppingCart createdCart = shoppingCartFactory.create(clientId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdCart);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity removeCart(@PathVariable String clientId){
        boolean found = shoppingCartFactory.invalidate(clientId);
        if(!found){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PostMapping("additem/{clientId}/{ProductId}")
    public ResponseEntity<ShoppingCart> addItem(@PathVariable("clientId") String clientId,@PathVariable("ProductId") Long ProductId, @RequestBody ObjectNode objectNode){
        ShoppingCart ExistingCart = shoppingCartService.FindByClientId(clientId);
        Product product = productService.read(ProductId);

        if(ExistingCart == null && product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ExistingCart.addItem(product,BigDecimal.valueOf(objectNode.get("unitPrice").asDouble()), objectNode.get("quantity").asInt());
        shoppingCartService.update(ExistingCart);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ExistingCart);
    }

    @DeleteMapping("removeitem/{clientId}/{ProductId}")
    public ResponseEntity<ShoppingCart> RemoveItem(@PathVariable("clientId") String clientId,@PathVariable("ProductId")  Long ProductId){
        ShoppingCart ExistingCart = shoppingCartService.FindByClientId(clientId);
        Product product = productService.read(ProductId);

        if(ExistingCart == null && product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ExistingCart.removeItem(product);
        shoppingCartService.update(ExistingCart);
        return ResponseEntity.status(HttpStatus.OK).body(ExistingCart);
    }


}
