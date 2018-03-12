package se.skyddsrum.hitta.service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public final class UpdatedDataResponse {

	private final ZonedDateTime shelterListUpdateTime;
	private final LocalDateTime shelterListUpdateExecutionTime;

	public UpdatedDataResponse(ZonedDateTime shelterListUpdateTime, LocalDateTime shelterListUpdateExecutionTime) {
		this.shelterListUpdateTime = shelterListUpdateTime;
		this.shelterListUpdateExecutionTime = shelterListUpdateExecutionTime;
	}

	public ZonedDateTime getShelterListUpdateTime() {
		return shelterListUpdateTime;
	}

	public LocalDateTime getShelterListUpdateExecutionTime() {
		return shelterListUpdateExecutionTime;
	}

}
