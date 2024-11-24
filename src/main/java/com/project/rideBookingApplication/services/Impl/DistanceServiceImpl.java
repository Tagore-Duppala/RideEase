package com.project.rideBookingApplication.services.Impl;

import com.project.rideBookingApplication.services.DistanceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistanceServiceImpl implements DistanceService {

    private static final String OSRM_API = "http://router.project-osrm.org/route/v1/driving/";


    @Override
    public Double calculateDistance(Point src, Point dest) {
        String URI = src.getX()+","+src.getY()+";"+dest.getX()+","+dest.getY();

        try {
            OSRMResponseDto osrmResponseDto = RestClient.builder()
                    .baseUrl(OSRM_API)
                    .build()
                    .get()
                    .uri(URI)
                    .retrieve()
                    .body(OSRMResponseDto.class);


            return osrmResponseDto.getRoutes().get(0).getDistance()/1000;
        }
        catch(Exception e){
            throw new RuntimeException("Error getting data from OSRM "+ e.getMessage());
        }
    }
}


@Data
class OSRMResponseDto{
    private List<OSRMRoute> routes;
}

@Data
class OSRMRoute{
    private Double distance;
}
