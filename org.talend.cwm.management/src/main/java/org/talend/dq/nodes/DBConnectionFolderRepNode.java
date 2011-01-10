// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.nodes;

import java.util.List;

import org.apache.log4j.Logger;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class DBConnectionFolderRepNode extends RepositoryNode {

    private static Logger log = Logger.getLogger(DBConnectionFolderRepNode.class);

    /**
     * DOC klliu DBConnectionFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public DBConnectionFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type) {
        super(object, parent, type);
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        RepositoryNode fetchNodeByFolder = new RepositoryNode(this.getObject(), this.getParent(), this.getType());
        ERepositoryObjectType contentType = this.getContentType();
        ProjectManager pManager = ProjectManager.getInstance();
        Container container = null;
        try {
            container = ProxyRepositoryFactory.getInstance()
                    .getMetadataConnection(pManager.getCurrentProject(), true);
            fetchRepositoryNodeByFolder(container, contentType, fetchNodeByFolder);
        } catch (PersistenceException e) {
            log.error(e, e);
        }

        return fetchNodeByFolder.getChildren();
    }

    public RepositoryNode fetchRepositoryNodeByFolder(Container patterns, ERepositoryObjectType parentItemType,
            RepositoryNode node) {
        RepositoryNode parent = node;
        for (Object object : patterns.getSubContainer()) {
            Container container = (Container) object;
            Property property = (Property) container.getProperty();
            // Item item = property.getItem();
            ERepositoryObjectType itemType = ERepositoryObjectType.getTypeFromKey(property.getLabel());

            if (itemType == null) {
                itemType = parentItemType;
            }
            Folder folder = new Folder(((Property) property), itemType);
            DBConnectionSubFolderRepNode childNodeFolder = new DBConnectionSubFolderRepNode(folder, parent,
                    ENodeType.SIMPLE_FOLDER);
            parent.getChildren().add(childNodeFolder);
            fetchRepositoryNodeByFolder(container, itemType, childNodeFolder);
        }
        // not folder or folders have no subFolder
        for (Object obj : patterns.getMembers()) {
            RepositoryViewObject viewObject = (RepositoryViewObject) obj;
            if (!viewObject.isDeleted()) {
                DBConnectionRepNode repNode = new DBConnectionRepNode(viewObject, node, ENodeType.REPOSITORY_ELEMENT);
                // viewObject.setRepositoryNode(repNode);
                parent.getChildren().add(repNode);

            }
        }
        return parent;
    }
}
