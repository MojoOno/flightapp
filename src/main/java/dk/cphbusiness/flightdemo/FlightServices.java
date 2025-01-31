package dk.cphbusiness.flightdemo;

import dk.cphbusiness.flightdemo.dtos.FlightDTO;
import dk.cphbusiness.flightdemo.dtos.FlightInfoDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dk.cphbusiness.flightdemo.FlightReader.getFlightInfoDetails;
import static dk.cphbusiness.flightdemo.FlightReader.getFlightsFromFile;

public class FlightServices
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("-------------");
        System.out.println(countDeparturesPerAirline("Turkish Airlines"));
        System.out.println("-------------");
        System.out.println(planesInAir("Turkish Airlines",
                LocalDateTime.of(2024, 8, 15, 02, 0, 0)));
        System.out.println("-------------");
        System.out.println(averageFlightTime("Turkish Airlines") + " hours");
        System.out.println("-------------");
        System.out.println(flightsBetweenAirports("Fukuoka", "Haneda Airport"));
        System.out.println("-------------");
        System.out.println(departureBeforeTime(LocalDateTime.of(2024, 8, 15, 1, 0, 0)));
        System.out.println("-------------");
        System.out.println(averageFlightTimeForEachAirline());

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

    //I want to calculate the average flight time for a specific airline
    public static double averageFlightTime(String airlineName) throws IOException
    {
        List<FlightDTO> flightList = getFlightsFromFile("flights.json");
        List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);

        double result = flightInfoDTOList.stream()
                .filter( flight -> flight.getAirline() != null)
                .filter(flight -> flight.getAirline().equals(airlineName))
                .mapToLong(flight -> flight.getDuration().toHours())
                .average()
                .orElse(0);
        return result;
    }

    // i want to make a list of flights that are operated between two specific airports
    public static List<FlightInfoDTO> flightsBetweenAirports(String origin, String destination) throws IOException
    {
        List<FlightDTO> flightList = getFlightsFromFile("flights.json");
        List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);

        List<FlightInfoDTO> result = flightInfoDTOList.stream()
                .filter(flight -> flight.getOrigin() != null)
                .filter(flight -> flight.getOrigin().equals(origin))
                .filter(flight -> flight.getDestination().equals(destination))
                .toList();

        return result;
    }


    public static List<FlightInfoDTO> departureBeforeTime(LocalDateTime departure) throws IOException
    {
        List<FlightDTO> flightList = getFlightsFromFile("flights.json");
        List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);

        List<FlightInfoDTO> result = flightInfoDTOList.stream()
                .filter(flight -> flight.getOrigin() != null)
                .filter(flight -> flight.getDeparture().isBefore(departure))
                .toList();

                return result;
    }

    //Add a new feature (calculate the average flight time for each airline)
    public static Map<String, Double> averageFlightTimeForEachAirline() throws IOException {
        List<FlightDTO> flightList = getFlightsFromFile("flights.json");
        List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);

        return flightInfoDTOList.stream()
                .filter(flight -> flight.getAirline() != null)
                .collect(Collectors.groupingBy(
                        FlightInfoDTO::getAirline,
                        Collectors.averagingDouble(flight -> flight.getDuration().toHours())
                ));
    }

}
