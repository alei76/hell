package ps.hell.ml.forest.classification.decisionTree.quad;

import java.io.Serializable;

public class Cell implements Serializable {
	private double x;
	private double y;
	private double hw;
	private double hh;

	public Cell(double x, double y, double hw, double hh) {
		this.x = x;
		this.y = y;
		this.hw = hw;
		this.hh = hh;
	}

	public boolean containsPoint(XyPoint point) {
		double first = point.x;
		double second = point.y;
		return ((this.x - this.hw <= first) && (this.x + this.hw >= first)
				&& (this.y - this.hh <= second) && (this.y + this.hh >= second));
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Cell))
			return false;

		Cell cell = (Cell) o;

		if (Double.compare(cell.hh, this.hh) != 0)
			return false;
		if (Double.compare(cell.hw, this.hw) != 0)
			return false;
		if (Double.compare(cell.x, this.x) != 0)
			return false;
		return (Double.compare(cell.y, this.y) == 0);
	}

	public int hashCode() {
		long temp = Double.doubleToLongBits(this.x);
		int result = (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(this.y);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(this.hw);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(this.hh);
		result = 31 * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getHw() {
		return this.hw;
	}

	public void setHw(double hw) {
		this.hw = hw;
	}

	public double getHh() {
		return this.hh;
	}

	public void setHh(double hh) {
		this.hh = hh;
	}
}