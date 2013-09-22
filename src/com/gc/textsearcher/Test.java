//package com.gc.textsearcher;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.DirectoryDialog;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Group;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.TableColumn;
//import org.eclipse.swt.widgets.TableItem;
//import org.eclipse.swt.widgets.Text;
//
//public class Test {
//	static String path;
//	static String extension;
//
//	public static void main(String[] args) {
//		Display display = new Display();
//		final Shell shell = new Shell(display);
//		GridLayout layout = new GridLayout(2, false);
//		shell.setLayout(layout);
//
//		Group group = new Group(shell, SWT.NONE);
//		group.setText("Search options");
//		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
//		gridData.horizontalSpan = 2;
//		group.setLayoutData(gridData);
//		group.setLayout(new GridLayout(3, false));
//
//		Label label = new Label(group, SWT.NONE);
//		label.setText("Text:");
//		GridData data = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//		label.setLayoutData(data);
//		// label.setToolTipText("This is the tooltip of this label");
//
//		final Text text = new Text(group, SWT.BORDER);
//		// text.setText("This is the text in the text widget");
//		// text.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
//		// text.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
//		data = new GridData(SWT.LEFT, SWT.LEFT, false, false, 2, 1);
//		text.setLayoutData(data);
//		// set widgets size to their preferred size
//		// text.pack();
//		// label.pack();
//
//		Label fileExtensionLabel = new Label(group, SWT.NONE);
//		fileExtensionLabel.setText("Extension:");
//		data = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//		fileExtensionLabel.setLayoutData(data);
//		// label.setToolTipText("This is the tooltip of this label");
//
//		final Text fileExtensionText = new Text(group, SWT.BORDER);
//		// text.setText("This is the text in the text widget");
//		// text.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
//		// text.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
//		data = new GridData(SWT.LEFT, SWT.LEFT, false, false, 2, 1);
//		fileExtensionText.setLayoutData(data);
//
//		Button openDialogButton = new Button(group, SWT.PUSH);
//		data = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
//		openDialogButton.setLayoutData(data);
//		openDialogButton.setText("Select folder...");
//
//		final Text pathText = new Text(group, SWT.BORDER);
//		// text.setText("This is the text in the text widget");
//		// text.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
//		// text.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
//		data = new GridData(SWT.LEFT, SWT.LEFT, false, false, 2, 1);
//		pathText.setLayoutData(data);
//
//		openDialogButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				path = new DirectoryDialog(shell).open();
//				pathText.setText(path);
//				System.out.println(path);
//			}
//		});
//
//		Button searchButton = new Button(group, SWT.PUSH);
//		data = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
//		searchButton.setLayoutData(data);
//		searchButton.setText("Search");
//
//		final Table table = new Table(shell, SWT.MULTI | SWT.BORDER
//				| SWT.FULL_SELECTION);
//		table.setLinesVisible(true);
//		table.setHeaderVisible(true);
//		data = new GridData(SWT.FILL, SWT.FILL, true, true);
//		data.heightHint = 200;
//		table.setLayoutData(data);
//
//		final String[] titles = { "#", "File Name", "Row", "Line" };
//		for (int i = 0; i < titles.length; i++) {
//			TableColumn column = new TableColumn(table, SWT.NONE);
//			column.setText(titles[i]);
//			table.getColumn(i).pack();
//		}
//		searchButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				table.removeAll();
//				File file = new File(path);
//				int i = 1;
//
//				MatcherBuilder<String> matcherBuilder = new MatcherBuilder<String>();
//
//				for (String pattern : fileExtensionText.getText().split(";")) {
//					matcherBuilder.addMatcher(new StringRegexMatcher(pattern));
//				}
//				FilesFinder filesFinder = new FilesFinder(matcherBuilder);
//				List<File> fileRows = null;
//				try {
//					fileRows = filesFinder.findFiles(file, text.getText());
//					for (File f : fileRows) {
//						FileRow fr = new FileRow(f);
//						if (fr.search(text.getText())) {
//							TableItem item = new TableItem(table, SWT.NONE);
//							item.setText(0, String.valueOf(i++));
//							item.setText(1, f.getAbsolutePath());
//							for (FileRow.StringResult stringResult : fr
//									.getStringResults()) {
//								TableItem itemRow = new TableItem(table,
//										SWT.NONE);
//								itemRow.setText(2, String.valueOf(stringResult
//										.getLineNumber()));
//								itemRow.setText(3, stringResult.getLineValue());
//							}
//						}
//					}
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				for (i = 0; i < titles.length; i++) {
//					table.getColumn(i).pack();
//				}
//				
//			}
//		});
//
//		shell.open();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		display.dispose();
//	}
//
//}
