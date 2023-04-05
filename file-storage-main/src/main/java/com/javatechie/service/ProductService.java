package com.javatechie.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

//    Object addNewProduct(String productDetails,
//                         MultipartFile[] img
//    );

    Object addNewProduct(String productDetails,
                         MultipartFile img
    );
}
