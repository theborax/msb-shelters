package se.skyddsrum.hitta;

import org.junit.Test;
/*
@RunWith(SpringRunner.class)
@SpringBootTest*/
public class HittaApplicationTests {

	/*
	 * Om dina in-koordi­nater är i RT90 eller SWEREF99 ska N-värdet vara ett sjusiffrigt heltal. E-värdet ska vara sexsiffrigt för SWEREF och sjusiffrigt för RT90.
	 */
	private static final int NORTH_WEST = 315;
	private static final int SOUTH_EAST = 135;

	@Test
	public void contextLoads() throws Exception {
		double distance = 2.0;
	/*	int latitude = (int)(56.0305090 * ShelterEntity.DECIMAL_MULTIPLIER);
		int longitude = (int)(14.1518100 * ShelterEntity.DECIMAL_MULTIPLIER);
		/*LatLng start = new LatLng(latitude, longitude);
		double diagonalDistance = Math.sqrt((distance * distance) + (distance * distance));
		LatLng northWestPoint = LatLngTool.travel(start, NORTH_WEST, diagonalDistance, LengthUnit.KILOMETER);
		LatLng southEastPoint = LatLngTool.travel(start, SOUTH_EAST, diagonalDistance, LengthUnit.KILOMETER);

		System.out.println(northWestPoint);
		System.out.println(southEastPoint);
		*/
	/*	
		if(latitude < 560574820 && latitude > 560215090 &&
				longitude > 141174150 && longitude < 141818100) {
			System.out.println("WOHOO");
		}
		
		// latitud = y = ökar rör sig norr
		// long = x = ökar rör sig åt höger
		//LatLng travel = LatLngTool.travel(start, NORTH, 2, LengthUnit.KILOMETER).get;
		
		//                        56.217561, 15.619873
		// Beräknat var för sig - 56.217565, 15.619888
		 
		 */
	}


}
