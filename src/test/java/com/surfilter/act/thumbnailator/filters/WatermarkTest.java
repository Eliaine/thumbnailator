package com.surfilter.act.thumbnailator.filters;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import org.junit.Test;

import com.surfilter.act.thumbnailator.filters.ImageFilter;
import com.surfilter.act.thumbnailator.filters.Watermark;
import com.surfilter.act.thumbnailator.geometry.Positions;
import com.surfilter.act.thumbnailator.test.BufferedImageComparer;
import com.surfilter.act.thumbnailator.util.BufferedImages;

/**
 * Tests for the {@link Watermark} filter.
 * 
 * @author coobird
 *
 */
public class WatermarkTest {
	/**
	 * Checks that the input image contents are not altered.
	 */
	@Test
	public void inputContentsAreNotAltered() {
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);

		BufferedImage watermarkImg = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);

		ImageFilter filter = new Watermark(Positions.BOTTOM_CENTER, watermarkImg, 0.5f, null);

		// when
		filter.apply(originalImage);

		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
}
