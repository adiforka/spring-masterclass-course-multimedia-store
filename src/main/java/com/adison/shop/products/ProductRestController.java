package com.adison.shop.products;

import com.adison.shop.common.PagedResult;
import com.adison.shop.common.web.PagedResultDTO;
import com.adison.shop.common.web.UriBuilder;
import com.adison.shop.users.UserDTO;
import com.adison.shop.users.UserRestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("${apiPrefix}/products")
@RestController
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final UriBuilder uriBuilder = new UriBuilder();

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ResponseEntity.badRequest().build();
        }
        var product = productMapper.toProduct(productDTO);
        var productId = productService.add(product).getId();
        var locationUri = uriBuilder.requestUriWithId(productId);
        return ResponseEntity.created(locationUri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        var product = productService.getById(id);
        var productDTO = productMapper.toProductDTO(product);
        productDTO.add(linkTo(methodOn(UserRestController.class).getUser(id)).withSelfRel());
        return ResponseEntity.ok(productDTO);
    }

    //how to better differentiate request urls?
    @GetMapping
    public PagedResultDTO<ProductDTO> getProductsByName(
            @RequestParam(defaultValue = "") String nameFragment,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        PagedResult<Product> productsPage = productService.getByName(nameFragment, pageNumber, pageSize);
        PagedResultDTO<ProductDTO> productsPageDTO = productMapper.toProductsPageDTO(productsPage);
        //hateoas
        productsPageDTO.add(linkTo(methodOn(ProductRestController.class)
                .getProductsByName(nameFragment, pageNumber, pageSize))
                .withSelfRel());
        return productsPageDTO;
    }

    @GetMapping("get-by-type")
    public PagedResultDTO<ProductDTO> getProductsByType(
            @RequestParam String type,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        var productsPage = productService.getByType(type, pageNumber, pageSize);
        var productsPageDTO = productMapper.toProductsPageDTO(productsPage);
        //hateoas
        productsPageDTO.add(linkTo(methodOn(ProductRestController.class)
                .getProductsByType(type, pageNumber, pageSize))
                .withSelfRel());
        return productsPageDTO;
    }

    //homework REST part 3
    @PostMapping(value = "{id}/files")
    public String submit(@PathVariable Long id, @RequestBody MultipartFile file) {
        //save to some storage
        return "File " + file.getOriginalFilename() + " uploaded";
    }
}
