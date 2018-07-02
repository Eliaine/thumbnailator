package com.surfilter.act.thumbnailator.filters;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FontImage {
	public static void main(String[] args) throws Exception {
		createImage("取证时间：2017-12-25 00：00:00", new Font("黑体", Font.BOLD, 60), Color.ORANGE);
	}

	// 根据str,font的样式以及输出文件目录
	@SuppressWarnings("restriction")
	public static BufferedImage createImage(String str, Font font, Color color) throws Exception {
		// 创建图片
		FontMetrics fms = sun.font.FontDesignMetrics.getMetrics(font);
		int width = fms.stringWidth(str);
		int height = fms.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

		// ---------- 增加下面的代码使得背景透明 -----------------
		Graphics2D gs = image.createGraphics();
		image = gs.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		gs.dispose();
		// ---------- 背景透明代码结束 -----------------

		Graphics g = image.getGraphics();
		g.setClip(0, 0, width, height);

		// g.setColor(Color.white);
		// g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
		g.setColor(color);// 在换成黑色
		g.setFont(font);// 设置画笔字体
		/** 用于获得垂直居中y */
		Rectangle clip = g.getClipBounds();
		FontMetrics fm = g.getFontMetrics(font);
		int ascent = fm.getAscent();
		int descent = fm.getDescent();
		int y = (clip.height - (ascent + descent)) / 2 + ascent;
		g.drawString(str, 0, y);
		g.dispose();
		// ImageIO.write(image, "png", outFile);// 输出png图片
		return image;
	}

	/**
	 * 旋转任意度数的方法
	 * 
	 * @param targetImg
	 * @param degree
	 * @param bgcolor
	 * @throws IOException
	 */
	public static BufferedImage rotateImage(int degree, BufferedImage sourceImage) throws IOException {
		int iw = sourceImage.getWidth();// 原始图象的宽度
		int ih = sourceImage.getHeight();// 原始图象的高度
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;// 将角度转换到0-360度之间
		double ang = Math.toRadians(degree);// 将角度转为弧度

		/**
		 * 确定旋转后的图象的高度和宽度
		 */

		if (degree == 180 || degree == 0 || degree == 360) {
			w = iw;
			h = ih;
		} else if (degree == 90 || degree == 270) {
			w = ih;
			h = iw;
		} else {
			int d = iw + ih;
			w = (int) (d * Math.abs(Math.cos(ang)));
			h = (int) (d * Math.abs(Math.sin(ang)));
		}

		x = (w / 2) - (iw / 2);// 确定原点坐标
		y = (h / 2) - (ih / 2);

		// 有两种旋转使用方式，第一使用AffineTransformOp，第二使用Graphics2D
		/*
		 * AffineTransform at = new AffineTransform(); at.rotate(ang, w / 2, h /
		 * 2);//旋转图象 at.translate(x, y); AffineTransformOp op = new
		 * AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		 * op.filter(sourceImage, rotatedImage); sourceImage = rotatedImage;
		 * 
		 * ImageIO.write(sourceImage, "PNG", file);//这里的格式化请使用PNG格式，否则旋转后会出现红眼效果
		 */

		BufferedImage bufferedImage = new BufferedImage(w, h, sourceImage.getType());
		Graphics2D g = bufferedImage.createGraphics();

		// ---------- 增加下面的代码使得背景透明 -----------------
		bufferedImage = g.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		g.dispose();
		g = bufferedImage.createGraphics();
		// ---------- 背景透明代码结束 -----------------

		g.rotate(Math.toRadians(degree), w / 2, h / 2);
		g.translate(x, y);
		g.drawImage(sourceImage, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}
}
