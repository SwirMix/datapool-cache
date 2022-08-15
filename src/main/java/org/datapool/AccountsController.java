package org.datapool;

import org.datapool.core.jwt.TokenObject;
import org.datapool.dto.*;
import org.datapool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountsController {
    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody AuthRq authRq){
        try {
            Token token = userService.auth(authRq);
            return new ResponseEntity(token, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(new ErrorMessage("invalid auth data"), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/remote/auth")
    public ResponseEntity updateRemoteToken(@RequestHeader String token){
        try {
            String remoteToken = userService.updateRemoteToken(token);
            return new ResponseEntity(new Token(remoteToken), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(new ErrorMessage("invalid auth data"), HttpStatus.BAD_REQUEST);
        }
    }
}
