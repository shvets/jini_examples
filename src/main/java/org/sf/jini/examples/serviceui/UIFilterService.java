package org.sf.jini.examples.serviceui;

import java.util.Set;
import java.util.HashSet;
import java.rmi.MarshalledObject;

import net.jini.core.entry.Entry;
import net.jini.lookup.ui.MainUI;
import net.jini.lookup.ui.factory.JFrameFactory;
import net.jini.lookup.ui.factory.JComponentFactory;
import net.jini.lookup.ui.attribute.UIFactoryTypes;
import net.jini.lookup.entry.Name;
import net.jini.lookup.entry.UIDescriptor;

import org.sf.jini.examples.common.JiniService;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.simpleservice.FilterImpl;

public class UIFilterService extends JiniService {

  public UIFilterService(Entry[] entries) throws Exception {
    super(entries);
  }

  protected Object getServiceObject() {
    return new FilterImpl();
  }

  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    Set<String> typeNames = new HashSet<String>();

    typeNames.add(JFrameFactory.TYPE_NAME);

    Set<UIFactoryTypes> uiAttributes = new HashSet<UIFactoryTypes>();

    uiAttributes.add(new UIFactoryTypes(typeNames));

    MarshalledObject factory = new MarshalledObject(new /*FilterFactory()*/FilterComponentFactory());

    UIDescriptor uiDescriptor = new UIDescriptor(MainUI.ROLE,
                                                 /*JFrameFactory.TOOLKIT*/JComponentFactory.TOOLKIT,
                                                 uiAttributes,
                                                 factory);

    Entry[] entries = new Entry[] { new Name("ui"), uiDescriptor };

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    UIFilterService service = new UIFilterService(entries);

    service.register(serviceLocator, 60*1000);

    System.out.println("Registered.");
  }

}
