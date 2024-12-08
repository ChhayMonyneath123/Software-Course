package com.springa.springa.Products;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class ProductController {
    private String pathUpload = "images/";
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public String getProducts(Model model) {
        List<ProductEntity> productEntity = productRepository.findAll();
        model.addAttribute("titleHeader", "List Product");
        model.addAttribute("products", productEntity);
        model.addAttribute("content", "fragment/list_product");
        return "products";
    }

    @GetMapping("/create-form")
    public String getProductForm(Model model) throws IOException {
        model.addAttribute("productForm", new ProductDTO());
        model.addAttribute("titleHeader", "Create Product");
        model.addAttribute("content", "fragment/form");
        return "products";
    }

    @PostMapping("/save-product")
    public String saveProduct(@Valid @ModelAttribute("productForm") ProductDTO productDTO, BindingResult bindingResult, Model model)
            throws IOException {
        if (bindingResult.hasErrors()) {
            if (productDTO.getImage().isEmpty()) {
                bindingResult.rejectValue("image", "error.productForm", "Image is required.");
            }
            model.addAttribute("titleHeader", "Create Product");
            model.addAttribute("content", "fragment/form");
            return "products";
        }

        MultipartFile image = productDTO.getImage();
        String imageUrl = "";
        Path pathImage = Paths.get(pathUpload + image.getOriginalFilename());
        Files.createDirectories(pathImage.getParent());
        Files.write(pathImage, image.getBytes());
        imageUrl = "/files/" + image.getOriginalFilename();
        productRepository.save(new ProductEntity(productDTO.getCode(), productDTO.getName(), productDTO.getCountry(),
                productDTO.getPrice(), imageUrl, productDTO.getCost(), productDTO.getDescription(),productDTO.getImageUrl()));

        return "redirect:/";
    }

    @GetMapping("/files/{file:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable("file") String filename) {
        try {
            Path imageFile = Paths.get(pathUpload).resolve(filename);
            Resource resource = new UrlResource(imageFile.toUri());
            if (resource.exists() || resource.isReadable())
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
        } catch (Exception e) {
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/update-product/{id}")
    public String getEditProduct(@PathVariable("id") Long id, Model model) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));

        // Populate DTO with product data
        ProductDTO productDTO = new ProductDTO(product);
        model.addAttribute("productForm", productDTO);
        model.addAttribute("titleHeader", "Edit Product");
        model.addAttribute("content", "fragment/form");
        return "products";
    }

    // Update product
    @PostMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute ProductDTO productDTO, BindingResult bindingResult, Model model)
            throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("titleHeader", "Edit Product");
            model.addAttribute("content", "fragment/form");
            return "products";
        }

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));

        // Update product fields from DTO
        product.updateFromDTO(productDTO);

        // Handle image upload if a new image is selected
        if (!productDTO.getImage().isEmpty()) {
            String imageUrl = saveImage(productDTO.getImage());
            product.setImage(imageUrl);
        }

        // Save updated product
        productRepository.save(product);

        return "redirect:/";
    }

    // Helper method to save an image and return the image URL
    private String saveImage(MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            Path pathImage = Paths.get(pathUpload + image.getOriginalFilename());
            Files.createDirectories(pathImage.getParent());
            Files.write(pathImage, image.getBytes());
            return "/files/" + image.getOriginalFilename();
        }
        return "";
    }
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return "redirect:/";
        } else {
            throw new IllegalArgumentException("Invalid product ID: " + id);
        }
    }

}