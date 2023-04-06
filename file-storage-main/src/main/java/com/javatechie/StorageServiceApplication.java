package com.javatechie;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.convert.ApplicationConversionService.configure;

//@SpringBootApplication
//@RestController
//@RequestMapping("/image")
//public class StorageServiceApplication {
//
//	@Autowired
//	private StorageService service;
//
//	@Autowired
//	private StorageRepository repository;
//
//	@PostMapping
//	public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
//		String uploadImage = service.uploadImage(file);
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(uploadImage);
//	}
//
//	@GetMapping("/{fileName}")
//	public ResponseEntity<?> downloadImage(@PathVariable String fileName){
//		byte[] imageData=service.downloadImage(fileName);
//		return ResponseEntity.status(HttpStatus.OK)
//				.contentType(MediaType.valueOf("image/png"))
//				.body(imageData);
//	}
////	@GetMapping
////	public ResponseEntity<?> listAllFiles() {
//////		List<String> fileNames = service.listAllFiles();
////		List<ImageData> fileNames = service.listAllImages();
////		return ResponseEntity.status(HttpStatus.OK).body(fileNames);
////	}
//@GetMapping
//	public List<Image> getAllImages() throws IOException {
//		List<ImageData> imageDataList =repository.findAll();
//		List<Image> imageList = new ArrayList<>();
//		for (ImageData imageData : imageDataList) {
//			byte[] imageDataBytes = imageData.getImageData();
//			byte[] imageBytes = Base64.getDecoder().decode(imageDataBytes);
//			Image image = new Image() {
//				@Override
//				public int getWidth(ImageObserver observer) {
//					return 0;
//				}
//
//				@Override
//				public int getHeight(ImageObserver observer) {
//					return 0;
//				}
//
//				@Override
//				public ImageProducer getSource() {
//					return null;
//				}
//
//				@Override
//				public Graphics getGraphics() {
//					return null;
//				}
//
//				@Override
//				public Object getProperty(String name, ImageObserver observer) {
//					return null;
//				}
//			};
//			imageList.add(image);
//		}
//		return imageList;
//	}

@SpringBootApplication
public class StorageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(StorageServiceApplication.class, args);


	}

	private static void configure(SerializationFeature serializationFeature, boolean b) {
		configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

}
