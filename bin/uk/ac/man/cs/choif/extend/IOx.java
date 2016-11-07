package uk.ac.man.cs.choif.extend;

import java.util.*;
import java.io.*;
/**
 * IO extensions
 * Creation date: (07/30/99 14:17:38)
 * @author: Freddy Choi
 */
public class IOx {
	// Replacement class for ObjectOutputStream
	private static class ObjOutput extends ObjectOutputStream {
		private static ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		private OutputStream out;

		public ObjOutput(File file) throws IOException {
			super(bOut);
			this.out = new FileOutputStream(file);
		}

		public ObjOutput(OutputStream out) throws IOException {
			super(bOut);
			this.out = out;
		}

		public void close() throws IOException {
			super.flush();
			bOut.writeTo(out);
			out.flush();
			out.close();
			super.close();
		}

		protected void finalize() throws Throwable {
			close();
			out = null;
			bOut = null;
			super.finalize();
		}
	}

	// A buffered channel for sending and recieving information within a process.
	public static class BufferedChannel {
		private ObjectOutputStream sender;
		private ObjectInputStream reciever;
		public BufferedChannel () {
			try {
				PipedOutputStream out = new PipedOutputStream();
				PipedInputStream in = new PipedInputStream(out);
				sender = new ObjectOutputStream(out);
				reciever = new ObjectInputStream(in);
			}
			catch (Exception e) {
				Debugx.handle(e);
			}
		}
		public boolean send(Object obj) {
			try {
				sender.writeObject(obj);
				return true;
			} catch (IOException e) {
				Debugx.handle(e);
				return false;
			}
		}
		public Object recieve() {
			try {
				return (Object) reciever.readObject();
			} catch (Exception e) {
				Debugx.handle(e);
				return null;
			}
		}
		public void close() {
			try {
				sender.flush();
				sender.close();
				reciever.close();
			}
			catch (Exception e) {
				Debugx.handle(e);
			}
		}
		protected void finalize() throws Throwable {
			close();
			super.finalize();
		}
	}
	
/**
 * Given a directory name, get the list of files in the directory
 * Creation date: (09/14/99 07:25:12)
 * @return java.io.File[]
 * @param dir java.io.File
 */
public static File[] fileList(File dir) {
	String[] filename = dir.list();
	Vector V = new Vector();
	File f;
	for (int i=filename.length; i-->0;) {
		f = new File(dir.getPath() + File.separator + filename[i]);
		if (f.isFile()) V.addElement(f);
	}
	File[] file = new File[V.size()];
	V.copyInto(file);
	return file;
}
/**
 * Load file into memory (maximum file length 4Gb)
 * Creation date: (09/23/99 15:30:17)
 * @return byte[]
 * @param file java.io.File
 */
public final static byte[] loadBinary(File file) {
	try {
		byte[] data = new byte[(int) file.length()];
		FileInputStream in = new FileInputStream(file);
		in.read(data);
		in.close();
		return data;
	}
	catch (Exception e) {
		Debugx.handle(e);
		return new byte[0];
	}
}
/**
 * Open an object input stream
 * Creation date: (09/15/99 10:00:09)
 * @return java.io.ObjectInput
 * @param file java.io.File
 */
public static ObjectInput openObjectInput(File file) {
	byte[] data = new byte[(int) file.length()];
	try {
		FileInputStream fIn = new FileInputStream(file);
		fIn.read(data);
		fIn.close();
		ObjectInputStream oIn = new ObjectInputStream(new ByteArrayInputStream(data));
		return oIn;
	}
	catch (Throwable e) {
		Debugx.handle(e);
		return null;
	}
}
/**
 * Create a new object output stream for a file
 * Creation date: (09/15/99 10:06:31)
 * @return java.io.ObjectOutput
 * @param file java.io.File
 */
public static ObjectOutput openObjectOutput(File file) {
	try {
		return new ObjOutput(file);
	}
	catch (Throwable e) {
		Debugx.handle(e);
		return null;
	}
}
/**
 * Read an array from the input stream.
 * Creation date: (09/08/99 17:25:41)
 * @return boolean[]
 * @param in java.io.ObjectInput
 */
public static boolean[] readBoolArray(ObjectInput in) throws IOException {
	boolean[] D = new boolean[in.readInt()];
	for (int i=0, ie=D.length; i<ie; i++) D[i] = in.readBoolean();
	return D;
}
/**
 * Read an array from the input stream.
 * Creation date: (09/08/99 17:25:41)
 * @return double[]
 * @param in java.io.ObjectInput
 */
public static double[] readDoubleArray(ObjectInput in) throws IOException {
	double[] D = new double[in.readInt()];
	for (int i=0, ie=D.length; i<ie; i++) D[i] = in.readDouble();
	return D;
}
/**
 * Read an array from the input stream.
 * Creation date: (09/08/99 17:25:41)
 * @return int[]
 * @param in java.io.ObjectInput
 */
public static int[] readIntArray(ObjectInput in) throws IOException {
	int[] D = new int[in.readInt()];
	for (int i=0, ie=D.length; i<ie; i++) D[i] = in.readInt();
	return D;
}
/**
 * Read an array from the input stream.
 * Creation date: (09/08/99 17:25:41)
 * @return long[]
 * @param in java.io.ObjectInput
 */
public static long[] readLongArray(ObjectInput in) throws IOException {
	long[] D = new long[in.readInt()];
	for (int i=0, ie=D.length; i<ie; i++) D[i] = in.readLong();
	return D;
}
/**
 * Read an array from the input stream.
 * Creation date: (09/08/99 17:25:41)
 * @return Object Object array, cast it to whatever it is suppose to be.
 * @param in java.io.ObjectInput
 */
public static Object readObjectArray(ObjectInput in) throws IOException, ClassNotFoundException {
	Object[] D = new Object[in.readInt()];
	for (int i=0, ie=D.length; i<ie; i++) D[i] = in.readObject();
	return D;
}
/**
 * Read an array from the input stream.
 * Creation date: (09/08/99 17:25:41)
 * @return String[]
 * @param in java.io.ObjectInput
 */
public static String[] readStringArray(ObjectInput in) throws IOException {
	String[] D = new String[in.readInt()];
	for (int i=0, ie=D.length; i<ie; i++) D[i] = in.readUTF();
	return D;
}
/**
 * Given an array, write it to object output.
 * Creation date: (09/08/99 17:21:31)
 * @param D double[]
 * @param out java.io.ObjectOutput
 */
public static void write(double[] D, ObjectOutput out) throws IOException {
	out.writeInt(D.length);
	for (int i=0, ie=D.length; i<ie; i++) out.writeDouble(D[i]);
}
/**
 * Given an array, write it to object output.
 * Creation date: (09/08/99 17:21:31)
 * @param D int[]
 * @param out java.io.ObjectOutput
 */
public static void write(int[] D, ObjectOutput out) throws IOException {
	out.writeInt(D.length);
	for (int i=0, ie=D.length; i<ie; i++) out.writeInt(D[i]);
}
/**
 * Given an array, write it to object output.
 * Creation date: (09/08/99 17:21:31)
 * @param D long[]
 * @param out java.io.ObjectOutput
 */
public static void write(long[] D, ObjectOutput out) throws IOException {
	out.writeInt(D.length);
	for (int i=0, ie=D.length; i<ie; i++) out.writeLong(D[i]);
}
/**
 * Given an array, write it to object output.
 * Creation date: (09/08/99 17:21:31)
 * @param D Object[]
 * @param out java.io.ObjectOutput
 */
public static void write(Object[] D, ObjectOutput out) throws IOException {
	out.writeInt(D.length);
	for (int i=0, ie=D.length; i<ie; i++) out.writeObject(D[i]);
}
/**
 * Given an array, write it to object output.
 * Creation date: (09/08/99 17:21:31)
 * @param D String[]
 * @param out java.io.ObjectOutput
 */
public static void write(String[] D, ObjectOutput out) throws IOException {
	out.writeInt(D.length);
	for (int i=0, ie=D.length; i<ie; i++) out.writeUTF(D[i]);
}
/**
 * Given an array, write it to object output.
 * Creation date: (09/08/99 17:21:31)
 * @param D short[]
 * @param out java.io.ObjectOutput
 */
public static void write(short[] D, ObjectOutput out) throws IOException {
	out.writeShort(D.length);
	for (int i=0, ie=D.length; i<ie; i++) out.writeShort(D[i]);
}
/**
 * Given an array, write it to object output.
 * Creation date: (09/08/99 17:21:31)
 * @param D boolean[]
 * @param out java.io.ObjectOutput
 */
public static void write(boolean[] D, ObjectOutput out) throws IOException {
	out.writeInt(D.length);
	for (int i=0, ie=D.length; i<ie; i++) out.writeBoolean(D[i]);
}
}
