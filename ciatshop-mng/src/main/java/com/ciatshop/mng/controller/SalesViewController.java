package com.ciatshop.mng.controller;

import com.ciatshop.mng.client.ItemApiClient;
import com.ciatshop.mng.client.SalesApiClient;
import com.ciatshop.mng.client.SalesEventApiClient;
import com.ciatshop.mng.dto.SalesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/view/sales")
@RequiredArgsConstructor
public class SalesViewController {

    private final SalesApiClient salesApiClient;
    private final ItemApiClient itemApiClient;
    private final SalesEventApiClient salesEventApiClient;

    @GetMapping
    public String list(
            @RequestParam(required = false) String salesYear,
            @RequestParam(required = false) Integer salesEventSeq,
            Model model) {

        model.addAttribute("years", salesApiClient.getYears());
        model.addAttribute("events", salesEventApiClient.getSalesEvents());
        model.addAttribute("selectedYear", salesYear);
        model.addAttribute("selectedEventSeq", salesEventSeq);

        if (salesYear != null || salesEventSeq != null) {
            List<SalesDto> salesList = salesApiClient.searchSales(salesYear, salesEventSeq);
            model.addAttribute("salesList", salesList);
            model.addAttribute("totalQnty",
                    salesList.stream().mapToInt(SalesDto::salesQnty).sum());
            model.addAttribute("totalAmount",
                    salesList.stream().mapToInt(s -> s.salesPrice() != null ? s.salesPrice() : 0).sum());
        }

        return "sales/list";
    }

    @GetMapping("/{salesSeq}")
    public String detail(@PathVariable Integer salesSeq, Model model) {
        model.addAttribute("sales", salesApiClient.getSales(salesSeq));
        return "sales/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("items", itemApiClient.getItems());
        model.addAttribute("events", salesEventApiClient.getSalesEvents());
        return "sales/form";
    }
}
