package com.terrynow.eclipse.stringedit.handlers;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.terrynow.eclipse.stringedit.StringUtils;

public class MultiLineHyperlinkDector extends AbstractHyperlinkDetector {

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {

		ITextEditor editor = (ITextEditor) getAdapter(ITextEditor.class);
		if (!editor.getTitle().endsWith(".java")) {
			return null;
		}
		IDocumentProvider dp = editor.getDocumentProvider();
		StringUtils textUtils = new StringUtils();
		textUtils.setDoc(dp.getDocument(editor.getEditorInput()).get());
		textUtils.setCursorPosition(getCursorPosition());
		boolean val = textUtils.proccess();
		int cursorPosition = textUtils.getCursorPosition();
		if (cursorPosition < 0) {
			return null;
		}
		if (!val) {
			return null;
		}

		return new IHyperlink[] { new MultiLineHyperlink(new Region(
				textUtils.getStart(), textUtils.getLength())) };
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

}
