import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class RenameFile {

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm__");
        String format = simpleDateFormat.format(new Date());
        try {
            Properties prop = System.getProperties();
            String os = prop.getProperty("os.name");
            System.out.println(os);
            String fileDirPath = "Channels";
            String channels = "channel";
            File file = new File(fileDirPath);
            File file1 = new File(channels);
            if (!file1.exists()) {
                System.out.println("channel file doesn't exist : " + file1.getAbsolutePath());
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(file1));
            if (file != null) {
                File[] files = file.listFiles();
                if (files != null && files.length != 0) {
                    String s;
                    while ((s = br.readLine()) != null) {
                        String number = s.substring(0, 4);
                        for (int i = 0; i < files.length; i++) {
                            File fileI = files[i];
                            if (fileI.exists() && fileI.getName().endsWith(".apk") && fileI.getName().contains(number)) {
                                String name = fileI.getName();
                                String original_name_cut = name.substring(0, name.length() - 8);
                                int i1 = s.indexOf("#");
                                String new_channel_name = format + s.substring(i1 + 1).trim();
                                String newFileName = original_name_cut + new_channel_name + ".apk";
                                String newName = fileI.getParentFile().getAbsolutePath() + File.separator + newFileName;
                                File fileNew = new File(newName);
                                boolean b = fileI.renameTo(fileNew);
                                if (b) {
                                    System.out.println("success,original name= " + fileI.getName() + "  \r\n new name = " + newFileName);
                                } else {
                                    System.out.println("failed");
                                }
                                break;
                            }
                        }
                    }
                    br.close();
                } else {
                    System.out.println("apkFile doesn't exist!");
                }
            }
        } catch (Exception e) {
            System.out.println("exception !" + e.getMessage());
        }

    }
}
