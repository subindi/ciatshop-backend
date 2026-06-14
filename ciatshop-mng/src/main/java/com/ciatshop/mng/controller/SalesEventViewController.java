package com.ciatshop.mng.controller;

import com.ciatshop.mng.client.SalesEventApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/sales-events")
@RequiredArgsConstructor
public class SalesEventViewController {

    private final SalesEventApiClient salesEventApiClient;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("events", salesEventApiClient.getSalesEvents());
        return "sales-event/list";
    }

    @GetMapping("/{eventSeq}")
    public String detail(@PathVariable Integer eventSeq, Model model) {
        model.addAttribute("event", salesEventApiClient.getSalesEvent(eventSeq));
        return "sales-event/detail";
    }

    @GetMapping("/new")
    public String createForm() {
        return "sales-event/form";
    }

    @GetMapping("/{eventSeq}/edit")
    public String editForm(@PathVariable Integer eventSeq, Model model) {
        model.addAttribute("event", salesEventApiClient.getSalesEvent(eventSeq));
        return "sales-event/form";
    }
}
