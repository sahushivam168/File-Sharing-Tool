import java.net.*;
import java.io.*;
class FileServer
{
private int port;
private ServerSocket serverSocket;
FileServer(int port)
{ 
try
{
this.port=port;
serverSocket=new ServerSocket(port);
startListening();
}catch(Exception ee)
{
System.out.println("Cannot start server : "+ee.getMessage());
System.exit(0);
}
}
public void startListening()
{
int mi,fni;
String Method,FileName; 
StringBuffer stringBuffer;
InputStream inputStream;
FileInputStream fileInputStream;
OutputStream outputStream;
FileOutputStream fileOutputStream;
byte requestLengthArray[]=new byte[4];
byte ak[]=new byte[1];
Socket xyz;
while(true)
{
System.out.println("Server is ready and is listening on port : "+port);
try
{
int x;
xyz=serverSocket.accept();
inputStream=xyz.getInputStream();
inputStream.read(requestLengthArray);
int rl=bytesToInt(requestLengthArray[0],requestLengthArray[1],requestLengthArray[2],requestLengthArray[3]);
byte Bytes[]=new byte[rl];
ak[0]=66;
outputStream=xyz.getOutputStream();
outputStream.write(ak);
outputStream.flush();
inputStream.read(Bytes);
ByteArrayInputStream bais=new ByteArrayInputStream(Bytes);
ObjectInputStream ois=new ObjectInputStream(bais);
String request=(String)ois.readObject();
mi=request.indexOf('@');
Method=request.substring(0,mi);
fni=request.indexOf('#',mi+1);
FileName=request.substring(mi+1,fni);
File file=new File(FileName);
if(Method.equalsIgnoreCase("GET"))
{
if(!file.exists()) 
{
ak[0]=99;
outputStream.write(ak);
outputStream.flush();
}
else
{
ak[0]=100;
outputStream.write(ak);
outputStream.flush();
inputStream.read();
byte dataPacket[]=new byte[1024];
fileInputStream=new FileInputStream(file);
long fL=file.length();
byte fileLength[]=new byte[8];
fileLength[0]=(byte)(fL>>56);
fileLength[1]=(byte)(fL>>48);
fileLength[2]=(byte)(fL>>40);
fileLength[3]=(byte)(fL>>32);
fileLength[4]=(byte)(fL>>24);
fileLength[5]=(byte)(fL>>16);
fileLength[6]=(byte)(fL>>8);
fileLength[7]=(byte)(fL>>0);
outputStream.write(fileLength);
outputStream.flush();
inputStream.read();
long i=0;
System.out.println("File"+fileLength);
while(i<fL)
{

i=i+fileInputStream.read(dataPacket);
outputStream.write(dataPacket);
outputStream.flush();
}
System.out.println("File Transfer Successfull.");
}
xyz.close();
}
else
{
//Send programming
}
}catch(Exception e)
{
System.out.println(e);
}
}
}
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
public static void main(String gg[])
{
FileServer fs=new FileServer(Integer.parseInt(gg[0]));
}
}