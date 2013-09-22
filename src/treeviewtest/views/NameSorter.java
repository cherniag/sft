package treeviewtest.views;

import static com.gc.textsearcher.Utils.format;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

class NameSorter extends ViewerSorter {
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1.getClass().equals(TreeObject.class) && e2.getClass().equals(TreeObject.class)){
			String s1 = ((TreeObject)e1).getName();
			int indexOfColon = s1.indexOf(":");
			s1=s1.substring(0, indexOfColon);
			String s2 = ((TreeObject)e2).getName();
			indexOfColon = s2.indexOf(":");
			s2=s2.substring(0, indexOfColon);
			System.out.println(format("s1=%s s2=%s",s1,s2));
			return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
		}
		return super.compare(viewer, e1, e2);
	}
}