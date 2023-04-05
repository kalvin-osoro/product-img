package com.javatechie.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.model.Product;
import com.javatechie.model.ProductImage;
import com.javatechie.respository.ProductImageRepository;
import com.javatechie.respository.ProductRepository;
import com.javatechie.service.FileStorageService;
import com.javatechie.service.ProductService;
import com.javatechie.util.ImageUtils;
import com.javatechie.wrappers.AddProductWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

   private final ProductRepository productRepository;

    private final FileStorageService fileStorageService;
    private final ProductImageRepository productImageRepository;



    @Override
    public Object addNewProduct(String productDetails,
                                MultipartFile img
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AddProductWrapper addProductWrapper = mapper.readValue(
                    productDetails, AddProductWrapper.class);
            if (addProductWrapper == null) throw new RuntimeException("Bad request");
            Product product =new Product();
            product.setName(addProductWrapper.getName());
            product.setPrice(addProductWrapper.getPrice());
            product.setDescription(addProductWrapper.getDescription());
            Product savedProduct = productRepository.save(product);

//          Product savedproduct = productRepository.save(product);
            String imagePath = fileStorageService.saveFileWithSpecificFileNameV(
                    "product_" + savedProduct.getId() + ".PNG", img, ImageUtils.getSubFolder());
            //save
            List<String> filePathList = new ArrayList<>();
            filePathList.add(imagePath);
            List<String> downloadUrlList = new ArrayList<>();
            for (String filePath : filePathList) {
                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/upload/" + ImageUtils.getSubFolder() + "/")
                        .path(filePath)
                        .toUriString();
                downloadUrlList.add(downloadUrl);

                //save to db
                product.setImagePath(downloadUrl);

                ProductImage productImage =new ProductImage();
                productImage.setProductEntity(savedProduct.getId());
                productImage.setFilePath(downloadUrl);
                productImage.setFileName(filePath);
                productImageRepository.save(productImage);

                log.info("downloadUrl is {}", downloadUrl);
                log.info("filePath is {}", filePath);


            }
            return true;
        } catch (Exception e) {
            log.error("Error occurred while adding product", e);
        }
        return false;
    }
    }



