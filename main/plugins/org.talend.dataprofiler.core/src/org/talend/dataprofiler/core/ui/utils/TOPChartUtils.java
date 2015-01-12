// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableMenuGenerator;
import org.talend.dataprofiler.core.ui.editor.preview.model.MenuItemEntity;
import org.talend.dataprofiler.core.ui.editor.preview.model.dataset.CustomerDefaultBAWDataset;
import org.talend.dataprofiler.service.ITOPChartService;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.explore.IDataExplorer;
import org.talend.dq.helper.AbstractOSGIServiceUtils;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-8 Detailled comment
 * 
 */
public class TOPChartUtils extends AbstractOSGIServiceUtils {

    private static Logger log = Logger.getLogger(TOPChartUtils.class);

    private static TOPChartUtils instance;

    private ITOPChartService chartService;

    public static TOPChartUtils getInstance() {
        if (instance == null) {
            instance = new TOPChartUtils();
        }
        return instance;
    }

    @Override
    public boolean isServiceInstalled() {
        initService(true);
        return this.chartService != null;
    }

    @Override
    public String getPluginName() {
        return ITOPChartService.PLUGIN_NAME;
    }

    @Override
    public String getJarFileName() {
        return ITOPChartService.JAR_FILE_NAME;
    }

    @Override
    public String getServiceName() {
        return ITOPChartService.class.getName();
    }

    @Override
    protected void setService(BundleContext context, ServiceReference serviceReference) {
        if (serviceReference != null) {
            Object obj = context.getService(serviceReference);
            if (obj != null) {
                this.chartService = (ITOPChartService) obj;
            }
        }
    }

    // No need to download,only for check
    public boolean isTOPChartInstalled() {
        initService(false);
        return this.chartService != null;
    }

    public Object createBarChart(String title, Object dataset, boolean showLegend) {
        if (isServiceInstalled()) {
            return chartService.createBarChart(title, ((CustomerDefaultCategoryDataset) dataset).getDataset(), showLegend);
        }
        return null;
    }

    public Object createBarChart(String title, Object dataset) {
        if (isServiceInstalled()) {
            return chartService.createBarChart(title, ((CustomerDefaultCategoryDataset) dataset).getDataset());
        }
        return null;
    }

    public Object createBenfordChart(String axisXLabel, String categoryAxisLabel, Object dataset, List<String> dotChartLabels,
            double[] formalValues, String title) {
        if (isServiceInstalled()) {
            return chartService.createBenfordChart(axisXLabel, categoryAxisLabel,
                    ((CustomerDefaultCategoryDataset) dataset).getDataset(), dotChartLabels, formalValues, title);
        }
        return null;
    }

    public Object createTalendChartComposite(Object parentComponent, int style, Object chart, boolean useBuffer) {
        if (isServiceInstalled()) {
            return chartService.createTalendChartComposite(parentComponent, style, chart, useBuffer);
        }
        return null;
    }

    public Object createChartComposite(Object composite, int style, Object chart, boolean useBuffer) {
        if (isServiceInstalled()) {
            return chartService.createChartComposite(composite, style, chart, useBuffer);
        }
        return null;
    }

    public Object createChartCompositeWithSpecialSize(Object composite, int style, Object chart, boolean useBuffer, int height,
            int width) {
        if (isServiceInstalled()) {
            return chartService.createChartCompositeWithSpecialSize(composite, style, chart, useBuffer, height, width);
        }
        return null;
    }

    public Object createChartCompositeWithFull(Object composite, Object chart) {
        if (isServiceInstalled()) {
            return chartService.createChartCompositeWithFull(composite, chart);
        }
        return null;
    }

    public Object getDatasetFromChart(Object chart, int datasetIndex) {
        if (isServiceInstalled()) {
            return chartService.getDatasetFromChart(chart, datasetIndex);
        }
        return null;
    }

    public void decorateChart(Object chart, boolean withPlot) {
        if (isServiceInstalled()) {
            chartService.decorateChart(chart, withPlot);
        }
    }

