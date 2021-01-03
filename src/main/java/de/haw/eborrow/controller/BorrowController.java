package de.haw.eborrow.controller;

import de.haw.eborrow.models.Borrow;
import de.haw.eborrow.models.Item;
import de.haw.eborrow.models.ItemDTO;
import de.haw.eborrow.models.User;
import de.haw.eborrow.repository.BorrowRepository;
import de.haw.eborrow.repository.ItemRepository;
import de.haw.eborrow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping
public class BorrowController {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    BorrowRepository borrowRepository;

    @PostMapping("/borrow")
    public ResponseEntity<Borrow> borrowItem(@RequestBody Map<String, Object> borrow) {
        System.out.println(borrow);

        try {
            Long userId = Long.valueOf((int) borrow.get("userId"));
            User user = userRepository.getOne(userId);

            Long itemId = Long.valueOf((int) borrow.get("itemId"));
            Item item = itemRepository.getOne(itemId);

            if (!item.isAvailable())
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);

            Date borrowFrom = new SimpleDateFormat("yyyy-MM-dd").parse((String) borrow.get("borrowFrom"));
            Date borrowTo = new SimpleDateFormat("yyyy-MM-dd").parse((String) borrow.get("borrowTo"));

            if (borrowFrom.getDate() > borrowTo.getDate())
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            Borrow _borrow = borrowRepository.save(new Borrow(user, item, borrowFrom, borrowTo));
            item.addItemToBorrowedItems(_borrow);
            item.setAvailable(false);
            user.addItemToBorrowedItems(_borrow);

            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/borrow/{user-id:}")
    public ResponseEntity<List<Borrow>> getBorrowedItems(@PathVariable("user-id") String _userId) {
        try {
            Long userId = Long.valueOf(_userId);
            User user = userRepository.getOne(userId);
            List<Borrow> borrowedList = borrowRepository.findByUser(user);

            System.out.println("getBorrowedItems...." + borrowedList);
            return new ResponseEntity<>(borrowedList, HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println("getBorrowedItems....error");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
