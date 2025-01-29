package dk.cphbusiness.flightdemo;

import dk.cphbusiness.flightdemo.dtos.FlightDTO;
import dk.cphbusiness.flightdemo.dtos.FlightInfoDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static dk.cphbusiness.flightdemo.FlightReader.getFlightInfoDetails;
import static dk.cphbusiness.flightdemo.FlightReader.getFlightsFromFile;

public class FlightServices
{
    public static void main(String[] args) throws IOException
    {
        System.out.println(countDeparturesPerAirline("Turkish Airlines"));
        System.out.println(planesInAir("Turkish Airlines",
                LocalDateTime.of(2024, 8, 15, 02, 0, 0)));
    }

    public static long countDeparturesPerAirline(String airlineName) throws IOException
    {
        List<FlightDTO> flightList = getFlightsFromFile("flights.json");
        List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);

        long result = flightInfoDTOList.stream()
                .filter(flight -> flight.getAirline() != null)
                .filter(flight -> flight.getAirline().equals(airlineName))
                .count();

        return result;
    }

    public static long planesInAir(String airlineName, LocalDateTime time) throws IOException
    {
        List<FlightDTO> flightList = getFlightsFromFile("flights.json");
        List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);

        long result = flightInfoDTOList.stream()
                .filter(flight -> flight.getAirline() != null)
                .filter(flight -> flight.getAirline().equals(airlineName))
                .filter(flight -> flight.getDeparture().isBefore(time))
                .filter(flight -> flight.getArrival().isAfter(time))
                .count();

        return result;
    }

}
