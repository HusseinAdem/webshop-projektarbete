package com.webshop.webshopprojektarbete.service;

import com.webshop.webshopprojektarbete.entity.Products;
import com.webshop.webshopprojektarbete.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Products> fetchAllProducts() {
        return productRepo.findAll();
    }

    public List<Products> findByName(String s){
        return productRepo.findByName(s);
    }

    public List<Products> findByBrand(String s){
        return productRepo.findByBrand(s);
    }

    public List<Products> findByColor(String s){
        return productRepo.findByColor(s);
    }

    public List<Products> findings(String s){
        List<Products> found = new ArrayList<>();
        for (int i = 0; i < findByBrand(s).size(); i++) {
            found.add(findByBrand(s).get(i));
        }
        for (int i = 0; i < findByName(s).size(); i++) {
            found.add(findByName(s).get(i));
        }
        for (int i = 0; i < findByColor(s).size(); i++) {
            found.add(findByColor(s).get(i));
        }
        return found;
    }

    public List<Products> sortProducts(List<Products> allProducts, String sortType) {
        if (sortType.equalsIgnoreCase("price high")) {
            allProducts.sort(Comparator.comparingInt(Products::getPrice).reversed());
        } else if (sortType.equalsIgnoreCase("alphabetical")) {
            allProducts.sort(Comparator.comparing(Products::getName));
        } else if (sortType.equalsIgnoreCase("Popularity")) {
            Collections.shuffle(allProducts);
        } else {
            allProducts.sort(Comparator.comparingInt(Products::getPrice));
        }
        return allProducts;
    }

    public byte[] getImageBytes(String path) {
        // Load the image file
        Resource resource = new ClassPathResource(path);
        byte[] imageBytes;
        try {
            imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imageBytes;
    }

    public HttpHeaders getImageHeaders(int imageBytesLength) {
        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytesLength);
        //headers.setCacheControl("no-store"); // Prevent caching

        return headers;
    }

}
