package org.tokioschool.flightapp.base.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.tokioschool.flightapp.base.service.BaseTransactionalService;

@Controller
@RequiredArgsConstructor
public class BaseTransactionalApiController {

    private final BaseTransactionalService baseTransactionalService;

    @GetMapping("/base/transaction/non-transactional-with-multiple-commit")
    public ResponseEntity<Void> nonTransactionalWithMultipleCommit(){
        baseTransactionalService.nonTransactionalWithMultipleCommit();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/base/transaction/non-transactional-with-one-commit")
    public ResponseEntity<Void> nonTransactionalWithOneCommit(){

        baseTransactionalService.nonTransactionalWithOneCommit();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/base/transaction/transactional-with-one-commit")
    public ResponseEntity<Void> transactionalWithOneCommit(){

        baseTransactionalService.transactionalWithOneCommit();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/base/transaction/non-transactional-with-exception")
    public ResponseEntity<Void> nonTransactionalWithException(){

        baseTransactionalService.nonTransactionalWithException();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/base/transaction/transactional-with-unchecked-rollback")
    public ResponseEntity<Void> transactionalWithUncheckedRollback(){

        baseTransactionalService.transactionalWithUncheckedRollback();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/base/transaction/transactional-with-checked-commit")
    public ResponseEntity<Void> transactionalWithCheckedCommit() throws Exception {

        baseTransactionalService.transactionalWithCheckedCommit();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/base/transaction/transactional-with-checked-rollback")
    public ResponseEntity<Void> transactionalWithCheckedRollback() throws Exception {

        baseTransactionalService.transactionalWithCheckedRollback();

        return ResponseEntity.ok().build();
    }


}
