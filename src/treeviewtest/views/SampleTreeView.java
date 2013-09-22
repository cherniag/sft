package treeviewtest.views;

import static com.gc.textsearcher.Utils.format;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.ide.IDE;

import com.gc.textsearcher.FileRow;
import com.gc.textsearcher.FileRow.StringResult;
import com.gc.textsearcher.FilesFinder;
import com.gc.textsearcher.MatcherBuilder;
import com.gc.textsearcher.NegateStringRegexMatcher;
import com.gc.textsearcher.StringRegexMatcher;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SampleTreeView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "treeviewtest.views.SampleTreeView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action openSearchDialogAction;
	private Action deleteSearchResultsAction;
	private Action openInSystemEditorAction;
	private Action openInEclipseEditorAction;
	private Action doubleClickAction;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		protected TreeParent invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
/*
 * We will set up a dummy model to initialize tree heararchy.
 * In a real code, you will connect to a real model and
 * expose its hierarchy.
 */
		void initialize() {
			invisibleRoot = new TreeParent("");
		}
	}
	/**
	 * The constructor.
	 */
	public SampleTreeView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "TreeViewTest.viewer");
		makeActions();
		hookContextMenu();
		//hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleTreeView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(openSearchDialogAction);
		manager.add(new Separator());
		manager.add(deleteSearchResultsAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(openSearchDialogAction);
		manager.add(deleteSearchResultsAction);
		manager.add(openInEclipseEditorAction);
		manager.add(openInSystemEditorAction);
		//manager.add(new Separator());
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(openSearchDialogAction);
		manager.add(deleteSearchResultsAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		openSearchDialogAction = new Action() {
			public void run() {
				openSearchDialog();
			}
		};
		openSearchDialogAction.setText("New search");
		openSearchDialogAction.setToolTipText("New search dialog");
		openSearchDialogAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		
		deleteSearchResultsAction = new Action() {
			public void run() {
				clearResults();
			}
		};
		deleteSearchResultsAction.setText("Clear all");
		deleteSearchResultsAction.setToolTipText("Remove all results");
		deleteSearchResultsAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		openInSystemEditorAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				String path=getPath(obj);
				if(null != path){
					Program.launch(path);
				}
			}
		};
		openInSystemEditorAction.setText("Open in system editor");
		openInSystemEditorAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_FILE));
		
		openInEclipseEditorAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				String path=getPath(obj);
				IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(path));
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try{
					IDE.openEditorOnFileStore(page, fileStore);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		openInEclipseEditorAction.setText("Open in IDE");
		openInEclipseEditorAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT));
	}
	
	private String getPath(Object obj) {
		if(obj.getClass().equals(TreeObject.class) 
				&& !((TreeObject)obj).getName().startsWith("Search")){
			return ((TreeObject)obj).getParent().getName();
		}else if(obj.getClass().equals(TreeParent.class) 
				&& !((TreeParent)obj).getName().startsWith("Search")){
			return ((TreeObject)obj).getName();
		}else{
			return null;
		}
	}

	protected void clearResults() {
		viewer.setContentProvider(new ViewContentProvider());
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void openSearchDialog() {
		final MainDialog areaDialog = new MainDialog(viewer.getControl().getShell());
		if(Window.CANCEL == areaDialog.open() || areaDialog.getRootPath()==null 
				|| areaDialog.getRootPath().isEmpty()){
			return;
		}
		File file = new File(areaDialog.getRootPath());
		MatcherBuilder matcherBuilder = new MatcherBuilder();
		for (String pattern : areaDialog.getPathPattern().split(";")) {
			matcherBuilder.addOrMatcher(new StringRegexMatcher(pattern));
		}
		for (String pattern : areaDialog.getExcludePathPattern().split(";")) {
			matcherBuilder.addAndMatcher(new NegateStringRegexMatcher(pattern));
		}
		matcherBuilder.build();
		FilesFinder filesFinder = new FilesFinder(matcherBuilder);
		List<File> fileRows = null;
		final List<FileRow> items = new ArrayList<FileRow>();
		try {
			fileRows = filesFinder.findFiles(file);
			for (File f : fileRows) {
				FileRow fr = new FileRow(f);
				if (fr.search(areaDialog.getContainsText())) {
					items.add(fr);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ViewContentProvider contentProvider = new ViewContentProvider(){
			void initialize() {
				TreeParent root = new TreeParent(format("Search \"%s\" in \"%s\"",
						areaDialog.getContainsText(), areaDialog.getRootPath()));
				for (FileRow fileRow:items){
					TreeParent treeParent = new TreeParent(fileRow.getAbsolutePath());
					for(StringResult stringResult : fileRow.getStringResults()){
						TreeObject treeObject = new TreeObject(stringResult.getLineNumber()+ ": " +
								stringResult.getLineValue());
						treeParent.addChild(treeObject);
					}
					root.addChild(treeParent);
				}
				invisibleRoot = new TreeParent("");
				invisibleRoot.addChild(root);
			}
		};
		viewer.setContentProvider(contentProvider);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}