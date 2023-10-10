package com.wisedu.minos.casp.portal.common.utils;

//import com.wisedu.amp.display.common.gif.GifDecoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述：图片相关工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ImageUtil
 * @Author: jcx
 * @Date: 2020/7/29
 */
public class ImageUtil {
    /**
     * 重新设定图像的长高宽
     *
     * @param originalImage 图像数据
     * @param width         宽
     * @param height        高
     * @return
     */
    public static BufferedImage imageResize (BufferedImage originalImage, Integer width, Integer height) {
        if ( width <= 0 ) {
            width = 1;
        }
        if ( height <= 0 ) {
            height = 1;
        }
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    /**
     * 按照给点的比例放大图像
     * 当缩减比例小于等于0时不发生任何变化
     *
     * @param originalImage 图像数据
     * @param withdRatio    宽度缩减比例
     * @param heightRatio   高度缩减比例
     * @return 图像数据
     */
    public static BufferedImage imageMagnifyRatio (BufferedImage originalImage, Integer withdRatio, Integer heightRatio) {
        if ( withdRatio <= 0 ) {
            withdRatio = 1;
        }
        if ( heightRatio <= 0 ) {
            heightRatio = 1;
        }
        int width = originalImage.getWidth() * withdRatio;
        int height = originalImage.getHeight() * heightRatio;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    /**
     * 按照给点的比例缩小图像
     * 当缩减比例小于等于0时不发生任何变化
     *
     * @param originalImage 图像数据
     * @param withdRatio    宽度缩减比例
     * @param heightRatio   高度缩减比例
     * @return 图像数据
     */
    public static BufferedImage imageShrinkRatio (BufferedImage originalImage, Integer withdRatio, Integer heightRatio) {
        if ( withdRatio <= 0 ) {
            withdRatio = 1;
        }
        if ( heightRatio <= 0 ) {
            heightRatio = 1;
        }
        int width = originalImage.getWidth() / withdRatio;
        int height = originalImage.getHeight() / heightRatio;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    /**
     * 根据路径获取图片
     *
     * @param path 本地路径 or 网络地址
     * @return 图片
     * @throws IOException
     */
    public static BufferedImage getImageByPath(String path) throws IOException {
        if (StringUtil.isBlank(path)) {
            return null;
        }

        InputStream stream = FileUtil.getStreamByFileName(path);
        return ImageIO.read(stream);
    }

    /**
     * 根据路径获取gif图片
     *
     * @param path
     * @return
     * @throws IOException
     */
//    public static GifDecoder getGifByPath(String path) throws IOException {
//        if (StringUtil.isBlank(path)) {
//            return null;
//        }
//        GifDecoder decoder = new GifDecoder();
//        decoder.read(FileUtil.getStreamByFileName(path));
//        return decoder;
//    }

    /**
     * 将矩形图变成圆图，常用于logo的处理
     *
     * @param image        原始图片
     * @return 返回圆图
     */
    public static BufferedImage makeRoundImg(BufferedImage image,
                                             boolean borderEnable,
                                             Color borderColor) {
        int size, x, y;
        if(image.getWidth() > image.getHeight()) {
            size = image.getHeight();
            y = 0;
            x = (image.getWidth() - image.getHeight()) >> 1;
        } else {
            size = image.getWidth();
            x = 0;
            y = (image.getHeight()- image.getWidth()) >> 1;
        }

        // 设置原图
        BufferedImage ans =  makeRoundImg(image, new Rectangle(x, y, size, size), size);

        // 设置边框
        if(borderEnable) {
            ans = makeRoundBorder(ans, size, borderColor);
        }
        return ans;
    }


    /**
     * 生成圆角图片
     *
     * @param image
     * @param cornerRadius 圆角的弧度（根据实测效果，一般建议为图片宽度的1/4）, 0表示直角
     * @return
     */
    public static BufferedImage makeRoundImg(BufferedImage image,
                                             Rectangle rectangle,
                                             int cornerRadius) {
        int x = (int) rectangle.getX(), y = (int) rectangle.getY();
        int w = (int) rectangle.getWidth();
        int h = (int) rectangle.getHeight();

        BufferedImage output = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
                cornerRadius));


        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, -x, -y, null);

        g2.dispose();
        return output;
    }



    /**
     * 生成边框
     *
     * @param image        原图
     * @param cornerRadius 角度（根据实测效果，一般建议为图片宽度的1/4）, 0表示直角
     * @param color        边框颜色
     * @return
     */
    public static BufferedImage makeRoundBorder(BufferedImage image,
                                                int cornerRadius,
                                                Color color) {
        int size = image.getWidth() / 15;
        int w = image.getWidth() + size;
        int h = image.getHeight() + size;
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color == null ? Color.WHITE : color);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        g2.drawImage(image, size >> 1, size >> 1, null);
        g2.dispose();

        return output;
    }


    /**
     * 生成圆角图片
     *
     * @param image        原始图片
     * @param cornerRadius 圆角的弧度（根据实测效果，一般建议为图片宽度的1/4）, 0表示直角
     * @return 返回圆角图
     */
    public static BufferedImage makeRoundedCorner(BufferedImage image,
                                                  int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
                cornerRadius));


        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }
}
