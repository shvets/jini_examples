<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page contentType="text/html; charset=UTF-8"%>

<%@page import="net.jini.core.lookup.ServiceRegistrar"%>
<%@page import="net.jini.core.discovery.LookupLocator"%>

<%@page import="org.sf.jini.examples.simpleservice.Filter"%>
<%@page import="org.sf.jini.examples.simpleservice.SimpleFilterClient"%>

<%!
  private String servletURL = "JiniTest.jsp";

  private SimpleFilterClient client = new SimpleFilterClient();

  private ServiceRegistrar registrar;

  private boolean jiniServicesUp = true;

  public void init() {
    System.out.println("Started ...");

    try {
      LookupLocator lookupLocator = new LookupLocator("jini://localhost");

      registrar = lookupLocator.getRegistrar();
    }
    catch(Exception e) {
      jiniServicesUp = false;
      //e.printStackTrace();
    }
  }                     
%>

<html>

<head>
  <title>Jini Test</title>
</head>

<body>
<%
if(!jiniServicesUp) {
%>
  Jini Services don't run.
<%
}
  else {
    String message = request.getParameter("message");

    if(message == null || message.length() == 0) { %>
      <form action="<%=servletURL%>">
        Message: <input type="textfield" name="message">
        <br>
        <input type="submit">
      </form>
<%
    }
    else {
      Filter filter = (Filter)client.lookup(registrar);

      if(filter == null) {
        out.println("<P>No service- try again later</P>");
      }
      else { %>
        <P>Got response: <%=filter.filter(message)%></P>

        <form action="<%=servletURL%>">
          Message: <input type="textfield" name="message">
          <br>
          <input type="submit">
        </form>
<%
      }
    }
  }
%>

</body>

</html>
