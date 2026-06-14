package com.ciatshop.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventItem {

    private Integer eventItemSeq;
    private Integer salesEventSeq;
    private String itemCd;
    private String itemNm;
    private Integer itemSellPrice;
    private int inqnty;
    private Integer sellPrice;
    private String useYn;
    private LocalDateTime regDt;
    private LocalDateTime updtDt;
}
