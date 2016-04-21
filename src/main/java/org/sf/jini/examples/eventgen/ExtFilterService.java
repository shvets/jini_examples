package org.sf.jini.examples.eventgen;

import org.sf.jini.examples.common.ProxiedJiniService;
import org.sf.jini.examples.common.SingleServiceLocator;
import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;

public class ExtFilterService extends ProxiedJiniService {
  private ExtFilterImpl impl = new ExtFilterImpl();

  public ExtFilterService(Exporter exporter) throws Exception {
    createProxyAdapter(exporter);

    //setRemote(impl);

    export(impl);

    createServiceItem("eventgen");
  }

  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    ExtFilterService service = new ExtFilterService(exporter);

    service.register(serviceLocator, 3*60*1000);

    System.out.println("Registered.");
  }

}
