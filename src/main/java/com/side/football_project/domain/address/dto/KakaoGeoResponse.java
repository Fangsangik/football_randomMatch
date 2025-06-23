package com.side.football_project.domain.address.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoGeoResponse {

    private List<Document> documents;

    @Getter
    public static class Document {
        private String address_name;
        private String x; // longitude
        private String y; // latitude
    }
}
