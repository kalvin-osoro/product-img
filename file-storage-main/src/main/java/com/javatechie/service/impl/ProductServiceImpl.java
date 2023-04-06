package com.javatechie.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    @Value("${spring.server.name}")
    private String serverName;

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

            if (img.isEmpty()) {
                throw new RuntimeException("Image file is empty");
            }
//
////          Product savedproduct = productRepository.save(product);

            String imagePath = fileStorageService.saveFileWithSpecificFileNameV(
                    "product_" + savedProduct.getId() + ".PNG", img, ImageUtils.getSubFolder());

//            String fileExtension = FilenameUtils.getExtension(img.getOriginalFilename());
//            String fileName = "product_" + savedProduct.getId() + "_" +
//                    ImageUtils.generateUniqueNoByDate() + "." + fileExtension;
//            String imagePath = fileStorageService.saveFileWithSpecificFileNameV(fileName, img,



                    //save
            List<String> filePathList = new ArrayList<>();
            filePathList.add(imagePath);
            List<String> downloadUrlList = new ArrayList<>();
            for (String filePath : filePathList) {
//                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()

//                        .path("/upload/" + ImageUtils.getSubFolder() + "/")
//                        .path(filePath)
//                        .toUriString();
//                downloadUrlList.add(downloadUrl);

                String fileDownLoadUri = UriComponentsBuilder.fromUriString(serverName)
                        .path("/product/view-product")
                        .queryParam("fileName", filePath)
                        .build()
                        .encode()
                        .toUriString();
                log.info("The composed path: " + fileDownLoadUri);

                //save to db
                product.setImagePath(fileDownLoadUri);
                productRepository.save(product);

//                ProductImage productImage =new ProductImage();
//                productImage.setProductEntity(savedProduct.getId());
////                productImage.setFilePath(downloadUrl);
//                productImage.setFilePath(downloadUrlList.get(0));
//                productImage.setFileName(filePath);
//                productImageRepository.save(productImage);



                log.info("downloadUrl is {}", fileDownLoadUri);
                log.info("filePath is {}", filePath);


            }
//            return true;
            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
            log.error("Error occurred while adding product", e);
        }
        return false;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


//    @Override
//    public List<Product> listProducts(MultipartFile file) {
//        List<Product> products = productRepository.findAll();
//        List<AddProductWrapper> addProductWrappers = new ArrayList<>();
//        for (Product product: products) {
//            AddProductWrapper addProductWrapper = getDtoFromCategory(Product, file);
//            AddProductWrapper.add(categoryDto);
////            productDtos.add(getProductDto(product));
//        }
//        return categoryDtos;
//    }







//    @Override
//    public Object getProductById(AssetByIdRequest model) {
//        try {
//            if (model.getAssetId() == null) {
//                log.error("Asset id is null");
//                return null;
//            }
//            //get merchant by id
//            DFSVoomaAssetEntity acquiringOnboardEntity = dfsVoomaAssetRepository.findById(model.getAssetId()).get();
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectNode asset = mapper.createObjectNode();
//            asset.put("id", acquiringOnboardEntity.getId());
//            asset.put("condition", acquiringOnboardEntity.getAssetCondition().toString());
//            asset.put("serialNo", acquiringOnboardEntity.getSerialNumber());
//            asset.put("dateIssued", acquiringOnboardEntity.getDateAssigned().getTime());
//            asset.put("dsrId", acquiringOnboardEntity.getDsrId());
//            asset.put("terminalId", acquiringOnboardEntity.getTerminalId());
//            asset.put("assetNumber", acquiringOnboardEntity.getAssetNumber());
//            asset.put("visitDate", acquiringOnboardEntity.getVisitDate());
//            asset.put("totalTransaction", 0);
//            asset.put("location", acquiringOnboardEntity.getLocation());
//            asset.put("AgentName", acquiringOnboardEntity.getMerchantName());
//            asset.put("status", acquiringOnboardEntity.getStatus().toString());
//            List<DFSVoomaAssetFilesEntity> dfsVoomaFileUploadEntities = dfsVoomaAssetFilesRepository.findByIdAsset(model.getAssetId());
//            ArrayNode fileUploads = mapper.createArrayNode();
//            for (DFSVoomaAssetFilesEntity dfsVoomaFileUploadEntity : dfsVoomaFileUploadEntities) {
//                ObjectNode fileUpload = mapper.createObjectNode();
//                String[] fileName = dfsVoomaFileUploadEntity.getFileName().split("/");
//                fileUpload.put("fileName", fileName[fileName.length - 1]);
//                fileUploads.add(fileUpload);
//            }
//            asset.put("fileUploads", fileUploads);
//            return asset;
//        } catch (Exception e) {
//            log.error("Error occurred while fetching merchant by id", e);
//        }
//        return null;
//    }



}



