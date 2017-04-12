package com.justsayozz.shop;

import com.justsayozz.shop.Logics.DatabaseManager;
import com.justsayozz.shop.Models.API.CreateUserRequest;
import com.justsayozz.shop.Models.API.CreateUserResponse;
import com.justsayozz.shop.Models.DatabaseResult;
import com.justsayozz.shop.Models.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by justs on 12.04.2017.
 */
@RestController
public class RestService {
    @RequestMapping(value = "/createUser", method = POST)
    public CreateUserResponse createUser(@RequestBody CreateUserRequest body) {
        DatabaseResult<User> databaseResult = DatabaseManager.sharedInstance().createUser(body.login, body.password);
        CreateUserResponse response = new CreateUserResponse();

        response.user = databaseResult.data;
        response.message = databaseResult.message;

        return response;
    }
}
