package com.javatechie.service;

import com.javatechie.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

//    Object addNewProduct(String productDetails,
//                         MultipartFile[] img
//    );

    Object addNewProduct(String productDetails,
                         MultipartFile img
    );

    List<Product> getAllProducts();
}
