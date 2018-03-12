package se.skyddsrum.hitta.remote;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class MsbShelterLink {

	@XmlAttribute(name = "href")
	private String url;
	
	public MsbShelterLink() {
	}

	public MsbShelterLink(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
