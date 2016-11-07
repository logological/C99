package uk.ac.man.cs.choif.extend;

/**
 * Maths extensions
 * Creation date: (07/24/99 20:22:36)
 * @author: Freddy Choi
 */
public class Mathx {
	/* Coefficients for the gammln function. Values
	obtained from numerical recipes in C */
	private final static double[] cof = new double[]{76.18009172947146,
												 -86.50532032941677,
												 24.01409824083091,
												 -1.231739572450155,
												 0.1208650973866179E-2,
												 -0.5395239384953E-5};
	/* Table of precomputed values for factln */
	private static float[] factln_p = new float[101];
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
 * Compute the natural log of the gamma function. Code converted
 * from that on p.214 of Numerical recipes in C.
 * @return float
 * @param xx float
 */
public static float gammln (final float xx) {
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
 * Compute x to the power of n.
 * Creation date: (07/24/99 20:23:12)
 * @return int
 * @param x int
 * @param n int
 */
public static int power(int x, int n) {
	return (int) Math.pow((double) x, (double) n);
}
/**
 * Round the given value to a specific number of decimal places
 * Creation date: (10/26/99 15:41:46)
 * @return double
 * @param value double
 * @param decimalPlaces int
 */
public static double round(double value, int decimalPlaces) {
	int multiplier = power(10, decimalPlaces);
	return Math.round(value * multiplier) / (double) multiplier;
}
/**
 * Compute x square
 * Creation date: (07/24/99 20:24:52)
 * @return int
 * @param x int
 */
public static int square(int x) {
	return x * x;
}
/**
 * <li>Calculates eigenvalues and eigenvectors.
 * <li>Ported from numerical recipes in C
 */
private static void tqli(double d[], final double e[], final int n, double[][] z)
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
private static void tred2(final double[][] a, final int n, double d[], double e[])
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
}
