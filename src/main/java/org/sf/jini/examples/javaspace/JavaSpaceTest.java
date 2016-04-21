package org.sf.jini.examples.javaspace;

import java.rmi.RMISecurityManager;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;

import net.jini.space.JavaSpace;

import org.sf.jini.examples.common.Util;

/**
 * Tests "javaspace" Jini service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class JavaSpaceTest {
  static {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
  }

  /**
   * Prints messages.
   *
   * @param javaSpace Javaspace service
   * @param txManager transaction manager
   * @throws Exception the exception
   */
  public void printMessages(JavaSpace javaSpace, TransactionManager txManager) 
              throws Exception {
    Transaction.Created transactionWrapper =
                        TransactionFactory.create(txManager, 1000);

    Transaction tx = transactionWrapper.transaction;

    System.out.println("Messages:");
    while(true) {
      Entry entry = javaSpace.takeIfExists(new Message(), tx, 10);

      if(entry == null) {
        break;
      }

      System.out.println(entry);
    }

    tx.abort();
  }

  /**
   * Main entry point.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    JavaSpaceTest test = new JavaSpaceTest();

    TransactionManager txManager = Util.getTransactionManager(registrar);

    if(txManager == null ) {
      System.out.println("TransactionManager service cannot be found.");
      return;
    }

    JavaSpace javaSpace = Util.getJavaSpace(registrar);

    if(javaSpace == null ) {
      System.out.println("JavaSpace service cannot be found.");
      return;
    }

    // 1. test without transaction

    javaSpace.write(new Message("message1", "john", "peter"), null, Lease.FOREVER);
    javaSpace.write(new Message("message2", "alex", "vano"), null, Lease.FOREVER);

    test.printMessages(javaSpace, txManager);

    // 2. test with transaction
    Transaction.Created transactionWrapper =
                        TransactionFactory.create(txManager, 60*10*1000);

    Transaction tx = transactionWrapper.transaction;

    javaSpace.write(new Message("message3", "inna", "anton"), tx, Lease.FOREVER);

    tx.commit();

    // 3. check messages for inna

    Entry entry = javaSpace.read(new Message(null, "inna", null), null, Lease.FOREVER);

    System.out.println("Message for inna: " + entry);

    // 4. remove all messages

    transactionWrapper = TransactionFactory.create(txManager, 60*10*1000);
    Transaction tx2 = transactionWrapper.transaction;

    while(true) {
      Entry e = javaSpace.takeIfExists(new Message(), tx2, 10);

      if(e == null) {
        break;
      }
    }

    tx2.commit();

    test.printMessages(javaSpace, txManager);

    System.exit(0);
  }
   
}
