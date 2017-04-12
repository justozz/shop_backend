package com.justsayozz.shop;

import com.justsayozz.shop.Logics.DatabaseManager;
import com.justsayozz.shop.Models.API.*;
import com.justsayozz.shop.Models.Category;
import com.justsayozz.shop.Models.DatabaseResult;
import com.justsayozz.shop.Models.User;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by justs on 12.04.2017.
 */
@RestController
public class RestService {
    @RequestMapping(value = "/images/{filename}", method = GET)
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String filename) {
        try {
            FileInputStream is = new FileInputStream(String.format(Locale.ENGLISH, "data/images/%s.png", filename));
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new InputStreamResource(is));
        } catch (FileNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/createUser", method = POST)
    public CreateUserResponse createUser(@RequestBody CreateUserRequest body) {
        DatabaseResult<User> databaseResult = DatabaseManager.sharedInstance().createUser(body.login, body.password);
        CreateUserResponse response = new CreateUserResponse();

        response.user = databaseResult.data;
        response.message = databaseResult.message;

        return response;
    }

    @RequestMapping(value = "/signIn", method = POST)
    public SignInResponse signIn(@RequestBody SignInRequest body) {
        DatabaseResult<Boolean> databaseResult = DatabaseManager.sharedInstance().signIn(body.login, body.password);
        return new SignInResponse(databaseResult.data, databaseResult.message);
    }

    @RequestMapping(value = "/categories", method = POST)
    public GetCategoriesResponse getCategories() {
        List<Category> categories = DatabaseManager.sharedInstance().getCategories();
        return new GetCategoriesResponse(categories);
    }
}
