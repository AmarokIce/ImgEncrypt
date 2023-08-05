package club.someoneice.imgencrypt;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main {
    static final Encrypt encrypt = Encrypt.INSTANCE;
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("请选择模式: [1.加密图片到二位图像, 2.解密二维码]");
        String input = reader.readLine();

        if (Objects.equals(input, "1")) {
            reader.close();
            input();
        } else if (Objects.equals(input, "2")) {
            System.out.println("请输入解密秘钥");
            String key = reader.readLine();
            reader.close();
            output(key);
        } else {
            System.out.println("请输入1或2！");
            reader.close();
        }
    }

    private static void output(String key) {
        JFileChooser chooser = new JFileChooser();
        int flag = chooser.showOpenDialog(new JFrame());
        if (flag == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file.isFile()) {
                try {
                    String data = encoderImg(ImageIO.read(file));
                    if (!encrypt.decoderImg(data, key, new File(file.getPath().replace(file.getName(), ""))))
                        System.out.println("输出失败，请检查秘钥或图像是否完整！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void input() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            int flag = chooser.showOpenDialog(new JFrame());
            if (flag == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();
                StringBuilder builder = new StringBuilder();
                for (File file : files) {
                    if (!file.isFile()) continue;
                    String name = file.getName();
                    if (!name.contains(".png")) continue;
                    builder.append(encrypt.encoderImg(ImageIO.read(file), name));
                }

                int wight = (int) Math.ceil(Math.sqrt(builder.length()));
                BufferedImage img = new BufferedImage(wight, wight, BufferedImage.TYPE_INT_RGB);
                String data = builder.toString();
                for (int y = 0; y < wight; y ++) {
                    for (int x = 0; x < wight; x ++) {
                        int dataNumber = y * wight + x;
                        if (dataNumber < data.length()) {
                            char c = data.charAt(dataNumber);
                            if (c == '0') img.setRGB(x, y, Color.WHITE.getRGB());
                            else if (c == '1') img.setRGB(x, y, Color.BLACK.getRGB());
                            else if (c == 'S') img.setRGB(x, y, Color.GRAY.getRGB());
                            else if (c == '=') img.setRGB(x, y, Color.YELLOW.getRGB());
                            else if (c == ';') img.setRGB(x, y, Color.GREEN.getRGB());
                        } else img.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }

                ImageIO.write(img, "PNG", new File(files[0].getPath().replace(files[0].getName(), ""), "outputImage.png"));
                System.out.println("成功输出内容！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String encoderImg(BufferedImage img) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < img.getHeight(); y ++) {
            for (int x = 0; x < img.getWidth(); x ++) {
                int color = img.getRGB(x, y);
                if (color == Color.WHITE.getRGB()) sb.append('0');
                else if (color == Color.BLACK.getRGB()) sb.append('1');
                else if (color == Color.GRAY.getRGB()) sb.append('S');
                else if (color == Color.YELLOW.getRGB()) sb.append('=');
                else if (color == Color.GREEN.getRGB()) sb.append(';');
            }
        }

        return sb.toString();
    }
}
