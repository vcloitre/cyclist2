package edu.utexas.cycic;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class ConnectorLine extends Line {
	Line right = new Line(){
		{
			setStrokeWidth(2);
		}
	};
	Line left = new Line(){
		{
			setStrokeWidth(2);
		}
	};
	
	Line right1 = new Line(){
		{
			setStrokeWidth(2);
		}
	};
	Line left1 = new Line(){
		{
			setStrokeWidth(2);
		}
	};
	
	Text text = new Text();
	public void updatePosition(){
		double x1 = getEndX();
		double y1 = getEndY();
		
		double x2 = getStartX();
		double y2 = getStartY();
		
		right1.setStartX(x1 + (x2-x1)*0.33);
		right1.setStartY(y1 + (y2-y1)*0.33);
		
		right1.setEndX((x1 + (x2-x1)*0.38)+5.0*(y2-y1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		right1.setEndY((y1 + (y2-y1)*0.38)-5.0*(x2-x1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		
		left1.setStartX(x1 + (x2-x1)*0.33);
		left1.setStartY(y1 + (y2-y1)*0.33);

		left1.setEndX((x1 + (x2-x1)*0.38)-5.0*(y2-y1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		left1.setEndY((y1 + (y2-y1)*0.38)+5.0*(x2-x1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		
		right.setStartX(x1 + (x2-x1)*0.66);
		right.setStartY(y1 + (y2-y1)*0.66);
		
		right.setEndX((x1 + (x2-x1)*0.71)+5.0*(y2-y1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		right.setEndY((y1 + (y2-y1)*0.71)-5.0*(x2-x1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		
		left.setStartX(x1 + (x2-x1)*0.66);
		left.setStartY(y1 + (y2-y1)*0.66);

		left.setEndX((x1 + (x2-x1)*0.71)-5.0*(y2-y1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		left.setEndY((y1 + (y2-y1)*0.71)+5.0*(x2-x1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		
		text.setX(x1 + (x2-x1)*0.55+10.0*(y2-y1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
		text.setY(y1 + (y2-y1)*0.55-10.0*(x2-x1)/Math.sqrt(Math.pow((y2-y1), 2)+Math.pow(x2-x1, 2)));
	}

}
