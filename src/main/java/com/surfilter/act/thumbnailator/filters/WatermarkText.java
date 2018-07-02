package com.surfilter.act.thumbnailator.filters;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.surfilter.act.thumbnailator.geometry.Position;

public class WatermarkText implements ImageFilter {
	/**
	 * The position of the watermark.
	 */
	private final Position position;

	/**
	 * The watermark text.
	 */
	private final String logoText;

	/**
	 * The opacity of the watermark.
	 */
	private final float opacity;

	/**
	 * 旋转度
	 */
	private final Integer degree;

	/**
	 * 水印文字字体
	 */
	private Font font = new Font("宋体", Font.BOLD, 36);
	/**
	 * 水印文字颜色
	 */
	private Color color = Color.gray;

	/**
	 * 
	 * @param position
	 *            The position of the watermark.
	 * @param logoText
	 *            文本信息
	 * @param opacity
	 *            The opacity of the watermark.
	 *            <p>
	 *            The value should be between {@code 0.0f} and {@code 1.0f},
	 *            where {@code 0.0f} is completely transparent, and {@code 1.0f}
	 *            is completely opaque.
	 * @param degree
	 *            旋转度
	 * @param font
	 *            字体信息
	 * @param color
	 *            颜色
	 */
	public WatermarkText(Position position, String logoText, float opacity, Integer degree, Font font, Color color) {
		if (position == null) {
			throw new NullPointerException("Position is null.");
		}
		if (logoText == null) {
			throw new NullPointerException("Watermark text is null.");
		}
		if (opacity > 1.0f || opacity < 0.0f) {
			throw new IllegalArgumentException("Opacity is out of range of " + "between 0.0f and 1.0f.");
		}

		this.position = position;
		this.logoText = logoText;
		this.opacity = opacity;
		this.degree = degree;
		this.font = font;
		this.color = color;
	}

	/**
	 * 获取水印文字总长度
	 * 
	 * @param c
	 *            文字
	 * @param g
	 *            图片
	 * @return 文字长度
	 */
	public int getCharLen(char c, Graphics2D g) {
		return g.getFontMetrics(g.getFont()).charWidth(c);
	}

	public BufferedImage apply(BufferedImage srcImg) {
		try {
			int srcImgWidth = srcImg.getWidth();
			int srcImgHeight = srcImg.getHeight();
			int srcImgType = srcImg.getType();

			int borderWidth = 5;
			int waterImgWidth = getWaterImgWidth(srcImgWidth, srcImgHeight, borderWidth);

			// 背景图
			BufferedImage imgWithWatermark = new BufferedImage(srcImgWidth, srcImgHeight, srcImgType);

			Graphics2D g = imgWithWatermark.createGraphics();
			g.drawImage(srcImg, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

			FontMetrics fm = g.getFontMetrics(font);
			int onelineHeight = font.getSize();

			List<String> wordList = new ArrayList<String>();
			int tempCharLen = 0;// 单字符长度
			int tempLineLen = 0;// 单行字符总长度临时计算
			StringBuffer sb = new StringBuffer();
			// 文字叠加,自动换行叠加
			for (char tempChar : logoText.toCharArray()) {
				tempCharLen = fm.charWidth(tempChar);
				if (tempLineLen + tempCharLen < waterImgWidth) {
					sb.append(tempChar);// 追加字符
					tempLineLen += tempCharLen;
				} else {
					tempLineLen = tempCharLen;
					wordList.add(sb.toString());
					sb.delete(0, sb.length());
					sb.append(tempChar);
				}
			}
			if (sb.toString().length() > 0) {
				wordList.add(sb.toString());
				sb.delete(0, sb.length());
			}

			int waterImgHeight = wordList.size() * onelineHeight;

			BufferedImage bufferedImage = new BufferedImage(waterImgWidth, waterImgHeight, srcImgType);
			Graphics2D water = bufferedImage.createGraphics();
			int tempX = 0;
			int tempY = 0;
			for (String word : wordList) {
				// 水印
				BufferedImage watermarkImg = FontImage.createImage(word, font, color);
				water.drawImage(watermarkImg, tempX, tempY, null);
				tempY += onelineHeight;
			}
			water.dispose();

			// 4、设置水印旋转
			if (null != degree) {
				bufferedImage = FontImage.rotateImage(degree, bufferedImage);
			}

			Point p = position.calculate(srcImgWidth, srcImgHeight, bufferedImage.getWidth(), bufferedImage.getHeight(),
					borderWidth, borderWidth, borderWidth, borderWidth);
			g.drawImage(bufferedImage, p.x, p.y, null);
			g.dispose();

			return imgWithWatermark;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Integer initDegree() {
		Integer degreen = null;
		if (null != degree) {
			degreen = degree % 360;
			if (degreen < 0)
				degreen = 360 + degreen;// 将角度转换到0-360度之间
		}
		return degreen;
	}

	private int getWaterImgWidth(int srcImgWidth, int srcImgHeight, int borderWidth) {
		int waterImgWidth = 0;
		int iw = Double.valueOf(srcImgWidth * 0.9).intValue() - borderWidth * 2;// 原始图象的宽度
		int ih = Double.valueOf(srcImgHeight * 0.9).intValue() - borderWidth * 2;// 原始图象的高度
		if (initDegree() != null) {
			int degree = initDegree();
			double ang = Math.toRadians(degree);// 将角度转为弧度
			if (degree == 180 || degree == 0 || degree == 360) {
				waterImgWidth = iw;
			} else if (degree == 90 || degree == 270) {
				waterImgWidth = ih;
			} else {
				iw = Double.valueOf(srcImgWidth * 0.5).intValue() - borderWidth * 2;// 原始图象的宽度
				ih = Double.valueOf(srcImgHeight * 0.5).intValue() - borderWidth * 2;// 原始图象的高度
				int d = iw + ih;
				waterImgWidth = (int) (d * Math.abs(Math.cos(ang)));
			}
		}
		return waterImgWidth;
	}
}
