package se.skyddsrum.hitta.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "shelters")
@Table(name = "shelters", indexes = { @Index(name = "city_index", columnList = "city", unique = false),
		@Index(name = "name_index", columnList = "name", unique = false),
		@Index(name = "latitude_index", columnList = "latitude", unique = false),
		@Index(name = "longitude_index", columnList = "longitude", unique = false),
		@Index(name = "number_of_occupants_index", columnList = "numberOfOccupants", unique = false) })
public class ShelterEntity {

	@Id
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false, length = 64)
	private String name;

	@Column(nullable = false)
	private double latitude;

	@Column(nullable = false)
	private double longitude;

	@Column(nullable = false, length = 64)
	private String address;

	@Column(nullable = false, length = 64)
	private String city;

	@Column(nullable = false, length = 36)
	private int numberOfOccupants;

	@Column(nullable = false, length = 36)
	private String typeOfOccupants;

	@Column(nullable = false, length = 36)
	private String pointOfContact;

	@Column(nullable = false, length = 36)
	private String resources;
	
	protected ShelterEntity() {
		
	}

	public ShelterEntity(Long id, String name, double latitude, double longitude, String address, String city,
			int numberOfOccupants, String typeOfOccupants, String pointOfContact, String resources) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address == null ? null : address.toLowerCase();
		this.city = city == null ? null : city.toLowerCase();
		this.numberOfOccupants = numberOfOccupants;
		this.typeOfOccupants = typeOfOccupants;
		this.pointOfContact = pointOfContact;
		this.resources = resources;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getNumberOfOccupants() {
		return numberOfOccupants;
	}

	public void setNumberOfOccupants(int numberOfOccupants) {
		this.numberOfOccupants = numberOfOccupants;
	}

	public String getTypeOfOccupants() {
		return typeOfOccupants;
	}

	public void setTypeOfOccupants(String typeOfOccupants) {
		this.typeOfOccupants = typeOfOccupants;
	}

	public String getPointOfContact() {
		return pointOfContact;
	}

	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public boolean hasAnyNullValues() {
		return id == null || name == null || address == null || city == null
				|| typeOfOccupants == null || pointOfContact == null || resources == null;
	}

	@Override
	public String toString() {
		return "ShelterEntity [id=" + id + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", address=" + address + ", city=" + city + ", numberOfOccupants=" + numberOfOccupants
				+ ", typeOfOccupants=" + typeOfOccupants + ", pointOfContact=" + pointOfContact + ", resources="
				+ resources + "]";
	}

}
