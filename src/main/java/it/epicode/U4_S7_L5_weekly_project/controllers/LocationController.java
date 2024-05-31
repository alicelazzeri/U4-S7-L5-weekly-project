package it.epicode.U4_S7_L5_weekly_project.controllers;

import it.epicode.U4_S7_L5_weekly_project.entities.Location;
import it.epicode.U4_S7_L5_weekly_project.exceptions.BadRequestException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NoContentException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.LocationDTO;
import it.epicode.U4_S7_L5_weekly_project.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // GET all

    @GetMapping
    public ResponseEntity<Page<Location>> getAllLocations(Pageable pageable) {
        Page<Location> locations = locationService.getAllLocations(pageable);
        if (locations.isEmpty()) {
            throw new NoContentException("No locations were found.");
        } else {
            ResponseEntity<Page<Location>> responseEntity = new ResponseEntity<>(locations, HttpStatus.OK);
            return responseEntity;
        }
    }

    // GET id

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById (@PathVariable long id) {
        Location location = locationService.getLocationById(id);
        if (location == null) {
            throw new NotFoundException(id);
        } else {
            ResponseEntity<Location> responseEntity = new ResponseEntity<>(location, HttpStatus.OK);
            return responseEntity;
        }
    }

    // POST

    @PostMapping
    public ResponseEntity<Location> saveLocation(@RequestBody @Validated LocationDTO locationPayload,BindingResult validation){
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Location locationToBeSaved = Location.builder()
                    .withLocationName(locationPayload.locationName())
                    .withLocationCity(locationPayload.locationCity())
                    .withEvents(locationPayload.events())
                    .build();
            Location savedLocation = locationService.saveLocation(locationToBeSaved);
            ResponseEntity<Location> responseEntity = new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
            return responseEntity;
        }
    }

    // PUT

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable long id, @RequestBody @Validated LocationDTO updatedLocationPayload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Location locationToBeUpdated = locationService.getLocationById(id);
            if (locationToBeUpdated == null) {
                throw new NotFoundException(id);
            }
            locationToBeUpdated.setLocationName(updatedLocationPayload.locationName());
            locationToBeUpdated.setLocationCity(updatedLocationPayload.locationCity());
            locationToBeUpdated.setEvents(updatedLocationPayload.events());

            Location updatedLocation = locationService.updateLocation(id, updatedLocationPayload);
            ResponseEntity<Location> responseEntity = new ResponseEntity<>(updatedLocation, HttpStatus.OK);
            return responseEntity;
        }
    }

    // DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable long id) {
        locationService.deleteLocation(id);
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return responseEntity;
    }
}
