package uk.ac.man.cs.choif.extend;

import java.io.*;
import java.util.*;
import uk.ac.man.cs.choif.extend.io.*;
import uk.ac.man.cs.choif.extend.sort.*;
/**
 * Numerical recipes in C : 2nd Edition
 * Creation date: (02/04/00 15:35:06)
 * @author: Freddy Choi
 */
public class NRCx {
	/** float Reference */
	public static class floatR {
		public float value = 0;
		public String toString() { return Float.toString(value); }
	}

	/* Table of precomputed values for factln */
	private static float[] factln_p = new float[101];

/**
 * P. 617 Computes the mean and variance of a dataset
 * Note, the first element data[0] does not count 
 * Creation date: (02/07/00 02:08:31)
 * @param data float[]
 * @param ave floatR 
 *				Mean
 * @param var floatR
 *				Variance
 */
public final static void avevar(final float[] data, floatR ave, floatR var) {
	int n = data.length-1;
	int j;
	float s;

	ave.value=(var.value=0);
	for (j=1;j<=n;j++) ave.value += data[j];
	ave.value /= n;
	for (j=1;j<=n;j++) {
		s=data[j]-ave.value;
		var.value += s*s;
	}
	var.value /= (n-1);
}
/**
 * P. 227 : Continued fraction evaluation
 * Creation date: (02/07/00 02:32:51)
 * @return float
 * @param a float
 * @param b float
 * @param x float
 */
public final static float betacf(float a, float b, float x) {
	final int ITMAX = 100;
	final float EPS = 3e-7f;
	float qap,qam,qab,em,tem,d;
	float bz,bm=1,bp,bpp;
	float az=1,am=1,ap,app,aold;
	int m;

	qab=a+b;
	qap=a+1;
	qam=a-1;
	bz=1-qab*x/qap;
	for (m=1;m<=ITMAX;m++) {
		em=(float) m;
		tem=em+em;
		d=em*(b-em)*x/((qam+tem)*(a+tem));
		ap=az+d*am;
		bp=bz+d*bm;
		d = -(a+em)*(qab+em)*x/((qap+tem)*(a+tem));
		app=ap+d*az;
		bpp=bp+d*bz;
		aold=az;
		am=ap/bpp;
		bm=bp/bpp;
		az=app/bpp;
		bz=1;
		if (Math.abs(az-aold) < (EPS*Math.abs(az))) return az;
	}
	Debugx.handle(new Error("betacf : a or b too big, or ITMAX too small"));
	return 0;
}
/**
 * P. 227 : Incomplete beta function
 * Creation date: (02/07/00 02:25:16)
 * @return float
 * @param a float
 * @param b float
 * @param x float
 */
public final static float betai(final float a, final float b, final float x) {
	float bt;
	//float gammln(),betacf();

	if (x < 0.0 || x > 1.0) Debugx.handle(new Error("betai : x < 0 || x > 1"));
	if (x == 0.0 || x == 1.0) bt=0;
	else
		bt=(float) Math.exp(gammln(a+b)-gammln(a)-gammln(b)+a*Math.log(x)+b*Math.log(1.0-x));
	if (x < (a+1.0)/(a+b+2.0))
		return bt*betacf(a,b,x)/a;
	else
		return 1-bt*betacf(b,a,1-x)/b;
}
/**
 * Given a directory of sample files, compute a table
 * of inter-file differences and significance
 * Creation date: (02/07/00 03:06:19)
 * @param dir java.io.File
 * @param out java.io.File
 */
public final static void compareData(final File dir, final File outFile) {
	LineOutput out = new LineOutput(outFile);
	File[] file = IOx.fileList(dir);
	Arrayx.sort(file, new FileNameAsc());

	float[] data1;
	float[] data2;
	floatR m = new floatR();
	floatR v = new floatR();
	floatR d = new floatR();
	floatR prob = new floatR();
	for (int i=0; i<file.length; i++) {
		for (int j=0; j<file.length; j++) {
			out.println("====================");
			out.println("data1 : " + file[i].getName());
			out.println("data2 : " + file[j].getName());
			data1 = readData(file[i]);
			data2 = readData(file[j]);
			avevar(data1, m, v);
			out.println("data1 (m,v)    : " + m + " " + v);
			avevar(data2, m, v);
			out.println("data2 (m,v)    : " + m + " " + v);
			ftest(data1, data2, d, prob);
			out.println("F-test (f,p)   : " + d + " " + prob);
			tutest(data1, data2, d, prob);
			out.println("T-test (t,p)   : " + d + " " + prob);
			kstwo(data1, data2, d, prob);
			out.println("KS-test (ks,p) : " + d + " " + prob);
		}
	}
}
/**
 * Given a matrix M, compute its eigenvalues and eigenvectors
 * @param M double[][] Matrix
 * @param eigenvalues double[] A vector for holding the calculated eigenvalues
 * @param eigenvectors double[][] Matrix A matrix for holding the calculated eigenvectors
 */
public static void eigen(double[][] M, double[] eigenvalue, double[][] eigenvector)
{
	// Matrix access indices
	int i,j;

	// Create array for P (accessed from 1 rather than 0)
	double[][] P = new double[M.length+1][M[0].length+1];
	for (i=1; i<=P.length; i++) {
		for (j=1; j<=P[0].length; j++) {
			P[i][j] = M[i-1][j-1];
		}
	}

	// Store for holding diagonals of tridiagonalised P
	double[] d = new double[P.length];
	// Store for holding off diagonals of tridiagonalised P
	double[] od = new double[P.length];

	// Tridiagonalise P
	tred2(P, P.length, d, od);
	// Calculate the eigenvalues and eigenvectors
	tqli(d, od, P.length, P);

	// d now contains the n eigenvalues of Kx
	// P now contains the n eigenvectors of Kx
	// Normalise d (sum of eigenvalues = 1) and copy d into L
	double sumOfd = 0.0;

	for (i=1; i<=P.length; i++) {
		/** DEVELOPER'S NOTE - IMPROVE
		Some eigenvalues are reported as being negative. This is caused
		by inaccuracy in the algorithm. The negative values seems to be all
		very small (in the order of -1e-16). Improve with the LAPACK algorithm.
		For now it is forced to be positive, should be ok for now. */
		d[i] = Math.abs(d[i]); // Force all eigenvalues to be positive and non-zero
		if (d[i] == 0) d[i] = 0.0000000001; // NUDGING eigenvalues to ensure they are not zero which might be a cause of the NaN problem
		sumOfd += d[i]; // Compute sum
	}
	for (i=1; i<=P.length; i++) eigenvalue[i-1] = d[i] / sumOfd; // Scale and copy
	
	// Copy P into e
	for (i=1; i<=P.length; i++) {
		for (j=1; j<=P[0].length; j++) {
			eigenvector[i-1][j-1] = P[i][j];
		}
	}
}
/**
 * Given a matrix M, compute its eigenvalues and eigenvectors
 * @param M double[][] Matrix
 * @param eigenvalues double[] A vector for holding the calculated eigenvalues
 * @param eigenvectors double[][] Matrix A matrix for holding the calculated eigenvectors
 */
public static void eigen(float[][] M, float[] eigenvalue, float[][] eigenvector)
{
	// Matrix access indices
	int i,j;

	// Create array for P (accessed from 1 rather than 0)
	double[][] P = new double[M.length+1][M[0].length+1];
	for (i=1; i<P.length; i++) {
		for (j=1; j<P[0].length; j++) {
			P[i][j] = M[i-1][j-1];
		}
	}

	// Store for holding diagonals of tridiagonalised P
	double[] d = new double[P.length];
	// Store for holding off diagonals of tridiagonalised P
	double[] od = new double[P.length];

	// Tridiagonalise P
	tred2(P, M.length, d, od);
	// Calculate the eigenvalues and eigenvectors
	tqli(d, od, M.length, P);

	// d now contains the n eigenvalues of Kx
	// P now contains the n eigenvectors of Kx
	// Normalise d (sum of eigenvalues = 1) and copy d into L
	double sumOfd = 0.0;

	for (i=1; i<P.length; i++) {
		/** DEVELOPER'S NOTE - IMPROVE
		Some eigenvalues are reported as being negative. This is caused
		by inaccuracy in the algorithm. The negative values seems to be all
		very small (in the order of -1e-16). Improve with the LAPACK algorithm.
		For now it is forced to be positive, should be ok for now. */
		d[i] = Math.abs(d[i]); // Force all eigenvalues to be positive and non-zero
		if (d[i] == 0) d[i] = 0.0000000001; // NUDGING eigenvalues to ensure they are not zero which might be a cause of the NaN problem
		sumOfd += d[i]; // Compute sum
	}
	for (i=1; i<P.length; i++) eigenvalue[i-1] = (float) (d[i] / sumOfd); // Scale and copy
	
	// Copy P into e
	for (i=1; i<P.length; i++) {
		for (j=1; j<P[0].length; j++) {
			eigenvector[i-1][j-1] = (float) P[i][j];
		}
	}
}
/**
 * Compute the natural log of n!. Short cut code converted from
 * that on p.215 of Numerical recipes in C
 * @return float
 * @param n int
 */
public static float factln(final int n) {
	if (n<=1) return 0;
	// In range of precomputed table
	if (n<=100) return (factln_p[n]!=0 ? factln_p[n] : (factln_p[n]=gammln(n+1)));
	// Out of range of table
	else return gammln(n+1);
}
/**
 * P. 619 : F-test for significantly different variances
 * Creation date: (02/07/00 02:17:53)
 * @param data1 float[]
 * @param data2 float[]
 * @param f floatR
 *			Ratio of one variance to the other, so values
 *			either >> 1 or << 1 will indicate very significant
 *			differences.
 * @param prob floatR
 *			The significance of f, small values indicate that
 *			the two arrays have significantly different variances.
 */
public final static void ftest(final float[] data1, final float[] data2, floatR f, floatR prob) { 
	int n1=data1.length-1;
	int n2=data2.length-1;
	floatR var1=new floatR();
	floatR var2=new floatR();
	floatR ave1=new floatR();
	floatR ave2=new floatR();
	float df1,df2;

	avevar(data1,ave1,var1);
	avevar(data2,ave2,var2);
	if (var1.value > var2.value) {
		f.value=var1.value/var2.value;
		df1=n1-1;
		df2=n2-1;
	} else {
		f.value=var2.value/var1.value;
		df1=n2-1;
		df2=n1-1;
	}
	prob.value = (float) 2.0*betai(0.5f*df2, 0.5f*df1, df2/(df2+df1*(f.value)));
	if (prob.value > 1.0) prob.value=2-prob.value;

}
/**
 * P. 214 : Compute the natural log of the gamma function.
 * Creation date: (02/07/00 00:14:24)
 * @return float
 * @param xx float
 */
public final static float gammln (final float xx) {
	final double[] cof = new double[]{76.18009172947146,
											 -86.50532032941677,
											 24.01409824083091,
											 -1.231739572450155,
											 0.1208650973866179E-2,
											 -0.5395239384953E-5};
	/* Internal arithmetic will be done in double precision. A
	nice feature that can be omitted if five-figure accuracy is
	good enough */
	double x, y, tmp, ser;
	int j;

	y=x=xx;
	tmp=x+5.5;
	tmp -= ((x+0.5)*Math.log(tmp));
	ser=1.000000000190015;
	for (j=0;j<=5;j++) ser += cof[j]/++y;
	return (float) (-tmp+Math.log(2.5066282746310005*ser/x));
}
/**
 * P. 625 :  K-S statistic
 * Note, the first value data1[0] and data2[0] do not count.
 * Creation date: (02/07/00 00:21:31)
 * @param data1 float[]	data[1...n]
 * @param data2 float[] data[1...m]
 * @param d floatR
 *			K-S statistic, the greatest distance between the two
 *			cumulative distributions.
 * @param prob floatR
 *			Significance level for the null hypothesis that the
 *			data sets are drawn from the same distribution. Small
 *			values show that the cumulative distribution function
 *			of data1 is significantly difference from that of data2.
 */
public final static void kstwo(final float[] data1, final float[] data2, floatR d, floatR prob) {
	int j1=1,j2=1;
	float en1,en2,fn1=0,fn2=0,dt,d1,d2;

	en1=data1.length;
	en2=data2.length;
	d.value=0;
	sort(data1);
	sort(data2);
	while (j1 < data1.length && j2 < data2.length) {
		if ((d1=data1[j1]) <= (d2=data2[j2])) {
			fn1=(j1++)/en1;
		}
		if (d2 <= d1) {
			fn2=(j2++)/en2;
		}
		if ((dt=Math.abs(fn2-fn1)) > d.value) d.value=dt;
	}
	prob.value=probks((float) Math.sqrt(en1*en2/(en1+en2))*(d.value));
}
/**
 * P 626 : Kolmogorov-Smirnov probability function
 * Creation date: (02/07/00 00:14:24)
 * @return float
 * @param alam float
 */
public final static float probks(final float alam) {
	int j;
	float a2,fac=2,sum=0,term,termbf=0;
	final float EPS1 = 0.001f;
	final float EPS2 = 1.0e-8f;

	a2 = -2*alam*alam;
	for (j=1;j<=100;j++) {
		term=fac*(float) Math.exp(a2*j*j);
		sum += term;
		if (Math.abs(term) <= EPS1*termbf || Math.abs(term) <= EPS2*sum) return sum;
		fac = -fac;
		termbf=Math.abs(term);
	}
	return 1;
}
/**
 * Read data from a file for use with NRCx routines
 * Creation date: (02/07/00 00:34:45)
 * @return float[] 
 *			data[1...n], note the first element, i.e. data[0]
 *			is not used.
 * @param f java.io.File
 *			Data file where each line is a float.
 */
public final static float[] readData(final File f) {
	try {
		LineInput in = new LineInput(f);
		Vector v = new Vector(1000,1000);
		while (in.hasMoreElements()) v.addElement(new Float((String) in.nextElement()));
		float[] r = new float[v.size() + 1];
		for (int i=r.length; i-->1;) r[i] = ((Float) v.elementAt(i-1)).floatValue();
		return r;
	}
	catch (Exception e) {
		return new float[1];
	}
}
/**
 * P. 337 : Sort an array of floats ra[1...n]
 * note the first value ra[0] does not count
 * Creation date: (02/07/00 00:03:40)
 * @param ra float[]
 */
public final static void sort(float[] ra) {
	int n=ra.length-1,l,j,ir,i;
	float rra;

	l=(n >> 1)+1;
	ir=n;
	for (;;) {
		if (l > 1)
			rra=ra[--l];
		else {
			rra=ra[ir];
			ra[ir]=ra[1];
			if (--ir == 1) {
				ra[1]=rra;
				return;
			}
		}
		i=l;
		j=l << 1;
		while (j <= ir) {
			if (j < ir && ra[j] < ra[j+1]) ++j;
			if (rra < ra[j]) {
				ra[i]=ra[j];
				j += (i=j);
			}
			else j=ir+1;
		}
		ra[i]=rra;
	}
}
/**
 * Square the given value
 * Creation date: (02/07/00 02:54:31)
 * @return float
 * @param v float
 */
public final static float sqr(final float v) {
	return v * v;
}
/**
 * <li>Calculates eigenvalues and eigenvectors.
 * <li>Ported from numerical recipes in C
 */
public final static void tqli(double d[], final double e[], final int n, double[][] z)
{
	int m,l,iter,i,k;
	double s,r,p,g,f,dd,c,b;

	for (i=2;i<=n;i++) e[i-1]=e[i];
	e[n]=0.0;
	for (l=1;l<=n;l++) {
		iter=0;
		do {
			for (m=l;m<=n-1;m++) {
				dd=Math.abs(d[m])+Math.abs(d[m+1]);
				if ((Math.abs(e[m])+dd) == dd) break;
			}
			if (m != l) {
				if (iter++ == 30) Debugx.msg("tqli", "Too many iterations in TQLI");
				g=(d[l+1]-d[l])/(2.0*e[l]);
				r=Math.sqrt(g*g+1);
				g=d[m]-d[l]+e[l]/(g+(g>=0 ? Math.abs(r) : -Math.abs(r)));
				s=c=1.0;
				p=0.0;
				for (i=m-1;i>=l;i--) {
					f=s*e[i];
					b=c*e[i];
					e[i+1]=(r=Math.sqrt(f*f+g*g));
					if (r == 0.0) {
					  d[i+1] -= p;
					  e[m]=0.0;
					  break;
					}
					s=f/r;
					c=g/r;
					g=d[i+1]-p;
					r=(d[i]-g)*s+2.0*c*b;
					d[i+1]=g+(p=s*r);
					g=c*r-b;
					/* Next loop can be omitted if eigenvectors not wanted */
					for (k=1;k<=n;k++) {
						f=z[k][i+1];
						z[k][i+1]=s*z[k][i]+c*f;
						z[k][i]=c*z[k][i]-s*f;
					}
				}
				if (r == 0.0 && i >= l) continue;
				d[l]-=p;
				e[l]=g;
				e[m]=0.0;
			}
		} while (m != l);
	}
}
/**
 * <li>Construct tridiagonal matrix
 * <li>Ported from numerical recipes in C
 */
public final static void tred2(final double[][] a, final int n, double d[], double e[])
{
	int l,k,j,i;
	double scale,hh,h,g,f;

	for (i=n;i>=2;i--) {
		l=i-1;
		h=scale=0.0;
		if (l > 1) {
			for (k=1;k<=l;k++) scale += Math.abs(a[i][k]);
			if (scale == 0.0) 
				e[i]=a[i][l];
			else {
				for (k=1;k<=l;k++) {
					a[i][k] /= scale;
					h += a[i][k]*a[i][k];
				}
				f=a[i][l];
				if (f>=0.0) g = -Math.sqrt(h); else g=Math.sqrt(h);
				e[i]=scale*g;
				h -= f*g;
				a[i][l]=f-g;
				f=0.0;
				for (j=1;j<=l;j++) {
				/* Next statement can be omitted if eigenvectors not wanted */
					a[j][i]=a[i][j]/h;
					g=0.0;
					for (k=1;k<=j;k++)
						g += a[j][k]*a[i][k];
					for (k=j+1;k<=l;k++)
						g += a[k][j]*a[i][k];
					e[j]=g/h;
					f += e[j]*a[i][j];
				}
				hh=f/(h+h);
				for (j=1;j<=l;j++) {
					f=a[i][j];
					e[j]=g=e[j]-hh*f;
					for (k=1;k<=j;k++)
						a[j][k] -= (f*e[k]+g*a[i][k]);
				}
			}
		} else
			e[i]=a[i][l];
		d[i]=h;
	}
	/* Next statement can be omitted if eigenvectors not wanted */
	d[1]=0.0;
	e[1]=0.0;
	/* Contents of this loop can be omitted if eigenvectors not
			wanted except for statement d[i]=a[i][i]; */
	for (i=1;i<=n;i++) {
		l=i-1;
		if (d[i] != 0) {
			for (j=1;j<=l;j++) {
				g=0.0;
				for (k=1;k<=l;k++)
					g += a[i][k]*a[k][j];
				for (k=1;k<=l;k++)
					a[k][j] -= g*a[k][i];
			}
		}
		d[i]=a[i][i];
		a[i][i]=1.0;
		for (j=1;j<=l;j++) a[j][i]=a[i][j]=0.0;
	}
}
/**
 * P. 617 : Student's t-test for significantly different means where the
 * two distributions have significantly different variances. Use the f-test
 * to find out whether the two datasets have significantly different variances.
 * Creation date: (02/07/00 02:46:17)
 * @param data1 float[]
 * @param data2 float[]
 * @param t floatR
 *			Student's t. A small value of significance (0.05 or 0.01)
 *			means that the observed difference is "very significant".
 * @param prob floatR
 *			The significance of t. Small values indicate that the arrays
 *			have significantly difference means
 */
public final static void tutest(final float[] data1, final float[] data2, floatR t, floatR prob) {
	int n1=data1.length-1;
	int n2=data2.length-1;
	floatR var1=new floatR();
	floatR var2=new floatR();
	floatR ave1=new floatR();
	floatR ave2=new floatR();
	float df;

	avevar(data1,ave1,var1);
	avevar(data2,ave2,var2);
	t.value=(ave1.value-ave2.value) / (float) Math.sqrt(var1.value/n1+var2.value/n2);
	df=sqr(var1.value/n1+var2.value/n2)/(sqr(var1.value/n1)/(n1-1)+sqr(var2.value/n2)/(n2-1));
	prob.value=betai(0.5f*df, 0.5f, df/(df+sqr(t.value)));

}
}
