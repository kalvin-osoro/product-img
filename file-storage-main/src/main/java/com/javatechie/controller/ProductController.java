package com.javatechie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.javatechie.model.Product;
import com.javatechie.respository.ProductRepository;
import com.javatechie.service.FileStorageService;
import com.javatechie.service.ProductService;
import com.javatechie.util.BaseAppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;


   private final ObjectMapper objectMapper;

   private final FileStorageService fileStorageService;


    @PostMapping("/add-product")
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestParam("productDetails") String productDetails,
                                                  @RequestParam("img") MultipartFile img
                                                  ) {
        Object product=productService.addNewProduct(productDetails,img);
        boolean success = product != null;
        //Response
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        if(success){
            //Object
            ObjectNode node = objectMapper.createObjectNode();
//          node.put("id",0);

            return ResponseEntity.ok(new BaseAppResponse(1,node,"Request Processed Successfully"));
        }else{

            //Response
            return ResponseEntity.ok(new BaseAppResponse(0,objectMapper.createObjectNode(),"Request could NOT be processed. Please try again later"));
        }
    }

    @GetMapping("/view-product")
    public ResponseEntity<Resource> getFileByName(@RequestParam (value="fileName")String fileName)
    {
        try {
            Resource file = fileStorageService.loadFileAsResourceByName(fileName);
            if (fileName.endsWith("PNG") || fileName.endsWith("png")) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + file.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                        .body(file);
            } else {
                //return pdf
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + file.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                        .body(file);
            }
//            MediaType mediaType = (file.getFilename().endsWith("PNG")) ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + file.getFilename() + "\"")
//                    .contentType(mediaType)
//                    .body(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/view-products")
    public ResponseEntity<List<Resource>> getAllFiles() {
        try {
            List<Resource> files = fileStorageService.loadAllFilesAsResources();
            if (files.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(files);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





        @GetMapping("/all")
        public ResponseEntity<List<Product>> getAllProducts() {
            List<Product> productList = productService.getAllProducts();
            return new ResponseEntity<>(productList, HttpStatus.OK);
        }




//    @PostMapping("/add-product")
//    public ResponseEntity<?> addProduct(@RequestParam("productDetails") String productDetails,
//                                        @RequestParam("img") MultipartFile img
//    ) {
//        Object product=productService.addNewProduct(productDetails,img);
//        boolean success = product != null;
//
//        //Response
//        ObjectMapper objectMapper = new ObjectMapper();
//        if(success){
//            //Object
//            ObjectNode node = objectMapper.createObjectNode();
////          node.put("id",0);
//
//            return ResponseEntity.ok(new BaseAppResponse(1,node,"Request Processed Successfully"));
//        }else{
//
//            //Response
//            return ResponseEntity.ok(new BaseAppResponse(0,objectMapper.createObjectNode(),"Request could NOT be processed. Please try again later"));
//        }
//    }


}
