package se.skyddsrum.hitta.service;

import org.springframework.util.StringUtils;

import se.skyddsrum.hitta.storage.ShelterEntity;

public final class DetailedShelterResponse extends ShelterResponse {

	public DetailedShelterResponse(ShelterEntity shelterEntity) {
		super(shelterEntity);
	}

	public Long getId() {
		return shelterEntity.getId();
	}

	public String getName() {
		return shelterEntity.getName();
	}

	public String getAddress() {
		return shelterEntity.getAddress() == null ? null : StringUtils.capitalize(shelterEntity.getAddress());
	}

	public String getCity() {
		return shelterEntity.getCity() == null ? null : StringUtils.capitalize(shelterEntity.getCity());
	}

	public String getTypeOfOccupants() {
		return shelterEntity.getTypeOfOccupants();
	}

	public String getPointOfContact() {
		return shelterEntity.getPointOfContact();
	}

	public String getResources() {
		return shelterEntity.getResources();
	}

}
