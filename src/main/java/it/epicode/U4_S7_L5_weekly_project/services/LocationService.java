package it.epicode.U4_S7_L5_weekly_project.services;

import it.epicode.U4_S7_L5_weekly_project.entities.Location;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.LocationDTO;
import it.epicode.U4_S7_L5_weekly_project.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // GET all
    @Transactional(readOnly = true)
    public Page<Location> getAllLocations(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    // GET id
    @Transactional(readOnly = true)
    public Location getLocationById(long id) {
        return locationRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // POST
    @Transactional
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    // PUT
    @Transactional
    public Location updateLocation(long id, LocationDTO updatedLocation) {
        Location locationToBeUpdated = this.getLocationById(id);
        locationToBeUpdated.setLocationCity(updatedLocation.locationCity());
        locationToBeUpdated.setLocationName(updatedLocation.locationName());
        return locationRepository.save(locationToBeUpdated);
    }

    // DELETE
    @Transactional
    public void deleteLocation(long id) {
        locationRepository.deleteById(id);
    }
}
