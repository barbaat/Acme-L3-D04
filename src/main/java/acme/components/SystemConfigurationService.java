
package acme.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.datatypes.Money;

@Service
public class SystemConfigurationService {

	@Autowired
	private SystemConfigurationRepository repo;


	public String translateBoolean(final boolean bool, final String lang) {
		String res = "";
		if (lang.equals("en"))
			res = bool ? "Yes" : "No";
		else if (lang.equals("es"))
			res = bool ? "Si" : "No";
		return res;
	}

	public String translateDate(final Date date, final String lang) {
		String res = "";
		if (lang.equals("es")) {
			final SimpleDateFormat spanishFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			res = spanishFormat.format(date);
		} else if (lang.equals("en")) {
			final SimpleDateFormat englishFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			res = englishFormat.format(date);
		}
		return res;
	}

	public String translateMoney(final Money money, final String lang) {
		String res = "";
		if (lang.equals("es")) {
			final double decimal = money.getAmount() % 1;
			final double entero = money.getAmount() - decimal;
			res = entero + "," + decimal + " " + money.getCurrency();
		} else if (lang.equals("en")) {
			final double decimal = money.getAmount() % 1;
			final double entero = money.getAmount() - decimal;
			res = entero + "." + decimal + " " + money.getCurrency();
		}
		return res;
	}

}
