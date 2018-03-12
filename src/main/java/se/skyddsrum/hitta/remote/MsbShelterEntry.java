package se.skyddsrum.hitta.remote;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MsbShelterEntry {

	@XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
	private MsbShelterLink link;

	@XmlElement(name = "updated", namespace = "http://www.w3.org/2005/Atom")
	private String updated;

	public MsbShelterEntry() {
	}
	
	public MsbShelterLink getLink() {
		return link;
	}

	public void setLink(MsbShelterLink link) {
		this.link = link;
	}
	
	public String getUpdated() {
		return updated;
	}

}
