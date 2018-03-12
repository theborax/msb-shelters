package se.skyddsrum.hitta.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends PagingAndSortingRepository<ShelterEntity, Long> {

	Page<ShelterEntity> findByCity(String city, Pageable pageable);

	@Query("select s from shelters s where latitude < ?1 and latitude > ?3 and longitude > ?2 and longitude < ?4")
	Page<ShelterEntity> findSheltersWithinPoints(double upperLat, double upperLong, double lowerLat, double lowerLong, Pageable pageable);
}
