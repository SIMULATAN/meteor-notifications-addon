package com.github.simulatan.meteornotificationsaddon.utils;

import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;

import java.util.regex.Pattern;

/**
 * Color Code Rendering inspired by Hyperium Client
 */
public class DrawUtils {

	public static final Pattern COLOR_CODE_PATTERN = Pattern.compile("[§|&][0123456789abcdefklmnor]");
	private static final int[] COLOR_CODES = {
		0xFF000000,
		0xFF0000AA,
		0xFF00AA00,
		0xFF00AAAA,
		0xFFAA0000,
		0xFFAA00AA,
		0xFFFFAA00,
		0xFFAAAAAA,
		0xFF555555,
		0xFF5555FF,
		0xFF55FF55,
		0xFF55FFFF,
		0xFFFF5555,
		0xFFFF55FF,
		0xFFFFFF55,
		0xFFFFFFFF
	};

	public static double render(String text, double x, double y, java.awt.Color color, boolean shadow) {
		TextRenderer renderer = TextRenderer.get();
		double width = 0;

		int currentColor = color.getRGB();
		char[] characters = text.toCharArray();
		float originalX = (float) x;

		String[] parts = COLOR_CODE_PATTERN.split(text);
		int index = 0;
		for (String s : parts) {
			for (String s2 : s.split("\n")) {
				for (String s3 : s2.split("\r")) {
					renderer.render(s3, x, y, new Color(currentColor));
					x += renderer.getWidth(s3);

					index += s3.length();
					if (index < characters.length && characters[index] == '\r') {
						x = originalX;
						index++;
					}
				}
				if (index < characters.length && characters[index] == '\n') {
					x = originalX;
					y += renderer.getHeight(shadow) * 2;
					index++;
				}
			}
			if (index < characters.length) {
				char colorCode = characters[index];
				if (colorCode == '§' || colorCode == '&') {
					char colorChar = characters[index + 1];
					int codeIndex = ("0123456789abcdef").indexOf(colorChar);
					if (codeIndex < 0) {
						if (colorChar == 'r') {
							currentColor = color.getRGB();
						}
					} else {
						currentColor = COLOR_CODES[codeIndex];
					}
					index += 2;
				}
			}
		}
		return width;
	}

	public static Renderer2DQuad renderer;

	public static void init() {
		renderer = new Renderer2DQuad(false);
	}

	public static void drawRoundedQuad(double x, double y, double width, double height, double radius, Color color) {
		renderer.quadRounded(x, y, width, height, color, radius, true);
	}

	public static void drawRoundedQuad(double x, double y, double width, double height, double radius, Color color, boolean roundTop) {
		renderer.quadRounded(x, y, width, height, color, radius, roundTop);
	}

	public static void drawQuad(double x, double y, double width, double height, Color color) {
		renderer.quad(x, y, width, height, color);
	}

	public static double render(String text, double x, double y, java.awt.Color color) {
		return render(text, x, y, color, false);
	}

	public static double getWidth(String text) {
		return TextRenderer.get().getWidth(text.replaceAll(COLOR_CODE_PATTERN.pattern(), ""));
	}
}
