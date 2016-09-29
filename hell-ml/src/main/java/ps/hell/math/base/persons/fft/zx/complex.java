package ps.hell.math.base.persons.fft.zx;

public class complex {
	public double real;
	public double imag;

	public complex() {
		this.real = 0.0D;
		this.imag = 0.0D;
	}

	public complex(double x, double y) {
		this.real = x;
		this.imag = y;
	}

	public complex add(complex x, complex y) {
		complex sol = new complex();
		sol.real = (x.real + y.real);
		sol.imag = (x.imag + y.imag);
		return sol;
	}

	public complex sub(complex x, complex y) {
		complex sol = new complex();
		sol.real = (x.real - y.real);
		sol.imag = (x.imag - y.imag);
		return sol;
	}

	public complex mul(complex x, complex y) {
		complex sol = new complex();
		double a = x.real;
		double b = x.imag;
		double c = y.real;
		double d = y.imag;
		sol.real = (a * c - (b * d));
		sol.imag = (b * c + a * d);
		return sol;
	}

	public complex div(complex x, complex y) {
		complex sol = new complex();
		double a = x.real;
		double b = x.imag;
		double c = y.real;
		double d = y.imag;
		double DIV = c * c + d * d;
		sol.real = ((a * c + b * d) / DIV);
		sol.imag = ((b * c - (a * d)) / DIV);
		return sol;
	}

	public complex conj(complex x) {
		complex sol = new complex();
		sol.real = x.real;
		sol.imag = (-x.imag);
		return sol;
	}

	public double abs(complex x) {
		double sol = 0.0D;
		double tmp = Math.pow(x.real, 2.0D) + Math.pow(x.imag, 2.0D);
		sol = Math.sqrt(tmp);
		return sol;
	}

	public double arg(complex x) {
		double sol = 0.0D;
		sol = Math.atan2(x.imag, x.real);
		return sol;
	}

	public complex random() {
		complex sol = new complex();
		sol.real = Math.random();
		sol.imag = Math.random();
		return sol;
	}

	public complex scale_add(double x, complex y) {
		complex xx = new complex(x, 0.0D);
		return add(xx, y);
	}

	public complex scale_sub(double x, complex y) {
		complex xx = new complex(x, 0.0D);
		return sub(xx, y);
	}

	public complex scale_mul(double x, complex y) {
		complex xx = new complex(x, 0.0D);
		return mul(xx, y);
	}

	public complex scale_div(double x, complex y) {
		complex xx = new complex(x, 0.0D);
		return div(xx, y);
	}
}