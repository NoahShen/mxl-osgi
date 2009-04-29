package net.sf.mxlosgi.mxlosgisock5bundle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;


/**
 * @author noah
 * 
 */
public class SOCKS5Negotiator
{

	public static void establishSOCKS5ConnectionToProxy(Socket socket, String addr) throws IOException
	{

		byte[] cmd = new byte[3];
		
		OutputStream out = new DataOutputStream(socket.getOutputStream());
		InputStream in = new DataInputStream(socket.getInputStream());
		
		cmd[0] = (byte) 0x05;
		cmd[1] = (byte) 0x01;
		cmd[2] = (byte) 0x00;


		out.write(cmd);


		byte[] response = new byte[2];
		
		in.read(response);
		
		cmd = createOutgoingSocks5Message(1, addr);
		out.write(cmd);
		createIncomingSocks5Message(in);
	}

	public static byte[] createOutgoingSocks5Message(int cmd, String strAddr)
	{
		byte addr[] = strAddr.getBytes();

		byte[] data = new byte[7 + addr.length];
		data[0] = (byte) 5;
		data[1] = (byte) cmd;
		data[2] = (byte) 0;
		data[3] = (byte) 0x3;
		data[4] = (byte) addr.length;

		System.arraycopy(addr, 0, data, 5, addr.length);
		data[data.length - 2] = (byte) 0;
		data[data.length - 1] = (byte) 0;

		return data;
	}

	public static String createIncomingSocks5Message(InputStream in) throws IOException
	{
		byte[] cmd = new byte[5];
		in.read(cmd, 0, 5);

		byte[] addr = new byte[cmd[4]];
		in.read(addr, 0, addr.length);
		String strAddr = new String(addr);
		
		in.read();
		in.read();

		return strAddr;
	}

	public static void establishSocks5UploadConnection(OutputStream out, InputStream in) throws IOException, XMPPException
	{

		// first byte is version should be 5
		int b = in.read();
		if (b != 5)
		{
			throw new XMPPException("Only SOCKS5 supported");
		}

		// second byte number of authentication methods supported
		b = in.read();
		int[] auth = new int[b];
		for (int i = 0; i < b; i++)
		{
			auth[i] = in.read();
		}

		int authMethod = -1;
		for (int anAuth : auth)
		{
			authMethod = (anAuth == 0 ? 0 : -1); // only auth method 0, no authentication, supported
			if (authMethod == 0)
			{
				break;
			}
		}
		if (authMethod != 0)
		{
			throw new XMPPException("Authentication method not supported");
		}

		byte[] cmd = new byte[2];
		cmd[0] = (byte) 0x05;
		cmd[1] = (byte) 0x00;
		out.write(cmd);

		String addr = createIncomingSocks5Message(in);
		cmd = createOutgoingSocks5Message(0, addr);
		out.write(cmd);
	}
}