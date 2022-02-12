import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Composer {
    public static void main(String[] args) {
        String projectPath = getAbsolutePathOfProject();
        String blogPath = Path.of(projectPath, "blog").toString();
        String privatePath = Path.of(projectPath, "private").toString();

        String combineBlogPath = Path.of(projectPath, "BLOG.md").toString();
        String combinePrivatePath = Path.of(projectPath, "PRIVATE.md").toString();
        combineMultiFilesToOneFile(privatePath, combinePrivatePath);
//        combineMultiFilesToOneFile(blogPath, combineBlogPath);
    }

    private static String getAbsolutePathOfProject() {
        String currentPath = System.getProperty("user.dir");
        return Paths.get(currentPath).getParent().toString();
    }

    private static List<Path> getAllPaths(String path) throws IOException {
        List<Path> paths = Files
                .walk(Path.of(path))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        return paths;
    }

    private static void combineMultiFilesToOneFile(String folder, String file) {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            List<Path> paths = getAllPaths(folder);
            paths.sort(Collections.reverseOrder());

            bufferedWriter = new BufferedWriter(new FileWriter(file));

            for (Path path : paths) {
                bufferedReader = new BufferedReader(new FileReader(path.toFile()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(line + "\n");
                }
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
