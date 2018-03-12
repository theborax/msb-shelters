package se.skyddsrum.hitta.remote.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;
import com.github.goober.coordinatetransformation.positions.WGS84Position;

import se.skyddsrum.hitta.remote.MsbDataDownloader;
import se.skyddsrum.hitta.remote.MsbShelterFileXml;
import se.skyddsrum.hitta.storage.ShelterEntity;
import se.skyddsrum.hitta.storage.ShelterRepository;

public class MsbDataDownloaderImpl implements MsbDataDownloader {

	private static final String MSB_SHELTER_URL = "https://gis-services.metria.se/msbfeed/skyddsrum.xml";
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final String ID = "OBJECTID";
	private static final String NAME = "name";
	private static final String ADDRESS = "serviceLBA";
	private static final String CITY = "serviceLBC";
	private static final String NUMBER_OF_OCCUPANTS = "numberOfOc";
	private static final String TYPE_OF_OCCUPANTS = "typeOfOccu";
	private static final String POINT_OF_CONTACT = "pointOfCon";
	private static final String RESOURCE_FILTERS = "resourceFi";

	private final Logger log = LoggerFactory.getLogger(MsbDataDownloaderImpl.class);
	private final RestTemplate restTemplate;
	private final ShelterRepository shelterRepository;
	private ZonedDateTime updateTime;
	private LocalDateTime updateExecutionTime;

	@Value("${skyddsrum.local.file:}")
	private String localFile;

	public MsbDataDownloaderImpl(RestTemplate restTemplate, ShelterRepository shelterRepository) {
		this.restTemplate = restTemplate;
		this.shelterRepository = shelterRepository;
	}

	@PostConstruct
	public void initialLoad() throws Exception {
		if (!updateMsbData()) {
			log.error("Failed to do initial load of data, no data to serve, quitting");
			System.exit(1);
		}
	}

	@Override
	public ZonedDateTime getShelterListUpdateTime() {
		return updateTime;
	}

	@Override
	public LocalDateTime getShelterListUpdateExecutionTime() {
		return updateExecutionTime;
	}

	@Override
	public boolean updateMsbData() {
		if (localFile != null && !localFile.isEmpty()) {
			return processLocalFile();
		}

		try {
			File msbShapeFile = downloadMsbFile();
			if (msbShapeFile == null) {
				return false;
			}
			log.info("Start processing of file downloaded from MSB {}", msbShapeFile);
			processShapeFile(msbShapeFile.toURI().toURL());
			return true;
		} catch (Exception e) {
			log.warn("Failed to process file from MSB site");
			return false;
		}
	}

	private boolean processLocalFile() {
		try {
			File localShapeFile = new File(localFile);
			if (!localShapeFile.exists()) {
				log.error("Requested local file {} does not exist!", localShapeFile);
				return false;
			}
			LocalDateTime fileModifiedLocalTime = LocalDateTime.ofEpochSecond(localShapeFile.lastModified() / 1000, 0,
					ZoneOffset.UTC);
			ZonedDateTime fileModifiedZonedTime = ZonedDateTime.of(fileModifiedLocalTime, ZoneId.systemDefault());
			if (updateTime != null && updateTime.isEqual(fileModifiedZonedTime)) {
				log.info("Local file is up to date, ignoring");
				return true;
			}
			log.info("Loading local file {}", localFile);
			processShapeFile(localShapeFile.toURI().toURL());
			updateTime = fileModifiedZonedTime;
			return true;
		} catch (Exception e) {
			log.warn("Failed to update shelters from local file {}", localFile, e);
			return false;
		}
	}

	private File downloadMsbFile() throws Exception {
		MsbShelterFileXml msbShelterFile = restTemplate.getForObject(MSB_SHELTER_URL, MsbShelterFileXml.class, 200);
		if (msbShelterFile.getUrl() == null) {
			log.error("Failed to get URL for MSB Shelters file");
			return null;
		}

		Path tempDirectory = Files.createTempDirectory("msb");
		log.info("Downloading {} and extracting into {}", msbShelterFile.getUrl(), tempDirectory.toString());

		HttpURLConnection urlConnection = (HttpURLConnection) new URL(msbShelterFile.getUrl()).openConnection();
		ZipInputStream zis = new ZipInputStream(urlConnection.getInputStream());
		ZipEntry zipEntry = zis.getNextEntry();
		byte[] buffer = new byte[1024];
		File shapeFile = null;
		while (zipEntry != null) {
			String fileName = zipEntry.getName();
			log.info("Unzipped file {}", fileName);
			File newFile = new File(tempDirectory.toString() + File.separator + fileName);
			if (fileName.toLowerCase().endsWith(".shp")) {
				shapeFile = newFile;
			}
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();

		updateTime = msbShelterFile.getUpdated();
		return shapeFile;
	}

	private void processShapeFile(URL shapeFile) throws Exception {
		ShapefileDataStore dataStore = new ShapefileDataStore(shapeFile);
		dataStore.setCharset(UTF8);

		String typeName = dataStore.getTypeNames()[0];
		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();

		int totalEntries = 0;
		int processedEntries = 0;
		try (FeatureIterator<SimpleFeature> features = collection.features()) {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				if (processAndStoreFeature(feature)) {
					processedEntries++;
				}
				totalEntries++;
			}
		}
		log.info("Processed {}/{} entries", processedEntries, totalEntries);
		updateExecutionTime = LocalDateTime.now();
	}

	private boolean processAndStoreFeature(SimpleFeature feature) {
		try {
			ShelterEntity shelter = processShelterFromFeature(feature);
			return shelterRepository.save(shelter) != null;
		} catch (Exception e) {
			log.warn("Failed to process shelter from feature {}", feature, e);
		}
		return false;
	}

	private ShelterEntity processShelterFromFeature(SimpleFeature feature) {
		GeometryAttribute geometryAttribute = feature.getDefaultGeometryProperty();
		BoundingBox bounds = geometryAttribute.getBounds();
		SWEREF99Position position = new SWEREF99Position(bounds.getMinY(), bounds.getMinX());
		WGS84Position wgs84 = position.toWGS84();

		double latitude = wgs84.getLatitude();
		double longitude = wgs84.getLongitude();
		Long id = (Long) feature.getAttribute(ID);
		String name = (String) feature.getAttribute(NAME);
		String address = (String) feature.getAttribute(ADDRESS);
		String city = (String) feature.getAttribute(CITY);
		String numberOfOccupantsString = (String) feature.getAttribute(NUMBER_OF_OCCUPANTS);
		String typeOfOccupants = (String) feature.getAttribute(TYPE_OF_OCCUPANTS);
		String pointOfContact = (String) feature.getAttribute(POINT_OF_CONTACT);
		String resources = (String) feature.getAttribute(RESOURCE_FILTERS);

		int numberOfOccupants = 0;
		try {
			numberOfOccupants = Integer.parseInt(numberOfOccupantsString);
		} catch (Exception e) {
			log.warn("Failed to parse number of occupants {}", numberOfOccupantsString, e);
		}

		return new ShelterEntity(id, name, latitude, longitude, address, city, numberOfOccupants, typeOfOccupants,
				pointOfContact, resources);
	}
}
