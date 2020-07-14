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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Path;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.resource.ResourceManager;

public class AddValidPhoneForRegionCountIndicator extends AbstractWorksapceUpdateTask {

    private final String INDICATORNAME = "Valid Phone Number For Region Count";//$NON-NLS-1$

    private final String SRCFOLDERPATH = "/indicators/Phone Number Statistics/"; //$NON-NLS-1$

    @Override
    public Date getOrder() {
        return createDate(2020, 7, 14);
    }

    @Override
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        DQStructureManager manager = DQStructureManager.getInstance();
        IFolder desFolder = ResourceManager.getSystemIndicatorFolder().getFolder("Phone Number Statistics");
        if (!desFolder.getFile(INDICATORNAME).exists()) {
            manager
                    .copyFilesToFolder(CorePlugin.getDefault(), new Path(SRCFOLDERPATH).toString(), false, desFolder,
                            null);
        }
        return true;
    }

}
