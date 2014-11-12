 /**
 * @file MediaFireHTTP.java
 * @author Michael Kinuthia
 * @Brief connect to Mediafire API and download a file
 *
 */
package mit.fi.filedownload;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.AuthenticationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;




public class MediaFireHttp {
      private String APP_id;
      private String API_key;
      private String email;
      private String password;
      private String evaluate = null;
      private String key = null;
      private String token = null;
      private String link_type = "direct_download";
      private String downloadlink = null;
      private String downloadlinkkey = null;
      private String sessiontoken = null;
      private String quickkey = null;
      private String signature = null;
      private String folderkey = null;
     
      public MediaFireHttp(String API_key, String APP_id, String email, String Passwordd){
          this.API_key = API_key;
          this.APP_id = APP_id;
          this.email = email;
          this.password = Passwordd;
      }
      
      public void connection(String email, String password) throws IOException, AuthenticationException{
          DefaultHttpClient clientside = new DefaultHttpClient();
          try{
            signature = DigestUtils.shaHex(email + password + this.getApplicationID() + this.getAPIkey());  
            URIBuilder URL_link = URL_link = new URIBuilder("https://www.mediafire.com/api/1.1/user/get_session_token.php");;
            URL_link.addParameter("email", this.getemail());
            URL_link.addParameter("password", this.getpassword());            
            URL_link.addParameter("application_id", this.getApplicationID());
            URL_link.addParameter("signature", signature); 
            URL_link.addParameter("token_version", "1");  
            
            HttpGet gethttp = new HttpGet(URL_link.toString());             
            HttpResponse responsehttp = clientside.execute(gethttp);                 
            HttpEntity entityhttp = responsehttp.getEntity();    
            XPathFactory xdata = XPathFactory.newInstance();
            XPath xpath = xdata.newXPath();
            Node responsenode = (Node) xpath.evaluate("/", new InputSource(entityhttp.getContent()),XPathConstants.NODE);
            sessiontoken = xpath.evaluate("/response/session_token", responsenode);
            key = xpath.evaluate("/response/secret_key", responsenode);
            EntityUtils.consume(entityhttp);             
          }catch(URISyntaxException ex){
              Logger.getLogger(MediaFireHttp.class.getName()).log(Level.SEVERE, null, ex);
          }catch (XPathExpressionException ex) {
                Logger.getLogger(MediaFireHttp.class.getName()).log(Level.SEVERE, null, ex);
            }         
      }       
      
      
        public void getcontent() throws IOException, AuthenticationException{
          DefaultHttpClient httpclient = new DefaultHttpClient();
          try{
              URIBuilder url_get = new URIBuilder("http://www.mediafire.com/api/1.1/folder/get_content.php");                 
              url_get.addParameter("folder_key",""); 
              url_get.addParameter("session_token",sessiontoken);
              url_get.addParameter("content_type", "files");               
              url_get.addParameter("response_format","xml");               
              HttpGet gethttp = new HttpGet(url_get.toString());              
              HttpResponse responsehttp = httpclient.execute(gethttp);                 
              HttpEntity entityhttp = responsehttp.getEntity();    
              XPathFactory xdata = XPathFactory.newInstance();
              XPath xpath = xdata.newXPath();
              Node downloadnode = (Node) xpath.evaluate("/", new InputSource(entityhttp.getContent()),XPathConstants.NODE);
              quickkey = xpath.evaluate("//quickkey", downloadnode); 
              folderkey = xpath.evaluate("//quickkey", downloadnode);
          }catch(URISyntaxException ex){
              Logger.getLogger(MediaFireHttp.class.getName()).log(Level.SEVERE, null, ex);
          } catch (XPathExpressionException ex) {
                Logger.getLogger(MediaFireHttp.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
      
      public void downloadfile() throws IOException, AuthenticationException{
          DefaultHttpClient httpclient = new DefaultHttpClient();
          try{
              URIBuilder url = new URIBuilder("http://www.mediafire.com/api/1.2/file/get_links.php");
              url.addParameter("link_type", link_type);
              url.addParameter("session_token", sessiontoken);
              url.addParameter("quick_key", quickkey);
              url.addParameter("response_format", "xml");
              HttpGet gethttp = new HttpGet(url.toString());             
              HttpResponse responsehttp = httpclient.execute(gethttp);                 
              HttpEntity entityhttp = responsehttp.getEntity();    
              XPathFactory xdata = XPathFactory.newInstance();
              XPath xpath = xdata.newXPath();
              Node downloadnode = (Node) xpath.evaluate("/", new InputSource(entityhttp.getContent()),XPathConstants.NODE);
              downloadlink = xpath.evaluate("//direct_download", downloadnode);                          
              System.out.println("Copy the following download link and past in on your browser to download the file:\n");
              System.out.println("The download link is: \n" + downloadlink); 
          }catch(URISyntaxException ex){
              Logger.getLogger(MediaFireHttp.class.getName()).log(Level.SEVERE, null, ex);
          } catch (XPathExpressionException ex) {
                Logger.getLogger(MediaFireHttp.class.getName()).log(Level.SEVERE, null, ex);
            }
      } 
      
    public String getApplicationID() {
        return APP_id;
    }    

    public String getAPIkey() {
        return API_key;
    }
    public String getemail(){
        return email;
    }
    public String getpassword(){
        return password;
    }
}
