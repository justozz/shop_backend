package com.justsayozz.shop.Models.API;

import com.justsayozz.shop.Models.Category;

import java.util.List;

/**
 * Created by justs on 12.04.2017.
 */
public class GetCategoriesResponse {
    public List<Category> categories;

    public GetCategoriesResponse(List<Category> categories) {
        this.categories = categories;
    }
}
