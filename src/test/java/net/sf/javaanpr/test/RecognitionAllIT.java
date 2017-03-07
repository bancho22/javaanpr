package net.sf.javaanpr.test;

import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

@RunWith(Parameterized.class)
public class RecognitionAllIT {

    private File plateFile;
    private String plateCorrect;

    public RecognitionAllIT(File plateFile, String plateCorrect) {
        this.plateFile = plateFile;
        this.plateCorrect = plateCorrect;
    }
    
    @Test
    public void testAllPlates() throws IOException, ParserConfigurationException, SAXException{
        CarSnapshot car = new CarSnapshot(new FileInputStream(plateFile));
        Intelligence intel = new Intelligence();
        String spz = intel.recognize(car);
        
        assertThat(plateCorrect, equalTo(spz));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testDataCreator() throws FileNotFoundException, IOException {
        String inputFolder  = "src/test/resources/snapshots";
        String outputFolder = "src/test/resources/results.properties";
        
        InputStream outputStream = new FileInputStream(new File(outputFolder)); // ;)
        Properties properties = new Properties();
        properties.load(outputStream);
        outputStream.close();
        
        assertThat(properties.size(), greaterThan(0));
        
        File inputFolderContent = new File(inputFolder);
        File[] snapshots = inputFolderContent.listFiles();
        
        Collection<Object[]> dataForOneImage = new ArrayList();
        for (File file : snapshots) {
            String name = file.getName();
            String plateExpected = properties.getProperty(name);
            dataForOneImage.add(new Object[]{file, plateExpected});
        }
        return dataForOneImage;
    }

}
