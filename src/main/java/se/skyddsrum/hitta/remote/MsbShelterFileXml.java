package se.skyddsrum.hitta.remote;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "feed", namespace = "http://www.w3.org/2005/Atom")
@XmlAccessorType(XmlAccessType.FIELD)
public class MsbShelterFileXml {

	@XmlElement(name = "entry", namespace = "http://www.w3.org/2005/Atom")
	private MsbShelterEntry entry;

	public MsbShelterFileXml() {
	}

	public MsbShelterEntry getEntry() {
		return entry;
	}

	public void setEntry(MsbShelterEntry entry) {
		this.entry = entry;
	}

	public String getUrl() {
		if(entry == null || entry.getLink() == null) {
			return null;
		}
		return entry.getLink().getUrl();
	}
	
	public ZonedDateTime getUpdated() {
		if(entry == null || entry.getLink() == null) {
			return null;
		}
		
		return ZonedDateTime.parse(entry.getUpdated());
	}
}
