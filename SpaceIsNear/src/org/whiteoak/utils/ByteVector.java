/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.utils;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author LPzhelud
 */
public class ByteVector {

    private byte[] buf;
    int capacity, size = 0;
    final static int DEFAULT_CAPACITY = 10;

    public ByteVector(int cap) {
	this(new byte[cap]);
    }

    public ByteVector() {
	this(10);
    }

    public ByteVector(byte[] bufer) {
	this.buf = bufer;
	capacity = bufer.length;
    }

    public void addElement(byte b) {
	if (size < capacity) {
	    buf[size] = b;
	    size++;
	} else {
	    byte bufnew[] = new byte[capacity*2];
	    System.arraycopy(buf, 0, bufnew, 0, capacity);
	    buf = bufnew;
	    capacity *= 2;
	    addElement(b);
	}
    }

    public byte elementAt(int i) {
	return buf[i];
    }

    public byte[] getBytes() {
	byte[] bufn = new byte[size];
	System.arraycopy(buf, 0, bufn, 0, size);
	return bufn;
    }

    public int size() {
	return size;
    }

    @Override
    public String toString() {
	try {
	    return new String(getBytes(), "UTF-8");
	} catch (UnsupportedEncodingException ex) {
	    return new String(getBytes());
	}
    }

    public void addElement(int c) {
	addElement((byte) c);
    }

    public String getClassName() {
	return "ByteVector.class";
    }

    public String getStringWithNumbers() {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < size; i++) {
	    byte b = buf[i];
	    sb.append(b);
	    sb.append(' ');
	}
	sb.deleteCharAt(sb.length() - 1);
	return sb.toString();
    }
}
