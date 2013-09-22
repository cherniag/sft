package treeviewtest.views;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.gc.textsearcher.StaticDataHolder;

public class MainDialog extends TitleAreaDialog{
	private Shell initialShell;
	private Text containsText;
	private Label label;
	private Text pathPatternText;
	private Label pathPatternLabel;
	private Button openFolderDialogButton;
	private Combo pathSelectionCombo;
	
	private String findText;
	private String pathPattern;
	private String excludePathPattern;
	private String rootPath;
	private Label excludePathPatternLabel;
	private Text excludePathPatternText;
	
	
	public MainDialog(Shell parentShell) {
		super(parentShell);
		initialShell = parentShell;
	}
	
	@Override
	public boolean close() {
		System.out.println("MainDialog.close()");
		pathPattern = pathPatternText.getText();
		findText = containsText.getText();
		rootPath=pathSelectionCombo.getText();
		excludePathPattern = excludePathPatternText.getText();
		StaticDataHolder.setProperty("last.location", rootPath);
		StaticDataHolder.setProperty("last.path.pattern", pathPattern);
		StaticDataHolder.setProperty("last.exclude.path.pattern", excludePathPattern);
		StaticDataHolder.setProperty("last.find.text", findText);
		pushNewPathToItems();
		StaticDataHolder.setData("path.list",pathSelectionCombo.getItems());
		return super.close();
	}

	private void pushNewPathToItems() {
		if(pathSelectionCombo.getText()==null || pathSelectionCombo.getText().isEmpty()){
			return;
		}
		for(String s:pathSelectionCombo.getItems()){
			if (s!=null && s.equals(pathSelectionCombo.getText())){
				return;
			}
		}
		int newSize = pathSelectionCombo.getItems().length < 5 ? pathSelectionCombo.getItems().length + 1 : 5;
		String[] newItems = new String[newSize];
		for (int i=0;i<pathSelectionCombo.getItems().length&&i<newSize-1;i++){
			newItems[i+1]=pathSelectionCombo.getItems()[i];
		}
		newItems[0]=pathSelectionCombo.getText();
		pathSelectionCombo.setItems(newItems);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Search text in files");
		setMessage("Specify file name using regular expressions " +
				"(for example, to search in \".doc\" files use \".*doc\" value)." +
				"You can use several patterns, separated by \";\"." +
				"For example: \".*doc;.*txt\" will search in .doc or .txt files");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		if(area.getChildren()!=null && area.getChildren().length>0){
			area.getChildren()[0].dispose();
		}
		area.setLayout(new GridLayout(3, false));
		initComponents(area);
		return area;
	}
	
	public String getContainsText() {
		return findText;
	}
	
	public String getPathPattern(){
		return pathPattern;
	}
	
	public String getExcludePathPattern(){
		return excludePathPattern;
	}
	
	public String getRootPath() {
		return rootPath;
	}

	private void initComponents(Composite parent) {
		initContainsTextLabel(parent);
		initContainsText(parent);
		initPathPatternLabel(parent);
		initPathPatternText(parent);
		initExludePathPatternLabel(parent);
		initExludePathPatternText(parent);
		initOpenFolderDialogButton(parent);
		initPathSelectionComboBox(parent);
	}


	private void initExludePathPatternText(Composite parent) {
		excludePathPatternText = new Text(parent, SWT.BORDER);
		excludePathPatternText.setToolTipText("Exclude file name pattern - usual regular expression, semicolon delimeted");
		excludePathPatternText.setLayoutData(getGridData(2, true));
		excludePathPatternText.setText(StaticDataHolder.getProperty("last.exclude.path.pattern", ""));
	}

	private void initExludePathPatternLabel(Composite parent) {
		excludePathPatternLabel = new Label(parent, SWT.NONE);
		excludePathPatternLabel.setToolTipText("Exclude file name pattern - usual regular expression, semicolon delimeted");
		excludePathPatternLabel.setText("Exclude path pattern:");
		excludePathPatternLabel.setLayoutData(getGridData(1, false));
	}

	private GridData getGridData(int horizontalSpan, boolean grabHorizontalSpace) {
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = grabHorizontalSpace;
		data.horizontalSpan=horizontalSpan;
		return data;
	}
	
	private void initPathSelectionComboBox(Composite parent) {
		pathSelectionCombo = new Combo(parent, SWT.BORDER);
		pathSelectionCombo.setLayoutData(getGridData(2, true));
		Object items = StaticDataHolder.getData("path.list");
		if(items!=null && items.getClass().equals(String[].class)){
			pathSelectionCombo.setItems((String[]) items);
		}
		pathSelectionCombo.setText(StaticDataHolder.getProperty("last.location", ""));
	}

	private void initOpenFolderDialogButton(Composite parent) {
		openFolderDialogButton = new Button(parent, SWT.PUSH);
		openFolderDialogButton.setLayoutData(getGridData(1, false));
		openFolderDialogButton.setText("Select folder...");
		openFolderDialogButton.addSelectionListener(new SelectionAdapter() {
			private String path;
			@Override
			public void widgetSelected(SelectionEvent e) {
				path = new DirectoryDialog(initialShell).open();
				pathSelectionCombo.setText(path);				
			}
		});
	}

	private void initPathPatternText(Composite parent) {
		pathPatternText = new Text(parent, SWT.BORDER);
		pathPatternText.setToolTipText("File name pattern - usual regular expression, semicolon delimeted");
		pathPatternText.setLayoutData(getGridData(2, true));
		pathPatternText.setText(StaticDataHolder.getProperty("last.path.pattern", ""));
	}

	private void initPathPatternLabel(Composite parent) {
		pathPatternLabel = new Label(parent, SWT.NONE);
		pathPatternLabel.setToolTipText("File name pattern - usual regular expression, semicolon delimeted");
		pathPatternLabel.setText("File name pattern:");
		pathPatternLabel.setLayoutData(getGridData(1, false));
	}

	private void initContainsText(Composite parent) {
		containsText = new Text(parent, SWT.BORDER);
		containsText.setLayoutData(getGridData(2, true));
		containsText.setText(StaticDataHolder.getProperty("last.find.text", ""));
	}

	private void initContainsTextLabel(Composite parent) {
		label = new Label(parent, SWT.NONE);
		label.setText("Contains text:");
		label.setLayoutData(getGridData(1, false));
	}

	
}
