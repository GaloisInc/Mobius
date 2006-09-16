package umbra;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

/**
 * The action is used to commit changes made to Java source code.
 * After running it the rebuild action will create a Bytecode related
 * to the commited version.
 * 
 * @author Wojciech Wąs
 *
 */
public class CommitAction implements IEditorActionDelegate {
	
	/**
	 * TODO
	 */
	private IEditorPart editor;

	/**
	 * TODO
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editor = targetEditor;
	}

	/**
	 * TODO
	 */
	public void run(IAction action) {
			editor.doSave(null);
			IFile file = ((FileEditorInput)editor.getEditorInput()).getFile();
			IPath active = file.getFullPath();
			String lastSegment = active.lastSegment().replaceFirst(".java", ".class");
			String fnameFrom = active.removeLastSegments(1).append("_" + lastSegment).toOSString();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot(); 
			IFile fileFrom = root.getFile(new Path(fnameFrom));
			try {
				fileFrom.delete(true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
	}

	/**
	 * TODO
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
