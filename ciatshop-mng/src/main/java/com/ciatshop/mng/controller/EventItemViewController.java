package com.ciatshop.mng.controller;

import com.ciatshop.mng.client.ItemApiClient;
import com.ciatshop.mng.client.SalesEventApiClient;
import com.ciatshop.mng.client.EventItemApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view/event-items")
@RequiredArgsConstructor
public class EventItemViewController {

    private final SalesEventApiClient salesEventApiClient;
    private final EventItemApiClient eventItemApiClient;
    private final ItemApiClient itemApiClient;

    @GetMapping
    public String list(@RequestParam(required = false) Integer salesEventSeq, Model model) {
        model.addAttribute("events", salesEventApiClient.getSalesEvents());
        model.addAttribute("selectedEventSeq", salesEventSeq);

        if (salesEventSeq != null) {
            model.addAttribute("eventItems", eventItemApiClient.getEventItems(salesEventSeq));
            model.addAttribute("allItems", itemApiClient.getItems());
            model.addAttribute("selectedEvent", salesEventApiClient.getSalesEvent(salesEventSeq));
        }

        return "event-item/list";
    }
}
