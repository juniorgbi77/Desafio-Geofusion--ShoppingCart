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

import javax.transaction.Transactional;
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


    @GetMapping
    public ResponseEntity readAll() {
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartService.readAll());
    }

    @GetMapping("/getaverageticket")
    public ResponseEntity getAverageTicket() {
        if (shoppingCartService.readAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Não existe nenhum carrinho!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartFactory.getAverageTicketAmount());
    }

    @PostMapping("/{clientId}")
    public ResponseEntity<ShoppingCart> createCart(@PathVariable String clientId) {
        ShoppingCart createdCart = shoppingCartFactory.create(clientId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdCart);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity removeCart(@PathVariable String clientId) {
        boolean found = shoppingCartFactory.invalidate(clientId);
        if (!found) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Esse carrinho não existe!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Deletado com sucesso!");
    }

    @PostMapping("additem/{clientId}/{productId}")
    @Transactional
    public ResponseEntity addItem(@PathVariable("clientId") String clientId, @PathVariable("productId") Long ProductId, @RequestBody ObjectNode objectNode) {

        ShoppingCart ExistingCart = shoppingCartService.FindByClientId(clientId);
        if (ExistingCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse carrinho não existe!");
        }

        Product product = productService.read(ProductId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse produto não existe!");
        }

        if (objectNode.get("unitPrice") == null || objectNode.get("quantity") == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("O parametro 'unitPrice' e 'quantity' são necessarios ");
        }

        ExistingCart.addItem(product, BigDecimal.valueOf(objectNode.get("unitPrice").asDouble()), objectNode.get("quantity").asInt());
        shoppingCartService.update(ExistingCart);
        return ResponseEntity.status(HttpStatus.OK).body(ExistingCart);
    }

    @DeleteMapping("removeitem/{clientId}/{productId}")
    @Transactional
    public ResponseEntity RemoveItem(@PathVariable("clientId") String clientId, @PathVariable("productId") Long productId) {
        ShoppingCart ExistingCart = shoppingCartService.FindByClientId(clientId);
        if (ExistingCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse carrinho não existe!");
        }

        Product product = productService.read(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse produto não existe!");
        }

        ExistingCart.removeItem(product);
        shoppingCartService.update(ExistingCart);
        return ResponseEntity.status(HttpStatus.OK).body(ExistingCart);
    }

    @PostMapping("changeprice/{clientId}/{itemID}")
    @Transactional
    public ResponseEntity ChangePrice(@PathVariable("clientId") String clientId, @PathVariable("itemID") Long itemID, @RequestBody ObjectNode objectNode) {

        ShoppingCart ExistingCart = shoppingCartService.FindByClientId(clientId);
        if (ExistingCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse carrinho não existe!");
        }

        if (objectNode.get("unitPrice") == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("O parametro 'unitPrice' é necessario ");
        }

        if(ExistingCart.changeValue(itemID, BigDecimal.valueOf(objectNode.get("unitPrice").asDouble()))){
            shoppingCartService.update(ExistingCart);
            return ResponseEntity.status(HttpStatus.OK).body(ExistingCart);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse Item não existe no carrinho!");

    }


}
