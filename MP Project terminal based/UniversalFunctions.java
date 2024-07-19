public class UniversalFunctions {
    public static String appendTxtExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex == filename.length() - 4) {

            return filename;
        } else {
            return filename + ".txt";
        }
    }
}
