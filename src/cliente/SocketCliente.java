package cliente;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SocketCliente 
{
	public void execute() 
	{	
		Scanner teclado = new Scanner(System.in);

        try 
        {   
        	         

            String msg = "";

            while (!msg.toUpperCase().equals("SAIR") && !msg.toUpperCase().equals("FECHAR")) 
            {
            	Socket cliente = new Socket("192.168.254.50", 54321);
            	
            	System.out.println("Conectado, digite o caminho para enviar os seus arquivos para o servidor...");
            	
                msg = teclado.nextLine();
                
                if (msg.toUpperCase().equals("SAIR") || msg.toUpperCase().equals("FECHAR")) 
                {
					break;
				}
                
                File[] files = new File(msg).listFiles();
                
                if (files != null) 
                {
                	int contagem = 1;
                    int qtdArquivos = files.length;                    

                	BufferedOutputStream bos = new BufferedOutputStream(cliente.getOutputStream());
                    DataOutputStream dos = new DataOutputStream(bos);

                    dos.writeInt(qtdArquivos);

                    System.out.println(String.format("Processando e enviando %s arquivos...", qtdArquivos));
                    
                    for(File file : files)
                    {             
                    	System.out.println(String.format("Enviando %s arquivo de %s...", contagem, qtdArquivos));
                    	
                        long length = file.length();
                        
                        dos.writeLong(length);

                        String name = file.getName();
                        dos.writeUTF(name);

                        FileInputStream fis = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fis);

                        int count = 0;
                        
                        while((count = bis.read()) != -1) 
                        { 
                            bos.write(count);
                        }
                        
                        bis.close();  
                        
                        contagem++;
                    }
                    
                    dos.close();
                    
                    System.out.println("Arquivos enviados com sucesso!");
                }
            }
        } 
        catch (UnknownHostException e) 
        {   
        	System.out.println(e.getMessage());
        } 
        catch (IOException e) 
        {            
        	System.out.println(e.getMessage());
        }

        System.out.println("Conexão encerrada");
	}
}
