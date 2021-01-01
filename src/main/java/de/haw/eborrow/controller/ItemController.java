package de.haw.eborrow.controller;

import de.haw.eborrow.models.Item;
import de.haw.eborrow.models.ItemDTO;
import de.haw.eborrow.models.User;
import de.haw.eborrow.repository.ItemRepository;
import de.haw.eborrow.repository.UserRepository;
import de.haw.eborrow.services.FilesStorageService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping
public class ItemController {

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FilesStorageService storageService;

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems(@RequestParam(required = false) String title) {
        try {
            List<Item> items = new ArrayList<>();

            if (title == null)
                itemRepository.findAll().forEach(items::add);
            else
                itemRepository.findByTitleContaining(title);

            if (items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/items/user/{id}")
    public ResponseEntity<List<Item>> getItemsByUserId(@PathVariable("id")long id) {
        try {
            System.out.println(id);
            long userId = Long.valueOf(id);
            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            System.out.println(user);
            List<Item> items = new ArrayList<>();

            itemRepository.findAll().stream().filter(item -> item.getUser().equals(user.get())).forEach(items::add);
            System.out.println(items);

            if (items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") long id) {
        Optional<Item> itemData = itemRepository.findById(id);

        if (itemData.isPresent()) {
            System.out.println(itemData.get());
            return new ResponseEntity<>(itemData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping(value = "/items")
    public ResponseEntity<Item> createItem(@ModelAttribute ItemDTO itemDTO,@RequestParam(value = "fileImage",
            required = false) MultipartFile picture) {

        try {
            String title = itemDTO.getTitle();
            String description = itemDTO.getDescription();
            boolean available = Boolean.parseBoolean(itemDTO.getAvailable());
            Long _userId = Long.valueOf(itemDTO.getUser());
            User user = userRepository.getOne(_userId);
            String fileName = "";
            if (picture != null) {
                fileName = itemDTO.getUser() + "_" + itemDTO.getTitle() + "_" +
                        StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));
                String directory = "items/";
                storageService.save(picture, fileName,directory);
            }

            Item _item = itemRepository.save(new Item(title, description, fileName, available, user));
            user.addItem(_item);

            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable("id") long id, @ModelAttribute Item item,@RequestParam(value = "fileImage",
            required = false) MultipartFile picture ) {
        Optional<Item> itemData = itemRepository.findById(id);
        System.out.println(item);
        if (itemData.isPresent()) {
            Item _item = itemData.get();
            _item.setTitle(item.getTitle());
            _item.setDescription(item.getDescription());
            _item.setAvailable(item.isAvailable());
            String fileName = "";
            if (picture != null) {
                fileName = item.getUser() + "_" + item.getTitle() + "_" +
                        StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));
                String directory = "items/";
                storageService.save(picture, fileName,directory);
                item.setPicture(fileName);
            }
            _item.setPicture(item.getPicture());
            return new ResponseEntity<>(itemRepository.save(_item), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") long id) {
        try {
            itemRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/items")
    public ResponseEntity<HttpStatus> deleteAllItems() {
        try {
            itemRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/items/available")
    public ResponseEntity<List<Item>> findByAvailable() {
        try {
            List<Item> items = itemRepository.findByAvailable(true);

            if (items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(
            value = "/items/get-img/{pic:.+}",
            produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE}
    )
    public byte[] getImageWithMediaTyp(@PathVariable("pic") String pic) {
        InputStream in = null;
        try {
            in = storageService.load("/items/",pic).getInputStream();
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            return null;
        }catch (RuntimeException e){
            return null;
        }
    }

}
