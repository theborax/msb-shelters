package se.skyddsrum.hitta.remote;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface MsbDataDownloader {

	ZonedDateTime getShelterListUpdateTime();
	LocalDateTime getShelterListUpdateExecutionTime();
	boolean updateMsbData();
}
