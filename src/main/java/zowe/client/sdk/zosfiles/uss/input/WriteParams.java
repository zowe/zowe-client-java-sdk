/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zowe.client.sdk.zosfiles.uss.input;

/**
 *
 * @author jimko
 */
public class WriteParams {
    boolean text;
    boolean binary;
    
    // if text
    String fileEncoding;
    String crlf;
    String textContent;
    
    // if binary
    byte[] binaryContents;
   
}
