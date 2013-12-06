package com.terrynow.eclipse.stringedit;

public abstract interface TextChangeListener {
	public abstract void textChanged(String str, boolean unicode);
}