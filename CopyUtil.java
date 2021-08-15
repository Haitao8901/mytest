package file.copy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CopyUtil {
    private static CopyUtil copyUtil;

    private CopyUtil() {
    }

    public static CopyUtil getCopyUtil() {
        if (Objects.isNull(copyUtil)) {
            return new CopyUtil();
        }
        return copyUtil;
    }

    public void copy(String source, String... targets){
        Path sourcePath = Paths.get(source);
        List<Path> paths = Stream.of(targets).map(target -> Paths.get(target)).collect(
                () ->
                        new ArrayList<>(),
                (targetL, path) ->
                        targetL.add(path) ,
                (targetL, adds) ->
                        targetL.addAll(adds));
        paths.forEach(path -> {
            try {
                copy(sourcePath, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void copy(Path source, Path target) throws IOException {
            Path finalTarget = target;
            if(!source.getFileName().equals(target.getFileName())){
                finalTarget = target.resolve(source.getFileName());
            }
            Path finalTarget1 = finalTarget;
            Files.walk(source).forEach(path -> {
                try {
                    Path targetPath = finalTarget1.resolve(source.relativize(path));
                    if(Files.isDirectory(path)){
                        Files.createDirectories(targetPath);
                    }else{
                        Files.copy(path,targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }
}
