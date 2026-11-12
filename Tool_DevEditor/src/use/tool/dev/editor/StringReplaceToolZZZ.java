package use.tool.dev.editor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.txt.stream.FileTextLineReplacerZZZ;
import basic.zBasic.util.file.txt.stream.FileTextStringReplacerZZZ;

public class StringReplaceToolZZZ {
	
		 private static final Charset UTF8 = StandardCharsets.UTF_8;

		    public static void main(String[] args) {
		    	try {
			        if (args.length < 4) {
			            System.out.println("Verwendung:");
			            System.out.println("  java StringReplaceToolZZZ <Pfad-zum-Ordner> <Endung-der-Dateien> <zu-findender-String> <ersetzender String>");
			            return;
			        }
			        
			        String sReplacementIn = args[3];
			        
			        //Absichern, so übergibt man einen Leerstring. Damit soll man einen Text durch einen Leerstring ersetzen können.			      
			        if(sReplacementIn.equals("\"\"")) {
			        	sReplacementIn = "";
			        }
			        final String sReplacement = sReplacementIn;  
			        
			        String sToFind = args[2];
			        
	
			        Path inputPath = Paths.get(args[0]);
			        System.out.println("Starting using directory: '" + inputPath.toString() + "'");
			        
			        String sEndingIn = args[1];
			        if(sEndingIn.equals("\"\"")) {
			        	sEndingIn="*";
			        }
			        if(StringZZZ.startsWith(sEndingIn, ".")) {
						sEndingIn = "." + sEndingIn;
					}
			        final String sEnding = sEndingIn;
			        
			        System.out.println("Starting using ending: '" + sEnding + "'");
			     
					if(StringZZZ.isEmpty(args[0]) | StringZZZ.isEmpty(args[1])) {
						ExceptionZZZ ez = new ExceptionZZZ("Leerstrings für Ordner oder Dateiendung sind nicht vorgesehen");
						throw ez;
					}
					
			        if (Files.isDirectory(inputPath)) {
			        	// Alle .sEndingIn-Dateien im Verzeichnisbaum durchlaufen
			            Files.walkFileTree(inputPath, new SimpleFileVisitor<Path>() {
			                @Override
			                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			                	try {			                		
				                    if (file.toString().endsWith(sEnding)) {			                    				                    	
				                        processFile(file, sToFind, sReplacement);
				                    }
				                    return FileVisitResult.CONTINUE;
				                } catch (ExceptionZZZ e) {
									e.printStackTrace();
									return FileVisitResult.TERMINATE;
						        }			                    			                    
			                }
			            });
			        } else if (Files.isRegularFile(inputPath) && inputPath.toString().endsWith(sEnding)) {
			            // Nur eine konkrete Datei verarbeiten
			            processFile(inputPath, sToFind, sReplacement);			        
			        } else {
			            System.out.println("Ungültiger Pfad: " + inputPath);
			        }		        
		    	} catch (ExceptionZZZ e) {
					e.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
		    }

		    private static void processFile(Path file, String sStringToFind, String sStringToReplace) throws ExceptionZZZ {
		        try {
		        	System.out.println("Reading File:");
		        	System.out.println(file.toString());
		            
		        	FileTextStringReplacerZZZ objTextReplacer = new FileTextStringReplacerZZZ(file);
		        	objTextReplacer.replaceAndSave(sStringToFind, sStringToReplace);
		        	objTextReplacer.close();

		        } catch (IOException e) {
		            System.err.println("Fehler bei Datei " + file + ": " + e.getMessage());
		        }
		    }
}
