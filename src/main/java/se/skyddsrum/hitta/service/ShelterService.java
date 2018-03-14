package se.skyddsrum.hitta.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import se.skyddsrum.hitta.remote.MsbDataDownloader;
import se.skyddsrum.hitta.remote.impl.MsbDataDownloaderImpl;
import se.skyddsrum.hitta.storage.ShelterEntity;
import se.skyddsrum.hitta.storage.ShelterRepository;

@RestController
@RequestMapping("/v1")
public class ShelterService {

	private static final String SORTING_COLUMN = "numberOfOccupants";
	private static final int PAGE_SIZE = 500;
	private static final int DETAILED_PAGE_SIZE = 150;
	private static final int NORTH_WEST = 315;
	private static final int SOUTH_EAST = 135;

	private final Logger log = LoggerFactory.getLogger(MsbDataDownloaderImpl.class);
	private final MsbDataDownloader msbDataDownloader;
	private final ShelterRepository shelterRepository;

	public ShelterService(MsbDataDownloader msbDataDownloader, ShelterRepository shelterRepository) {
		this.msbDataDownloader = msbDataDownloader;
		this.shelterRepository = shelterRepository;
	}

	@GetMapping("/updated")
	public UpdatedDataResponse getUpdateTimes() {
		return new UpdatedDataResponse(msbDataDownloader.getShelterListUpdateTime(), msbDataDownloader.getShelterListUpdateExecutionTime());
	}

	@GetMapping("/city/{city}")
	public ShelterPageResponse getSheltersByCity(@PathVariable String city,
			@RequestParam(name ="page", defaultValue = "0") int page,
			@RequestParam(name ="detailed", defaultValue = "false") boolean detailed) {
		int itemCount = detailed ? DETAILED_PAGE_SIZE : PAGE_SIZE;
		PageRequest pageRequest = PageRequest.of(page, itemCount, Sort.Direction.DESC, SORTING_COLUMN);
		Page<ShelterEntity> shelterPage = shelterRepository.findByCity(city.toLowerCase(), pageRequest);
		if(detailed) {
			return new ShelterPageResponse(shelterPage.getNumber(), shelterPage.getTotalPages(), shelterPage.getContent()
					.stream().map(DetailedShelterResponse::new).collect(Collectors.toList()));
		}
		return new ShelterPageResponse(shelterPage.getNumber(), shelterPage.getTotalPages(), shelterPage.getContent()
				.stream().map(ShelterResponse::new).collect(Collectors.toList()));
	}

	@GetMapping("")
	public ShelterPageResponse getSheltersByCenterPoint(@RequestParam double latitude,
			@RequestParam double longitude,
			@RequestParam(name ="distance", defaultValue = "2.0") double distance,
			@RequestParam(name ="page", defaultValue = "0") int page,
			@RequestParam(name ="detailed", defaultValue = "false") boolean detailed) {
		LatLng start = new LatLng(latitude, longitude);
		double diagonalDistance = Math.sqrt((distance * distance) + (distance * distance));
		LatLng northWestPoint = LatLngTool.travel(start, NORTH_WEST, diagonalDistance, LengthUnit.KILOMETER);
		LatLng southEastPoint = LatLngTool.travel(start, SOUTH_EAST, diagonalDistance, LengthUnit.KILOMETER);

		int itemCount = detailed ? DETAILED_PAGE_SIZE : PAGE_SIZE;
		PageRequest pageRequest = PageRequest.of(page, itemCount, Sort.Direction.DESC, SORTING_COLUMN);

		Page<ShelterEntity> shelterPage = shelterRepository.findSheltersWithinPoints(northWestPoint.getLatitude(),
				northWestPoint.getLongitude(),
				southEastPoint.getLatitude(),
				southEastPoint.getLongitude(),
				latitude,
				longitude,
				pageRequest);

		if(detailed) {
			return new ShelterPageResponse(shelterPage.getNumber(), shelterPage.getTotalPages(), shelterPage.getContent()
					.stream().map(DetailedShelterResponse::new).collect(Collectors.toList()));
		}
		return new ShelterPageResponse(shelterPage.getNumber(), shelterPage.getTotalPages(), shelterPage.getContent()
				.stream().map(ShelterResponse::new).collect(Collectors.toList()));
	}

	@GetMapping("/box")
	public ShelterPageResponse getSheltersByBoundingbox(@RequestParam double northWestLatitude,
			@RequestParam double northWestLongitude,
			@RequestParam double southEastLatitude,
			@RequestParam double southEastLongitude,
			@RequestParam(name ="page", defaultValue = "0") int page,
			@RequestParam(name ="detailed", defaultValue = "false") boolean detailed) {
		if(northWestLatitude < southEastLatitude) {
			throw new BadRequestException("South (east) latitude needs to be lower than the north (west) one");
		} else if(southEastLongitude < northWestLongitude) {
			throw new BadRequestException("(North) west longitude needs to be lower than the (south) east one");
		}
		
		int itemCount = detailed ? DETAILED_PAGE_SIZE : PAGE_SIZE;
		PageRequest pageRequest = PageRequest.of(page, itemCount, Sort.Direction.DESC, SORTING_COLUMN);
		
		double centerLatitude = ((northWestLatitude - southEastLatitude) / 2) + southEastLatitude;
		double centerLongitude = ((southEastLongitude - northWestLongitude) / 2) +  northWestLongitude;

		Page<ShelterEntity> shelterPage = shelterRepository.findSheltersWithinPoints(northWestLatitude,
				northWestLongitude,
				southEastLatitude,
				southEastLongitude,
				centerLatitude,
				centerLongitude,
				pageRequest);

		if(detailed) {
			return new ShelterPageResponse(shelterPage.getNumber(), shelterPage.getTotalPages(), shelterPage.getContent()
					.stream().map(DetailedShelterResponse::new).collect(Collectors.toList()));
		}
		return new ShelterPageResponse(shelterPage.getNumber(), shelterPage.getTotalPages(), shelterPage.getContent()
				.stream().map(ShelterResponse::new).collect(Collectors.toList()));
	}

}
