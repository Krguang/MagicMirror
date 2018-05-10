package com.yy.k.magicmirror;

public class Serial {
	
	public native int 	Open(int Port,int Rate);
	public native int 	Close();
	public native int[]	Read();
	public native int	Write(int[] buffer,int len);

}
