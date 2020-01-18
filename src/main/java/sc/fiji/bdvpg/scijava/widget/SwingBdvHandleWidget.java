package sc.fiji.bdvpg.scijava.widget;

import bdv.util.BdvHandle;
import org.scijava.Priority;
import org.scijava.object.ObjectService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;
import sc.fiji.bdvpg.scijava.BdvHandleHelper;
import sc.fiji.bdvpg.scijava.services.SacService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swing implementation of {@link BdvHandleWidget}.
 *
 * @author Nicolas Chiaruttini
 */

@Plugin(type = InputWidget.class, priority = Priority.EXTREMELY_HIGH)
public class SwingBdvHandleWidget extends SwingInputWidget<BdvHandle> implements
        BdvHandleWidget<JPanel> {

    @Override
    protected void doRefresh() {
    }

    @Override
    public boolean supports(final WidgetModel model) {
        return super.supports(model) && model.isType(BdvHandle.class);
    }

    @Override
    public BdvHandle getValue() {
        return getSelectedBdvHandle();
    }

    @Parameter
    SacService sacService;

    JList list;

    public BdvHandle getSelectedBdvHandle() {
        return ((RenamableBdvHandle) list.getSelectedValue()).bdvh;
    }

    @Parameter
    ObjectService os;


    @Override
    public void set(final WidgetModel model) {
        super.set(model);
        List<RenamableBdvHandle> bdvhs = os.getObjects(BdvHandle.class).stream().map(bdvh -> new RenamableBdvHandle(bdvh)).collect(Collectors.toList());
        RenamableBdvHandle[] data = bdvhs.toArray(new RenamableBdvHandle[bdvhs.size()]);
        list = new JList(data); //data has type Object[]
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        list.addListSelectionListener((e)-> model.setValue(getValue()));
        getComponent().add(listScroller);
    }

    public class RenamableBdvHandle {

        public BdvHandle bdvh;

        public RenamableBdvHandle(BdvHandle bdvh) {
            this.bdvh = bdvh;
        }

        public String toString() {
            return BdvHandleHelper.getWindowTitle(bdvh);
        }

    }

}
