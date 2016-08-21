package elidation.io.resourceloader;

import elidation.io.resource.ClassPathResource;
import elidation.io.resource.Resource;

import java.io.File;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ClassPathResourceLoader extends AbstractResourceLoader {

    public ClassPathResourceLoader(String location)
    {
        super(location);
    }

    public Resource getResource() {
        if(location ==null )
            throw new RuntimeException();
        URL url = this.getClass().getClassLoader().getResource(location);
        if(url ==null )
            throw new RuntimeException();
        File file = new File(url.getFile());
        ClassPathResource classPathResource = new ClassPathResource(file);

        classPathResource.setLocation(location);
        classPathResource.setUrl(url);
        classPathResource.setFile(file);

        return classPathResource;
    }

}
