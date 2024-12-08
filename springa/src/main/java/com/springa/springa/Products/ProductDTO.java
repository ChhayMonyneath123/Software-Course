package com.springa.springa.Products;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Pattern;

public class ProductDTO {
    private Long id;
    private String code;
    @NotNull
    @NotEmpty(message = "Please input your name")
    // @Pattern(regexp = "/[a-zA-Z]+/", message = "Invalid Name")
    private String name;
    private String country;
    @Min(1)
    private double price;
    @ValidationCustomMultipart
    private MultipartFile image;
    private double cost;
    private String description;
    private String imageUrl;

    // Default constructor
    public ProductDTO() {
    }

    // Constructor for creating a ProductDTO with a product entity's data
    public ProductDTO(ProductEntity product) {
        this.id = product.getId();
        this.code = product.getCode();
        this.name = product.getName();
        this.country = product.getCountry();
        this.price = product.getPrice();
        this.cost = product.getCost();
        this.description = product.getDescription();
        this.imageUrl = product.getImage();
    }

    public ProductDTO(String code, String name, String country, double price, MultipartFile image, double cost,
            String description, String imageUrl) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.price = price;
        this.image = image;
        this.cost = cost;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
