package server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import multiConn.IRespostaServer;
import multiConn.MultiConex;


public class SocketServer implements IRespostaServer
{
	ArrayList<PrintStream> listOutputs = new ArrayList<PrintStream>();
	public void execute() 
	{
		try 
		{
			ServerSocket socket = new ServerSocket(54321);
			
			while(true) 
			{
				System.out.println(String.format(("Aguardando novas conexões na porta: %s"),socket.getLocalPort()));
				Socket cliente  = socket.accept();
				
				System.out.println(cliente.getInetAddress() + " conectou!\nAguardando envio de arquivos... ");
				
				listOutputs.add(new PrintStream(cliente.getOutputStream()));

	            new Thread(new MultiConex(cliente,this)).start();				
			}
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}

	@Override
	public synchronized void printMsgTodos(String msg) 
	{
		for (PrintStream printStream : listOutputs) 
		{
            printStream.println(msg);
        }
	}
}
