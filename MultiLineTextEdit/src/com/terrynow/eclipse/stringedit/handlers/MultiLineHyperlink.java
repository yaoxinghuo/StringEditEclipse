package com.terrynow.eclipse.stringedit.handlers;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

import com.terrynow.eclipse.stringedit.Activator;
import com.terrynow.eclipse.stringedit.StringUtils;
import com.terrynow.eclipse.stringedit.TextChangeListener;
import com.terrynow.eclipse.stringedit.gui.StringPopup;

public class MultiLineHyperlink implements IHyperlink, TextChangeListener {

	private IRegion fRegion;

	StringUtils textUtils = new StringUtils();
	IDocument doc;

	public MultiLineHyperlink(IRegion region) {
		fRegion = region;
	}

	@Override
	public IRegion getHyperlinkRegion() {
		return fRegion;
	}

	@Override
	public String getHyperlinkText() {
		return Activator.PLUG_NAME;
	}

	@Override
	public String getTypeLabel() {
		return null;
	}

	@Override
	public void open() {
		openPopup();
	}

	private void openPopup() {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();

		if (!(part instanceof AbstractTextEditor)) {
			return;
		}

		AbstractTextEditor editor = (AbstractTextEditor) part;

		if (!editor.getTitle().endsWith(".java")) {
			return;
		}

		IDocumentProvider dp = editor.getDocumentProvider();
		doc = dp.getDocument(editor.getEditorInput());

		textUtils = new StringUtils();
		textUtils.setDoc(doc.get());
		textUtils.setCursorPosition(StringHandler.getCursorPosition());
		boolean val = textUtils.proccess();
		if (textUtils.getCursorPosition() < 0) {
			return;
		}
		if (!val) {
			return;
		}

		StyledText styledText = (StyledText) editor.getAdapter(Control.class);
		Point p = StringHandler.getPoint(styledText.getCaret());

		Rectangle r = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell().getBounds();

		Point pos = Display.getCurrent().getCursorLocation();
		Point location = new Point(StringHandler.min(p.x, r.x + r.width - 500,
				pos.x + 10), StringHandler.min(p.y, r.y + r.height - 160,
				pos.y + 10));

		final StringPopup textPopUp = new StringPopup(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), textUtils.getText(),
				location);
		textPopUp.open();
		textPopUp.setTextListenerAndFocus(this);
	}

	@Override
	public void textChanged(final String text, final boolean unicode) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// if (!textUtils.getText().equals(text)) {
				textUtils.setText(text);

				String escapedNew = textUtils.getNewExpression(unicode);
				try {
					doc.replace(textUtils.getStart(), textUtils.getLength(),
							escapedNew);
					textUtils.setLength(escapedNew.length());
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			// }
		});
	}

}
