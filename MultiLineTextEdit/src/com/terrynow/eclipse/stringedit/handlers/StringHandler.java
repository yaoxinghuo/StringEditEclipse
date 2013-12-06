package com.terrynow.eclipse.stringedit.handlers;

import javax.swing.UIManager;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

import com.terrynow.eclipse.stringedit.StringUtils;
import com.terrynow.eclipse.stringedit.TextChangeListener;
import com.terrynow.eclipse.stringedit.gui.StringPopup;

public class StringHandler extends AbstractHandler implements
		TextChangeListener {
	StringUtils textUtils = new StringUtils();
	IDocument doc;

	public StringHandler() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception localException) {
		}
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();

		if (!(part instanceof AbstractTextEditor)) {
			return null;
		}

		AbstractTextEditor editor = (AbstractTextEditor) part;

		if (!editor.getTitle().endsWith(".java")) {
			return null;
		}

		IDocumentProvider dp = editor.getDocumentProvider();
		doc = dp.getDocument(editor.getEditorInput());

		textUtils = new StringUtils();
		textUtils.setDoc(doc.get());
		textUtils.setCursorPosition(getCursorPosition());
		boolean val = textUtils.proccess();
		if (textUtils.getCursorPosition() < 0) {
			return null;
		}
		if (!val) {
			return null;
		}

		StyledText styledText = (StyledText) editor.getAdapter(Control.class);
		Point p = getPoint(styledText.getCaret());

		Rectangle r = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell().getBounds();

		Point pos = Display.getCurrent().getCursorLocation();
		Point location = new Point(min(p.x, r.x + r.width - 500, pos.x + 10),
				min(p.y, r.y + r.height - 160, pos.y + 10));

		final StringPopup textPopUp = new StringPopup(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), textUtils.getText(),
				location);
		textPopUp.open();
		textPopUp.setTextListenerAndFocus(this);

		return null;
	}

	public static int min(int i1, int i2, int i3) {
		return Math.min(Math.min(i1, i2), i3);
	}

	public static int getCursorPosition() {
		ISelection selection = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService()
				.getSelection();
		if ((selection instanceof ITextSelection)) {
			return ((ITextSelection) selection).getOffset();
		}
		return -1;
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

	public static Point getPoint(Caret c) {
		Point p = new Point(c.getBounds().x + 46, c.getBounds().y
				+ c.getBounds().height + 51);

		Control control = c.getParent();

		while (control.getParent() != null) {
			control = control.getParent();
			p.x += control.getBounds().x;
			p.y += control.getBounds().y;
		}

		return p;
	}
}