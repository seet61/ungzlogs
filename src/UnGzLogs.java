import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;


/**
 * Created by dmitry.arefyev on 07.10.2016.
 */
public class UnGzLogs {
    public static void main(String[] args) {
        String curr_dir = System.getProperty("user.dir");
        System.out.println("Рабочий каталог: " + curr_dir);
        //Check destination dir
        check_dest_dir(curr_dir);
        //Work with files
        get_dir(curr_dir);
    }

    public static void get_dir(String dir) {
        try {
            File[] file = new File(System.getProperty("user.dir"), "logs").listFiles();

            for (int i=0; i < file.length; i++){
                //System.out.println(file[i] + " dir: " + file[i].isDirectory());
                System.out.println("Работаем с каталогом: " + file[i].getName());
                if (file[i].isDirectory() && !file[i].getName().equals("for_parse")) {
                    File[] sub_file = file[i].listFiles();
                    for (int j=0; j < sub_file.length; j++) {
                        if (sub_file[j].isDirectory() == true) {
                            System.err.println("Доступно только одно вложение каталога!");
                        }
                        else {

                            ungz(sub_file[j]);
                        }
                    }
                }
                else if (!file[i].getName().equals("for_parse")) {
                    ungz(file[i]);
                }
                else {
                    System.out.println("Каталог пропускается!");
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public static void check_dest_dir(String curr_dir) {
        File destinationFolder = new File(new File(System.getProperty("user.dir"), "logs"), "for_parse");
        if (!destinationFolder.exists()) {
            System.out.println("Каталог назначения не существует, чистить нечего!");
        }
        else {
            File[] file = destinationFolder.listFiles();

            for (int i=0; i < file.length; i++){
                if (file[i].isDirectory()) {
                    File[] sub_file = file[i].listFiles();
                    for (int j=0; j < sub_file.length; j++) {
                        if (sub_file[j].isDirectory() == true) {
                            System.err.println("Доступно только одно вложение каталога!");
                        }
                        else {
                            del_file(sub_file[j]);
                        }
                    }
                    del_file(file[i]);
                }
                else if (!file[i].getName().equals("for_parse")) {
                    del_file(file[i]);
                }
                else {
                    del_file(file[i]);
                }
            }

            boolean success = destinationFolder.delete();
            if (success) {
                System.out.println("Каталог " + destinationFolder + " удален.");
            }
            else {
                System.err.println("Каталог " + destinationFolder + "не удален!");
            }
        }
    }

    private static void del_file(File name) {
        boolean success = name.delete();
        if (!success) {
            System.out.println("Файл не удален: " + name);
        }
    }

    private static void ungz(File zipFile) {
        System.out.println("Работаем с файлом: " + zipFile);
        String work_file = zipFile.toString();
        String out_file = zipFile.getName().toString();
        out_file = out_file.substring(0, out_file.lastIndexOf("."));
        File destinationFolder = new File(new File(System.getProperty("user.dir"), "logs"), "for_parse");
        String parrent_dirr = zipFile.getParent().toString();
        parrent_dirr = parrent_dirr.substring(parrent_dirr.lastIndexOf(File.separator));
        parrent_dirr = parrent_dirr.substring(1);
        destinationFolder = new File(destinationFolder, parrent_dirr);

        // if the output directory doesn't exist, create it
        if(!destinationFolder.exists())
            destinationFolder.mkdirs();

        try (FileInputStream in = new FileInputStream(work_file);
             GZIPInputStream gzin = new GZIPInputStream(in);
             FileOutputStream out = new FileOutputStream(destinationFolder + File.separator + out_file)
        ) {
            //Stream for read from file
            //in = new FileInputStream(work_file);
            //GZIPInputStream gzin = new GZIPInputStream(in);
            //Stream for read zip file
            //FileOutputStream out = new FileOutputStream(destinationFolder + File.separator + out_file);

            //buffer
            byte[] buffer = new byte[4096];
            int byte_read;
            while ((byte_read = gzin.read(buffer)) != -1) {
                out.write(buffer, 0, byte_read);
            }
            //in.close();
            //out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
