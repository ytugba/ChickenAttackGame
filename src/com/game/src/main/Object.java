package com.game.src.main;

public class Object {
	private double x;
	private double y;
	private double velX;
	private double velY;
	private int direction;
	private int width;
	private int height;
	
	public Object (double x, double y, int dir, int width, int height) {
		this.x = x;
		this.y = y;
		this.direction = dir;
		this.width = width;
		this.height = height;
	}
	
	public void tick() {
		x+=velX;
		y+=velY;
	}
	
	public double GetX() {
		return x;
	}
	public double GetY() {
		return y;
	}
	public double GetVelX() {
		return velX;
	}
	public double GetVelY() {
		return velY;
	}
	public int GetDirection() {
		return direction;
	}
	public double GetWidth() {
		return width;
	}
	public double GetHeight() {
		return height;
	}
	
	public void SetX(double x) {
		this.x = x;
	}
	public void SetY(double y) {
		this.y = y;
	}
	public void SetVelX(double vel_x) {
		this.velX = vel_x;
	}
	public void SetVelY(double vel_y) {
		this.velY = vel_y;
	}
	public void SetDirection(int dir) {
		this.direction = dir;
	}
}
