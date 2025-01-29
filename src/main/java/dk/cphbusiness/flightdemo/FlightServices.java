package dk.cphbusiness.flightdemo;

import dk.cphbusiness.flightdemo.dtos.FlightDTO;
import dk.cphbusiness.flightdemo.dtos.FlightInfoDTO;

import java.io.IOException;
import java.util.List;

import static dk.cphbusiness.flightdemo.FlightReader.getFlightInfoDetails;
import static dk.cphbusiness.flightdemo.FlightReader.getFlightsFromFile;

public class FlightServices
{
    public static void main(String[] args) throws IOException
    {System.out.println(countDeparturesPerAirline("Royal Jordanian"));


    }

    public static long countDeparturesPerAirline(String airlineName) throws IOException {
        List<FlightDTO> flightList = getFlightsFromFile("flights.json");
        List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);

        long result = flightInfoDTOList.stream()
                .filter(flight -> flight.getAirline() != null)
                .filter(flight -> flight.getAirline().equals(airlineName))
                .count();

        return result;
    }


}
