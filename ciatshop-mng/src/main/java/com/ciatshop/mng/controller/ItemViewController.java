package com.ciatshop.mng.controller;

import com.ciatshop.mng.client.ItemApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/items")
@RequiredArgsConstructor
public class ItemViewController {

    private final ItemApiClient itemApiClient;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", itemApiClient.getItems());
        return "item/list";
    }

    @GetMapping("/{itemCd}")
    public String detail(@PathVariable String itemCd, Model model) {
        model.addAttribute("item", itemApiClient.getItem(itemCd));
        return "item/detail";
    }

    @GetMapping("/new")
    public String createForm() {
        return "item/form";
    }

    @GetMapping("/{itemCd}/edit")
    public String editForm(@PathVariable String itemCd, Model model) {
        model.addAttribute("item", itemApiClient.getItem(itemCd));
        return "item/form";
    }
}
