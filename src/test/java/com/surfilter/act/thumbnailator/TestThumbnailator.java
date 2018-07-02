package com.surfilter.act.thumbnailator;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import javax.imageio.ImageIO;

import com.surfilter.act.thumbnailator.Thumbnails;
import com.surfilter.act.thumbnailator.geometry.Positions;

public class TestThumbnailator {

	public static void main(String[] args) {

		try {
			String fileName = "G:\\72a040fd90aaa849523e3183621340ff.png";
			String waterFileName = "G:\\new2_72a040fd90aaa849523e3183621340ff.png";
			String crawlerTime = "http://zwfw.hunan.gov.cn/hnvirtualhall/serviceguide/jsp/serviceapprovegr.jsp?type=xndtgr&main=1&typeid=016&typename=%25u793E%25u4F1A%25u4FDD%25u969C%25uFF08%25u793E%25u4F1A%25u4FDD%25u9669%25u3001%25u793E%25u4F1A%25u6551%25u52A9%25uFF09&areacode=430000000000";
			String markPic = "G:\\nav_item2.png";
			Thumbnails.of(fileName).scale(1f).outputQuality(0.9d)
					.watermark(Positions.CENTER, ImageIO.read(new File(markPic)), 0.9f, -45)
					.watermark(Positions.TOP_LEFT, crawlerTime, 0.6f, 90, new Font("黑体", Font.BOLD, 50), Color.RED)
					.watermark(Positions.CENTER, crawlerTime, 0.3f, 180, new Font("黑体", Font.BOLD, 30), Color.RED)
					.watermark(Positions.BOTTOM_RIGHT, crawlerTime, 1f, 0, new Font("黑体", Font.BOLD, 30), Color.RED)
					.toFile(waterFileName);
		} catch (Exception e) {

		}
	}
}
