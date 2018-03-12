package se.skyddsrum.hitta.service;

import java.util.List;

public final class ShelterPageResponse {

	private final int pageNumber;
	private final int totalPages;
	private final List<ShelterResponse> shelters;

	public ShelterPageResponse(int pageNumber, int totalPages, List<ShelterResponse> shelters) {
		this.pageNumber = pageNumber;
		this.totalPages = totalPages;
		this.shelters = shelters;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public List<ShelterResponse> getShelters() {
		return shelters;
	}

}
