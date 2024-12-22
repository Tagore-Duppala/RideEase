package com.project.rideEase.services.Impl;

import com.project.rideEase.services.DistanceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistanceServiceImpl implements DistanceService {

    private static final String OSRM_API = "http://router.project-osrm.org/route/v1/driving/";


    @Override
    public Double[] calculateDistance(Point src, Point dest) {
        Double[] response = new Double[2];
        String URI = src.getX()+","+src.getY()+";"+dest.getX()+","+dest.getY();

        try {
            OSRMResponseDto osrmResponseDto = RestClient.builder()
                    .baseUrl(OSRM_API)
                    .build()
                    .get()
                    .uri(URI)
                    .retrieve()
                    .body(OSRMResponseDto.class);

            response[0]=osrmResponseDto.getRoutes().get(0).getDistance()/1000;
            response[1]=osrmResponseDto.getRoutes().get(0).getDuration()/60;

            log.info("Distance: "+response[0]+" Km"+ "\nDuration: "+response[1]+" Min");
            return response;
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
    private Double duration;
}