    public void decorateColumnDependency(Object chart) {
        if (isServiceInstalled()) {
            chartService.decorateColumnDependency(chart);
        }
    }

    public void setOrientation(Object chart, boolean isHorizontal) {
        if (isServiceInstalled()) {
            chartService.setOrientation(chart, isHorizontal);
        }
    }

    public void setDisplayDecimalFormatOfChart(Object chart) {
        if (isServiceInstalled()) {
            chartService.setDisplayDecimalFormatOfChart(chart);
        }
    }

    public void addMouseListenerForChart(Object chartComposite, final Map<String, Object> menuMap, boolean useRowFirst) {
        if (isServiceInstalled()) {
            chartService.addMouseListenerForChart(chartComposite, menuMap, useRowFirst);
        }
    }

    public Object createPieChart(String title, Object dataset, boolean showLegend, boolean toolTips, boolean urls) {
        if (isServiceInstalled()) {
            return chartService.createPieChart(title, dataset, showLegend, toolTips, urls);
        }
        return null;
    }

    public Object createBoxAndWhiskerChart(String title, Object dataset) {
        if (isServiceInstalled()) {
            return chartService.createBoxAndWhiskerChart(title, ((CustomerDefaultBAWDataset) dataset).getDataset());
        }
        return null;
    }

    public Object createStackedBarChart(String title, Object dataset, boolean showLegend) {
        if (isServiceInstalled()) {
            return chartService.createStackedBarChart(title, ((CustomerDefaultCategoryDataset) dataset).getDataset(), showLegend);
        }
        return null;
    }

    public Object createStackedBarChart(String title, Object dataset, boolean isHorizatal, boolean showLegend) {
        if (isServiceInstalled()) {
            return chartService.createStackedBarChart(title, ((CustomerDefaultCategoryDataset) dataset).getDataset(),
                    isHorizatal, showLegend);
        }
        return null;
    }

    public void addListenerToChartComp(Object chartComposite, final String referenceLink, final String menuText) {
        if (isServiceInstalled()) {
            chartService.addListenerToChartComp(chartComposite, referenceLink, menuText);
        }
    }

    // checkSql: =true , use the check sql service as the judgement, = false, come from the column ana, use the input
    // compute as the judgement
    public Menu createMenu(final Shell shell, final IDataExplorer explorer, final Analysis analysis,
            final ExecutionLanguage currentEngine, final ChartDataEntity currentDataEntity, final Indicator currentIndicator,
            final boolean checkSql) {
        Menu menu = new Menu(shell, SWT.POP_UP);

        int createPatternFlag = 0;
        MenuItemEntity[] itemEntities = ChartTableMenuGenerator.generate(explorer, analysis, currentDataEntity);
        for (final MenuItemEntity itemEntity : itemEntities) {
            MenuItem item = new MenuItem(menu, SWT.PUSH);
            item.setText(itemEntity.getLabel());
            item.setImage(itemEntity.getIcon());
            item.setEnabled(DrillDownUtils.isMenuItemEnable(currentDataEntity, itemEntity, analysis));
            item.addSelectionListener(createSelectionAdapter(analysis, currentEngine, currentDataEntity, currentIndicator,
                    itemEntity, checkSql));

            if (ChartTableFactory.isPatternFrequencyIndicator(currentIndicator) && createPatternFlag == 0) {
                ChartTableFactory.createMenuOfGenerateRegularPattern(analysis, menu, currentDataEntity);
            }

            createPatternFlag++;
        }
        return menu;
    }

