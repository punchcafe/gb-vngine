package dev.punchcafe.gbvng.gen.render.sprites;

public class ConverterFunctions {

    public static String hexStringFromInteger(int rgbValue){
        if(rgbValue == 0){
            return "000000";
        }
        final var stringRepresentation = Integer.toHexString(rgbValue);
        final var stringLength = stringRepresentation.length();
        if(stringLength > 6){
            return stringRepresentation.substring(stringLength - 6, stringLength);
        } else {
            return "0".repeat(6 - stringLength) + stringRepresentation;
        }
    }
}
