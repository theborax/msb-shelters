package se.skyddsrum.hitta.service;

import se.skyddsrum.hitta.storage.ShelterEntity;

public class ShelterResponse {
	
	final ShelterEntity shelterEntity;

	public ShelterResponse(ShelterEntity shelterEntity) {
		this.shelterEntity = shelterEntity;
	}

	public double getLatitude() {
		return shelterEntity.getLatitude();
	}

	public double getLongitude() {
		return shelterEntity.getLongitude();
	}

	public int getNumberOfOccupants() {
		return shelterEntity.getNumberOfOccupants();
	}


}
