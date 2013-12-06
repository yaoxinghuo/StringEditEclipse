package com.terrynow.eclipse.stringedit.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.terrynow.eclipse.stringedit.Activator;
import com.terrynow.eclipse.stringedit.TextChangeListener;

public class StringPopup extends PopupDialog {
	private Text textArea;
	private TextChangeListener textListener = null;
	private String initText;
	private Button unicodeBtn;
	private Link link;

	private Point defaultLocation;

	public StringPopup(Shell parent, String initText, Point defaultLocation) {
		super(parent, PopupDialog.INFOPOPUP_SHELLSTYLE, true, false, false,
				false, false, Activator.PLUG_NAME, null);
		this.initText = initText;
		this.defaultLocation = defaultLocation;
	}

	@Override
	protected Point getDefaultLocation(Point initialSize) {
		if (defaultLocation == null)
			return super.getDefaultLocation(initialSize);
		return defaultLocation;
	}

	@Override
	protected Color getBackground() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	}

	public void setTextListenerAndFocus(TextChangeListener listener) {
		this.textListener = listener;
		textArea.setFocus();
		textArea.setSelection(textArea.getText().length());
	}

	private void updateLinkStr(boolean showCopied) {
		if (textArea == null || link == null)
			return;
		String text = textArea.getText();
		if (StringUtils.isEmpty(text)) {
			link.setText("(Empty String)");
		} else {
			StringBuffer sb = new StringBuffer("<a>");
			sb.append("Copy ").append(text.length()).append(" Character");
			if (text.length() > 1)
				sb.append("s");
			if (showCopied)
				sb.append(" (Copied)");
			sb.append("</a>");
			link.setText(sb.toString());
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		link = new Link(composite, SWT.NO);
		GridData linkGridData = new GridData();
		linkGridData.widthHint = 360;
		link.setLayoutData(linkGridData);
		link.setText("<a>Copy</a>");
		this.setShellStyle(HOVER_SHELLSTYLE);
		link.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				if (textArea == null || StringUtils.isEmpty(textArea.getText()))
					return;
				StringSelection stringSelection = new StringSelection(textArea
						.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit()
						.getSystemClipboard();
				clipboard.setContents(stringSelection, null);
				updateLinkStr(true);
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});

		textArea = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		textArea.setText(initText);
		textArea.setSelection(initText.length());
		textArea.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				textChanged();
			}
		});
		GridData gridData = new GridData();
		gridData.widthHint = 400;
		gridData.heightHint = 160;
		textArea.setLayoutData(gridData);

		// final IEclipsePreferences prefs = InstanceScope.INSTANCE
		// .getNode(Activator.PLUGIN_ID);
		// boolean unicodeStr = prefs.getBoolean("unicode_str", false);
		unicodeBtn = new Button(composite, SWT.CHECK);
		// unicodeBtn.setSelection(unicodeStr);
		unicodeBtn.setText("Unicode String Format");
		unicodeBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (textArea == null)
					return;
				textChanged();
				// prefs.putBoolean("unicode_str", unicodeBtn.getSelection());
				// try {
				// prefs.sync();
				// } catch (BackingStoreException e) {
				// e.printStackTrace();
				// }
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		updateLinkStr(false);

		return composite;
	}

	private void textChanged() {
		if (textListener != null && textArea != null && unicodeBtn != null)
			textListener.textChanged(textArea.getText(),
					unicodeBtn.getSelection());
		updateLinkStr(false);
	}
}