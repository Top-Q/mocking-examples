package topq;


import lombok.SneakyThrows;

import java.util.Scanner;

public class ResourceUtils {

    @SneakyThrows
    public static String resourceToString(final String resourceName) {
        try (Scanner s = new Scanner(new ResourceUtils().getClass().getClassLoader().getResourceAsStream(resourceName))){
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }


}
