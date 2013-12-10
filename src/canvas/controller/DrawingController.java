package canvas.controller;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import stroke.StrokeProperties;
import canvas.Drawable;
import client.ClientController;

public abstract class DrawingController implements MouseListener, MouseMotionListener {
	protected ClientController clientController;
	protected StrokeProperties strokeProperties;
	protected Drawable canvas;
	
	public DrawingController(ClientController clientController,
			StrokeProperties strokeProperties, Drawable canvas) {
		this.clientController = clientController;
		this.strokeProperties = strokeProperties;
		this.canvas = canvas;
	}
}
