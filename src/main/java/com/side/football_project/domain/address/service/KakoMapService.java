package com.side.football_project.domain.address.service;

import com.side.football_project.domain.address.dto.LatLngDto;

public interface KakoMapService {
    public LatLngDto getCoordinates(String address);
}
