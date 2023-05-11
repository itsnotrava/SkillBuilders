package model;

import java.util.Random;

public class OTPGenerator {

	public static int generateOTP() {
		Random random = new Random();
		return random.nextInt(88888)+11111;
	}
}
