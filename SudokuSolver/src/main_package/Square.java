package main_package;

import java.awt.Color;
import java.awt.Graphics;

public class Square {
	private final Color BORDERCOLOR = Color.BLACK;

	private Color c;
	private int xPos;
	private int yPos;
	private int width = 90;
	private int height = 90;

	public Square(Color c, int xPos, int yPos, int width, int height) {
		this.c = c;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
	}

	public Square(Color c, int width, int height) {
		this.c = c;
		this.width = width;
		this.height = height;
	}

	public void setColor(Color c) {
		this.c = c;
	}

	public Color getColor() {
		return c;
	}

	public void setX(int xPos) {
		this.xPos = xPos;
	}

	public int getX() {
		return xPos;
	}

	public void setY(int yPos) {
		this.yPos = yPos;
	}

	public int getY() {
		return yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void paintSquare(Graphics g, Color c) {
		g.setColor(c);
		g.fillRect(xPos, yPos, width, height);
		g.setColor(BORDERCOLOR);
		g.drawRect(xPos, yPos, width - 1, height - 1);
	}
}
