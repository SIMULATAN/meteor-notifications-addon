package com.github.simulatan.meteornotificationsaddon.utils;

import com.github.simulatan.meteornotificationsaddon.mixins.MeshMixin;
import meteordevelopment.meteorclient.renderer.MeshBuilder;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.utils.render.color.Color;

import static org.lwjgl.system.MemoryUtil.memPutInt;

public class Renderer2DQuad extends Renderer2D {

	// Rounded quad
	private final double circleNone = 0;
	private final double circleQuarter = Math.PI / 2;
	private final double circleHalf = circleQuarter * 2;
	private final double circleThreeQuarter = circleQuarter * 3;

	public Renderer2DQuad(boolean texture) {
		super(texture);
	}

	public void quadRoundedOutline(double x, double y, double width, double height, Color color, double r, double s) {
		r = getR(r, width, height);
		if (r <= 0) {
			quad(x, y, width, s, color);
			quad(x, y + height - s, width, s, color);
			quad(x, y + s, s, height - s * 2, color);
			quad(x + width - s, y + s, s, height - s * 2, color);
		} else {
			//top
			circlePartOutline(x + r, y + r, r, circleThreeQuarter, circleQuarter, color, s);
			quad(x + r, y, width - r * 2, s, color);
			circlePartOutline(x + width - r, y + r, r, circleNone, circleQuarter, color, s);
			//middle
			quad(x, y + r, s, height - r * 2, color);
			quad(x + width - s, y + r, s, height - r * 2, color);
			//bottom
			circlePartOutline(x + width - r, y + height - r, r, circleQuarter, circleQuarter, color, s);
			quad(x + r, y + height - s, width - r * 2, s, color);
			circlePartOutline(x + r, y + height - r, r, circleHalf, circleQuarter, color, s);
		}
	}

	public void quadRounded(double x, double y, double width, double height, Color color, double r, boolean roundTop) {
		r = getR(r, width, height);
		if (r <= 0)
			quad(x, y, width, height, color);
		else {
			if (roundTop) {
				//top
				circlePart(x + r, y + r, r, circleThreeQuarter, circleQuarter, color);
				quad(x + r, y, width - 2 * r, r, color);
				circlePart(x + width - r, y + r, r, circleNone, circleQuarter, color);
				//middle
				quad(x, y + r, width, height - 2 * r, color);
			} else {
				//middle
				quad(x, y, width, height - r, color);
			}
			//bottom
			circlePart(x + width - r, y + height - r, r, circleQuarter, circleQuarter, color);
			quad(x + r, y + height - r, width - 2 * r, r, color);
			circlePart(x + r, y + height - r, r, circleHalf, circleQuarter, color);
		}
	}

	public void quadRoundedSide(double x, double y, double width, double height, Color color, double r, boolean right) {
		r = getR(r, width, height);
		if (r <= 0)
			quad(x, y, width, height, color);
		else {
			if (right) {
				circlePart(x + width - r, y + r, r, circleNone, circleQuarter, color);
				circlePart(x + width - r, y + height - r, r, circleQuarter, circleQuarter, color);
				quad(x, y, width - r, height, color);
				quad(x + width - r, y + r, r, height - r * 2, color);
			} else {
				circlePart(x + r, y + r, r, circleThreeQuarter, circleQuarter, color);
				circlePart(x + r, y + height - r, r, circleHalf, circleQuarter, color);
				quad(x + r, y, width - r, height, color);
				quad(x, y + r, r, height - r * 2, color);
			}
		}
	}

	private double getR(double r, double w, double h) {
		if (r * 2 > h) {
			r = h / 2;
		}
		if (r * 2 > w) {
			r = w / 2;
		}
		return r;
	}

	private int getCirDepth(double r, double angle) {
		return Math.max(1, (int) (angle * r / circleQuarter));
	}

	public void circlePart(double x, double y, double r, double startAngle, double angle, Color color) {
		int cirDepth = getCirDepth(r, angle);
		double cirPart = angle / cirDepth;
		int center = triangles.vec2(x, y).color(color).next();
		int prev = vecOnCircle(x, y, r, startAngle, color);
		for (int i = 1; i < cirDepth + 1; i++) {
			int next = vecOnCircle(x, y, r, startAngle + cirPart * i, color);
			triangle(prev, center, next, triangles);
			prev = next;
		}
	}

	public static void triangle(int i1, int i2, int i3, MeshBuilder entity) {
		MeshMixin accessor = (MeshMixin) entity;
		long p = accessor.getIndicesPointer() + entity.getIndicesCount() * 4L;

		memPutInt(p, i1);
		memPutInt(p + 4, i2);
		memPutInt(p + 8, i3);

		accessor.setIndicesCount(entity.getIndicesCount() + 3);
		accessor.growIfNeeded();
	}

	public void circlePartOutline(double x, double y, double r, double startAngle, double angle, Color color, double outlineWidth) {
		if (outlineWidth >= r) {
			circlePart(x, y, r, startAngle, angle, color);
			return;
		}
		int cirDepth = getCirDepth(r, angle);
		double cirPart = angle / cirDepth;
		int innerPrev = vecOnCircle(x, y, r - outlineWidth, startAngle, color);
		int outerPrev = vecOnCircle(x, y, r, startAngle, color);
		for (int i = 1; i < cirDepth + 1; i++) {
			int inner = vecOnCircle(x, y, r - outlineWidth, startAngle + cirPart * i, color);
			int outer = vecOnCircle(x, y, r, startAngle + cirPart * i, color);
			triangles.quad(inner, innerPrev, outerPrev, outer);
			innerPrev = inner;
			outerPrev = outer;
		}
	}

	private int vecOnCircle(double x, double y, double r, double angle, Color color) {
		return triangles.vec2(x + Math.sin(angle) * r, y - Math.cos(angle) * r).color(color).next();
	}
}
