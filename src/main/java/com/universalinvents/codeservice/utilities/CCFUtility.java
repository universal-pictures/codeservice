/*
 * Copyright 2019 Universal City Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.universalinvents.codeservice.utilities;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * An implementation of the CCF spec found here:
 * https://www.uvcentral.com/sites/default/files/files/PublicSpecs/UltraViolet%20Common%20Code%20Format%20v2_10.pdf
 *
 * @author kkirkland
 */
public class CCFUtility {

    private static final char[] LEGAL_CHARS = "23456789BCDFGHJKLMNPQRSTVWXY".toCharArray();

    public static String generateCode(String studioPrefix) {
        String uniqueId = RandomStringUtils.random(14, 0, 0, true, true, LEGAL_CHARS);

        int checkDigit = checkSum(uniqueId, true);

        return (studioPrefix + uniqueId + Integer.toString(checkDigit));
    }

    /**
     * Check to see if the given code string is potentially valid.  This simply confirms
     * that the string meets the requirements for the Luhn mod N algorithm and does not
     * guarantee that the code is actually registered.
     *
     * @param numberString
     * @param onlyUniqueId Does the numberString contain only the unique portion?
     *                     If there's a studio prefix and a check digit contained in the string, specify 'false'.
     *                     If there's no studio prefix or check digit contained in the string, specify 'true'.
     * @return
     */
    public static boolean validate(String numberString, boolean onlyUniqueId) {
        return (checkSum(numberString, onlyUniqueId) == Integer.parseInt(
                numberString.substring(numberString.length() - 1, numberString.length())));
    }

    private static int checkSum(String numberString, boolean onlyUniqueId) {
        int sum = 0, checkDigit = 0;

        if (!onlyUniqueId) {
            numberString = numberString.substring(1, numberString.length() - 1);
        }

        boolean isDouble = true;
        for (int i = numberString.length() - 1; i >= 0; i--) {
            int k = determineCodePoint(numberString.charAt(i), isDouble);
            sum += k;
            isDouble = !isDouble;
        }

        if ((sum % 10) > 0) checkDigit = (10 - (sum % 10));

        return checkDigit;
    }

    private static int determineCodePoint(char c, boolean isDouble) {
        int i = (int) c;
        int sub = i - 48;

        int x = 0;
        if (isDouble) {
            int n = sub / 5;
            x = 2 * sub - 9 * n;
        } else {
            x = sub;
        }

        return x;
    }


    /*
     * main for debugging purposes only.
     */
    public static void main(String args[]) {
        String code = generateCode("U");
        System.out.println(code);
        System.out.println("isValid?: " + validate(code, false));

        String number = "7992739871";
        System.out.println(number + ": " + checkSum(number, true));  // Should be 3

        number = "139MT";
        System.out.println(number + ": " + checkSum(number, true));  // Should be 8
    }
}
