package com.game.src.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener {
	
	Game game;
	
	MouseInput(Game game){
		this.game = game;
	}

	public void mousePressed(MouseEvent e) {
		game.mousePressed(e);	
	}

	public void mouseClicked(MouseEvent e) {
		//EMPTY FUNCTION
	}
	
	public void mouseReleased(MouseEvent e) {
		//EMPTY FUNCTION
	}

	public void mouseEntered(MouseEvent e) {
		//EMPTY FUNCTION
	}

	public void mouseExited(MouseEvent e) {
		//EMPTY FUNCTION
	}
	
}