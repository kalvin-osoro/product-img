package com.javatechie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.javatechie.respository.ProductRepository;
import com.javatechie.service.ProductService;
import com.javatechie.util.BaseAppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;


   private final ObjectMapper objectMapper;


    @PostMapping("/add-product")
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestParam("productDetails") String productDetails,
                                                  @RequestParam("img") MultipartFile img
                                                  ) {
        Object product=productService.addNewProduct(productDetails,img);
        boolean success = product != null;

        //Response
        ObjectMapper objectMapper = new ObjectMapper();
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