    private SelectionAdapter createSelectionAdapter(final Analysis analysis1, final ExecutionLanguage currentEngine,
            final ChartDataEntity currentDataEntity, final Indicator currentIndicator, final MenuItemEntity itemEntity,
            final boolean checkSql) {
        return new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (ExecutionLanguage.JAVA == currentEngine) {
                    try {
                        DrillDownEditorInput input = new DrillDownEditorInput(analysis1, currentDataEntity, itemEntity);
                        Boolean check = checkSql ? SqlExplorerUtils.getDefault().getSqlexplorerService() != null : input
                                .computeColumnValueLength(input.filterAdaptDataList());

                        if (check) {
                            CorePlugin
                                    .getDefault()
                                    .getWorkbench()
                                    .getActiveWorkbenchWindow()
                                    .getActivePage()
                                    .openEditor(input,
                                            "org.talend.dataprofiler.core.ui.editor.analysis.drilldown.drillDownResultEditor");//$NON-NLS-1$
                        } else {
                            if (!checkSql) {
                                MessageDialog.openWarning(null,
                                        Messages.getString("DelimitedFileIndicatorEvaluator.badlyForm.Title"),//$NON-NLS-1$
                                        Messages.getString("DelimitedFileIndicatorEvaluator.badlyForm.Message"));//$NON-NLS-1$
                            }
                        }

                    } catch (PartInitException e1) {
                        log.error(e1, e1);
                    }
                } else {
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            Connection tdDataProvider = SwitchHelpers.CONNECTION_SWITCH.doSwitch(analysis1.getContext()
                                    .getConnection());
                            String query = itemEntity.getQuery();
                            String editorName = currentIndicator.getName();
                            SqlExplorerUtils.getDefault().runInDQViewer(tdDataProvider, query, editorName);
                        }

                    });
                }
            }
        };
    }

    public Object createDefaultCategoryDataset() {
        if (isServiceInstalled()) {
            return chartService.createDefaultCategoryDataset();
        }
        return null;
    }

    public void addValueToCategoryDataset(Object dataset, double value, String labelX, String labelY) {
        if (isServiceInstalled()) {
            chartService.addValueToCategoryDataset(dataset, value, labelX, labelY);
        }
    }

    public Object createPieDataset(Map<String, Double> valueMap) {
        if (isServiceInstalled()) {
            return chartService.createPieDataset(valueMap);
        }
        return null;
    }

    public Object createDefaultBoxAndWhiskerCategoryDataset(Double mean, Double median, Double q1, Double q3,
            Double minRegularValue, Double maxRegularValue) {
        if (isServiceInstalled()) {
            return chartService.createDefaultBoxAndWhiskerCategoryDataset(mean, median, q1, q3, minRegularValue, maxRegularValue);
        }
        return null;
    }

    public Object createXYDataset(Map<Integer, Double> valueMap) {
        if (isServiceInstalled()) {
            return chartService.createXYDataset(valueMap);
        }
        return null;
    }

    public int getColumnCount(Object dataset) {
        if (isServiceInstalled()) {
            return chartService.getColumnCount(dataset);
        }
        return Integer.MIN_VALUE;
    }

    public int getRowCount(Object dataset) {
        if (isServiceInstalled()) {
            return chartService.getRowCount(dataset);
        }
        return Integer.MIN_VALUE;
    }

    public Number getValue(Object dataset, int row, int column) {
        if (isServiceInstalled()) {
            return chartService.getValue(dataset, row, column);
        }
        return null;
    }

    public Comparable getColumnKey(Object dataset, int column) {
        if (isServiceInstalled()) {
            return chartService.getColumnKey(dataset, column);
        }
        return null;
    }

    public void setValue(Object dataset, Number value, Comparable rowKey, Comparable columnKey) {
        if (isServiceInstalled()) {
            chartService.setValue(dataset, value, rowKey, columnKey);
        }
    }

    public void clearDataset(Object dataset) {
        if (isServiceInstalled()) {
            chartService.clearDataset(dataset);
        }
    }

    public void refrechChart(Object chartComp, Object chart) {
        if (isServiceInstalled()) {
            chartService.refrechChart(chartComp, chart);
        }
    }

    public void clearDefaultBoxAndWhiskerCategoryDataset(Object dataset) {
        if (isServiceInstalled()) {
            chartService.clearDefaultBoxAndWhiskerCategoryDataset(dataset);
        }
    }

    public Object createTaskSeriesCollection() {
        if (isServiceInstalled()) {
            return chartService.createTaskSeriesCollection();
        }
        return null;
    }

    public Object createTaskSeries(String keyOfDataset) {
        if (isServiceInstalled()) {
            return chartService.createTaskSeries(keyOfDataset);
        }
        return null;
    }

    public void addTaskToTaskSeries(Object taskSeries, String key, Date[] date) {
        if (isServiceInstalled()) {
            chartService.addTaskToTaskSeries(taskSeries, key, date);
        }
    }

    public void addSeriesToCollection(Object taskSeriesCollection, Object taskSeries) {
        if (isServiceInstalled()) {
            chartService.addSeriesToCollection(taskSeriesCollection, taskSeries);
        }
    }

    public Object createGanttChart(String chartAxies, Object ganttDataset) {
        if (isServiceInstalled()) {
            return chartService.createGanttChart(chartAxies, ganttDataset);
        }
        return null;
    }

    public void addSeriesToDefaultXYZDataset(Object dataset, String keyOfDataset, double[][] data) {
        if (isServiceInstalled()) {
            chartService.addSeriesToDefaultXYZDataset(dataset, keyOfDataset, data);
        }
    }

    public Object createBubbleChart(String chartName, Object dataset) {
        if (isServiceInstalled()) {
            return chartService.createBubbleChart(chartName, dataset);
        }
        return null;
    }

    public Object createDefaultXYZDataset() {
        if (isServiceInstalled()) {
            return chartService.createDefaultXYZDataset();
        }
        return null;
    }

    public void createAnnotOnGantt(Object chart, List<Object[]> rowList, int multiDateColumn, int nominal) {
        if (isServiceInstalled()) {
            chartService.createAnnotOnGantt(chart, rowList, multiDateColumn, nominal);
        }
    }

    public void showChartInFillScreen(Object chart, boolean isCountAvgNull, boolean isMinMaxDate) {
        if (isServiceInstalled()) {
            chartService.showChartInFillScreen(chart, isCountAvgNull, isMinMaxDate);
        }
    }

    public void addSpecifiedListenersForCorrelationChart(Object chartcomp, Object chart, final boolean isAvg,
            final boolean isDate, final Map<Integer, Object> keyWithAdapter) {
        if (isServiceInstalled()) {
            chartService.addSpecifiedListenersForCorrelationChart(chartcomp, chart, isAvg, isDate, keyWithAdapter);
        }
    }

    public int getSeriesCount(Object chart) {
        if (isServiceInstalled()) {
            return chartService.getSeriesCount(chart);
        }
        return Integer.MIN_VALUE;
    }

    public int getSeriesRowCount(Object chart) {
        if (isServiceInstalled()) {
            return chartService.getSeriesRowCount(chart);
        }
        return Integer.MIN_VALUE;
    }

    public Object createSelectionAdapterForButton(final Object chart, final boolean isCountAvg, final boolean isMinMax) {
        if (isServiceInstalled()) {
            return chartService.createSelectionAdapterForButton(chart, isCountAvg, isMinMax);
        }
        return null;
    }

    public String getSeriesKeyOfBubbleChart(Object chart, int index) {
        if (isServiceInstalled()) {
            return chartService.getSeriesKeyOfBubbleChart(chart, index);
        }
        return StringUtils.EMPTY;
    }

    public String getSeriestKeyOfGanttChart(Object chart, int index) {
        if (isServiceInstalled()) {
            return chartService.getSeriestKeyOfGanttChart(chart, index);
        }
        return StringUtils.EMPTY;
    }

    public Object createChartCompositeForCorrelationAna(Object parent, Object chart, int height) {
        if (isServiceInstalled()) {
            return chartService.createChartCompositeForCorrelationAna(parent, chart, height);
        }
        return null;
    }

    @Override
    protected String getMissingMessageName() {
        return "TOPChartUtils.missingTopChart"; //$NON-NLS-1$
    }

    @Override
    protected String getRestartMessageName() {
        return "TOPChartUtils.restartToLoadTopChart"; //$NON-NLS-1$
    }

}
