import java.net.*;
import java.io.*;
import java.util.*;
class ClientTool
{
 public static void main(String command[]) 
{
Scanner scanner=new Scanner(System.in);
if(command[0].equals("ln"))
{
System.out.println("ln version 10.1.1");
while(true)
{
System.out.print("ln>");
String lnCommand=scanner.nextLine();
if(lnCommand.equalsIgnoreCase("quit;")) break;
String c=lnCommand.substring(lnCommand.length()-1);
int mi=lnCommand.indexOf(' ');
String Method=lnCommand.substring(0,mi); 
int fni=lnCommand.indexOf(';',mi+1);
String FileName=lnCommand.substring(mi+1,fni);
if(c.equals(";") || lnCommand.equals(";"))
{
if(Method.equalsIgnoreCase("GET"))
{ 
fileClient(Method,FileName);
}
else
{
fileClient(Method,FileName);
}
}
}
}
}
//--------------------------------------------------------------------------------------
public static void fileClient(String Method,String FileName)
{
String request=Method+"@"+FileName+"#";
if(Method.equalsIgnoreCase("GET"))
{
try
{
Socket socket=new Socket("localhost",6030);
byte ak[]=new byte[1];
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(request);
oos.flush();
byte[] bytes=baos.toByteArray(); 
baos.close();
int blength=bytes.length;
byte LByte[]=new byte[4];
LByte=intToBytes(blength);
OutputStream outputStream=socket.getOutputStream();
outputStream.write(LByte);
outputStream.flush ();
InputStream inputStream=socket.getInputStream();
inputStream.read();
outputStream.write(bytes);
outputStream.flush();
inputStream.read(ak);
if(ak[0]==99)
{
System.out.println("File not Found!");
return;
}
outputStream.write(ak);
outputStream.flush();
byte fileLength[]=new byte[8];
byte dataPacket[]=new byte[1024];
inputStream.read(fileLength);
long fL=(fileLength[0]<<56 |(fileLength[1] & 0xFF)<<48 |(fileLength[2] & 0xFF)<<40|(fileLength[3] & 0xFF)<<32|(fileLength[4] & 0xFF)<<24|(fileLength[5] & 0xFF)<<16|(fileLength[6] & 0xFF)<<8|(fileLength[7] & 0xFF));
outputStream.write(ak);
outputStream.flush();
FileOutputStream fileOutputStream=new FileOutputStream(FileName);
int k=0;
System.out.println(fL);
System.out.print("File Receiving......");
for(long i=0;i<fL;)
{
k=inputStream.read(dataPacket);
//if(k==-1) break;
i=i+k;

fileOutputStream.write(dataPacket);
}
System.out.print("File is Received Successfully.");
fileOutputStream.close();
socket.close();
}
catch(Exception e)
{
System.out.println(e);
}
}
else
{
//SEND programming
}
}
//--------------------------------------------------------------------------------------
public static byte[] intToBytes(int i)
{
byte[] result=new byte[4];
result[0]=(byte)(i>>24);
result[1]=(byte)(i>>16);
result[2]=(byte)(i>>8);
result[3]=(byte)(i>>0);
return result;
}
public static int bytesToInt(byte b0,byte b1,byte b2,byte b3)
{
 return (((b0    )<< 24) | ((b1 & 0xff) <<16)  | ((b2 & 0xff) <<8) | ((b3 & 0xff)   )); 

}
}