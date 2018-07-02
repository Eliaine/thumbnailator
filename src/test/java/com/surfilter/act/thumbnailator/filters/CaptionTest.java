package com.surfilter.act.thumbnailator.filters;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import org.junit.Test;

import com.surfilter.act.thumbnailator.filters.Caption;
import com.surfilter.act.thumbnailator.filters.ImageFilter;
import com.surfilter.act.thumbnailator.geometry.Positions;
import com.surfilter.act.thumbnailator.test.BufferedImageComparer;
import com.surfilter.act.thumbnailator.util.BufferedImages;

/**
 * Tests for the {@link Caption} filter.
 * 
 * @author coobird
 *
 */
public class CaptionTest
{
	/**
	 * Checks that the input image contents are not altered.
	 */
	@Test
	public void inputContentsAreNotAltered()
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = new Caption(
				"hello",
				new Font("Monospaced", Font.PLAIN, 14),
				Color.black,
				Positions.BOTTOM_CENTER, 0
		);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
}
