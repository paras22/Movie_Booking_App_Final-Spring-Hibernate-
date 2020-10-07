package com.upgrad.mtb.controllers;

import com.upgrad.mtb.entity.Booking;
import com.upgrad.mtb.entity.Theatre;
import com.upgrad.mtb.dto.BookingDTO;
import com.upgrad.mtb.dto.TheatreDTO;
import com.upgrad.mtb.exceptions.*;
import com.upgrad.mtb.security.jwt.JwtTokenProvider;
import com.upgrad.mtb.services.CustomerService;
import com.upgrad.mtb.services.TheatreService;
import com.upgrad.mtb.utils.DTOEntityConverter;
import com.upgrad.mtb.utils.EntityDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TheatreController {
    @Autowired
    TheatreService theatreService;
    @Autowired
    DTOEntityConverter dtoEntityConverter;
    @Autowired
    EntityDTOConverter entityDTOConverter;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    CustomerService customerService;


    @RequestMapping(value= {"/sayHelloTheatre"},method= RequestMethod.GET)
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<String>("Hello World To All From TheatreController", HttpStatus.OK);
    }


    @PostMapping(value="/theatres",consumes= MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
    public ResponseEntity newTheatre(@RequestBody TheatreDTO theatreDTO , @RequestHeader(value = "X-ACCESS-TOKEN") String accessToken) throws MovieDetailsNotFoundException, TheatreDetailsNotFoundException, CustomerDetailsNotFoundException, StatusDetailsNotFoundException, LanguageDetailsNotFoundException, BookingDetailsNotFoundException, APIException, BadCredentialsException {
        String username = jwtTokenProvider.getUsername(accessToken);
        if(username == null)
            throw new APIException("Please add proper authentication");
        if(!customerService.getCustomerDetailsByUsername(username).getUserType().getUserType().equalsIgnoreCase("Admin"))
            throw new BadCredentialsException("This feature is only available to admin");
        Theatre newTheatre = dtoEntityConverter.convertToTheatreEntity(theatreDTO);
        Theatre savedTheatre = theatreService.acceptTheatreDetails(newTheatre);
        TheatreDTO savedTheatreDTO = entityDTOConverter.convertToTheatreDTO(savedTheatre);
        return ResponseEntity.ok(savedTheatreDTO);
    }

    @GetMapping("/theatres/{id}")
    public ResponseEntity getTheatreDetails(@PathVariable("id") int id) throws TheatreDetailsNotFoundException {
        System.out.println(theatreService.getTheatreDetails(id));
        Theatre theatre =  theatreService.getTheatreDetails(id);
        TheatreDTO theatreDTO = entityDTOConverter.convertToTheatreDTO(theatre);
        return ResponseEntity.ok(theatreDTO);
    }

    @PutMapping("/theatres/{id}")
    public ResponseEntity updateTheatreDetails(@PathVariable(name = "id") int id , @RequestBody TheatreDTO theatreDTO) throws TheatreDetailsNotFoundException, MovieDetailsNotFoundException, CustomerDetailsNotFoundException, StatusDetailsNotFoundException, LanguageDetailsNotFoundException, BookingDetailsNotFoundException {
        Theatre newTheatre = dtoEntityConverter.convertToTheatreEntity(theatreDTO);
        Theatre updatedTheatre = theatreService.updateTheatreDetails(id, newTheatre);
        TheatreDTO updatedTheatreDTO = entityDTOConverter.convertToTheatreDTO(updatedTheatre);
        return ResponseEntity.ok(updatedTheatreDTO);
    }

    @GetMapping(value="/theatres",produces=MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
    public ResponseEntity findAllTheatre() {
        List<Theatre> theatres = theatreService.getAllTheatreDetails();
        List<TheatreDTO> theatreDTOList = new ArrayList<>();
        for(Theatre theatre : theatres){
            theatreDTOList.add(entityDTOConverter.convertToTheatreDTO(theatre));
        }
        System.out.println("Number of theatres :" + theatres.size());
        return ResponseEntity.ok(theatreDTOList);
    }

    @GetMapping("/theatres/{id}/bookings")
    public ResponseEntity getAllBookingForTheatre(@PathVariable("id") int id) throws TheatreDetailsNotFoundException {
        Theatre theatre = theatreService.getTheatreDetails(id);
        List<Booking> bookings = theatre.getBookings();
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for(Booking booking : bookings){
            bookingDTOList.add(entityDTOConverter.convertToBookingDTO(booking));
        }
        System.out.println("Numer of bookings for theatre " + theatre.getTheatreName() + " : " + bookings.size());
        return ResponseEntity.ok(bookings);
    }
}
