package club.someoneice.imgencrypt;

public class BinaryUtil {
    private static final String SEPARATOR = " ";

    public static String toBinaryString(String str) {
        StringBuilder builder = new StringBuilder();
        for (byte aByte : str.getBytes()) {
            StringBuilder s = new StringBuilder(Integer.toBinaryString(aByte));
            builder.append(s).append(SEPARATOR);
        }

        return builder.toString();
    }

    public static String toString(String binStr) {
        StringBuilder builder = new StringBuilder();
        for (String str : binStr.split(SEPARATOR))
            builder.append(binToChar(str));
        return builder.toString();
    }

    private static int[] binsToIntArray(String binStr) {
        char[] temp = binStr.toCharArray();
        int[] result = new int[temp.length];
        for(int i = 0; i < temp.length; i++)
            result[i] = temp[i] - 48;
        return result;
    }

    private static char binToChar(String binStr){
        int[] temp=binsToIntArray(binStr);
        int sum=0;
        for(int i = 0; i < temp.length; i++)
            sum += temp[temp.length-1-i] << i;
        return (char)sum;
    }
}
