package burp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuItem;


public class BurpExtender implements IBurpExtender, IContextMenuFactory, IHttpListener  {

    private IBurpExtenderCallbacks callbacks;
    private static PrintWriter stdout;
    private static boolean isEnable = true;
    IExtensionHelpers helpers = null;

    protected static String I_S_DOMIAIN = "Include Second-Level Domain to Scope";
    protected static String E_CUR_DOMIAIN = "Exclude Current Domain From Scope";
    protected static String E_CUR_URL = "Exclude Current URL From Scope";

    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();

        callbacks.setExtensionName("Exclude from Scope");


        stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println("Author: xiaoxiaoleo");
        stdout.println("Repo: https://github.com/xiaoxiaoleo/BurpSuite-Exclude-From-Scope");

        callbacks.registerContextMenuFactory(BurpExtender.this);
        callbacks.registerHttpListener(this);

    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        if(invocation.getInvocationContext() == invocation.CONTEXT_PROXY_HISTORY){
            if(true) {

                //String curURL = iURL.getProtocol() + "://"+  iURL.getHost()  + "/" +  iURL.getPath();
                //String topDomain = iURL.getHost().split(".")[-2].concat(".").concat(iURL.getHost().split(".")[-1]);

                List<JMenuItem> menu = new ArrayList<JMenuItem>();
                JMenuItem jItem1 = new JMenuItem(I_S_DOMIAIN);
                JMenuItem jItem2 = new JMenuItem(E_CUR_DOMIAIN);
                //JMenuItem jItem3 = new JMenuItem(E_CUR_URL + ": " + iURL.toString());
                menu.add(jItem1);
                menu.add(jItem2);
                //menu.add(jItem3);
                jItem1.addActionListener(new ActionListener(){

                    public void actionPerformed(ActionEvent arg0) {
                        IHttpRequestResponse req = invocation.getSelectedMessages()[0];
                        IRequestInfo request = helpers.analyzeRequest(req.getRequest());
                        String host = "fail.get.host";
                        for(String headLine : request.getHeaders()){
                            if (headLine.toLowerCase().startsWith("host")){
                                host = headLine.split(": ")[1];
                            }

                        }

                        String curDomain = "https" + "://"+ host;
                        String curDomainHTTP = "http" + "://"+ host;
                        URL url, urlHTTP;
                        try {
                            url = new URL(curDomain);
                            callbacks.includeInScope(url);
                            urlHTTP = new URL(curDomainHTTP);
                            callbacks.includeInScope(urlHTTP);
                        } catch (MalformedURLException ex) {

                        }
                    }
                });

                jItem2.addActionListener(new ActionListener(){

                    public void actionPerformed(ActionEvent arg0) {
                        IHttpRequestResponse req = invocation.getSelectedMessages()[0];

                        IRequestInfo request = helpers.analyzeRequest(req.getRequest());
                        String host = "fail.get.host";
                        for(String headLine : request.getHeaders()){
                            if (headLine.toLowerCase().startsWith("host")){
                                host = headLine.split(": ")[1];
                            }

                        }

                        String curDomain = "https" + "://"+ host;
                        String curDomainHTTP = "http" + "://"+ host;
                        URL url, urlHTTP;
                        try {
                            url = new URL(curDomain);
                            callbacks.excludeFromScope(url);
                            urlHTTP = new URL(curDomainHTTP);
                            callbacks.excludeFromScope(urlHTTP);
                        } catch (MalformedURLException ex) {

                        }
                    }
                });


                return menu;
            }
        }
        return null;
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo){
      /*    if(messageIsRequest) {
            return;
        }

      IRequestInfo requestInfo = helpers.analyzeRequest(messageInfo.getRequest());
        if(!requestInfo.headers.get(0)!!.startsWith("OPTIONS")) {
            return
        }

        val response = messageInfo.response
        val responseInfo = cb.helpers.analyzeResponse(response)
        val headers = responseInfo.headers
        headers.add("Content-Type: application/octet-stream")
        messageInfo.response = cb.helpers.buildHttpMessage(headers, response.copyOfRange(responseInfo.bodyOffset, response.size))
*/    }


/*
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().startsWith("Exclude ")) {
                URL url;
                try {
                    url = new URL(e.getActionCommand().split(": ")[1]);
                    this.callbacks.excludeFromScope(url);
                } catch (MalformedURLException ex) {

                }

            }
        }catch(Exception exec){

        }

    }
*/




}