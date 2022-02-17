package br.com.geofusion.cart.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Classe que representa um item no carrinho de compras.
 */

@Data
@Entity
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Product product;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    /**
     * Construtor da classe Item.
     *
     * @param product
     * @param unitPrice
     * @param quantity
     */
    public Item(Product product, BigDecimal unitPrice, int quantity) {
        super();
        this.product = product;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    /**
     * Retorna o produto.
     *
     * @return Produto
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * Retorna o valor unitário do item.
     *
     * @return BigDecimal
     */
    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    /**
     * Retorna a quantidade dos item.
     *
     * @return int
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Retorna o valor total do item.
     *
     * @return BigDecimal
     */
    public BigDecimal getAmount() {
        BigDecimal quantity = new BigDecimal(this.quantity);
        return this.unitPrice.multiply(quantity);
    }
}

