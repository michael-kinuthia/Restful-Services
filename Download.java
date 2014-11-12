/**
 * @Author Michael Kinuthia
 * @file Download.java
 * @Brief Call and execute the methods in MediaFireHTTP.java
 * @Info Application Key and Application_ID gottenb from the MediaFire Account
 */
package mit.fi.filedownload;
import java.io.IOException;
import javax.naming.AuthenticationException;


public class Download 
{
    public static void main( String[] args )
    {
        String app_id ="Application_ID";
        String api_key ="Application Key";
        String email = "Your email";
        String password ="Your Password";
        MediaFireHttp media = new MediaFireHttp(api_key,app_id,email,password);        
        try{
            media.connection(email, password);            
            media.getcontent();
            media.downloadfile(); 
        }catch(IOException ex){
        }catch(AuthenticationException ex){
        }
        
      
    }
}
