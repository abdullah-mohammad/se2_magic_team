package de.haw.eborrow.controller;

import de.haw.eborrow.models.*;
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
import java.util.*;
import java.util.stream.Collectors;

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
        try {
            Long userId = Long.valueOf((int) borrow.get("userId"));
            User user = userRepository.getOne(userId);

            Long itemId = Long.valueOf((int) borrow.get("itemId"));
            Item item = itemRepository.getOne(itemId);

            Date borrowFrom = new SimpleDateFormat("yyyy-MM-dd").parse((String) borrow.get("borrowFrom"));
            Date borrowTo = new SimpleDateFormat("yyyy-MM-dd").parse((String) borrow.get("borrowTo"));

            if (borrowFrom.getDate() > borrowTo.getDate())
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            if (!(item.getAvailableFrom().getDate() <= borrowFrom.getDate() &&
                    item.getAvailableTo().getDate() >= borrowTo.getDate()))
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);


            int itemExistInBorrow = borrowRepository.getBorrowedItemIn(itemId, borrowFrom, borrowTo);

            if (itemExistInBorrow != 0)
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);


            Borrow _borrow = borrowRepository.save(new Borrow(user, item, borrowFrom, borrowTo));
            item.addItemToBorrowedItems(_borrow);
            user.addItemToBorrowedItems(_borrow);

            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/borrow/user/{id}")
    public ResponseEntity<List<Borrow>> getBorrowedItems(@PathVariable("id") long id) {
        try {
            System.out.println(id);
            Long userId = Long.valueOf(id);
            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<Borrow> borrowedList = borrowRepository.findByUser(user);

            if (borrowedList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            System.out.println("getBorrowedItems...." + borrowedList);
            return new ResponseEntity<>(borrowedList, HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println("getBorrowedItems....error");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/borrows/{item-id}")
    public List<BorrowDTO> getAllBorrowsOfItem(@PathVariable("item-id") String _itemId) {
        try {
            Long itemId = Long.valueOf(_itemId);
            Item item = itemRepository.getOne(itemId);
            List<Borrow> borrowedList = borrowRepository.findByItem(item);

            return borrowedList.stream().map(myborrow ->
                    new BorrowDTO(myborrow.getId(), myborrow.getBorrowFrom(), myborrow.getBorrowTo())).collect(Collectors.toList());
        } catch (Exception ex) {
            System.out.println("getAllBorrowsOfItem....error");
        }
        return null;
    }

    @PostMapping("/borrow/check")
    public ResponseEntity<Integer> itemExistBereich(@RequestBody Map<String, Object> borrow) {
        int itemExistInBorrow = 0;
        try {
            Long itemId = Long.valueOf((int) borrow.get("itemId"));

            Date borrowFrom = new SimpleDateFormat("yyyy-MM-dd").parse((String) borrow.get("borrowFrom"));
            Date borrowTo = new SimpleDateFormat("yyyy-MM-dd").parse((String) borrow.get("borrowTo"));

            itemExistInBorrow = borrowRepository.getBorrowedItemIn(itemId, borrowFrom, borrowTo);
            if (itemExistInBorrow != 0)
                return new ResponseEntity<>(itemExistInBorrow, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
        return new ResponseEntity<>(0, HttpStatus.OK);
    }

}
