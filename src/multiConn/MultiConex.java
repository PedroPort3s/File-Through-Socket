package multiConn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MultiConex implements Runnable {
	 private Socket client;
    private IRespostaServer resposta;

	    public MultiConex(Socket client, IRespostaServer respostaServ){
	        this.client = client;
	        resposta = respostaServ;
	    }

	    @Override
	    public void run()
	    {
	        int count = 0;
	        
	        String diretorioArquivos = String.format("%s//Downloads//socket",System.getProperty("user.home"));

	        try 
	        {  
            	BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
            	DataInputStream dis = new DataInputStream(bis);
            	
            	File[] arquivos = new File[dis.readInt()];
            	
            	System.out.println(String.format("Recebendo %s arquivo(s) de %s...",arquivos.length, client.getInetAddress()));

            	for(int i = 0; i < arquivos.length; i++)
            	{   
            		long fileLength = dis.readLong();
            		String nomeArquivo = dis.readUTF();
            		
            		System.out.println(String.format("Recebendo arquivo %s - %s...", i + 1, nomeArquivo));
            		
					arquivos[i] = new File(diretorioArquivos + "//" + nomeArquivo);

					FileOutputStream fileOutputStream = new FileOutputStream(arquivos[i]);
					
					BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);

					for(int j = 0; j < fileLength; j++) 
					{
						outputStream.write(bis.read());
					}

					outputStream.close();
            	}

            	dis.close();
                
            	System.out.println("Arquivos gravados com sucesso!");
	            	
	                //resposta.printMsgTodos("Arquivos gravados com sucesso!")
	        } 
	        catch (Exception e) 
	        {
	        	System.out.println("Erro: " + e.getMessage());
	        }
	        finally 
	        {
	        	try 
	        	{
					client.close();
					System.out.println("socket fechado!");
					resposta.printMsgTodos("Aguardando novas conexões!");
				} 
	        	catch (IOException e) 
	        	{
					e.printStackTrace();
				}	
			}
	    }

}
