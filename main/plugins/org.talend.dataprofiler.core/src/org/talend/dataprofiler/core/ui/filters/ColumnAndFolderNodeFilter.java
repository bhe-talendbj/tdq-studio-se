// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DFColumnFolderRepNode;
import org.talend.dq.nodes.DFColumnRepNode;

/**
 * used to hide the column node and column folder node.
 */
public class ColumnAndFolderNodeFilter extends ViewerFilter {

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof DBColumnRepNode || element instanceof DFColumnRepNode || element instanceof DBColumnFolderRepNode
                || element instanceof DFColumnFolderRepNode) {
            return false;
        }
        return true;
    }
}
