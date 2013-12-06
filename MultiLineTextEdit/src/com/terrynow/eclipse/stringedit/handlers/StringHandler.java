package com.terrynow.eclipse.stringedit.handlers;

import javax.swing.UIManager;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
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

		final StringPopup textPopUp = new StringPopup(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), textUtils.getText());
		textPopUp.open();
		textPopUp.setTextListenerAndFocus(this);

		return null;
	}

	private int getCursorPosition() {
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
}