/*
 * FavouriteLinkTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing;

import org.junit.jupiter.api.Test;

public class FavouriteLinkTest extends TestHarness {

	@Test
	public void test100Positive() {
		super.requestHome();
		super.clickOnMenu("Anonymous", "77932863P: Barba Trejo, Francisco Javier");
		super.checkCurrentUrl("https://shop.realbetisbalompie.es");
		super.requestHome();
		super.clickOnMenu("Anonymous", "29531591M: Bernal Martin, Angela");
		super.checkCurrentUrl("https://es.shein.com");
		super.requestHome();
		super.clickOnMenu("Anonymous", "21150738F: Garcia Berdejo, Jose Maria");
		super.checkCurrentUrl("https://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fchannel%2FUCxT6yeg3kKoi6-RJ5ut3wmw%3Fcbrd%3D1&gl=ES&m=0&pc=yt&cm=2&hl=en-GB&src=1");
		super.requestHome();
		super.clickOnMenu("Anonymous", "77860187N: Iglesias Martín, Mercedes");
		super.checkCurrentUrl("https://www.rafanadalacademy.com");
		super.requestHome();
		super.clickOnMenu("Anonymous", "77819063N: Martín Sánchez, Paola");
		super.checkCurrentUrl("https://www.youtube.com/watch?v=o1dfTZS8E90");

	}

}
