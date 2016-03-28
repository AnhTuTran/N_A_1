import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileInfoPeer {
	String fileName;
	String path;
	int numberOfChunks;
	//private long fileSize;
	boolean []chunkStatus;
	public static final int maxFileSize = 256*1024;	//256KB
	
	public FileInfoPeer(String name, String path, boolean upOrDown) throws IOException {
		// TODO Auto-generated constructor stub
		fileName = name;
		this.path = path;
		if (upOrDown) {
			getNumberOfChunks(); 
			chunkStatus = new boolean[numberOfChunks];
			Arrays.fill(chunkStatus, false);
		}
	}
	
	void getNumberOfChunks() {
		
		File myFile = new File(path + "/"+fileName);
		byte [] buffer = new byte[maxFileSize];
		numberOfChunks = 0;
		
		try (BufferedInputStream inputStream = new BufferedInputStream
				(new FileInputStream(myFile))) {
			int temp = 0; 
			while ((temp = inputStream.read(buffer)) > 0) {
				numberOfChunks++;
			}
		}
		catch (IOException e){
			e.printStackTrace();
			//System.err.println("File not found");
		}
		//this.fileSize = fileSize;
		//return	(int) ((fileSize%maxFileSize == 0) ? fileSize/maxFileSize : fileSize/maxFileSize + 1);  		
		
	}
	
	int splitChunkThFile(byte[] buffer, int chunkTh) throws IOException {
		File myFile = new File(path + "/"+fileName);
		int chunkNumber = 1;
		//byte[] buffer = new byte[maxFileSize];
		//Arrays.fill(buffer, (byte) 0);
		try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(myFile)))
		{				
			int temp = 0; 
			while ((temp = inputStream.read(buffer)) > 0) {
				if (chunkNumber == chunkTh) {
					//System.out.println(buffer.toString());
					return temp;
				}
				chunkNumber++;
			}
		return -1;
		}
	}
	
	void  makeChunkTh(int chunkTh, byte[] buffer, int size) throws FileNotFoundException, IOException {
		File subFile = new File(path, fileName + "." + Integer.toString(chunkTh));
		try (FileOutputStream outputStream = new FileOutputStream(subFile)) {
			outputStream.write(buffer, 0, size); 				
		}
	}
	
	
	
	void mergeChunks(File myFile) throws IOException {
		List<File> list = new ArrayList<File>();
		int temp = 1;
		while (temp <= numberOfChunks) {
			list.add(new File(path + "//" + fileName + "." + Integer.toString(temp)));
			temp++;
		}
		try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(myFile))) {
			for (File chunk:list) {
				Files.copy(chunk.toPath(), outputStream);
				chunk.delete();
				
			}
		}
	}
}