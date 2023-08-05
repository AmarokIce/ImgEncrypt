package club.someoneice.imgencrypt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Encrypt {
    public static final Encrypt INSTANCE = new Encrypt();
    final String bKeySB = "===";
    final String endSB = ";;";

    Base64.Decoder decoder = Base64.getDecoder();
    Base64.Encoder encoder = Base64.getEncoder();

    public String encoderImg(BufferedImage img, String key) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(img, "png", output);
            byte[] bytes = output.toByteArray();
            output.close();

            String base64 = encoder.encodeToString(bytes);
            key = key.substring(0, key.indexOf(".png"));

            return BinaryUtil.toBinaryString(encoder.encodeToString(key.getBytes())).replaceAll(" ", "S") + bKeySB + BinaryUtil.toBinaryString(base64).replaceAll(" ", "S") + endSB;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean decoderImg(String org, String key, File filePath) {

        String bKey = BinaryUtil.toBinaryString(encoder.encodeToString(key.getBytes())).replaceAll(" ", "S") + bKeySB;
        if (org.contains(bKey)) {
            org = org.substring(org.indexOf( bKey) + bKey.length());
            org = org.substring(0, org.indexOf(endSB)).replaceAll("S", " ");
            org = BinaryUtil.toString(org);
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(decoder.decode(org)));
                File imgFile = new File(filePath, key + ".png");
                if (!imgFile.isFile()) imgFile.createNewFile();
                ImageIO.write(image, "PNG", imgFile);
                System.out.println(key + ".png 已经存储!");

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
