package com.adison.shop.products;

import com.adison.shop.payments.LocalMoney;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final Long PRODUCT1_ID = 666L;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    private ProductService productService;

    private static final Product PRODUCT_1 = Product.builder()
            .id(PRODUCT1_ID)
            .name("ESP Eclipse II")
            .description("Single cutaway solid-body electric guitar")
            .price(LocalMoney.of(1999.99))
            .type(ProductType.AUDIO)
            .build();

    private static final Product PRODUCT_2 = Product.builder()
            .name("Gibson Les Paul Standard 2020")
            .description("Single cutaway solid-body electric guitar")
            .price(LocalMoney.of(2499.99))
            .type(ProductType.AUDIO)
            .build();

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, productMapper);
    }

    @Test
    void shouldAddProduct() {
        //given
        when(productRepository.save(any(Product.class))).then(returnsFirstArg());
        //when
        Product addedProduct = productService.add(PRODUCT_1);
        //then
        assertEquals(PRODUCT_1, addedProduct);
    }

    @Test
    void shouldUpdateProductWhenValidProductAndIdGiven() {
        //given
        when(productRepository.save(any(Product.class))).then(returnsFirstArg());
        when(productRepository.findById(PRODUCT1_ID)).thenReturn(Optional.ofNullable(PRODUCT_1));
        doCallRealMethod().when(productMapper).updateProduct(PRODUCT_2, PRODUCT_1);
        productService.add(PRODUCT_1);
        //when
        Product updatedProduct = productService.update(PRODUCT_2, PRODUCT1_ID);
        //then
        assertNotNull(updatedProduct);
        assertEquals(PRODUCT_2.getName(), updatedProduct.getName());
        assertEquals(PRODUCT_2.getPrice(), updatedProduct.getPrice());
        assertEquals(PRODUCT_2.getDescription(), updatedProduct.getDescription());

        verify(productRepository).flush();
        verify(productRepository, atMost(1)).findById(anyLong());
        verify(productMapper).updateProduct(any(Product.class), any(Product.class));
    }
}
